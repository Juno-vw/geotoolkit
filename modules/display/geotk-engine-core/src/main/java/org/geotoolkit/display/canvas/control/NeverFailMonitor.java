/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2005 - 2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008 - 2009, Geomatys
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
package org.geotoolkit.display.canvas.control;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotoolkit.util.logging.Logging;

/**
 * Monitor which tryes to never fail the rendering.
 * @author Johann Sorel (Geomatys)
 */
public class NeverFailMonitor extends AbstractCanvasMonitor{

    private static final Logger LOGGER = Logging.getLogger(NeverFailMonitor.class);
    
    /**
     * {@inheritDoc }
     */
    @Override
    public void exceptionOccured(Exception ex, Level level) {
        //just log the error
        LOGGER.log(level,"", ex);
    }

}
