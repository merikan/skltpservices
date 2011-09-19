package se.skl.tp.ticket.transformers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

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
	    XMLEventReader samlTicket = dummyToTestgetSamlTicket();

	    final ReversibleXMLStreamReader originalRequest = (ReversibleXMLStreamReader) msg.getPayload();
	    ByteArrayOutputStream updatedRequest = addSamlTicketToOriginalRequest(originalRequest, samlTicket);
	    return setPayload(msg, updatedRequest);
	} catch (Exception e) {
	    log.error("Could not transform saml ticket", e);
	    throw new IllegalStateException("Could not transform saml ticket");
	}
    }

    private MuleMessage setPayload(MuleMessage msg, ByteArrayOutputStream updatedRequest) throws XMLStreamException {
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

    private void addSamlTicketToHeader(XMLEventWriter outgoingMessageWriter, XMLEventReader samlTicket)
	    throws XMLStreamException {

	while (samlTicket.hasNext()) {

	    XMLEvent nextEvent = samlTicket.nextEvent();

	    if (nextEvent.isStartElement()) {
		outgoingMessageWriter.add(nextEvent.asStartElement());
	    } else if (nextEvent.isEndElement()) {
		outgoingMessageWriter.add(nextEvent.asEndElement());
	    } else if (nextEvent.isCharacters()) {
		outgoingMessageWriter.add(nextEvent.asCharacters());
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

    private XMLEventReader dummyToTestgetSamlTicket() throws XMLStreamException {
	InputStream samlTicket = Thread.currentThread().getContextClassLoader()
		.getResourceAsStream("sampleSamlTicket.xml");
	XMLEventReader eventReader = xmlInputFactory.createXMLEventReader(samlTicket);
	return eventReader;
    }
}
