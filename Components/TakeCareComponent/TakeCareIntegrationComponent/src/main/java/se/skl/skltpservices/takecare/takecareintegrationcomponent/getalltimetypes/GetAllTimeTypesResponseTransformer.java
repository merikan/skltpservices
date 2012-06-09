package se.skl.skltpservices.takecare.takecareintegrationcomponent.getalltimetypes;

import javax.xml.bind.JAXBElement;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;

import se.riv.crm.scheduling.getalltimetypes.v1.GetAllTimeTypesResponseType;
import se.riv.crm.scheduling.getalltimetypes.v1.GetAllTimeTypesType;
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
	private static final JaxbUtil jaxbUtil_outgoing = new JaxbUtil(GetAllTimeTypesResponseType.class);

	/**
	 * Message aware transformer that ...
	 */
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {

		// Perform any message aware processing here, otherwise delegate as much
		// as possible to pojoTransform() for easier unit testing

		return pojoTransform(message.getPayload(), outputEncoding);
	}

	/**
	 * Simple pojo transformer method that can be tested with plain unit
	 * testing...
	 */
	public Object pojoTransform(Object src, String outputEncoding) throws TransformerException {
		log.debug("Transforming response payload: {}", src);

		GetTimeTypesResponse incoming_res = (GetTimeTypesResponse) jaxbUtil_incoming.unmarshal(src);

		String incoming_string = incoming_res.getGetTimeTypesResult();
		ProfdocHISMessage message = (ProfdocHISMessage) jaxbUtil_message.unmarshal(incoming_string);
		TimeTypes incoming_timeTypes = message.getTimeTypes();

		JAXBElement<GetAllTimeTypesResponseType> outgoing_res = new ObjectFactory().createGetAllTimeTypesResponse(new GetAllTimeTypesResponseType());
		for (TimeType incoming_timeType : incoming_timeTypes.getTimeType()) {
			TimeTypeType outgoing_timeType = new TimeTypeType();
			outgoing_timeType.setTimeTypeId(String.valueOf(incoming_timeType.getTimeTypeId()));
			outgoing_timeType.setTimeTypeName(incoming_timeType.getTimeTypeName());
			outgoing_res.getValue().getListOfTimeTypes().add(outgoing_timeType);
		}

		Object payloadOut = jaxbUtil_outgoing.marshal(outgoing_res);

		if (logger.isDebugEnabled()) {
			logger.debug("transformed payload to: " + payloadOut);
		}

		return payloadOut;

	}

	private String createFault(String errorMessage) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
				+ "<soap:Fault xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<faultcode>soap:Server</faultcode>" + "<faultstring>" + errorMessage + "</faultstring>"
				+ "</soap:Fault>";
	}

}