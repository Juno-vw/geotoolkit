/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2004-2009, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2009, Geomatys
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotoolkit.referencing.operation;

import java.util.Map;

import org.opengis.referencing.operation.Conversion;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.OperationMethod;
import org.opengis.referencing.operation.CylindricalProjection;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * Base class for cylindrical map projections.
 *
 * @author Martin Desruisseaux (IRD)
 * @version 3.00
 *
 * @see org.geotoolkit.referencing.crs.DefaultProjectedCRS
 * @see <A HREF="http://mathworld.wolfram.com/CylindricalProjection.html">Cylindrical projection on MathWorld</A>
 *
 * @since 2.0
 * @module
 */
public class DefaultCylindricalProjection extends DefaultProjection implements CylindricalProjection {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -969486613826553580L;

    /**
     * Constructs a new projection with the same values than the specified one, together with the
     * specified source and target CRS. While the source conversion can be an arbitrary one, it is
     * typically a {@linkplain DefiningConversion defining conversion}.
     *
     * @param definition The defining conversion.
     * @param sourceCRS  The source CRS.
     * @param targetCRS  The target CRS.
     * @param transform  Transform from positions in the {@linkplain #getSourceCRS source CRS}
     *                   to positions in the {@linkplain #getTargetCRS target CRS}.
     */
    public DefaultCylindricalProjection(final Conversion                definition,
                                        final CoordinateReferenceSystem sourceCRS,
                                        final CoordinateReferenceSystem targetCRS,
                                        final MathTransform             transform)
    {
        super(definition, sourceCRS, targetCRS, transform);
    }

    /**
     * Constructs a projection from a set of properties.
     * The properties given in argument follow the same rules than for the
     * {@linkplain AbstractCoordinateOperation#AbstractCoordinateOperation(Map,
     * CoordinateReferenceSystem, CoordinateReferenceSystem, MathTransform)
     * base-class constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param sourceCRS  The source CRS, or {@code null} if not available.
     * @param targetCRS  The target CRS, or {@code null} if not available.
     * @param transform  Transform from positions in the {@linkplain #getSourceCRS source coordinate
     *                   reference system} to positions in the {@linkplain #getTargetCRS target
     *                   coordinate reference system}.
     * @param method     The operation method.
     */
    public DefaultCylindricalProjection(final Map<String,?>             properties,
                                        final CoordinateReferenceSystem sourceCRS,
                                        final CoordinateReferenceSystem targetCRS,
                                        final MathTransform             transform,
                                        final OperationMethod           method)
    {
        super(properties, sourceCRS, targetCRS, transform, method);
    }
}
