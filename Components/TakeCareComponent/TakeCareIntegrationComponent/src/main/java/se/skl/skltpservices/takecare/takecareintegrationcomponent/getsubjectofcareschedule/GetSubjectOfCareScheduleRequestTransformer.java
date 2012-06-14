package se.skl.skltpservices.takecare.takecareintegrationcomponent.getsubjectofcareschedule;

import java.util.Date;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;

import se.riv.crm.scheduling.getsubjectofcareschedule.v1.GetSubjectOfCareScheduleType;
import se.skl.skltpservices.takecare.TakeCareDateHelper;
import se.skl.skltpservices.takecare.booking.GetBookings;
import se.skl.skltpservices.takecare.booking.getbookingsrequest.ProfdocHISMessage;

public class GetSubjectOfCareScheduleRequestTransformer extends AbstractMessageTransformer {

	private static final Logger log = LoggerFactory.getLogger(GetSubjectOfCareScheduleRequestTransformer.class);

	private static final JaxbUtil jaxbUtil_message = new JaxbUtil(ProfdocHISMessage.class);
	private static final JaxbUtil jaxbUtil_incoming = new JaxbUtil(GetSubjectOfCareScheduleType.class);
	private static final JaxbUtil jaxbUtil_outgoing = new JaxbUtil(GetBookings.class);

	/**
	 * Message aware transformer that ...
	 */
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		Object src = ((Object[]) message.getPayload())[1];
		message.setPayload(pojoTransform(src, outputEncoding));
		return message;
	}

	/**
	 * Simple pojo transformer method that can be tested with plain unit
	 * testing...
	 */
	protected Object pojoTransform(Object src, String encoding) throws TransformerException {

		if (logger.isDebugEnabled()) {
			log.debug("Transforming request payload: {}", src);
		}

		GetSubjectOfCareScheduleType incomingRequest = (GetSubjectOfCareScheduleType) jaxbUtil_incoming.unmarshal(src);
		String incomingHealthcarefacility = incomingRequest.getHealthcareFacility();
		String incomingSubjectOfCare = incomingRequest.getSubjectOfCare();

		ProfdocHISMessage message = new ProfdocHISMessage();
		message.setCareUnitId(incomingHealthcarefacility);
		message.setCareUnitIdType("hsaid");
		message.setInvokingSystem("InvSysMVK");
		message.setMsgType("Request");
		message.setBookingId(null);
		message.setPatientId(incomingSubjectOfCare);
		message.setTime(TakeCareDateHelper.yyyyMMddHHmmss(new Date()));

		GetBookings outgoingRequest = new GetBookings();
		outgoingRequest.setCareunitid(incomingHealthcarefacility);
		outgoingRequest.setCareunitidtype("hsaid");
		outgoingRequest.setExternaluser("ExtUsrMVK");
		outgoingRequest.setTcpassword("");
		outgoingRequest.setTcusername("");
		outgoingRequest.setXml(jaxbUtil_message.marshal(message));

		Object outgoingPayload = jaxbUtil_outgoing.marshal(outgoingRequest);

		if (logger.isDebugEnabled()) {
			logger.debug("transformed payload to: " + outgoingPayload);
		}

		return outgoingPayload;

	}

}