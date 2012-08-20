package se.riv.itintegration.registry;

import javax.jws.WebService;

import org.mule.util.StringUtils;

import se.riv.itintegration.registry.getlogicaladdresseesbyservicecontract.v1.rivtabp21.GetLogicalAddresseesByServiceContractResponderInterface;
import se.riv.itintegration.registry.getlogicaladdresseesbyservicecontractresponder.v1.GetLogicalAddresseesByServiceContractResponseType;
import se.riv.itintegration.registry.getlogicaladdresseesbyservicecontractresponder.v1.GetLogicalAddresseesByServiceContractType;
import se.riv.itintegration.registry.v1.ServiceContractNamespaceType;

@WebService(serviceName = "GetLogicalAddresseesByServiceContractResponderService", endpointInterface = "se.riv.itintegration.registry.getlogicaladdresseesbyservicecontract.v1.rivtabp21.GetLogicalAddresseesByServiceContractResponderInterface", portName = "GetLogicalAddresseesByServiceContractResponderPort", targetNamespace = "urn:riv:itintegration:registry:GetLogicalAddresseesByServiceContract:1:rivtabp21", wsdlLocation = "schemas/interactions/GetLogicalAddresseesByServiceContractInteraction/GetLogicalAddresseesByServiceContractInteraction_1.0_RIVTABP21.wsdl")
public class GetLogicalAddresseesByServiceContractTestProducer implements
		GetLogicalAddresseesByServiceContractResponderInterface {

	@Override
	public GetLogicalAddresseesByServiceContractResponseType getLogicalAddresseesByServiceContract(
			String logicalAddress, GetLogicalAddresseesByServiceContractType parameters) {

		String serviceConsumerHsaId = parameters.getServiceConsumerHsaId();
		ServiceContractNamespaceType serviceContractNamespaceType = parameters.getServiceContractNameSpace();
		GetLogicalAddresseesByServiceContractResponseType response = new GetLogicalAddresseesByServiceContractResponseType();

		if (StringUtils.equals(serviceConsumerHsaId, "Kalel")) {
			response.getLogicalAddress().add("Lion");
			response.getLogicalAddress().add("Cliff Valley");
			response.getLogicalAddress().add("Aoaoao");
		}

		return response;
	}

}
