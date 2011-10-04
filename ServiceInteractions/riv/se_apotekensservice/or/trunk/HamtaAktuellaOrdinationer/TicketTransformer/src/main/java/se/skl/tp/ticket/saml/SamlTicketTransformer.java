package se.skl.tp.ticket.saml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.module.xml.stax.ReversibleXMLStreamReader;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.skl.tp.ticket.TicketMachine;
import se.skl.tp.ticket.argos.ArgosHeader;
import se.skl.tp.ticket.argos.ArgosHeaderHelper;

/**
 * Transformer is responsible to add a SAML ticket to the incoming request,
 * based on Argos header information.
 * 
 */
public class SamlTicketTransformer extends AbstractMessageAwareTransformer {

    private static Logger log = LoggerFactory.getLogger(SamlTicketTransformer.class);

    private final XMLInputFactory xmlInputFactory;
    private XMLOutputFactory xmlOutputFactory;

    public SamlTicketTransformer() {
	xmlInputFactory = XMLInputFactory.newInstance();
	xmlOutputFactory = XMLOutputFactory.newInstance();
    }

    @Override
    public Object transform(MuleMessage msg, String encoding) throws TransformerException {
	log.info("Saml ticket transformer executing");
	try {
	    XMLEventReader samlTicket = extractSamlTicket(msg);
	    final ReversibleXMLStreamReader originalRequest = (ReversibleXMLStreamReader) msg.getPayload();
	    ByteArrayOutputStream updatedRequest = addSamlTicketToOriginalRequest(originalRequest, samlTicket);
	    return updatePayload(msg, updatedRequest);
	} catch (Exception e) {
	    log.error("Could not transform/apply saml ticket to message", e);
	    throw new IllegalStateException("Could not transform/apply saml ticket to message");
	}
    }

    XMLEventReader extractSamlTicket(MuleMessage msg) throws XMLStreamException, FactoryConfigurationError {
	ArgosHeader argosHeader = new ArgosHeaderHelper().extractArgosHeader(msg);
	String samlTicketStr = new TicketMachine().produceSamlTicket(argosHeader);
	StringReader stringReader = new StringReader(samlTicketStr);
	XMLEventReader samlTicket = XMLInputFactory.newInstance().createXMLEventReader(stringReader);
	return samlTicket;
    }

    private MuleMessage updatePayload(MuleMessage msg, ByteArrayOutputStream updatedRequest) throws XMLStreamException {
	ByteArrayInputStream bis = new ByteArrayInputStream(updatedRequest.toByteArray());
	XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(bis);
	msg.setPayload(new ReversibleXMLStreamReader(reader));
	return msg;
    }

    ByteArrayOutputStream addSamlTicketToOriginalRequest(final XMLStreamReader originalRequest,
	    XMLEventReader samlTicket) throws XMLStreamException {

	final ByteArrayOutputStream outgoingMessage = new ByteArrayOutputStream();
	final XMLEventReader originalRequestEvents = xmlInputFactory.createXMLEventReader(originalRequest);
	final XMLEventWriter outgoingMessageWriter = xmlOutputFactory.createXMLEventWriter(outgoingMessage);
	boolean insideArgosHeader = false;

	while (originalRequestEvents.hasNext()) {
	    final XMLEvent event = originalRequestEvents.nextEvent();

	    if (isNextEventArgusStartHeader(event)) {
		addSamlTicketToHeader(outgoingMessageWriter, samlTicket);
		insideArgosHeader = true;
		outgoingMessageWriter.add(event);
	    }

	    if (isNextEventArgusEndHeader(event)) {
		insideArgosHeader = false;
	    }

	    if (!insideArgosHeader) {
		outgoingMessageWriter.add(event);
	    }
	}

	outgoingMessageWriter.flush();
	outgoingMessageWriter.close();
	return outgoingMessage;
    }

    private void addSamlTicketToHeader(XMLEventWriter header, XMLEventReader samlTicket) throws XMLStreamException {

	while (samlTicket.hasNext()) {

	    XMLEvent nextEvent = samlTicket.nextEvent();

	    if (nextEvent.isStartElement()) {
		header.add(nextEvent.asStartElement());
	    } else if (nextEvent.isEndElement()) {
		header.add(nextEvent.asEndElement());
	    } else if (nextEvent.isCharacters()) {
		header.add(nextEvent.asCharacters());
	    }
	}
    }

    public boolean isNextEventArgusStartHeader(final XMLEvent event) {
	if (event.isStartElement()) {
	    return isArgosElement(event.asStartElement());
	}
	return false;
    }

    private boolean isArgosElement(final StartElement se) {
	return se.getName().getLocalPart().equals("ArgosHeader");
    }

    public boolean isNextEventArgusEndHeader(final XMLEvent event) {
	if (event.isEndElement()) {
	    return isArgosElement(event.asEndElement());
	}
	return false;
    }

    private boolean isArgosElement(final EndElement se) {
	return se.getName().getLocalPart().equals("ArgosHeader");
    }
}
