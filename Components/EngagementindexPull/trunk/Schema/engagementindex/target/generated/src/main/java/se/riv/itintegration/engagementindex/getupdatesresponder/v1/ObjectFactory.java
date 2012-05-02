
package se.riv.itintegration.engagementindex.getupdatesresponder.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the se.riv.itintegration.engagementindex.getupdatesresponder.v1 package. 
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

    private final static QName _GetUpdatesResponse_QNAME = new QName("urn:riv:itintegration:engagementindex:GetUpdatesResponder:1", "GetUpdatesResponse");
    private final static QName _GetUpdates_QNAME = new QName("urn:riv:itintegration:engagementindex:GetUpdatesResponder:1", "GetUpdates");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: se.riv.itintegration.engagementindex.getupdatesresponder.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetUpdatesResponseType }
     * 
     */
    public GetUpdatesResponseType createGetUpdatesResponseType() {
        return new GetUpdatesResponseType();
    }

    /**
     * Create an instance of {@link GetUpdatesType }
     * 
     */
    public GetUpdatesType createGetUpdatesType() {
        return new GetUpdatesType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUpdatesResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:itintegration:engagementindex:GetUpdatesResponder:1", name = "GetUpdatesResponse")
    public JAXBElement<GetUpdatesResponseType> createGetUpdatesResponse(GetUpdatesResponseType value) {
        return new JAXBElement<GetUpdatesResponseType>(_GetUpdatesResponse_QNAME, GetUpdatesResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUpdatesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:itintegration:engagementindex:GetUpdatesResponder:1", name = "GetUpdates")
    public JAXBElement<GetUpdatesType> createGetUpdates(GetUpdatesType value) {
        return new JAXBElement<GetUpdatesType>(_GetUpdates_QNAME, GetUpdatesType.class, null, value);
    }

}
