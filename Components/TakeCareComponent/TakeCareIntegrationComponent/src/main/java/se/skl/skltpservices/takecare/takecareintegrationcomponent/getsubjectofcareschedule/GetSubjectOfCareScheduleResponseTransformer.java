package se.skl.skltpservices.takecare.takecareintegrationcomponent.getsubjectofcareschedule;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.mule.api.transformer.TransformerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;

import se.riv.crm.scheduling.getsubjectofcareschedule.v1.GetSubjectOfCareScheduleResponseType;
import se.riv.crm.scheduling.getsubjectofcareschedule.v1.ObjectFactory;
import se.riv.crm.scheduling.v1.TimeslotType;
import se.skl.skltpservices.takecare.TakeCareResponseTransformer;
import se.skl.skltpservices.takecare.booking.GetBookingsResponse;
import se.skl.skltpservices.takecare.booking.getbookingsresponse.ProfdocHISMessage;
import se.skl.skltpservices.takecare.booking.getbookingsresponse.ProfdocHISMessage.Bookings;
import se.skl.skltpservices.takecare.booking.getbookingsresponse.ProfdocHISMessage.Bookings.Booking;

public class GetSubjectOfCareScheduleResponseTransformer extends TakeCareResponseTransformer {

	private static final Logger log = LoggerFactory.getLogger(GetSubjectOfCareScheduleResponseTransformer.class);

	private static final JaxbUtil jaxbUtil_incoming = new JaxbUtil(GetBookingsResponse.class);
	private static final JaxbUtil jaxbUtil_message = new JaxbUtil(ProfdocHISMessage.class);

	private static final JaxbUtil jaxbUtil_outgoing = new JaxbUtil(GetSubjectOfCareScheduleResponseType.class);

	/**
	 * Simple pojo transformer method that can be tested with plain unit
	 * testing...
	 */
	public Object pojoTransform(Object src, String outputEncoding) throws TransformerException {
		log.debug("Transforming response payload: {}", src);

		GetBookingsResponse incoming_res = (GetBookingsResponse) jaxbUtil_incoming.unmarshal(src);
		String incoming_string = incoming_res.getGetBookingsResult();

		if (containsError(incoming_string)) {
			throwErrorResponse(incoming_string);
		}

		JAXBElement<GetSubjectOfCareScheduleResponseType> outgoing_res = creareOkResponse(incoming_string);
		Object payloadOut = jaxbUtil_outgoing.marshal(outgoing_res);

		if (logger.isDebugEnabled()) {
			logger.debug("transformed payload to: " + payloadOut);
		}

		return payloadOut;
	}

	private JAXBElement<GetSubjectOfCareScheduleResponseType> creareOkResponse(String incoming_string) {
		ProfdocHISMessage message = (ProfdocHISMessage) jaxbUtil_message.unmarshal(incoming_string);

		JAXBElement<GetSubjectOfCareScheduleResponseType> outgoing_res = new ObjectFactory()
				.createGetSubjectOfCareScheduleResponse(new GetSubjectOfCareScheduleResponseType());

		List<Bookings> incoming_timeTypes = message.getBookings();
		if (!incoming_timeTypes.isEmpty()) {
			Bookings bookings = incoming_timeTypes.get(0);

			for (Booking booking : bookings.getBooking()) {

				TimeslotType timeslot = new TimeslotType();
				timeslot.setBookingId(booking.getBookingId());
				timeslot.setCancelBookingAllowed(allowed(booking.getCancelAllowed()));
				// timeslot.setCareTypeID(value);
				// timeslot.setCareTypeName(value);
				timeslot.setEndTimeExclusive(String.valueOf(booking.getEndTime()));
				timeslot.setHealthcareFacility(bookings.getCareUnitId());
				timeslot.setHealthcareFacilityName(bookings.getCareUnitName());
				// timeslot.setMessageAllowed(value);
				timeslot.setPerformer(String.valueOf(booking.getResources().getResource().get(0).getResourceType()));
				timeslot.setPerformerName(String.valueOf(booking.getResources().getResource().get(0).getResourceName()));
				// timeslot.setPurpose(value);
				timeslot.setReason(booking.getPatientReason());
				timeslot.setRebookingAllowed(allowed(booking.getRescheduleAllowed()));
				timeslot.setResourceID("");
				timeslot.setResourceName("");
				timeslot.setStartTimeInclusive(String.valueOf(booking.getStartTime()));
				timeslot.setSubjectOfCare(String.valueOf(booking.getPatientId()));
				timeslot.setTimeTypeID(String.valueOf(booking.getTimeTypeId()));
				timeslot.setTimeTypeName(booking.getTimeTypeName_0020());

				outgoing_res.getValue().getTimeslotDetail().add(timeslot);
			}

		}

		return outgoing_res;
	}

	protected Boolean allowed(Short cancelAllowed) {
		return cancelAllowed != null && cancelAllowed == 1;
	}

}