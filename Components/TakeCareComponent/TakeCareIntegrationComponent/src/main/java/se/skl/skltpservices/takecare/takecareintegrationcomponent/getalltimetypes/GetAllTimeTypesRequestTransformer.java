package se.skl.skltpservices.takecare.takecareintegrationcomponent.getalltimetypes;

import java.math.BigInteger;

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
	protected Object pojoTransform(Object src, String encoding) throws TransformerException {

		System.out.println("Kallekula" + src);
		log.debug("Transforming request payload: {}", src);

		// Perform any message aware processing here, otherwise delegate as much
		// as possible to pojoTransform() for easier unit testing

		GetAllTimeTypesType incoming_req = (GetAllTimeTypesType) jaxbUtil_incoming.unmarshal(src);
		String incoming_healthcarefacility = incoming_req.getHealthcareFacility();

		ProfdocHISMessage message = new ProfdocHISMessage();
		message.setCareUnitId(incoming_healthcarefacility);
		message.setCareUnitIdType("hsaid");
		message.setInvokingSystem("InvSysMVK");
		message.setMsgType("Request");
		message.setTime(now());
		message.setTimeTypeRequest("Web");

		GetTimeTypes outRequest = new GetTimeTypes();
		outRequest.setCareunitid(incoming_healthcarefacility);
		outRequest.setCareunitidtype("HSAID");
		outRequest.setExternaluser("ExtUsrMVK");
		outRequest.setTcpassword("");
		outRequest.setTcusername("");
		outRequest.setXml(jaxbUtil_message.marshal(message));

		Object payloadOut = jaxbUtil_outgoing.marshal(outRequest);

		if (logger.isDebugEnabled()) {
			logger.debug("transformed payload to: " + payloadOut);
		}

		return payloadOut;

	}

	private BigInteger now() {
		// TODO Hur skall tiden representeras för TakeCare, kolla specen!
		return new BigInteger("20120607110000");
	}
}