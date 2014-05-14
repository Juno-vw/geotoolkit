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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import org.geotoolkit.gui.swing.resource.MessageBundle;
import org.geotoolkit.map.MapLayer;
import org.opengis.style.Halo;

/**
 * Halo panel
 * 
 * @author Johann Sorel
 * @module pending
 */
public class JHaloPane extends StyleElementEditor<Halo> {

    private MapLayer layer = null;

    /** Creates new form JHaloPanel */
    public JHaloPane() {
        super(Halo.class);
        initComponents();
        init();
    }

    private void init() {
        guiRadius.setModel(1d, 0d, Double.MAX_VALUE, 1);
    }

    @Override
    public void setLayer(final MapLayer layer) {
        this.layer = layer;
        guiFill.setLayer(layer);
        guiRadius.setLayer(layer);
    }

    @Override
    public MapLayer getLayer() {
        return layer;
    }

    @Override
    public void parse(final Halo halo) {
        if (halo != null) {
            guiFill.parse(halo.getFill());
            guiRadius.parse(halo.getRadius());
        }
    }

    @Override
    public Halo create() {
        return getStyleFactory().halo(guiFill.create(), guiRadius.create());
    }

    @Override
    public void apply() {
    }
    
    @Override
    protected Object[] getFirstColumnComponents() {
        return new Object[]{guiLabelWidth,guiFill};
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        guiFill = new JFillPane();
        guiLabelWidth = new JLabel();
        guiRadius = new JNumberExpressionPane();

        setOpaque(false);

        guiFill.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                JHaloPane.this.propertyChange(evt);
            }
        });

        guiLabelWidth.setText(MessageBundle.getString("radius")); // NOI18N

        guiRadius.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                JHaloPane.this.propertyChange(evt);
            }
        });

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(guiLabelWidth)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(guiRadius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
            .addComponent(guiFill, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.LEADING, false)
                    .addComponent(guiLabelWidth, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(guiRadius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(guiFill, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void propertyChange(PropertyChangeEvent evt) {//GEN-FIRST:event_propertyChange
        // TODO add your handling code here:
        if (PROPERTY_TARGET.equalsIgnoreCase(evt.getPropertyName())) {            
            firePropertyChange(PROPERTY_TARGET, null, create());
            parse(create());
        }
    }//GEN-LAST:event_propertyChange

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JFillPane guiFill;
    private JLabel guiLabelWidth;
    private JNumberExpressionPane guiRadius;
    // End of variables declaration//GEN-END:variables

}
