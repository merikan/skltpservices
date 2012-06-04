package se.skl.takecare.takecareintegrationcomponent.getalltimetypes;

import static se.skl.takecare.takecareintegrationcomponent.TakeCareIntegrationComponentMuleServer.getAddress;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.refapps.sd.sample.wsdl.v1.Fault;

import riv.interoperability.headers._1.ActorType;
import riv.interoperability.headers._1.ActorTypeEnum;
import se.riv.crm.scheduling.getalltimetypes.v1.rivtabp21.GetAllTimeTypesResponderInterface;
import se.riv.crm.scheduling.getalltimetypes.v1.rivtabp21.GetAllTimeTypesResponderService;
import se.riv.crm.scheduling.getalltimetypesresponder.v1.GetAllTimeTypesResponseType;
import se.riv.crm.scheduling.getalltimetypesresponder.v1.GetAllTimeTypesType;

public class GetAllTimeTypesTestConsumer {

	private static final Logger log = LoggerFactory.getLogger(GetAllTimeTypesTestConsumer.class);

	private GetAllTimeTypesResponderInterface _service = null;

	public static void main(String[] args) throws Fault {
		String serviceAddress = getAddress("GETALLTIMETYPES_INBOUND_URL");
		String subjectOfCareId = "19751026-6849";
		String healthcareFacility = "HSA-VKK123";

		GetAllTimeTypesTestConsumer consumer = new GetAllTimeTypesTestConsumer(serviceAddress);
		GetAllTimeTypesResponseType response = consumer.callService(subjectOfCareId, healthcareFacility);
		log.info("Returned value = " + response.getListOfTimeTypes());
	}

	public GetAllTimeTypesTestConsumer(String serviceAddress) {
		try {
			URL url = new URL(serviceAddress + "?wsdl");
			_service = new GetAllTimeTypesResponderService(url).getGetAllTimeTypesResponderPort();
		} catch (MalformedURLException e) {
			throw new RuntimeException("Malformed URL Exception: " + e.getMessage());
		}
	}

	public GetAllTimeTypesResponseType callService(String subjectOfCareId, String healthcareFacility) throws Fault {
		log.debug("Calling sample-soap-service with subjectOfCareId = {}, healthcareFacility", subjectOfCareId,
				healthcareFacility);

		GetAllTimeTypesType request = new GetAllTimeTypesType();
		request.setSubjectOfCare(subjectOfCareId);
		request.setHealthcareFacility(healthcareFacility);

		ActorType actorType = new ActorType();
		actorType.setActorId(subjectOfCareId);
		actorType.setActorType(ActorTypeEnum.SUBJECT_OF_CARE);

		String senderId = "1";

		return _service.getAllTimeTypes(senderId, actorType, request);
	}
}