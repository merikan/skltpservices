package se.skl.skltpservices.takecare.takecareintegrationcomponent.getalltimetypes;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.util.RecursiveResourceBundle;
import org.w3c.addressing_1_0.AttributedURIType;

import se.riv.crm.scheduling_1.GetAllTimeTypesResponderInterface;
import se.riv.crm.scheduling_1.GetAllTimeTypesResponseType;
import se.riv.crm.scheduling_1.GetAllTimeTypesType;
import se.riv.crm.scheduling_1.TimeTypeType;

@WebService(serviceName = "GetAllTimeTypesResponderService", portName = "GetAllTimeTypesResponderPort", targetNamespace = "urn:riv:crm:scheduling:GetAllTimeTypes:1:rivtabp20", name = "getAllTimeTypesService")
public class GetAllTimeTypesTestProducer implements GetAllTimeTypesResponderInterface {

	public static final String TEST_HEALTHCAREFACILITY_OK = "HSA-VKK123";

	public static final String TEST_HEALTHCAREFACILITY_INVALID_ID = "-1";

	public static final String TEST_ID_FAULT_TIMEOUT = "0";

	private static final Logger log = LoggerFactory.getLogger(GetAllTimeTypesTestProducer.class);
	private static final RecursiveResourceBundle rb = new RecursiveResourceBundle("TakeCareIntegrationComponent-config");
	private static final long SERVICE_TIMOUT_MS = Long.parseLong(rb.getString("SERVICE_TIMEOUT_MS"));

	private TimeTypeType createTimeType(String timeTypeId, String timeTypeName) {
		TimeTypeType timeTypeType = new TimeTypeType();
		timeTypeType.setTimeTypeId(timeTypeId);
		timeTypeType.setTimeTypeName(timeTypeName);
		return timeTypeType;
	}

	public GetAllTimeTypesResponseType getAllTimeTypes(AttributedURIType arg0, GetAllTimeTypesType request) {
		log.info("GetAllTimeTypesTestProducer received the request: {}", request);

		String healthcareFacility = request.getHealthcareFacility();

		// Return an error-message if invalid id
		if (TEST_HEALTHCAREFACILITY_INVALID_ID.equals(healthcareFacility)) {
			throw new RuntimeException("Invalid Id: " + healthcareFacility);
		}

		// Force a timeout if zero Id
		if (TEST_ID_FAULT_TIMEOUT.equals(healthcareFacility)) {
			try {
				Thread.sleep(SERVICE_TIMOUT_MS + 1000);
			} catch (InterruptedException e) {
			}
		}

		// Produce the response
		GetAllTimeTypesResponseType responseType = new GetAllTimeTypesResponseType();
		responseType.getListOfTimeTypes().add(createTimeType("0", "OPEN"));
		return responseType;
	}
}
