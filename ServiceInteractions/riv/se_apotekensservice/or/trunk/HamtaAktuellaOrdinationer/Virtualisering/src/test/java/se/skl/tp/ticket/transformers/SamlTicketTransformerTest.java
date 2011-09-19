package se.skl.tp.ticket.transformers;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.junit.Before;
import org.junit.Test;

public class SamlTicketTransformerTest {

    private XMLInputFactory xmlInputFactory = null;

    @Before
    public void setUpXmlFactory() {
	xmlInputFactory = XMLInputFactory.newInstance();
    }

    @Test
    public void addSamlTicketToRequest() throws Exception {

	final URL resource = Thread.currentThread().getContextClassLoader()
		.getResource("testFiles/HamtaAllaOrdinationer-request.xml");

	final XMLStreamReader requestWithNoTicket = xmlInputFactory.createXMLStreamReader(resource.openStream());

	final XMLEventReader samlTicket = getSamlTicket();

	final ByteArrayOutputStream requestIncludingTicket = new SamlTicketTransformer()
		.addSamlTicketToOriginalRequest(requestWithNoTicket, samlTicket);

	System.out.println(requestIncludingTicket.toString("UTF-8"));
    }

    private XMLEventReader getSamlTicket() throws XMLStreamException {

	InputStream samlTicket = Thread.currentThread().getContextClassLoader()
		.getResourceAsStream("testFiles/sampleSamlTicket.xml");
	XMLEventReader eventReader = xmlInputFactory.createXMLEventReader(samlTicket);
	return eventReader;
    }
}
