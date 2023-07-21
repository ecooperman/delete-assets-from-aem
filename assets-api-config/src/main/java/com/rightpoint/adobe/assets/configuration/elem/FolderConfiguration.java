package com.rightpoint.adobe.assets.configuration.elem;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pathToFolder" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "pathToFolder"
})
@XmlRootElement(name = "folderConfiguration")
public class FolderConfiguration {

    @XmlElement(required = true)
    protected String pathToFolder;

    /**
     * Gets the value of the pathToFolder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPathToFolder() {
        return pathToFolder;
    }

    /**
     * Sets the value of the pathToFolder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPathToFolder(String value) {
        this.pathToFolder = value;
    }
}
