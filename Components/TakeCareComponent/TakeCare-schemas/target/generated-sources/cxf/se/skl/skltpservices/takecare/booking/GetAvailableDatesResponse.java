
package se.skl.skltpservices.takecare.booking;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="GetAvailableDatesResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "getAvailableDatesResult"
})
@XmlRootElement(name = "GetAvailableDatesResponse")
public class GetAvailableDatesResponse {

    @XmlElement(name = "GetAvailableDatesResult")
    protected String getAvailableDatesResult;

    /**
     * Gets the value of the getAvailableDatesResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetAvailableDatesResult() {
        return getAvailableDatesResult;
    }

    /**
     * Sets the value of the getAvailableDatesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetAvailableDatesResult(String value) {
        this.getAvailableDatesResult = value;
    }

}
