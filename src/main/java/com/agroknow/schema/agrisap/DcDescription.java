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
    "agsDescriptionNotesOrAgsDescriptionEditionOrDctermsAbstract"
})
@XmlRootElement(name = "dc:description")
public class DcDescription {

    @XmlElements({
        @XmlElement(name = "ags:descriptionNotes", type = AgsDescriptionNotes.class),
        @XmlElement(name = "ags:descriptionEdition", type = AgsDescriptionEdition.class),
        @XmlElement(name = "dcterms:abstract", type = DctermsAbstract.class)
    })
    protected List<Object> agsDescriptionNotesOrAgsDescriptionEditionOrDctermsAbstract;

    /**
     * Gets the value of the agsDescriptionNotesOrAgsDescriptionEditionOrDctermsAbstract property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the agsDescriptionNotesOrAgsDescriptionEditionOrDctermsAbstract property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAgsDescriptionNotesOrAgsDescriptionEditionOrDctermsAbstract().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AgsDescriptionNotes }
     * {@link AgsDescriptionEdition }
     * {@link DctermsAbstract }
     * 
     * 
     */
    public List<Object> getAgsDescriptionNotesOrAgsDescriptionEditionOrDctermsAbstract() {
        if (agsDescriptionNotesOrAgsDescriptionEditionOrDctermsAbstract == null) {
            agsDescriptionNotesOrAgsDescriptionEditionOrDctermsAbstract = new ArrayList<Object>();
        }
        return this.agsDescriptionNotesOrAgsDescriptionEditionOrDctermsAbstract;
    }

}
