package se.skl.skltpservices.takecare.takecareintegrationcomponent.getalltimetypes;

import java.math.BigInteger;
import java.util.Date;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;
import org.soitoolkit.commons.mule.util.RecursiveResourceBundle;

import se.skl.skltpservices.takecare.booking.BookingSoap;
import se.skl.skltpservices.takecare.booking.gettimetypesresponse.ProfdocHISMessage;
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

	private static final JaxbUtil jaxbUtil_error = new JaxbUtil(
			se.skl.skltpservices.takecare.booking.error.ProfdocHISMessage.class);
	private static final JaxbUtil jaxbUtil_outgoing = new JaxbUtil(ProfdocHISMessage.class);

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

		if (TEST_HEALTHCAREFACILITY_INVALID_ID.equals(careunitid)) {
			return createErrorResponse(externaluser, careunitid);
		} else if (TEST_ID_FAULT_TIMEOUT.equals(careunitid)) {
			try {
				Thread.sleep(SERVICE_TIMOUT_MS + 1000);
			} catch (InterruptedException e) {
			}
		}
		return createOkResponse(externaluser, careunitid);
	}

	private String createOkResponse(String externaluser, String careunitid) {
		ProfdocHISMessage outgoing_response = new ProfdocHISMessage();
		outgoing_response.setCareUnit(careunitid);
		outgoing_response.setCareUnitType("hsaid");
		outgoing_response.setMethod("Booking.GetTimeTypes");
		outgoing_response.setMsgType("Response");
		outgoing_response.setSystem("ProfdocHIS");
		outgoing_response.setSystemInstance(0);
		outgoing_response.setTime(now());
		outgoing_response.setUser(externaluser);
		outgoing_response.setTimeTypes(buildTimeTypes(careunitid));
		return jaxbUtil_outgoing.marshal(outgoing_response);
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

	private String createErrorResponse(String careunitId, String externalUser) {

		se.skl.skltpservices.takecare.booking.error.ProfdocHISMessage error_response = new se.skl.skltpservices.takecare.booking.error.ProfdocHISMessage();
		error_response.setCareUnitType("hsaid");
		error_response.setCareUnit(careunitId);
		error_response.setError(new se.skl.skltpservices.takecare.booking.error.ProfdocHISMessage.Error());
		error_response.getError().setCode(3001);
		error_response.getError().setMsg("Illegal argument!");
		error_response.getError().setType("System");
		error_response.setMethod("Booking.GetTimeTypes");
		error_response.setMsgType("Error");
		error_response.setSystem("ProfdocHIS");
		error_response.setSystemInstance(0);
		error_response.setTime(now());
		error_response.setUser(externalUser);
		return jaxbUtil_error.marshal(error_response);
	}

	private BigInteger now() {
		return BigInteger.valueOf(new Date().getTime());
	}

}
