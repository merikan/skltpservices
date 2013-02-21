package se.skltp.aggregatingservices.riv.crm.requeststatus.getrequestactivities;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.util.RecursiveResourceBundle;

import se.riv.crm.requeststatus.getrequestactivities.v1.rivtabp21.GetRequestActivitiesResponderInterface;
import se.riv.crm.requeststatus.getrequestactivitiesresponder.v1.GetRequestActivitiesResponseType;
import se.riv.crm.requeststatus.getrequestactivitiesresponder.v1.GetRequestActivitiesType;
import se.riv.crm.requeststatus.v1.RequestActivityType;

@WebService(serviceName = "GetRequestActivitiesResponderService", portName = "GetRequestActivitiesResponderPort", targetNamespace = "urn:riv:crm:requeststatus:GetRequestActivities:1:rivtabp21", name = "GetRequestActivitiesInteraction")
public class RequestActivitiesTestProducer implements GetRequestActivitiesResponderInterface {

	private static final Logger log = LoggerFactory.getLogger(RequestActivitiesTestProducer.class);
    private static final RecursiveResourceBundle rb = new RecursiveResourceBundle("GetAggregatedRequestActivities-config");
	private static final long SERVICE_TIMOUT_MS = Long.parseLong(rb.getString("SERVICE_TIMEOUT_MS"));

	public static final String TEST_ID_ZERO_BOOKINGS = "000000000000";
	public static final String TEST_ID_ONE_BOOKING   = "111111111111";
	public static final String TEST_ID_MANY_BOOKINGS = "222222222222";
	public static final String TEST_ID_FAULT_INVALID_ID = "-1";
	public static final String TEST_ID_FAULT_TIMEOUT    = "0";

	public static final String TEST_BOOKING_ID_ONE_BOOKING      = "1001";
	public static final String TEST_BOOKING_ID_MANY_BOOKINGS_1  = "1002";
	public static final String TEST_BOOKING_ID_MANY_BOOKINGS_2  = "1003";
	public static final String TEST_BOOKING_ID_MANY_BOOKINGS_3  = "1004";
	public static final String TEST_BOOKING_ID_MANY_BOOKINGS_4  = "1004";
	public static final String TEST_BOOKING_ID_MANY_BOOKINGS_NEW_1  = "2001";
	public static final String TEST_BOOKING_ID_FAULT_INVALID_ID = "1005";


	public static final String TEST_LOGICAL_ADDRESS_1 = "HSA-ID-1";
	public static final String TEST_LOGICAL_ADDRESS_2 = "HSA-ID-2";
	public static final String TEST_LOGICAL_ADDRESS_3 = "HSA-ID-3";

	public static final long TEST_LOGICAL_ADDRESS_1_RESPONSE_TIME = 1000;                     // Normal 1 sec response time on system #1
	public static final long TEST_LOGICAL_ADDRESS_2_RESPONSE_TIME = SERVICE_TIMOUT_MS - 1000; // Slow but below the timeout on system #2
	public static final long TEST_LOGICAL_ADDRESS_3_RESPONSE_TIME = SERVICE_TIMOUT_MS + 1000; // Too slow on system #3, the timeout will kick in

	public static final String TEST_REASON_DEFAULT = "default reason";
	public static final String TEST_REASON_UPDATED = "updated reason";

	@Override
	public GetRequestActivitiesResponseType getRequestActivities(String logicalAddress, GetRequestActivitiesType request) {
		log.info("### Virtual service for GetRequestActivities call the source system with logical address: {} and patientId: {}", logicalAddress, request.getSubjectOfCareId());

		String id = request.getSubjectOfCareId();

		// Return an error-message if invalid id
		if (TEST_ID_FAULT_INVALID_ID.equals(id)) {
			throw new RuntimeException("Invalid Id: " + id);
		}

		// Force a timeout if zero Id
        if (TEST_ID_FAULT_TIMEOUT.equals(id)) {
	    	try {
				Thread.sleep(SERVICE_TIMOUT_MS + 1000);
			} catch (InterruptedException e) {}
        }


        // Simulate some processing
		long processingTime = 0;
		if      (TEST_LOGICAL_ADDRESS_1.equals(logicalAddress)) processingTime = TEST_LOGICAL_ADDRESS_1_RESPONSE_TIME;
		else if (TEST_LOGICAL_ADDRESS_2.equals(logicalAddress)) processingTime = TEST_LOGICAL_ADDRESS_2_RESPONSE_TIME;
		else if (TEST_LOGICAL_ADDRESS_3.equals(logicalAddress)) processingTime = TEST_LOGICAL_ADDRESS_3_RESPONSE_TIME;
    	try {
    		log.debug("## SLEEP FOR " + processingTime + " ms.");
    		Thread.sleep(processingTime );
    		log.debug("## SLEEP DONE.");
		} catch (InterruptedException e) {}
        
        // Lookup the response
        GetRequestActivitiesResponseType response = retreiveFromDb(logicalAddress, request.getSubjectOfCareId());
        if (response == null) {
        	// Return an empty response object instead of null if nothing is found
        	response = new GetRequestActivitiesResponseType();
        }

		log.info("### Virtual service got {} booknings in the reply from the source system with logical address: {} and patientId: {}", new Object[] {response.getRequestActivity().size(), logicalAddress, request.getSubjectOfCareId()});

		// We are done
        return response;
	}

	// Let's share this method with other testclasses... 
	static public RequestActivityType createResponse(String logicalAddress, String subjectOfCare, String id) {
		RequestActivityType response = new RequestActivityType();

		response.setCareUnit(logicalAddress);
		response.setSubjectOfCareId(subjectOfCare);
		response.setSenderRequestId(id);
		response.setReceiverRequestId("ReceiverRequestId");
		response.setTypeOfRequest("TypeOfRequest");
		response.setRequestMedium("RequestMedium");
		response.setRequestIssuedByPersonName("RequestIssuedByPersonName");
		response.setRequestIssuedByOrganizationalUnitId("RequestIssuedByOrganizationalUnitId");
		response.setRequestIssuedByOrganizationalUnitDescription("RequestIssuedByOrganizationalUnitDescription");
		response.setReceivingPersonName("ReceivingPersonName");
		response.setReceivingOrganizationalUnitId("ReceivingOrganizationalUnitId");
		response.setReceivingOrganizationalUnitDescription("ReceivingOrganizationalUnitDescription");
		
		return response;
	}

	//
	// A small db for timebookings for various test-source systems
	//
	private static Map<String, GetRequestActivitiesResponseType> BOOKING_DB = null;
	// new HashMap<String, GetRequestActivitiesResponseType>();
	
	public static void initDb() {
		log.debug("### INIT-DB INIT CALLED NOW, DB == NULL? " + (BOOKING_DB == null));

		// Start with resetting the db from old values.
		resetDb();
		
		// Patient with one booking, id = TEST_ID_ONE_BOOKING
		GetRequestActivitiesResponseType response = new GetRequestActivitiesResponseType();
		response.getRequestActivity().add(createResponse(TEST_LOGICAL_ADDRESS_1, TEST_ID_ONE_BOOKING, TEST_BOOKING_ID_ONE_BOOKING));
		storeInDb(TEST_LOGICAL_ADDRESS_1, TEST_ID_ONE_BOOKING, response);

		// Patient with four bookings spread over three logical-addresses, where one is on a slow system, i.e. that cause timeouts
		response = new GetRequestActivitiesResponseType();
		response.getRequestActivity().add(createResponse(TEST_LOGICAL_ADDRESS_1, TEST_ID_MANY_BOOKINGS, TEST_BOOKING_ID_MANY_BOOKINGS_1));
		storeInDb(TEST_LOGICAL_ADDRESS_1, TEST_ID_MANY_BOOKINGS, response);

		response = new GetRequestActivitiesResponseType();
		response.getRequestActivity().add(createResponse(TEST_LOGICAL_ADDRESS_2, TEST_ID_MANY_BOOKINGS, TEST_BOOKING_ID_MANY_BOOKINGS_2));
		response.getRequestActivity().add(createResponse(TEST_LOGICAL_ADDRESS_2, TEST_ID_MANY_BOOKINGS, TEST_BOOKING_ID_MANY_BOOKINGS_3));
		storeInDb(TEST_LOGICAL_ADDRESS_2, TEST_ID_MANY_BOOKINGS, response);

		response = new GetRequestActivitiesResponseType();
		response.getRequestActivity().add(createResponse(TEST_LOGICAL_ADDRESS_3, TEST_ID_MANY_BOOKINGS, TEST_BOOKING_ID_MANY_BOOKINGS_4));
		storeInDb(TEST_LOGICAL_ADDRESS_3, TEST_ID_MANY_BOOKINGS, response);
	}

	static public void resetDb() {
		log.debug("### RESET-DB INIT CALLED NOW, DB == NULL? " + (BOOKING_DB == null));
		BOOKING_DB = new HashMap<String, GetRequestActivitiesResponseType>();
	}

	static public void storeInDb(String logicalAddress, String subjectOfCareId, GetRequestActivitiesResponseType value) {
		log.debug("### STORE-DB INIT CALLED NOW, DB == NULL? " + (BOOKING_DB == null));
		if (BOOKING_DB == null) {
			initDb();
		}
		BOOKING_DB.put(logicalAddress + "|" + subjectOfCareId, value);
	}
	
	static public GetRequestActivitiesResponseType retreiveFromDb(String logicalAddress, String subjectOfCareId) {
		log.debug("### RETREIVE-DB INIT CALLED NOW, DB == NULL? " + (BOOKING_DB == null));
		if (BOOKING_DB == null) {
			initDb();
		}
        return BOOKING_DB.get(logicalAddress + "|" + subjectOfCareId);
	}
}