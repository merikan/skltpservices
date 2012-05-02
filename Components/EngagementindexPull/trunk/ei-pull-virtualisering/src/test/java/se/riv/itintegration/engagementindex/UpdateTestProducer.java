package se.riv.itintegration.engagementindex;

import javax.jws.WebService;

import se.riv.itintegration.engagementindex.update.v1.rivtabp21.UpdateResponderInterface;
import se.riv.itintegration.engagementindex.updateresponder.v1.UpdateResponseType;
import se.riv.itintegration.engagementindex.updateresponder.v1.UpdateType;

@WebService(serviceName = "UpdateResponderService", 
			endpointInterface = "se.riv.itintegration.engagementindex.update.v1.rivtabp21.UpdateResponderInterface",
			portName = "UpdateResponderPort", 
			targetNamespace = "urn:riv:itintegration:engagementindex:Update:1:rivtabp21", 
			wsdlLocation = "schemas/interactions/UpdateInteraction/UpdateInteraction_1.0_RIVTABP21.wsdl")
public class UpdateTestProducer implements UpdateResponderInterface {

	@Override
	public UpdateResponseType update(String arg0, UpdateType arg1) {
		
		UpdateResponseType response = new UpdateResponseType();
		
		return response;
	}

}
