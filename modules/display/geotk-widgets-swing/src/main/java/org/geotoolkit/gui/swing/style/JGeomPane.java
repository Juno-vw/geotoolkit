/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2007 - 2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008 - 2009, Johann Sorel
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotoolkit.gui.swing.style;

import org.geotoolkit.gui.swing.resource.MessageBundle;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import org.geotoolkit.map.MapLayer;

/**
 * Geometry panel
 *
 * @author Johann Sorel
 * @module pending
 */
public class JGeomPane extends StyleElementEditor<String> {

    /** Creates new form JGeomPane */
    public JGeomPane() {
        super(String.class);
        initComponents();
    }

    public void setLayer(final MapLayer layer){
        guiBox.setLayer(layer);
    }

    public MapLayer getLayer(){
        return guiBox.getLayer();
    }

    @Override
    public void parse(String target) {
        guiBox.setGeom(target);
    }

    @Override
    public String create() {
        return guiBox.getGeom();
    }
    
    @Override
    protected Object[] getFirstColumnComponents() {
        return new Object[]{guiLabel};
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        guiBox = new JGeomBox();
        guiLabel = new JLabel();

        setOpaque(false);

        guiBox.setEditable(true);

        guiLabel.setText(MessageBundle.getString("geometry")); // NOI18N

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(guiLabel)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(guiBox, GroupLayout.PREFERRED_SIZE, 76, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(guiLabel, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
                .addComponent(guiBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JGeomBox guiBox;
    private JLabel guiLabel;
    // End of variables declaration//GEN-END:variables

}
