package se.skl.skltpservices.takecare.takecareintegrationcomponent.getalltimetypes;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;
import org.soitoolkit.commons.mule.util.RecursiveResourceBundle;

import se.skl.skltpservices.takecare.booking.BookingSoap;
import se.skl.skltpservices.takecare.booking.gettimetypesresponse.ProfdocHISMessage.TimeTypes;
import se.skl.skltpservices.takecare.booking.gettimetypesresponse.ProfdocHISMessage.TimeTypes.TimeType;

@WebService(targetNamespace = "http://tempuri.org/", name = "BookingSoap", portName = "BookingSoap")
public class GetTimeTypesTestProducer implements BookingSoap {

	public static final String TEST_HEALTHCAREFACILITY_OK = "HSA-VKK123";

	public static final String TEST_HEALTHCAREFACILITY_INVALID_ID = "-1";

	public static final String TEST_ID_FAULT_TIMEOUT = "0";

	private static final Logger log = LoggerFactory.getLogger(GetTimeTypesTestProducer.class);
	private static final RecursiveResourceBundle rb = new RecursiveResourceBundle("TakeCareIntegrationComponent-config");
	private static final long SERVICE_TIMOUT_MS = Long.parseLong(rb.getString("SERVICE_TIMEOUT_MS"));

	private static final JaxbUtil jaxbUtil_incoming = new JaxbUtil(
			se.skl.skltpservices.takecare.booking.gettimetypesrequest.ProfdocHISMessage.class);
	private static final JaxbUtil outgoing_incoming = new JaxbUtil(
			se.skl.skltpservices.takecare.booking.gettimetypesresponse.ProfdocHISMessage.class);

	public String getAvailableDates(String tcusername, String tcpassword, String externaluser, String careunitidtype,
			String careunitid, String xml) {
		throw new UnsupportedOperationException();
	}

	public String getAvailableTimeslots(String tcusername, String tcpassword, String externaluser,
			String careunitidtype, String careunitid, String xml) {
		throw new UnsupportedOperationException();
	}

	public String cancelBooking(String tcusername, String tcpassword, String externaluser, String careunitidtype,
			String careunitid, String xml) {
		throw new UnsupportedOperationException();
	}

	public String getTimeTypes(String tcusername, String tcpassword, String externaluser, String careunitidtype,
			String careunitid, String xml) {

		se.skl.skltpservices.takecare.booking.gettimetypesresponse.ProfdocHISMessage outgoing_response = new se.skl.skltpservices.takecare.booking.gettimetypesresponse.ProfdocHISMessage();
		outgoing_response.setCareUnit("Careunit ID");
		outgoing_response.setCareUnitType("hsaid");
		outgoing_response.setMethod("Booking.GetTimeTypes");
		outgoing_response.setMsgType("Response");
		outgoing_response.setSystem("ProfdocHIS");
		outgoing_response.setSystemInstance(0);
		outgoing_response.setTime(null);
		outgoing_response.setUser("DRJEKYLL");

		outgoing_response.setTimeTypes(buildTimeTypes("Careunit ID"));

		return outgoing_incoming.marshal(outgoing_response);
	}

	private TimeTypes buildTimeTypes(String careunitId) {
		TimeTypes timeTypes = new TimeTypes();
		timeTypes.setCareUnitId(careunitId);
		timeTypes.setCareUnitIdType("hsaid");
		timeTypes.setCareUnitName("Careunit name");

		timeTypes.getTimeType().add(createTimeType(0, "Tidstyp0"));
		timeTypes.getTimeType().add(createTimeType(1, "Tidstyp1"));
		return timeTypes;
	}

	private TimeType createTimeType(int timeTypeId, String timeTypeName) {
		TimeType timeType = new TimeType();
		timeType.setTimeTypeId(timeTypeId);
		timeType.setTimeTypeName(timeTypeName);
		return timeType;
	}

	public String getBookings(String tcusername, String tcpassword, String externaluser, String careunitidtype,
			String careunitid, String xml) {
		throw new UnsupportedOperationException();
	}

	public String rescheduleBooking(String tcusername, String tcpassword, String externaluser, String careunitidtype,
			String careunitid, String xml) {
		throw new UnsupportedOperationException();
	}

	public String makeBooking(String tcusername, String tcpassword, String externaluser, String careunitidtype,
			String careunitid, String xml) {
		throw new UnsupportedOperationException();
	}

}
