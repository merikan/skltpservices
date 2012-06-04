package se.skl.takecare.takecareintegrationcomponent.getalltimetypes;

import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.soitoolkit.commons.mule.util.RecursiveResourceBundle;

import javax.jws.WebService;

import org.soitoolkit.refapps.sd.sample.schema.v1.Sample;
import org.soitoolkit.refapps.sd.sample.schema.v1.SampleResponse;
import org.soitoolkit.refapps.sd.sample.wsdl.v1.Fault;
import org.soitoolkit.refapps.sd.sample.wsdl.v1.SampleInterface;

import riv.interoperability.headers._1.ActorType;
import se.riv.crm.scheduling.getalltimetypes.v1.rivtabp21.GetAllTimeTypesResponderInterface;
import se.riv.crm.scheduling.getalltimetypesresponder.v1.GetAllTimeTypesResponseType;
import se.riv.crm.scheduling.getalltimetypesresponder.v1.GetAllTimeTypesType;
import se.riv.crm.scheduling.v1.TimeTypeType;

@WebService(serviceName = "GetAllTimeTypesResponderService", portName = "GetAllTimeTypesResponderPort", targetNamespace = "urn:riv:crm:scheduling:GetAllTimeTypes:1:rivtabp21", name = "getAllTimeTypesService")
public class GetAllTimeTypesTestProducer implements GetAllTimeTypesResponderInterface {

	public static final String TEST_SUBJECTOFCARE_OK = "19751026-6849";
	public static final String TEST_HEALTHCAREFACILITY_OK = "HSA-VKK123";

	public static final String TEST_SUBJECTOFCARE_INVALID_ID = "-1";
	public static final String TEST_HEALTHCAREFACILITY_INVALID_ID = "-1";

	public static final String TEST_ID_FAULT_TIMEOUT = "0";

	private static final Logger log = LoggerFactory.getLogger(GetAllTimeTypesTestProducer.class);
	private static final RecursiveResourceBundle rb = new RecursiveResourceBundle("TakeCareIntegrationComponent-config");
	private static final long SERVICE_TIMOUT_MS = Long.parseLong(rb.getString("SERVICE_TIMEOUT_MS"));

	public GetAllTimeTypesResponseType getAllTimeTypes(String string, ActorType actor, GetAllTimeTypesType request) {
		log.info("GetAllTimeTypesTestProducer received the request: {}", request);

		String subjectOfCareId = request.getSubjectOfCare();
		String healthcareFacility = request.getSubjectOfCare();

		// Return an error-message if invalid id
		if (TEST_SUBJECTOFCARE_INVALID_ID.equals(subjectOfCareId)) {
			throw new RuntimeException("Invalid Id: " + subjectOfCareId);
		}

		// Force a timeout if zero Id
		if (TEST_ID_FAULT_TIMEOUT.equals(subjectOfCareId)) {
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

	private TimeTypeType createTimeType(String timeTypeId, String timeTypeName) {
		TimeTypeType timeTypeType = new TimeTypeType();
		timeTypeType.setTimeTypeId(timeTypeId);
		timeTypeType.setTimeTypeName(timeTypeName);
		return timeTypeType;
	}
}
