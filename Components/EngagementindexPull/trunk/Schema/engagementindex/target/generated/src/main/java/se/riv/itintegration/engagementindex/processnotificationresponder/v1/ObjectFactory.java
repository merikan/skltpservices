
package se.riv.itintegration.engagementindex.processnotificationresponder.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the se.riv.itintegration.engagementindex.processnotificationresponder.v1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ProcessNotification_QNAME = new QName("urn:riv:itintegration:engagementindex:ProcessNotificationResponder:1", "ProcessNotification");
    private final static QName _ProcessNotificationResponse_QNAME = new QName("urn:riv:itintegration:engagementindex:ProcessNotificationResponder:1", "ProcessNotificationResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: se.riv.itintegration.engagementindex.processnotificationresponder.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ProcessNotificationType }
     * 
     */
    public ProcessNotificationType createProcessNotificationType() {
        return new ProcessNotificationType();
    }

    /**
     * Create an instance of {@link ProcessNotificationResponseType }
     * 
     */
    public ProcessNotificationResponseType createProcessNotificationResponseType() {
        return new ProcessNotificationResponseType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessNotificationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:itintegration:engagementindex:ProcessNotificationResponder:1", name = "ProcessNotification")
    public JAXBElement<ProcessNotificationType> createProcessNotification(ProcessNotificationType value) {
        return new JAXBElement<ProcessNotificationType>(_ProcessNotification_QNAME, ProcessNotificationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessNotificationResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:itintegration:engagementindex:ProcessNotificationResponder:1", name = "ProcessNotificationResponse")
    public JAXBElement<ProcessNotificationResponseType> createProcessNotificationResponse(ProcessNotificationResponseType value) {
        return new JAXBElement<ProcessNotificationResponseType>(_ProcessNotificationResponse_QNAME, ProcessNotificationResponseType.class, null, value);
    }

}
