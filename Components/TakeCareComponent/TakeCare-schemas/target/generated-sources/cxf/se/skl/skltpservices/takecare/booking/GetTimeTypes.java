
package se.skl.skltpservices.takecare.booking;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="tcusername" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tcpassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="externaluser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="careunitidtype" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="careunitid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="xml" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "tcusername",
    "tcpassword",
    "externaluser",
    "careunitidtype",
    "careunitid",
    "xml"
})
@XmlRootElement(name = "GetTimeTypes")
public class GetTimeTypes {

    protected String tcusername;
    protected String tcpassword;
    protected String externaluser;
    protected String careunitidtype;
    protected String careunitid;
    protected String xml;

    /**
     * Gets the value of the tcusername property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTcusername() {
        return tcusername;
    }

    /**
     * Sets the value of the tcusername property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTcusername(String value) {
        this.tcusername = value;
    }

    /**
     * Gets the value of the tcpassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTcpassword() {
        return tcpassword;
    }

    /**
     * Sets the value of the tcpassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTcpassword(String value) {
        this.tcpassword = value;
    }

    /**
     * Gets the value of the externaluser property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternaluser() {
        return externaluser;
    }

    /**
     * Sets the value of the externaluser property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternaluser(String value) {
        this.externaluser = value;
    }

    /**
     * Gets the value of the careunitidtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCareunitidtype() {
        return careunitidtype;
    }

    /**
     * Sets the value of the careunitidtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCareunitidtype(String value) {
        this.careunitidtype = value;
    }

    /**
     * Gets the value of the careunitid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCareunitid() {
        return careunitid;
    }

    /**
     * Sets the value of the careunitid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCareunitid(String value) {
        this.careunitid = value;
    }

    /**
     * Gets the value of the xml property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXml() {
        return xml;
    }

    /**
     * Sets the value of the xml property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXml(String value) {
        this.xml = value;
    }

}
