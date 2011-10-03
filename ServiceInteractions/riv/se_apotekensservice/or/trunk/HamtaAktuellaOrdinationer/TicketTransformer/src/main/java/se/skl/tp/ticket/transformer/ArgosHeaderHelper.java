/**
 * Copyright 2011 Sjukvardsradgivningen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public

 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the

 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,

 *   Boston, MA 02111-1307  USA
 */
package se.skl.tp.ticket.transformer;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.mule.api.MuleMessage;
import org.mule.module.xml.stax.ReversibleXMLStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ArgosHeaderHelper {

    private static Logger log = LoggerFactory.getLogger(ArgosHeaderHelper.class);

    private final XMLInputFactory xmlInputFactory;

    private static final String FORSKRIVAR_KOD = "forskrivarkod";
    private static final String LEGITIMATIONS_KOD = "legitimationskod";
    private static final String FORNAMN = "fornamn";
    private static final String EFTERNAMN = "efternamn";
    private static final String BEFATTNINGS_KOD = "befattningskod";
    private static final String ARBETSPLATS_KOD = "arbetsplatskod";
    private static final String ARBETSPLATS_NAMN = "arbetsplatsnamn";
    private static final String POSTORT = "postort";
    private static final String POSTADRESS = "postadress";
    private static final String POSTNUMMER = "postnummer";
    private static final String TELEFONNUMMER = "telefonnummer";
    private static final String REQUEST_ID = "requestId";
    private static final String ROLLNAMN = "rollnamn";
    private static final String DIRECTORY_ID = "directoryID";
    private static final String HSA_ID = "hsaID";
    private static final String KATALOG = "katalog";
    private static final String ORGANISATIONSNUMMER = "organisationsnummer";
    private static final String SYSTEMNAMN = "systemnamn";
    private static final String SYSTEMVERSION = "systemversion";
    private static final String SYSTEM_IP = "systemIp";
    private static final String YRKESGRUPP = "yrkesgrupp";

    private static final List<String> argosElements = new ArrayList<String>() {
	private static final long serialVersionUID = 1L;
	{
	    add(FORSKRIVAR_KOD);
	    add(LEGITIMATIONS_KOD);
	    add(FORNAMN);
	    add(EFTERNAMN);
	    add(BEFATTNINGS_KOD);
	    add(ARBETSPLATS_KOD);
	    add(ARBETSPLATS_NAMN);
	    add(POSTORT);
	    add(POSTADRESS);
	    add(POSTNUMMER);
	    add(TELEFONNUMMER);
	    add(REQUEST_ID);
	    add(ROLLNAMN);
	    add(DIRECTORY_ID);
	    add(HSA_ID);
	    add(KATALOG);
	    add(ORGANISATIONSNUMMER);
	    add(SYSTEMNAMN);
	    add(SYSTEMVERSION);
	    add(SYSTEM_IP);
	    add(YRKESGRUPP);
	}
    };

    ArgosHeaderHelper() {
	xmlInputFactory = XMLInputFactory.newInstance();
    }

    /**
     * Extract ArgosHeader from the message. If no argos information is found an
     * empty ArgosHeader is returned. The cursor in the mule message is reset
     * after argos header is extracted.
     * 
     * @param msg
     *            The message to get argos information from
     * @return The ArgosHeader filled with information from message
     * @throws IllegalStateException
     *             in case of any exceptions
     */
    ArgosHeader extractArgosHeader(MuleMessage msg) throws IllegalStateException {
	log.info("Argos header extractor executing");
	try {
	    final XMLEventReader argosRequestEvents = getXmlReaderWithTrackingEnabled(msg);
	    ArgosHeader argosHeader = new ArgosHeader();
	    while (argosRequestEvents.hasNext()) {
		final XMLEvent nextEvent = argosRequestEvents.nextEvent();
		if (isArgosStartElement(nextEvent)) {
		    extractNextValue(nextEvent, argosRequestEvents, argosHeader);
		}
	    }
	    return argosHeader;
	} catch (Exception e) {
	    log.error("Could not extract argos header information", e);
	    throw new IllegalStateException("Could not extract argos header information", e);
	} finally {
	    setCursorAtBeginningOfXML(msg);
	}
    }

    private XMLEventReader getXmlReaderWithTrackingEnabled(MuleMessage msg) throws XMLStreamException {
	final ReversibleXMLStreamReader argosRequestReader = (ReversibleXMLStreamReader) msg.getPayload();
	argosRequestReader.setTracking(true);
	final XMLEventReader argosRequestXmlEvents = xmlInputFactory.createXMLEventReader(argosRequestReader);
	return argosRequestXmlEvents;
    }

    private void setCursorAtBeginningOfXML(MuleMessage msg) {
	final ReversibleXMLStreamReader argosRequestReader = (ReversibleXMLStreamReader) msg.getPayload();
	argosRequestReader.reset();
    }

    private boolean isArgosStartElement(final XMLEvent nextEvent) {
	if (nextEvent.getEventType() == XMLStreamConstants.START_ELEMENT) {
	    StartElement startElement = nextEvent.asStartElement();
	    return argosElements.contains(startElement.getName().getLocalPart());
	}
	return false;
    }

    private void extractNextValue(final XMLEvent startEvent, final XMLEventReader originalRequestEvents,
	    ArgosHeader argosHeader) throws XMLStreamException {

	StartElement startElement = startEvent.asStartElement();
	String xmlElement = startElement.getName().getLocalPart();
	String xmlValue = null;

	final XMLEvent nextEvent = originalRequestEvents.nextEvent();
	if (nextEvent.isCharacters()) {
	    Characters characters = (Characters) nextEvent;
	    xmlValue = characters.getData();
	    setValueInArgosHeader(argosHeader, xmlElement, xmlValue);
	}
    }

    private void setValueInArgosHeader(ArgosHeader argosHeader, String xmlElement, String xmlValue) {
	if (FORSKRIVAR_KOD.equals(xmlElement)) {
	    argosHeader.setForskrivarkod(xmlValue);
	}
	if (LEGITIMATIONS_KOD.equals(xmlElement)) {
	    argosHeader.setLegitimationskod(xmlValue);
	}
	if (FORNAMN.equals(xmlElement)) {
	    argosHeader.setFornamn(xmlValue);
	}
	if (EFTERNAMN.equals(xmlElement)) {
	    argosHeader.setEfternamn(xmlValue);
	}
	if (BEFATTNINGS_KOD.equals(xmlElement)) {
	    argosHeader.setBefattningskod(xmlValue);
	}
	if (ARBETSPLATS_KOD.equals(xmlElement)) {
	    argosHeader.setArbetsplatskod(xmlValue);
	}
	if (ARBETSPLATS_NAMN.equals(xmlElement)) {
	    argosHeader.setArbetsplatsnamn(xmlValue);
	}
	if (POSTORT.equals(xmlElement)) {
	    argosHeader.setPostort(xmlValue);
	}
	if (POSTADRESS.equals(xmlElement)) {
	    argosHeader.setPostadress(xmlValue);
	}
	if (POSTNUMMER.equals(xmlElement)) {
	    argosHeader.setPostnummer(xmlValue);
	}
	if (TELEFONNUMMER.equals(xmlElement)) {
	    argosHeader.setTelefonnummer(xmlValue);
	}
	if (REQUEST_ID.equals(xmlElement)) {
	    argosHeader.setRequestId(xmlValue);
	}
	if (ROLLNAMN.equals(xmlElement)) {
	    argosHeader.setRollnamn(xmlValue);
	}
	if (DIRECTORY_ID.equals(xmlElement)) {
	    argosHeader.setDirectoryID(xmlValue);
	}
	if (HSA_ID.equals(xmlElement)) {
	    argosHeader.setHsaID(xmlValue);
	}
	if (KATALOG.equals(xmlElement)) {
	    argosHeader.setKatalog(xmlValue);
	}
	if (ORGANISATIONSNUMMER.equals(xmlElement)) {
	    argosHeader.setOrganisationsnummer(xmlValue);
	}
	if (SYSTEMNAMN.equals(xmlElement)) {
	    argosHeader.setSystemnamn(xmlValue);
	}
	if (SYSTEMVERSION.equals(xmlElement)) {
	    argosHeader.setSystemversion(xmlValue);
	}
	if (SYSTEM_IP.equals(xmlElement)) {
	    argosHeader.setSystemIp(xmlValue);
	}
	if (YRKESGRUPP.equals(xmlElement)) {
	    argosHeader.setYrkesGrupp(xmlValue);
	}
    }

}
