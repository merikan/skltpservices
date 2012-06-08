
package se.skl.skltpservices.takecare.booking.gettimetypesrequest;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="TimeTypes">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="TimeType" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="TimeTypeId" type="{http://www.w3.org/2001/XMLSchema}unsignedShort"/>
 *                             &lt;element name="TimeTypeName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="CareUnitIdType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="CareUnitId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="CareUnitName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="MsgType" type="{http://www.w3.org/2001/XMLSchema}string" fixed="Response" />
 *       &lt;attribute name="System" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="SystemInstance" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
 *       &lt;attribute name="Time" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" />
 *       &lt;attribute name="User" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="CareUnitType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="CareUnit" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Method" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "timeTypes"
})
@XmlRootElement(name = "ProfdocHISMessage")
public class ProfdocHISMessage {

    @XmlElement(name = "TimeTypes", required = true)
    protected ProfdocHISMessage.TimeTypes timeTypes;
    @XmlAttribute(name = "MsgType")
    protected String msgType;
    @XmlAttribute(name = "System", required = true)
    protected String system;
    @XmlAttribute(name = "SystemInstance", required = true)
    @XmlSchemaType(name = "unsignedShort")
    protected int systemInstance;
    @XmlAttribute(name = "Time", required = true)
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger time;
    @XmlAttribute(name = "User", required = true)
    protected String user;
    @XmlAttribute(name = "CareUnitType", required = true)
    protected String careUnitType;
    @XmlAttribute(name = "CareUnit", required = true)
    protected String careUnit;
    @XmlAttribute(name = "Method", required = true)
    protected String method;

    /**
     * Gets the value of the timeTypes property.
     * 
     * @return
     *     possible object is
     *     {@link ProfdocHISMessage.TimeTypes }
     *     
     */
    public ProfdocHISMessage.TimeTypes getTimeTypes() {
        return timeTypes;
    }

    /**
     * Sets the value of the timeTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProfdocHISMessage.TimeTypes }
     *     
     */
    public void setTimeTypes(ProfdocHISMessage.TimeTypes value) {
        this.timeTypes = value;
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
            return "Response";
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
     * Gets the value of the system property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystem() {
        return system;
    }

    /**
     * Sets the value of the system property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystem(String value) {
        this.system = value;
    }

    /**
     * Gets the value of the systemInstance property.
     * 
     */
    public int getSystemInstance() {
        return systemInstance;
    }

    /**
     * Sets the value of the systemInstance property.
     * 
     */
    public void setSystemInstance(int value) {
        this.systemInstance = value;
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
     * Gets the value of the user property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the value of the user property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUser(String value) {
        this.user = value;
    }

    /**
     * Gets the value of the careUnitType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCareUnitType() {
        return careUnitType;
    }

    /**
     * Sets the value of the careUnitType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCareUnitType(String value) {
        this.careUnitType = value;
    }

    /**
     * Gets the value of the careUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCareUnit() {
        return careUnit;
    }

    /**
     * Sets the value of the careUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCareUnit(String value) {
        this.careUnit = value;
    }

    /**
     * Gets the value of the method property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMethod() {
        return method;
    }

    /**
     * Sets the value of the method property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMethod(String value) {
        this.method = value;
    }


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
     *         &lt;element name="TimeType" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="TimeTypeId" type="{http://www.w3.org/2001/XMLSchema}unsignedShort"/>
     *                   &lt;element name="TimeTypeName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="CareUnitIdType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="CareUnitId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="CareUnitName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "timeType"
    })
    public static class TimeTypes {

        @XmlElement(name = "TimeType")
        protected List<ProfdocHISMessage.TimeTypes.TimeType> timeType;
        @XmlAttribute(name = "CareUnitIdType", required = true)
        protected String careUnitIdType;
        @XmlAttribute(name = "CareUnitId", required = true)
        protected String careUnitId;
        @XmlAttribute(name = "CareUnitName", required = true)
        protected String careUnitName;

        /**
         * Gets the value of the timeType property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the timeType property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTimeType().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ProfdocHISMessage.TimeTypes.TimeType }
         * 
         * 
         */
        public List<ProfdocHISMessage.TimeTypes.TimeType> getTimeType() {
            if (timeType == null) {
                timeType = new ArrayList<ProfdocHISMessage.TimeTypes.TimeType>();
            }
            return this.timeType;
        }

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
         * Gets the value of the careUnitName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCareUnitName() {
            return careUnitName;
        }

        /**
         * Sets the value of the careUnitName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCareUnitName(String value) {
            this.careUnitName = value;
        }


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
         *         &lt;element name="TimeTypeId" type="{http://www.w3.org/2001/XMLSchema}unsignedShort"/>
         *         &lt;element name="TimeTypeName" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
            "timeTypeId",
            "timeTypeName"
        })
        public static class TimeType {

            @XmlElement(name = "TimeTypeId")
            @XmlSchemaType(name = "unsignedShort")
            protected int timeTypeId;
            @XmlElement(name = "TimeTypeName", required = true)
            protected String timeTypeName;

            /**
             * Gets the value of the timeTypeId property.
             * 
             */
            public int getTimeTypeId() {
                return timeTypeId;
            }

            /**
             * Sets the value of the timeTypeId property.
             * 
             */
            public void setTimeTypeId(int value) {
                this.timeTypeId = value;
            }

            /**
             * Gets the value of the timeTypeName property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTimeTypeName() {
                return timeTypeName;
            }

            /**
             * Sets the value of the timeTypeName property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTimeTypeName(String value) {
                this.timeTypeName = value;
            }

        }

    }

}
