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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.agroknow.process.AdapterCDATA;
import com.agroknow.process.Annotatable;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "value" } )
@XmlRootElement(name = "ags:creatorPersonal")
public class AgsCreatorPersonal implements Annotatable {
    @XmlJavaTypeAdapter(AdapterCDATA.class)
	@XmlValue
    protected String value;
	
	@XmlAttribute
	protected String orcid;

    public String getOrcid() {
		return orcid;
	}

	public void setOrcid(String orcid) {
		this.orcid = orcid;
	}

	/**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getvalue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setvalue(String value) {
        this.value = value;
    }

	@Override
	public String getViaf() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setViaf(String viaf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getOnld() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOnld(String onld) {
		// TODO Auto-generated method stub
		
	}
    


}
