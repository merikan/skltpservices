package se.skl.skltpservices.takecare.takecareintegrationcomponent.getalltimetypes;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.StringUtils;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;

import se.riv.crm.scheduling.getalltimetypes.v1.GetAllTimeTypesResponseType;
import se.riv.crm.scheduling.getalltimetypes.v1.ObjectFactory;
import se.riv.crm.scheduling.v1.TimeTypeType;
import se.skl.skltpservices.takecare.booking.GetTimeTypesResponse;
import se.skl.skltpservices.takecare.booking.gettimetypesresponse.ProfdocHISMessage;
import se.skl.skltpservices.takecare.booking.gettimetypesresponse.ProfdocHISMessage.TimeTypes;
import se.skl.skltpservices.takecare.booking.gettimetypesresponse.ProfdocHISMessage.TimeTypes.TimeType;

public class GetAllTimeTypesResponseTransformer extends AbstractMessageTransformer {

	private static final Logger log = LoggerFactory.getLogger(GetAllTimeTypesResponseTransformer.class);

	private static final JaxbUtil jaxbUtil_incoming = new JaxbUtil(GetTimeTypesResponse.class);
	private static final JaxbUtil jaxbUtil_message = new JaxbUtil(ProfdocHISMessage.class);
	private static final JaxbUtil jaxbUtil_error = new JaxbUtil(
			se.skl.skltpservices.takecare.booking.error.ProfdocHISMessage.class);
	private static final JaxbUtil jaxbUtil_outgoing = new JaxbUtil(GetAllTimeTypesResponseType.class);

	/**
	 * Message aware transformer that transforms to crm:scheduling 1.0 from Take
	 * Care format.
	 */
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		return pojoTransform(message.getPayload(), outputEncoding);
	}

	protected Object pojoTransform(Object src, String outputEncoding) throws TransformerException {
		log.debug("Transforming response payload: {}", src);

		GetTimeTypesResponse incoming_res = (GetTimeTypesResponse) jaxbUtil_incoming.unmarshal(src);
		String incoming_string = incoming_res.getGetTimeTypesResult();

		if (containsError(incoming_string)) {
			throwErrorResponse(incoming_string);
		}

		JAXBElement<GetAllTimeTypesResponseType> outgoing_res = creareOkResponse(incoming_string);
		Object payloadOut = jaxbUtil_outgoing.marshal(outgoing_res);

		if (logger.isDebugEnabled()) {
			logger.debug("transformed payload to: " + payloadOut);
		}

		return payloadOut;
	}

	private boolean containsError(String incoming_string) {
		return StringUtils.contains(incoming_string, "Error");
	}

	private JAXBElement<GetAllTimeTypesResponseType> creareOkResponse(String incoming_string) {
		ProfdocHISMessage message = (ProfdocHISMessage) jaxbUtil_message.unmarshal(incoming_string);
		TimeTypes incoming_timeTypes = message.getTimeTypes();

		JAXBElement<GetAllTimeTypesResponseType> outgoing_res = new ObjectFactory()
				.createGetAllTimeTypesResponse(new GetAllTimeTypesResponseType());

		for (TimeType incoming_timeType : incoming_timeTypes.getTimeType()) {
			TimeTypeType outgoing_timeType = new TimeTypeType();
			outgoing_timeType.setTimeTypeId(String.valueOf(incoming_timeType.getTimeTypeId()));
			outgoing_timeType.setTimeTypeName(incoming_timeType.getTimeTypeName());
			outgoing_res.getValue().getListOfTimeTypes().add(outgoing_timeType);
		}
		return outgoing_res;
	}

	private void throwErrorResponse(String incoming_string) {
		se.skl.skltpservices.takecare.booking.error.ProfdocHISMessage message = (se.skl.skltpservices.takecare.booking.error.ProfdocHISMessage) jaxbUtil_error
				.unmarshal(incoming_string);

		throw new RuntimeException("resultCode: " + message.getError().getCode() + " resultText: "
				+ message.getError().getMsg());
	}
}