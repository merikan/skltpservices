
package se.riv.itintegration.engagementindex.getupdatesresponder.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;
import riv.itintegration.engagementindex._1.RegisteredResidentEngagementType;


/**
 * <p>Java class for GetUpdatesResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetUpdatesResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="responseIsComplete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="registeredResidentEngagement" type="{urn:riv:itintegration:engagementindex:1}RegisteredResidentEngagementType" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "GetUpdatesResponseType", propOrder = {
    "responseIsComplete",
    "registeredResidentEngagement",
    "any"
})
public class GetUpdatesResponseType {

    protected boolean responseIsComplete;
    protected List<RegisteredResidentEngagementType> registeredResidentEngagement;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the responseIsComplete property.
     * 
     */
    public boolean isResponseIsComplete() {
        return responseIsComplete;
    }

    /**
     * Sets the value of the responseIsComplete property.
     * 
     */
    public void setResponseIsComplete(boolean value) {
        this.responseIsComplete = value;
    }

    /**
     * Gets the value of the registeredResidentEngagement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the registeredResidentEngagement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRegisteredResidentEngagement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RegisteredResidentEngagementType }
     * 
     * 
     */
    public List<RegisteredResidentEngagementType> getRegisteredResidentEngagement() {
        if (registeredResidentEngagement == null) {
            registeredResidentEngagement = new ArrayList<RegisteredResidentEngagementType>();
        }
        return this.registeredResidentEngagement;
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
