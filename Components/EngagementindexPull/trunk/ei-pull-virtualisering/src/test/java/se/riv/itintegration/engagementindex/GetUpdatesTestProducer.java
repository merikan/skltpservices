package se.riv.itintegration.engagementindex;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.jws.WebService;

import riv.itintegration.engagementindex._1.EngagementType;
import riv.itintegration.engagementindex._1.RegisteredResidentEngagementType;
import se.riv.itintegration.engagementindex.getupdates.v1.rivtabp21.GetUpdatesResponderInterface;
import se.riv.itintegration.engagementindex.getupdatesresponder.v1.GetUpdatesResponseType;
import se.riv.itintegration.engagementindex.getupdatesresponder.v1.GetUpdatesType;

@WebService(serviceName = "GetUpdatesResponderService",
        endpointInterface = "se.riv.itintegration.engagementindex.getupdates.v1.rivtabp21.GetUpdatesResponderInterface",
        portName = "GetUpdatesResponderPort",
        targetNamespace = "urn:riv:itintegration:engagementindex:GetUpdates:1:rivtabp21",
        wsdlLocation = "schemas/interactions/GetUpdatesInteraction/GetUpdatesInteraction_1.0_RIVTABP21.wsdl")
public class GetUpdatesTestProducer implements GetUpdatesResponderInterface {

	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	@Override
	public GetUpdatesResponseType getUpdates(String arg0, GetUpdatesType request) {
		String serviceDomain = request.getServiceDomain();
		GetUpdatesResponseType response = new GetUpdatesResponseType();
		response.setResponseIsComplete(true);
		response.getRegisteredResidentEngagement().add(createRegisteredResidentEngagementType(serviceDomain, "197303160555"));
		return response;
	}

	private RegisteredResidentEngagementType createRegisteredResidentEngagementType(String serviceDomain,
			String registeredResidentIdentification) {
		RegisteredResidentEngagementType response = new RegisteredResidentEngagementType();
		response.setRegisteredResidentIdentification(registeredResidentIdentification);
		response.getEngagement().add(createEngagement(serviceDomain, registeredResidentIdentification));
		return response;
	}

	private EngagementType createEngagement(String serviceDomain, String registeredResidentIdentification) {
		EngagementType engagement = new EngagementType();
		engagement.setBusinessObjectInstanceIdentifier("bookingId");
		engagement.setCategorization("Booking");
		engagement.setClinicalProcessInterestId(UUID.randomUUID().toString());
		engagement.setCreationTime(dateFormat.format(new Date()));
		engagement.setLogicalAddress("Landstingets hsaid:Vårdgivarens HSA-id:Enhetens hsaid");
		engagement.setMostRecentContent(dateFormat.format(new Date()));
		engagement.setOwner("HSA-id");
		engagement.setRegisteredResidentIdentification(registeredResidentIdentification);
		engagement.setServiceDomain(serviceDomain);
		engagement.setSourceSystem("Systemets HSA-ID");
		engagement.setUpdateTime(dateFormat.format(new Date()));
		return engagement;
	}

}
