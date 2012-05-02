
package se.riv.itintegration.registry.getlogicaladdresseesbyservicecontractresponder.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the se.riv.itintegration.registry.getlogicaladdresseesbyservicecontractresponder.v1 package. 
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

    private final static QName _GetLogicalAddresseesByServiceContractResponse_QNAME = new QName("urn:riv:itintegration:registry:GetLogicalAddresseesByServiceContractResponder:1", "GetLogicalAddresseesByServiceContractResponse");
    private final static QName _GetLogicalAddresseesByServiceContract_QNAME = new QName("urn:riv:itintegration:registry:GetLogicalAddresseesByServiceContractResponder:1", "GetLogicalAddresseesByServiceContract");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: se.riv.itintegration.registry.getlogicaladdresseesbyservicecontractresponder.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetLogicalAddresseesByServiceContractType }
     * 
     */
    public GetLogicalAddresseesByServiceContractType createGetLogicalAddresseesByServiceContractType() {
        return new GetLogicalAddresseesByServiceContractType();
    }

    /**
     * Create an instance of {@link GetLogicalAddresseesByServiceContractResponseType }
     * 
     */
    public GetLogicalAddresseesByServiceContractResponseType createGetLogicalAddresseesByServiceContractResponseType() {
        return new GetLogicalAddresseesByServiceContractResponseType();
    }

    /**
     * Create an instance of {@link LogicalAddresseeRecordType }
     * 
     */
    public LogicalAddresseeRecordType createLogicalAddresseeRecordType() {
        return new LogicalAddresseeRecordType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLogicalAddresseesByServiceContractResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:itintegration:registry:GetLogicalAddresseesByServiceContractResponder:1", name = "GetLogicalAddresseesByServiceContractResponse")
    public JAXBElement<GetLogicalAddresseesByServiceContractResponseType> createGetLogicalAddresseesByServiceContractResponse(GetLogicalAddresseesByServiceContractResponseType value) {
        return new JAXBElement<GetLogicalAddresseesByServiceContractResponseType>(_GetLogicalAddresseesByServiceContractResponse_QNAME, GetLogicalAddresseesByServiceContractResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLogicalAddresseesByServiceContractType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:itintegration:registry:GetLogicalAddresseesByServiceContractResponder:1", name = "GetLogicalAddresseesByServiceContract")
    public JAXBElement<GetLogicalAddresseesByServiceContractType> createGetLogicalAddresseesByServiceContract(GetLogicalAddresseesByServiceContractType value) {
        return new JAXBElement<GetLogicalAddresseesByServiceContractType>(_GetLogicalAddresseesByServiceContract_QNAME, GetLogicalAddresseesByServiceContractType.class, null, value);
    }

}
