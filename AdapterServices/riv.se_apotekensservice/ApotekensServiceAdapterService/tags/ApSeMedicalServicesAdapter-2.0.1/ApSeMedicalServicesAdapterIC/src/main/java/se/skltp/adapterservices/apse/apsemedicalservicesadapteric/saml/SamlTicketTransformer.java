/**
 * Copyright (c) 2013 Center for eHalsa i samverkan (CeHis).
 * 							<http://cehis.se/>
 *
 * This file is part of SKLTP.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package se.skltp.adapterservices.apse.apsemedicalservicesadapteric.saml;

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
import org.soitoolkit.commons.mule.util.XmlUtil;

import se.skltp.adapterservices.apse.apsemedicalservicesadapteric.argos.ArgosHeader;
import se.skltp.adapterservices.apse.apsemedicalservicesadapteric.argos.ArgosHeaderHelper;
import se.skltp.adapterservices.apse.apsemedicalservicesadapteric.exception.TicketMachineException;
import se.skltp.adapterservices.apse.apsemedicalservicesadapteric.ticket.TicketMachine;

/**
 * Transformer is responsible to add a SAML ticket to the incoming request,
 * based on Argos header information.
 * 
 */
@SuppressWarnings("deprecation")
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
	    
		// Convert to String and then to a new XMLEStreamReader to avoid problems with Mule2 --> Mule3 usage of ReversibleXMLStreamReader
		// TODO Simplify this xml processing...
    	String xml = XmlUtil.convertReversibleXMLStreamReaderToString(originalRequest, "UTF-8");
		log.debug("XML before replacing argos header with saml ticket: \n" + xml);

    	XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new StringReader(xml));

	    ByteArrayOutputStream updatedRequest = addSamlTicketToOriginalRequest(xmlStreamReader, samlTicket);
	    MuleMessage updatedMuleMessage = updatePayload(msg, updatedRequest);

	    if (log.isDebugEnabled()) {
	    	String xmlAfter = XmlUtil.convertReversibleXMLStreamReaderToString((ReversibleXMLStreamReader) updatedMuleMessage.getPayload(), "UTF-8");
			log.debug("XML after replacing argos header with saml ticket: \n" + xmlAfter);
	    }
	    
		return updatedMuleMessage;
	} catch (Exception e) {
	    log.error("Could not transform/apply saml ticket to message", e);
	    throw new IllegalStateException("Could not transform/apply saml ticket to message");
	}
    }

    XMLEventReader extractSamlTicket(MuleMessage msg) throws XMLStreamException, FactoryConfigurationError, TicketMachineException {
		ArgosHeader argosHeader = new ArgosHeaderHelper().extractArgosHeader(msg);
		return createSamlTicketFromArgosHeader(argosHeader);
    }

	XMLEventReader createSamlTicketFromArgosHeader(ArgosHeader argosHeader) throws TicketMachineException, XMLStreamException, FactoryConfigurationError {
		String samlTicketStr = new TicketMachine().produceSamlTicket(argosHeader);
		log.debug("Created saml ticket: \n" + samlTicketStr);

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
		log.debug("Inside argos header, replaced it with SAML ticket");
	    }

	    if (isNextEventArgusEndHeader(event)) {
		insideArgosHeader = false;
		log.debug("Exit argos header");
		continue;
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
