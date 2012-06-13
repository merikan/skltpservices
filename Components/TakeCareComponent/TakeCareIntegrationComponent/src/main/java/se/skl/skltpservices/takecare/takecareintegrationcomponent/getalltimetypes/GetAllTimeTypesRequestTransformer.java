package se.skl.skltpservices.takecare.takecareintegrationcomponent.getalltimetypes;

import java.math.BigInteger;
import java.util.Date;

import org.apache.commons.lang.time.FastDateFormat;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;

import se.riv.crm.scheduling.getalltimetypes.v1.GetAllTimeTypesType;
import se.skl.skltpservices.takecare.booking.GetTimeTypes;
import se.skl.skltpservices.takecare.booking.gettimetypesrequest.ProfdocHISMessage;

public class GetAllTimeTypesRequestTransformer extends AbstractMessageTransformer {

	private static final Logger log = LoggerFactory.getLogger(GetAllTimeTypesRequestTransformer.class);

	private static final JaxbUtil jaxbUtil_message = new JaxbUtil(ProfdocHISMessage.class);
	private static final JaxbUtil jaxbUtil_incoming = new JaxbUtil(GetAllTimeTypesType.class);
	private static final JaxbUtil jaxbUtil_outgoing = new JaxbUtil(GetTimeTypes.class);

	/**
	 * Message aware transformer that transforms from crm:scheduling 1.0 to Take
	 * Care format.
	 */
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {

		Object src = ((Object[]) message.getPayload())[1];
		message.setPayload(pojoTransform(src, outputEncoding));
		return message;
	}

	protected Object pojoTransform(Object src, String encoding) throws TransformerException {

		if (logger.isDebugEnabled()) {
			log.debug("Transforming request payload: {}", src);
		}

		GetAllTimeTypesType incomingRequest = (GetAllTimeTypesType) jaxbUtil_incoming.unmarshal(src);
		String incomingHealthcarefacility = incomingRequest.getHealthcareFacility();

		ProfdocHISMessage message = new ProfdocHISMessage();
		message.setCareUnitId(incomingHealthcarefacility);
		message.setCareUnitIdType("hsaid");
		message.setInvokingSystem("InvSysMVK");
		message.setMsgType("Request");
		message.setTime(yyyyMMddHHmmss());
		message.setTimeTypeRequest("Web");

		GetTimeTypes outgoingRequest = new GetTimeTypes();
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

	private BigInteger yyyyMMddHHmmss() {
		FastDateFormat dateFormat = FastDateFormat.getInstance("yyyyMMddHHmmss");
		return new BigInteger(dateFormat.format(new Date()));
	}
}