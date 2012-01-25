
package riv.itintegration.engagementindex._1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * <p>Java class for EngagementType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EngagementType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="registeredResidentIdentification" type="{urn:riv:itintegration:engagementindex:1}RegisteredResidentIdentificationType"/>
 *         &lt;element name="serviceDomain" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element name="categorization" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="logicalAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="businessObjectInstanceIdentifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="healthIssueThreadId" type="{urn:riv:itintegration:engagementindex:1}HsaIdType" minOccurs="0"/>
 *         &lt;element name="mostRecentContent" type="{urn:riv:itintegration:engagementindex:1}TS" minOccurs="0"/>
 *         &lt;element name="sourceSystem" type="{urn:riv:itintegration:engagementindex:1}HsaIdType"/>
 *         &lt;element name="creationTime" type="{urn:riv:itintegration:engagementindex:1}TS" minOccurs="0"/>
 *         &lt;element name="updateTime" type="{urn:riv:itintegration:engagementindex:1}TS" minOccurs="0"/>
 *         &lt;element name="owner" type="{urn:riv:itintegration:engagementindex:1}HsaIdType" minOccurs="0"/>
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
@XmlType(name = "EngagementType", propOrder = {
    "registeredResidentIdentification",
    "serviceDomain",
    "categorization",
    "logicalAddress",
    "businessObjectInstanceIdentifier",
    "healthIssueThreadId",
    "mostRecentContent",
    "sourceSystem",
    "creationTime",
    "updateTime",
    "owner",
    "any"
})
public class EngagementType {

    @XmlElement(required = true)
    protected String registeredResidentIdentification;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String serviceDomain;
    @XmlElement(required = true)
    protected String categorization;
    @XmlElement(required = true)
    protected String logicalAddress;
    @XmlElement(required = true)
    protected String businessObjectInstanceIdentifier;
    protected String healthIssueThreadId;
    protected String mostRecentContent;
    @XmlElement(required = true)
    protected String sourceSystem;
    protected String creationTime;
    protected String updateTime;
    protected String owner;
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
     * Gets the value of the serviceDomain property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceDomain() {
        return serviceDomain;
    }

    /**
     * Sets the value of the serviceDomain property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceDomain(String value) {
        this.serviceDomain = value;
    }

    /**
     * Gets the value of the categorization property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategorization() {
        return categorization;
    }

    /**
     * Sets the value of the categorization property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategorization(String value) {
        this.categorization = value;
    }

    /**
     * Gets the value of the logicalAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogicalAddress() {
        return logicalAddress;
    }

    /**
     * Sets the value of the logicalAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogicalAddress(String value) {
        this.logicalAddress = value;
    }

    /**
     * Gets the value of the businessObjectInstanceIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusinessObjectInstanceIdentifier() {
        return businessObjectInstanceIdentifier;
    }

    /**
     * Sets the value of the businessObjectInstanceIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusinessObjectInstanceIdentifier(String value) {
        this.businessObjectInstanceIdentifier = value;
    }

    /**
     * Gets the value of the healthIssueThreadId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHealthIssueThreadId() {
        return healthIssueThreadId;
    }

    /**
     * Sets the value of the healthIssueThreadId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHealthIssueThreadId(String value) {
        this.healthIssueThreadId = value;
    }

    /**
     * Gets the value of the mostRecentContent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMostRecentContent() {
        return mostRecentContent;
    }

    /**
     * Sets the value of the mostRecentContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMostRecentContent(String value) {
        this.mostRecentContent = value;
    }

    /**
     * Gets the value of the sourceSystem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceSystem() {
        return sourceSystem;
    }

    /**
     * Sets the value of the sourceSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceSystem(String value) {
        this.sourceSystem = value;
    }

    /**
     * Gets the value of the creationTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreationTime() {
        return creationTime;
    }

    /**
     * Sets the value of the creationTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreationTime(String value) {
        this.creationTime = value;
    }

    /**
     * Gets the value of the updateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * Sets the value of the updateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpdateTime(String value) {
        this.updateTime = value;
    }

    /**
     * Gets the value of the owner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the value of the owner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwner(String value) {
        this.owner = value;
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
