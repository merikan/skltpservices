package se.skl.skltpservices.takecare.takecaretestproducer.cancelbooking;

import java.util.Date;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;
import org.soitoolkit.commons.mule.util.RecursiveResourceBundle;

import se.skl.skltpservices.takecare.booking.BookingSoap;
import se.skl.skltpservices.takecare.booking.cancelbookingresponse.ProfdocHISMessage;
import se.skl.skltpservices.takecare.booking.cancelbookingresponse.ProfdocHISMessage.BookingStatus;
import se.skl.skltpservices.takecare.takecaretestproducer.TakeCareTestProducer;
import se.skl.skltpservices.takecare.takecaretestproducer.TakeCareUtil;

@WebService(targetNamespace = "http://tempuri.org/", name = "BookingSoap", portName = "BookingSoap")
public class CancelBookingTestProducer extends TakeCareTestProducer implements BookingSoap {

	private static final JaxbUtil jaxbUtil_incoming = new JaxbUtil(
			se.skl.skltpservices.takecare.booking.cancelbookingrequest.ProfdocHISMessage.class);
	private static final JaxbUtil jaxbUtil_outgoing = new JaxbUtil(ProfdocHISMessage.class);

	public static final String TEST_BOOKINGID_OK = "1234567890";
	public static final String TEST_BOOKING_INVALID_ID = "0";
	public static final String TEST_ID_FAULT_TIMEOUT = "-1";

	private static final Logger log = LoggerFactory.getLogger(CancelBookingTestProducer.class);
	private static final RecursiveResourceBundle rb = new RecursiveResourceBundle("TakeCareTestProducer-config");
	private static final long SERVICE_TIMOUT_MS = Long.parseLong(rb.getString("SERVICE_TIMEOUT_MS"));

	public String cancelBooking(String tcusername, String tcpassword, String externaluser, String careunitidtype,
			String careunitid, String xml) {

		log.debug("Incoming username to TakeCare {}", tcusername);
		log.debug("Incoming password to TakeCare {}", tcpassword);
		log.debug("Incoming externaluser to TakeCare {}", externaluser);
		log.debug("Incoming careunitidtype to TakeCare {}", careunitidtype);
		log.debug("Incoming careunitid to TakeCare {}", careunitid);
		log.debug("Incoming xml to TakeCare {}", xml);

		se.skl.skltpservices.takecare.booking.cancelbookingrequest.ProfdocHISMessage incomingMessage = (se.skl.skltpservices.takecare.booking.cancelbookingrequest.ProfdocHISMessage) jaxbUtil_incoming
				.unmarshal(xml);

		String incomingBookingId = incomingMessage.getBookingId();

		if (TEST_BOOKING_INVALID_ID.equals(incomingBookingId)) {
			return createErrorResponse(externaluser, incomingBookingId);
		} else if (TEST_ID_FAULT_TIMEOUT.equals(incomingBookingId)) {
			try {
				Thread.sleep(SERVICE_TIMOUT_MS + 1000);
			} catch (InterruptedException e) {
			}
		}

		return createOkResponse(incomingBookingId);

	}

	private String createOkResponse(String bookingId) {

		ProfdocHISMessage outgoing_response = new ProfdocHISMessage();
		outgoing_response.setCareUnit("HSA-VKK123");
		outgoing_response.setCareUnitType(TakeCareUtil.HSAID);
		outgoing_response.setMethod("Booking.MakeBooking");
		outgoing_response.setMsgType(TakeCareUtil.RESPONSE);
		outgoing_response.setSystem("ProfdocHIS");
		outgoing_response.setSystemInstance(0);
		outgoing_response.setTime(yyyyMMddHHmmss(new Date()));
		outgoing_response.setUser("ExtUsrMVK");

		outgoing_response.setBookingStatus(createBookingStatus(bookingId));

		return jaxbUtil_outgoing.marshal(outgoing_response);
	}

	private BookingStatus createBookingStatus(String bookingId) {
		BookingStatus bookingStatus = new BookingStatus();
		bookingStatus.setBookingId(bookingId);
		bookingStatus.setStatus("CANCELLED");
		return bookingStatus;
	}

}
