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
package org.geotoolkit.gui.swing.propertyedit;


import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import org.geotoolkit.factory.FactoryFinder;
import org.apache.sis.util.iso.SimpleInternationalString;
import org.geotoolkit.map.MapContext;

/**
 * Context general panel
 *
 * @author Johann Sorel
 * @module
 */
public class ContextGeneralPanel extends AbstractPropertyPane {

    private MapContext context = null;

    /** Creates new form ContextGeneralPanel */
    public ContextGeneralPanel() {
        super("General",null,null,"General");
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new JLabel();
        gui_jtf_name = new JTextField();

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | Font.BOLD));
        jLabel1.setText("Name : ");

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(gui_jtf_name, GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(gui_jtf_name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void parse(){
        if(context != null){
            gui_jtf_name.setText(context.getDescription().getTitle().toString());
        }else{
            gui_jtf_name.setText("");
        }
    }


    @Override
    public void setTarget(final Object target) {
        if(target instanceof MapContext){
            context = (MapContext) target;
        }else{
            context = null;
        }
        parse();
    }

    @Override
    public void apply() {
        if(context != null){
            context.setDescription(FactoryFinder.getStyleFactory(null).description(
                    new SimpleInternationalString(gui_jtf_name.getText()),
                    new SimpleInternationalString("")));
        }
    }

    @Override
    public void reset() {
        parse();
    }

    @Override
    public boolean canHandle(Object target) {
        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JTextField gui_jtf_name;
    private JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

}
