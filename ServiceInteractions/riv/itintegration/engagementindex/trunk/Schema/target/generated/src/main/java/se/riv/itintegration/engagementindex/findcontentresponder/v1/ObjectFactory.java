
package se.riv.itintegration.engagementindex.findcontentresponder.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the se.riv.itintegration.engagementindex.findcontentresponder.v1 package. 
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

    private final static QName _FindContentResponse_QNAME = new QName("urn:riv:itintegration:engagementindex:FindContentResponder:1", "FindContentResponse");
    private final static QName _FindContent_QNAME = new QName("urn:riv:itintegration:engagementindex:FindContentResponder:1", "FindContent");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: se.riv.itintegration.engagementindex.findcontentresponder.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FindContentType }
     * 
     */
    public FindContentType createFindContentType() {
        return new FindContentType();
    }

    /**
     * Create an instance of {@link FindContentResponseType }
     * 
     */
    public FindContentResponseType createFindContentResponseType() {
        return new FindContentResponseType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindContentResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:itintegration:engagementindex:FindContentResponder:1", name = "FindContentResponse")
    public JAXBElement<FindContentResponseType> createFindContentResponse(FindContentResponseType value) {
        return new JAXBElement<FindContentResponseType>(_FindContentResponse_QNAME, FindContentResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindContentType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:itintegration:engagementindex:FindContentResponder:1", name = "FindContent")
    public JAXBElement<FindContentType> createFindContent(FindContentType value) {
        return new JAXBElement<FindContentType>(_FindContent_QNAME, FindContentType.class, null, value);
    }

}
