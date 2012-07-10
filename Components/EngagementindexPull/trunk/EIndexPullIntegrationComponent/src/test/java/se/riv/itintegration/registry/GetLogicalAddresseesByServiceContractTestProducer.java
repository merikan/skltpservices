package se.riv.itintegration.registry;

import javax.jws.WebService;

import org.mule.util.StringUtils;
import se.riv.itintegration.registry.getlogicaladdresseesbyservicecontract.v1.rivtabp21.GetLogicalAddresseesByServiceContractResponderInterface;
import se.riv.itintegration.registry.getlogicaladdresseesbyservicecontractresponder.v1.GetLogicalAddresseesByServiceContractResponseType;
import se.riv.itintegration.registry.getlogicaladdresseesbyservicecontractresponder.v1.GetLogicalAddresseesByServiceContractType;
import se.riv.itintegration.registry.v1.ServiceContractNamespaceType;

import java.util.ArrayList;

@WebService(serviceName = "GetLogicalAddresseesByServiceContractResponderService", 
			endpointInterface = "se.riv.itintegration.registry.getlogicaladdresseesbyservicecontract.v1.rivtabp21.GetLogicalAddresseesByServiceContractResponderInterface",
			portName = "GetLogicalAddresseesByServiceContractResponderPort", 
			targetNamespace = "urn:riv:itintegration:registry:GetLogicalAddresseesByServiceContract:1:rivtabp21", 
			wsdlLocation = "schemas/interactions/GetLogicalAddresseesByServiceContractInteraction/GetLogicalAddresseesByServiceContractInteraction_1.0_RIVTABP21.wsdl")
public class GetLogicalAddresseesByServiceContractTestProducer implements GetLogicalAddresseesByServiceContractResponderInterface {

	@Override
	public GetLogicalAddresseesByServiceContractResponseType getLogicalAddresseesByServiceContract(String logicalAddress, GetLogicalAddresseesByServiceContractType parameters) {
        String serviceConsumerHsaId = parameters.getServiceConsumerHsaId();
        ServiceContractNamespaceType serviceContractNamespaceType = parameters.getServiceContractNameSpace();
		GetLogicalAddresseesByServiceContractResponseType response = new GetLogicalAddresseesByServiceContractResponseType();
        ArrayList<String> logicalAddresses = new ArrayList<String>();
        if (StringUtils.equals(serviceConsumerHsaId, "Kalel")) {
            logicalAddresses.add("Lion");
            logicalAddresses.add("Cliff Valley");
            logicalAddresses.add("Aoaoao");
        }
        // Not a nice pattern but JAX only generates a getter.
        response.getLogicalAddress().addAll(logicalAddresses);
		return response;
	}

}
