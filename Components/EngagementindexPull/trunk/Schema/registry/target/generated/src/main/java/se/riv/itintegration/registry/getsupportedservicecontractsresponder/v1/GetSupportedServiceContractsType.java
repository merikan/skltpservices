
package se.riv.itintegration.registry.getsupportedservicecontractsresponder.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * <p>Java class for GetSupportedServiceContractsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetSupportedServiceContractsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serviceConsumerHsaId" type="{urn:riv:itintegration:registry:1}HsaIdType"/>
 *         &lt;element name="logicalAdress" type="{urn:riv:itintegration:registry:1}LogicalAddressType"/>
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
@XmlType(name = "GetSupportedServiceContractsType", propOrder = {
    "serviceConsumerHsaId",
    "logicalAdress",
    "any"
})
public class GetSupportedServiceContractsType {

    @XmlElement(required = true)
    protected String serviceConsumerHsaId;
    @XmlElement(required = true)
    protected String logicalAdress;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the serviceConsumerHsaId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceConsumerHsaId() {
        return serviceConsumerHsaId;
    }

    /**
     * Sets the value of the serviceConsumerHsaId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceConsumerHsaId(String value) {
        this.serviceConsumerHsaId = value;
    }

    /**
     * Gets the value of the logicalAdress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogicalAdress() {
        return logicalAdress;
    }

    /**
     * Sets the value of the logicalAdress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogicalAdress(String value) {
        this.logicalAdress = value;
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
