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


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "agsCitationTitleOrAgsCitationIdentifierOrAgsCitationNumberOrAgsCitationChronology"
})
@XmlRootElement(name = "ags:citation")
public class AgsCitation {

    @XmlElements({
        @XmlElement(name = "ags:citationTitle", type = AgsCitationTitle.class),
        @XmlElement(name = "ags:citationIdentifier", type = AgsCitationIdentifier.class),
        @XmlElement(name = "ags:citationNumber", type = AgsCitationNumber.class),
        @XmlElement(name = "ags:citationChronology", type = AgsCitationChronology.class)
    })
    protected List<Object> agsCitationTitleOrAgsCitationIdentifierOrAgsCitationNumberOrAgsCitationChronology;

    /**
     * Gets the value of the agsCitationTitleOrAgsCitationIdentifierOrAgsCitationNumberOrAgsCitationChronology property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the agsCitationTitleOrAgsCitationIdentifierOrAgsCitationNumberOrAgsCitationChronology property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAgsCitationTitleOrAgsCitationIdentifierOrAgsCitationNumberOrAgsCitationChronology().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AgsCitationTitle }
     * {@link AgsCitationIdentifier }
     * {@link AgsCitationNumber }
     * {@link AgsCitationChronology }
     * 
     * 
     */
    public List<Object> getAgsCitationTitleOrAgsCitationIdentifierOrAgsCitationNumberOrAgsCitationChronology() {
        if (agsCitationTitleOrAgsCitationIdentifierOrAgsCitationNumberOrAgsCitationChronology == null) {
            agsCitationTitleOrAgsCitationIdentifierOrAgsCitationNumberOrAgsCitationChronology = new ArrayList<Object>();
        }
        return this.agsCitationTitleOrAgsCitationIdentifierOrAgsCitationNumberOrAgsCitationChronology;
    }

}
