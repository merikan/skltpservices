
package se.riv.itintegration.registry.getsupportedservicecontractsresponder.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the se.riv.itintegration.registry.getsupportedservicecontractsresponder.v1 package. 
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

    private final static QName _GetSupportedServiceContracts_QNAME = new QName("urn:riv:itintegration:registry:GetSupportedServiceContractsResponder:1", "GetSupportedServiceContracts");
    private final static QName _GetSupportedServiceContractsResponse_QNAME = new QName("urn:riv:itintegration:registry:GetSupportedServiceContractsResponder:1", "GetSupportedServiceContractsResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: se.riv.itintegration.registry.getsupportedservicecontractsresponder.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetSupportedServiceContractsType }
     * 
     */
    public GetSupportedServiceContractsType createGetSupportedServiceContractsType() {
        return new GetSupportedServiceContractsType();
    }

    /**
     * Create an instance of {@link GetSupportedServiceContractsResponseType }
     * 
     */
    public GetSupportedServiceContractsResponseType createGetSupportedServiceContractsResponseType() {
        return new GetSupportedServiceContractsResponseType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSupportedServiceContractsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:itintegration:registry:GetSupportedServiceContractsResponder:1", name = "GetSupportedServiceContracts")
    public JAXBElement<GetSupportedServiceContractsType> createGetSupportedServiceContracts(GetSupportedServiceContractsType value) {
        return new JAXBElement<GetSupportedServiceContractsType>(_GetSupportedServiceContracts_QNAME, GetSupportedServiceContractsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSupportedServiceContractsResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:itintegration:registry:GetSupportedServiceContractsResponder:1", name = "GetSupportedServiceContractsResponse")
    public JAXBElement<GetSupportedServiceContractsResponseType> createGetSupportedServiceContractsResponse(GetSupportedServiceContractsResponseType value) {
        return new JAXBElement<GetSupportedServiceContractsResponseType>(_GetSupportedServiceContractsResponse_QNAME, GetSupportedServiceContractsResponseType.class, null, value);
    }

}
