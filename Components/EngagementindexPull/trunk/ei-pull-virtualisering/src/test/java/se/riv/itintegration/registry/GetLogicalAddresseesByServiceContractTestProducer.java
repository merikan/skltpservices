package se.riv.itintegration.registry;

import javax.jws.WebService;

import se.riv.itintegration.registry.getlogicaladdresseesbyservicecontract.v1.rivtabp21.GetLogicalAddresseesByServiceContractResponderInterface;
import se.riv.itintegration.registry.getlogicaladdresseesbyservicecontractresponder.v1.GetLogicalAddresseesByServiceContractResponseType;
import se.riv.itintegration.registry.getlogicaladdresseesbyservicecontractresponder.v1.GetLogicalAddresseesByServiceContractType;

@WebService(serviceName = "GetLogicalAddresseesByServiceContractResponderService", 
			endpointInterface = "se.riv.itintegration.registry.getlogicaladdresseesbyservicecontract.v1.rivtabp21.GetLogicalAddresseesByServiceContractResponderInterface",
			portName = "GetLogicalAddresseesByServiceContractResponderPort", 
			targetNamespace = "urn:riv:itintegration:registry:GetLogicalAddresseesByServiceContract:1:rivtabp21", 
			wsdlLocation = "schemas/interactions/GetLogicalAddresseesByServiceContractInteraction/GetLogicalAddresseesByServiceContractInteraction_1.0_RIVTABP21.wsdl")
public class GetLogicalAddresseesByServiceContractTestProducer implements
		GetLogicalAddresseesByServiceContractResponderInterface {

	@Override
	public GetLogicalAddresseesByServiceContractResponseType getLogicalAddresseesByServiceContract(String arg0,
			GetLogicalAddresseesByServiceContractType arg1) {
		
		GetLogicalAddresseesByServiceContractResponseType response = new GetLogicalAddresseesByServiceContractResponseType();
		
		return response;
	}

}
