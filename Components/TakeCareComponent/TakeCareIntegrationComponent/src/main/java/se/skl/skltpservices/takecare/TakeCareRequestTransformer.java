package se.skl.skltpservices.takecare;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

public abstract class TakeCareRequestTransformer extends AbstractMessageTransformer {

	/**
	 * Simple pojo transformer that transforms crm:scheduling 1.0 to Take Care
	 * format.
	 * 
	 * @param message
	 * @param outputEncoding
	 */
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		Object src = ((Object[]) message.getPayload())[1];
		message.setPayload(pojoTransform(src, outputEncoding));
		return message;
	}

	protected abstract Object pojoTransform(Object src, String encoding) throws TransformerException;

}
