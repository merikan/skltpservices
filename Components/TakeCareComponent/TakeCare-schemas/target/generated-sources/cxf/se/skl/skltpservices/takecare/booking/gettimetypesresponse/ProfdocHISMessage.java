
package se.skl.skltpservices.takecare.booking.gettimetypesresponse;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *         &lt;element name="CareUnitIdType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CareUnitId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TimeTypeRequest" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="MsgType" type="{http://www.w3.org/2001/XMLSchema}string" fixed="Request" />
 *       &lt;attribute name="Time" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" />
 *       &lt;attribute name="InvokingSystem" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "careUnitIdType",
    "careUnitId",
    "timeTypeRequest"
})
@XmlRootElement(name = "ProfdocHISMessage")
public class ProfdocHISMessage {

    @XmlElement(name = "CareUnitIdType", required = true)
    protected String careUnitIdType;
    @XmlElement(name = "CareUnitId", required = true)
    protected String careUnitId;
    @XmlElement(name = "TimeTypeRequest", required = true)
    protected String timeTypeRequest;
    @XmlAttribute(name = "MsgType")
    protected String msgType;
    @XmlAttribute(name = "Time", required = true)
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger time;
    @XmlAttribute(name = "InvokingSystem", required = true)
    protected String invokingSystem;

    /**
     * Gets the value of the careUnitIdType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCareUnitIdType() {
        return careUnitIdType;
    }

    /**
     * Sets the value of the careUnitIdType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCareUnitIdType(String value) {
        this.careUnitIdType = value;
    }

    /**
     * Gets the value of the careUnitId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCareUnitId() {
        return careUnitId;
    }

    /**
     * Sets the value of the careUnitId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCareUnitId(String value) {
        this.careUnitId = value;
    }

    /**
     * Gets the value of the timeTypeRequest property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeTypeRequest() {
        return timeTypeRequest;
    }

    /**
     * Sets the value of the timeTypeRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeTypeRequest(String value) {
        this.timeTypeRequest = value;
    }

    /**
     * Gets the value of the msgType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgType() {
        if (msgType == null) {
            return "Request";
        } else {
            return msgType;
        }
    }

    /**
     * Sets the value of the msgType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgType(String value) {
        this.msgType = value;
    }

    /**
     * Gets the value of the time property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTime() {
        return time;
    }

    /**
     * Sets the value of the time property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTime(BigInteger value) {
        this.time = value;
    }

    /**
     * Gets the value of the invokingSystem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvokingSystem() {
        return invokingSystem;
    }

    /**
     * Sets the value of the invokingSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvokingSystem(String value) {
        this.invokingSystem = value;
    }

}
