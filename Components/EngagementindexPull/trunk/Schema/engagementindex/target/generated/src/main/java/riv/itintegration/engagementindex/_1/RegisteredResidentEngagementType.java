
package riv.itintegration.engagementindex._1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * <p>Java class for RegisteredResidentEngagementType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RegisteredResidentEngagementType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="registeredResidentIdentification" type="{urn:riv:itintegration:engagementindex:1}RegisteredResidentIdentificationType"/>
 *         &lt;element name="engagement" type="{urn:riv:itintegration:engagementindex:1}EngagementType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;any/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegisteredResidentEngagementType", propOrder = {
    "registeredResidentIdentification",
    "engagement",
    "any"
})
public class RegisteredResidentEngagementType {

    @XmlElement(required = true)
    protected String registeredResidentIdentification;
    protected List<EngagementType> engagement;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the registeredResidentIdentification property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegisteredResidentIdentification() {
        return registeredResidentIdentification;
    }

    /**
     * Sets the value of the registeredResidentIdentification property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegisteredResidentIdentification(String value) {
        this.registeredResidentIdentification = value;
    }

    /**
     * Gets the value of the engagement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the engagement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEngagement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EngagementType }
     * 
     * 
     */
    public List<EngagementType> getEngagement() {
        if (engagement == null) {
            engagement = new ArrayList<EngagementType>();
        }
        return this.engagement;
    }

    /**
     * Gets the value of the any property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Element }
     * {@link Object }
     * 
     * 
     */
    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }

}
