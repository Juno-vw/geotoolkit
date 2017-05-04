/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2014, Geomatys
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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2014.04.20 at 07:08:32 PM CEST
//


package org.geotoolkit.owc.xml.v10;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * Service or inline content offering for the resource targetted at OGC compliant clients.
 *
 * <p>Java class for OfferingType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="OfferingType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="operation" type="{http://www.opengis.net/owc/1.0}OperationType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="content" type="{http://www.opengis.net/owc/1.0}ContentType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="styleSet" type="{http://www.opengis.net/owc/1.0}StyleSetType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/choice>
 *       &lt;attribute name="code" use="required" type="{http://www.opengis.net/owc/1.0}OfferingTypeCodeType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OfferingType", propOrder = {
    "operationOrContentOrStyleSet"
})
public class OfferingType {

    @XmlElementRefs({
        @XmlElementRef(name = "operation", namespace = "http://www.opengis.net/owc/1.0", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "styleSet", namespace = "http://www.opengis.net/owc/1.0", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "content", namespace = "http://www.opengis.net/owc/1.0", type = JAXBElement.class, required = false)
    })
    @XmlAnyElement(lax = true)
    protected List<Object> operationOrContentOrStyleSet;
    @XmlAttribute(name = "code", required = true)
    protected String code;

    /**
     * Gets the value of the operationOrContentOrStyleSet property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the operationOrContentOrStyleSet property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOperationOrContentOrStyleSet().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link OperationType }{@code >}
     * {@link JAXBElement }{@code <}{@link StyleSetType }{@code >}
     * {@link JAXBElement }{@code <}{@link ContentType }{@code >}
     * {@link Object }
     * {@link Element }
     *
     *
     */
    public List<Object> getOperationOrContentOrStyleSet() {
        if (operationOrContentOrStyleSet == null) {
            operationOrContentOrStyleSet = new ArrayList<Object>();
        }
        return this.operationOrContentOrStyleSet;
    }

    /**
     * Gets the value of the code property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCode(String value) {
        this.code = value;
    }

}
