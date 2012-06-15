package se.skl.skltpservices.takecare;

import org.apache.commons.lang.StringUtils;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;

import se.skl.skltpservices.takecare.booking.error.ProfdocHISMessage;

public abstract class TakeCareResponseTransformer extends AbstractMessageTransformer {

	private static final JaxbUtil jaxbUtil_error = new JaxbUtil(ProfdocHISMessage.class);

	/**
	 * Message aware transformer.
	 */
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		if (message.getExceptionPayload() != null) {
			return message;
		}
		return pojoTransform(message.getPayload(), outputEncoding);
	}

	protected abstract Object pojoTransform(Object src, String outputEncoding) throws TransformerException;

	protected void throwProfdocHISErrorMessage(String incoming_string) {
		ProfdocHISMessage message = (ProfdocHISMessage) jaxbUtil_error.unmarshal(incoming_string);
		throw new RuntimeException("resultCode: " + message.getError().getCode() + " resultText: "
				+ message.getError().getMsg());
	}

	protected boolean containsError(String incoming_string) {
		return StringUtils.contains(incoming_string, "Error");
	}

	public Boolean allowed(Short cancelAllowed) {
		return cancelAllowed != null && cancelAllowed == 1;
	}

}
