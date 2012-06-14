package se.skl.skltpservices.takecare.takecareintegrationcomponent.getsubjectofcareschedule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigInteger;

import org.junit.Test;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;
import org.soitoolkit.commons.mule.util.MiscUtil;

import se.skl.skltpservices.takecare.booking.GetBookings;
import se.skl.skltpservices.takecare.booking.getbookingsrequest.ProfdocHISMessage;

public class GetSubjectOfCareScheduleRequestTransformerTest {

	private static final JaxbUtil jaxbUtil_outgoing = new JaxbUtil(GetBookings.class);
	private static final JaxbUtil jaxbUtil_message = new JaxbUtil(ProfdocHISMessage.class);

	@Test
	public void testTransformer_ok() throws Exception {

		String input = MiscUtil
				.readFileAsString("src/test/resources/testfiles/GetSubjectOfCareSchedule/request-input.xml");

		GetSubjectOfCareScheduleRequestTransformer transformer = new GetSubjectOfCareScheduleRequestTransformer();
		String result = (String) transformer.pojoTransform(input, "UTF-8");

		/* Bookings */
		GetBookings bookings = (GetBookings) jaxbUtil_outgoing.unmarshal(result);
		String careunitId = bookings.getCareunitid();
		String careunitType = bookings.getCareunitidtype();
		String externalUser = bookings.getExternaluser();
		String tcPassword = bookings.getTcpassword();
		String tcUsername = bookings.getTcusername();
		String xml = bookings.getXml();

		assertEquals("hsaid", careunitType);
		assertEquals("HSA-VKK123", careunitId);
		assertEquals("ExtUsrMVK", externalUser);
		assertEquals("", tcPassword);
		assertEquals("", tcUsername);

		/* ProfdocHISMessage */
		ProfdocHISMessage message = (ProfdocHISMessage) jaxbUtil_message.unmarshal(xml);
		String msgCareunitId = message.getCareUnitId();
		String msgCareunitIdType = message.getCareUnitIdType();
		String msgInvokingSystem = message.getInvokingSystem();
		String msgMessageType = message.getMsgType();
		BigInteger msgTime = message.getTime();
		String bookingId = message.getBookingId();
		String patientId = message.getPatientId();

		assertEquals("HSA-VKK123", msgCareunitId);
		assertEquals("hsaid", msgCareunitIdType);
		assertEquals("InvSysMVK", msgInvokingSystem);
		assertEquals("Request", msgMessageType);
		assertEquals("191414141414", patientId);
		assertNull(bookingId);
		assertNotNull(msgTime);

	}
}