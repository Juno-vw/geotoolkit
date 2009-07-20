package org.geotoolkit.data.crs;

import junit.framework.TestCase;

import org.geotoolkit.data.memory.MemoryDataStore;
import org.geotoolkit.feature.simple.SimpleFeatureBuilder;
import org.geotoolkit.feature.simple.SimpleFeatureTypeBuilder;
import org.geotoolkit.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.geotoolkit.data.crs.ForceCoordinateSystemFeatureResults;
import org.geotoolkit.feature.collection.FeatureCollection;
import org.geotoolkit.geometry.jts.JTSEnvelope2D;

public class ForceCoordinateFeatureResultsTest extends TestCase {
    
    private static final String FEATURE_TYPE_NAME = "testType";
    private MemoryDataStore store;
    private CoordinateReferenceSystem wgs84;
    private CoordinateReferenceSystem utm32n;

    protected void setUp() throws Exception {
        wgs84 = CRS.decode("EPSG:4326");
        utm32n = CRS.decode("EPSG:32632");
        
        GeometryFactory fac=new GeometryFactory();
        Point p = fac.createPoint(new Coordinate(10,10) );
        
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(FEATURE_TYPE_NAME);
        builder.setCRS(wgs84);
        builder.add("geom", Point.class );
        
        SimpleFeatureType ft = builder.buildFeatureType();
        
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(ft);
        b.add( p );
        
        SimpleFeature[] features=new SimpleFeature[]{
           b.buildFeature(null) 
        };
        
        store = new MemoryDataStore(features);
    }

    public void testSchema() throws Exception {
        FeatureCollection original = store.getFeatureSource(FEATURE_TYPE_NAME).getFeatures();
        assertEquals(wgs84, original.getSchema().getCoordinateReferenceSystem());
        
        FeatureCollection forced = new ForceCoordinateSystemFeatureResults(original, utm32n);
        assertEquals(utm32n, forced.getSchema().getCoordinateReferenceSystem());
    }
    
    public void testBounds() throws Exception {
        FeatureCollection original = store.getFeatureSource(FEATURE_TYPE_NAME).getFeatures();
        
        FeatureCollection forced = new ForceCoordinateSystemFeatureResults(original, utm32n);
        assertEquals(new JTSEnvelope2D(10,10,10,10, utm32n), forced.getBounds());
    }
}