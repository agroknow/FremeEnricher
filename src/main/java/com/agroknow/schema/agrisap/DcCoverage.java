//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.28 at 02:35:22 PM EET 
//


package com.agroknow.schema.agrisap;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
	    "dctermsSpatialOrDctermsTemporal"
	})
@XmlRootElement(name = "dc:coverage")
public class DcCoverage {

	@XmlElements({
        @XmlElement(name = "dcterms:spatial", type = DctermsSpatial.class),
        @XmlElement(name = "dcterms:temporal", type = DctermsTemporal.class)
    })
	
	protected List<Object> dctermsSpatialOrDctermsTemporal;
	
//    @XmlValue
//    protected String value;
//
//    /**
//     * Gets the value of the value property.
//     * 
//     * @return
//     *     possible object is
//     *     {@link String }
//     *     
//     */
//    public String getvalue() {
//        return value;
//    }
//
//    /**
//     * Sets the value of the value property.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link String }
//     *     
//     */
//    public void setvalue(String value) {
//        this.value = value;
//    }
    
    public List<Object> getDctermsSpatialOrDctermsTemporal() {
        if (dctermsSpatialOrDctermsTemporal == null) {
        	dctermsSpatialOrDctermsTemporal = new ArrayList<Object>();
        }
        return this.dctermsSpatialOrDctermsTemporal;
    }

}
