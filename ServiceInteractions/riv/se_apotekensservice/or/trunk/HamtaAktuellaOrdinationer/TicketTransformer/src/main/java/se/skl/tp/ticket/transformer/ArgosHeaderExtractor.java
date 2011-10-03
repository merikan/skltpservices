package se.skl.tp.ticket.transformer;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.module.xml.stax.ReversibleXMLStreamReader;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transformer is responsible to add a SAML ticket to the incoming request,
 * based on Argos header information.
 * 
 */
public class ArgosHeaderExtractor extends AbstractMessageAwareTransformer {

    private static Logger log = LoggerFactory.getLogger(ArgosHeaderExtractor.class);

    public ArgosHeaderExtractor() {
    }

    @Override
    public Object transform(MuleMessage msg, String encoding) throws TransformerException {
	log.info("Saml ticket transformer executing");
	try {
	    markMessageRewindable(msg);
	    extractArgosHeader(msg);
	    rewindMessage(msg);
	    return msg;
	} catch (Exception e) {
	    log.error("Could not transform/apply saml ticket to message", e);
	    throw new IllegalStateException("Could not transform/apply saml ticket to message");
	}
    }

    private void markMessageRewindable(MuleMessage msg) {
	final ReversibleXMLStreamReader originalRequest = (ReversibleXMLStreamReader) msg.getPayload();
	originalRequest.setTracking(true);
    }

    private void rewindMessage(MuleMessage msg) {
	final ReversibleXMLStreamReader originalRequest = (ReversibleXMLStreamReader) msg.getPayload();
	originalRequest.reset();
    }

    ArgosHeader extractArgosHeader(MuleMessage msg) throws XMLStreamException, FactoryConfigurationError {
	ArgosHeader argosHeader = new ArgosHeaderHelper().extractArgosHeader(msg);
	msg.setProperty("ArgosHeader", argosHeader);
	return argosHeader;
    }
}
