package se.riv.itintegration.engagementindex;

import javax.jws.WebService;

import se.riv.itintegration.engagementindex.getupdates.v1.rivtabp21.GetUpdatesResponderInterface;
import se.riv.itintegration.engagementindex.getupdatesresponder.v1.GetUpdatesResponseType;
import se.riv.itintegration.engagementindex.getupdatesresponder.v1.GetUpdatesType;

@WebService(serviceName = "GetUpdatesResponderService", 
			endpointInterface = "se.riv.itintegration.engagementindex.getupdates.v1.rivtabp21.GetUpdatesResponderInterface",
			portName = "GetUpdatesResponderPort", 
			targetNamespace = "urn:riv:itintegration:engagementindex:GetUpdates:1:rivtabp21", 
			wsdlLocation = "schemas/interactions/GetUpdatesInteraction/GetUpdatesInteraction_1.0_RIVTABP21.wsdl")
public class GetUpdatesTestProducer implements GetUpdatesResponderInterface{

	@Override
	public GetUpdatesResponseType getUpdates(String arg0, GetUpdatesType arg1) {
		GetUpdatesResponseType response = new GetUpdatesResponseType();
		return response;
	}

}
