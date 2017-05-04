/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2008 - 2009, Geomatys
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
package org.geotoolkit.wps.xml.v100;

import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.geotoolkit.ows.xml.v110.DomainMetadataType;
import org.geotoolkit.wps.xml.LiteralDataDescription;


/**
 * Description of a literal output (or input).
 *
 * <p>Java class for LiteralOutputType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="LiteralOutputType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/ows/1.1}DataType" minOccurs="0"/>
 *         &lt;element name="UOMs" type="{http://www.opengis.net/wps/1.0.0}SupportedUOMsType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 * @module
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LiteralOutputType", propOrder = {
    "dataType",
    "uoMs"
})
@XmlSeeAlso({
    LiteralInputType.class
})
public class LiteralOutputType implements LiteralDataDescription {

    @XmlElement(name = "DataType", namespace = "http://www.opengis.net/ows/1.1")
    protected DomainMetadataType dataType;
    @XmlElement(name = "UOMs", namespace = "")
    protected SupportedUOMsType uoMs;

    public LiteralOutputType() {

    }

    public LiteralOutputType(DomainMetadataType dataType, SupportedUOMsType uoMs) {
        this.dataType = dataType;
        this.uoMs = uoMs;
    }

    /**
     * Data type of this set of values (e.g. integer, real, etc). This data type metadata should be included for each quantity whose data type is not a string.
     *
     * @return
     *     possible object is
     *     {@link DomainMetadataType }
     *
     */
    public DomainMetadataType getDataType() {
        return dataType;
    }

    /**
     * Data type of this set of values (e.g. integer, real, etc). This data type metadata should be included for each quantity whose data type is not a string.
     *
     * @param value
     *     allowed object is
     *     {@link DomainMetadataType }
     *
     */
    public void setDataType(final DomainMetadataType value) {
        this.dataType = value;
    }

    /**
     * Gets the value of the uoMs property.
     *
     * @return
     *     possible object is
     *     {@link SupportedUOMsType }
     *
     */
    public SupportedUOMsType getUOMs() {
        return uoMs;
    }

    /**
     * Sets the value of the uoMs property.
     *
     * @param value
     *     allowed object is
     *     {@link SupportedUOMsType }
     *
     */
    public void setUOMs(final SupportedUOMsType value) {
        this.uoMs = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[").append(this.getClass().getSimpleName()).append("]\n");
        if (dataType != null) {
            sb.append("Data type:").append(dataType).append('\n');
        }
        if (uoMs != null) {
            sb.append("uoMs:").append(uoMs).append('\n');
        }
        return sb.toString();
    }

    /**
     * Verify that this entry is identical to the specified object.
     * @param object Object to compare
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof LiteralOutputType) {
            final LiteralOutputType that = (LiteralOutputType) object;
            return Objects.equals(this.dataType, that.dataType) &&
                   Objects.equals(this.uoMs, that.uoMs);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.dataType);
        hash = 67 * hash + Objects.hashCode(this.uoMs);
        return hash;
    }

}
