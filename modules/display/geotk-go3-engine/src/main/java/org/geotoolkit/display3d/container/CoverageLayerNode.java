
package org.geotoolkit.display3d.container;

import com.ardor3d.bounding.BoundingBox;
import com.ardor3d.image.Image;
import com.ardor3d.image.Image.Format;
import com.ardor3d.image.Texture;
import com.ardor3d.image.util.AWTImageLoader;
import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Vector3;
import com.ardor3d.renderer.Renderer;
import com.ardor3d.renderer.state.TextureState;
import com.ardor3d.scenegraph.Mesh;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.shape.Quad;
import com.ardor3d.util.TextureManager;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotoolkit.coverage.grid.GridCoverage2D;
import org.geotoolkit.display3d.canvas.A3DCanvas;
import org.geotoolkit.display3d.primitive.A3DGraphic;
import org.geotoolkit.map.CoverageMapLayer;
import org.geotoolkit.referencing.CRS;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class CoverageLayerNode extends A3DGraphic{

//    private static final UpdateThread UPDATER = new UpdateThread();
//
//    static{
//        UPDATER.setPriority(Thread.MIN_PRIORITY);
//        UPDATER.start();
//    }

    private final CoverageMapLayer layer;

    public CoverageLayerNode(A3DCanvas canvas, CoverageMapLayer layer) {
        super(canvas);
        this.layer = layer;

//        LoadingThread loader = new LoadingThread();
//        loader.setPriority(Thread.MIN_PRIORITY);
//        loader.start();

        attachChild(buildQuad());

//        UpdateThread.nodes.put(this, new double[3]);
    }

    private Mesh buildQuad(){

        org.opengis.geometry.Envelope env = layer.getCoverageReader().getCoverageBounds();
        try {
            env = CRS.transform(env, canvas.getObjectiveCRS());
        } catch (TransformException ex) {
            Logger.getLogger(CoverageLayerNode.class.getName()).log(Level.SEVERE, null, ex);
        }

        final Quad back = new Quad();
        back.initialize(env.getSpan(0), env.getSpan(1));
        back.setTranslation(env.getMinimum(0) + env.getSpan(0)/2, 0, env.getMinimum(1) + env.getSpan(1)/2);
        back.setRotation(new Matrix3().fromAngleNormalAxis(Math.PI * -0.5, new Vector3(1, 0, 0)));
        back.setModelBound(new BoundingBox());
        back.updateModelBound();

        try {
            GridCoverage2D coverage = layer.getCoverageReader().read(null);
            RenderedImage img = coverage.getRenderedImage();
            Image image = AWTImageLoader.makeArdor3dImage(img, false);

            // Add a texture to the box.
            final TextureState ts = new TextureState();
            ts.setTexture(TextureManager.loadFromImage(image, Texture.MinificationFilter.Trilinear,
                    Format.Guess, true));
            back.setRenderState(ts);

//            // Add a material to the box, to show both vertex color and lighting/shading.
//            final MaterialState ms = new MaterialState();
//            ms.setColorMaterial(ColorMaterial.Diffuse);
//            box.setRenderState(ms);

        } catch (FactoryException ex) {
            Logger.getLogger(CoverageLayerNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformException ex) {
            Logger.getLogger(CoverageLayerNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CoverageLayerNode.class.getName()).log(Level.SEVERE, null, ex);
        }

        return back;

    }

    @Override
    public void draw(Renderer r) {

        if(update){
            synchronized(meshes){
                for(Object m : meshes){
                    if(m instanceof Mesh){
                        this.attachChild((Mesh)m);
                    }else if(m instanceof Node){
                        this.attachChild((Node)m);
                    }
                }
                meshes.clear();
                update = false;
            }
        }

        super.draw(r);
    }


    private volatile boolean update = false;
    private final List<Object> meshes = new ArrayList<Object>();

    private class LoadingThread extends Thread{

        @Override
        public void run() {
            try {
                sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CoverageLayerNode.class.getName()).log(Level.SEVERE, null, ex);
            }

            Mesh mesh = buildQuad();

            synchronized(meshes){
                meshes.add(mesh);
                update = true;
            }

        }

    }


    private static class UpdateThread extends Thread{

        private static final Map<CoverageLayerNode,double[]> nodes = new HashMap<CoverageLayerNode,double[]>();
        private static final int MIN_STEP = 100;

        @Override
        public void run() {
            while(true){
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                }

                synchronized(nodes){
                    for(CoverageLayerNode node : nodes.keySet()){
                        final double[] coords = node.canvas.getController().getCameraPosition();

                        if(coords != null){
                            final double[] lastPosition = nodes.get(node);
                            if( Math.abs(coords[0]-lastPosition[0]) > MIN_STEP ||
                                Math.abs(coords[1]-lastPosition[1]) > MIN_STEP ){
                                System.out.println("difference de position");
                                lastPosition[0] = coords[0];
                                lastPosition[1] = coords[1];

                                Mesh mesh = node.buildQuad();

                                synchronized(node){
                                    node._children.clear();
                                    node.attachChild(mesh);
                                }
                            }


                        }


                    }

                }

                

                

            }
        }

    }

}
