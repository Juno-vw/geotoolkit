/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2012, Geomatys
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotoolkit.ogc.xml.v100;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;

/**
 *
 * @author Guilhem Legal (Geomatys)
 * @module
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PropertyIsLessThanOrEqualTo")
public class PropertyIsLessThanOrEqualToType extends BinaryComparisonOpType implements PropertyIsLessThanOrEqualTo {

    public PropertyIsLessThanOrEqualToType() {

    }

    /**
     * Build a new Binary comparison operator
     */
    public PropertyIsLessThanOrEqualToType(final LiteralType literal, final PropertyNameType propertyName) {
        super(literal, propertyName);
    }

    public PropertyIsLessThanOrEqualToType(final PropertyIsLessThanOrEqualToType that) {
        super(that);
    }

    @Override
    public ComparisonOpsType getClone() {
        return new PropertyIsLessThanOrEqualToType(this);
    }
}
