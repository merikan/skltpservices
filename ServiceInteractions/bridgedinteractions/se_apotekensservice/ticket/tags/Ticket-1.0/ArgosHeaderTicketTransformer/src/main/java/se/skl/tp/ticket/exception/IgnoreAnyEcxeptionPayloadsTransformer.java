package se.skl.tp.ticket.exception;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageAwareTransformer;

/**
 * Transformer to handle the case when we get a soap fault from the producer and
 * just wants to redirect it back to the consumer. If message contains a
 * ExceptionPayload, this is the one that will be returned to the consumer. And
 * in this case the ExceptionPayload is empty and the Payload contains the
 * correct fault to be returned.
 * 
 * Note that tests for this transformer is performed in
 * HamtaAktuellaOrdinationer.
 * 
 */
public class IgnoreAnyEcxeptionPayloadsTransformer extends AbstractMessageAwareTransformer {

    @Override
    public Object transform(MuleMessage message, String outputEncoding) throws TransformerException {
	if (containsBothPayloadAndExceptionPayload(message)) {
	    removeExceptionPayload(message);
	}
	return message;
    }

    private void removeExceptionPayload(MuleMessage message) {
	logger.debug("Exception payload detected together with payload! Exception payload will be removed to return payload only");
	message.setExceptionPayload(null);
    }

    private boolean containsBothPayloadAndExceptionPayload(MuleMessage message) {
	return message.getPayload() != null && message.getExceptionPayload() != null;
    }
}
