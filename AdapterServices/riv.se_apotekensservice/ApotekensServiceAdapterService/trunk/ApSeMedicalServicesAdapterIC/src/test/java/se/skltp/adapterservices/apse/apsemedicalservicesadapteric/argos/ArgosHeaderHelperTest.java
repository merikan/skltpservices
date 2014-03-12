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
package se.skltp.adapterservices.apse.apsemedicalservicesadapteric.argos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.junit.Test;
import org.mockito.Mockito;
import org.mule.api.MuleMessage;
import org.mule.module.xml.stax.ReversibleXMLStreamReader;

import se.skltp.adapterservices.apse.apsemedicalservicesadapteric.argos.ArgosHeader;
import se.skltp.adapterservices.apse.apsemedicalservicesadapteric.argos.ArgosHeaderHelper;

public class ArgosHeaderHelperTest {

	static final XMLInputFactory xmlInputFactory  = XMLInputFactory.newInstance();

	@Test
    public void testExtractArgosHeaderWithAllInformationFromXMLEventReader() throws Exception {
    	ArgosHeader expectedArgosHeader = createExpectedArgosHeader();
    	String personnummer = "196308212817";
    	
        String xml = getXmlPayload(expectedArgosHeader, personnummer);
    	XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new StringReader(xml));
	    XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(xmlStreamReader);
	    ArgosHeader actualArgosHeader = new ArgosHeaderHelper().extractArgosHeaderFromXMLEventReader(xmlEventReader);
	    
		assertCompleteArgosHeader(expectedArgosHeader, actualArgosHeader);
    }

	@Test
    public void testEmptyArgosHeaderIsCreatedWhenNoInfoIsProvidedFromXMLEventReader() throws Exception {
    	ArgosHeader expectedArgosHeader = null;
    	String personnummer = "196308212817";
    	
        String xml = getXmlPayload(expectedArgosHeader, personnummer);
    	XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new StringReader(xml));
	    XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(xmlStreamReader);
	    ArgosHeader actualArgosHeader = new ArgosHeaderHelper().extractArgosHeaderFromXMLEventReader(xmlEventReader);
	    
	    assertArgosHeaderIsEmpty(actualArgosHeader);
    }

    @Test
    public void testEmptyArgosHeaderIsCreatedWhenNoInfoIsProvided() throws UnsupportedEncodingException, XMLStreamException {
		ArgosHeader expectedArgosHeader = null;
		String personnummer = "196308212817";

		MuleMessage muleMessageBasedOnArgosHeader = createMockedMuleMessage(expectedArgosHeader, personnummer);
		ArgosHeader actualArgosHeader = new ArgosHeaderHelper().extractArgosHeader(muleMessageBasedOnArgosHeader);
	
		assertArgosHeaderIsEmpty(actualArgosHeader);
    }

    @Test
    public void testExtractArgosHeaderWithAllInformation() throws UnsupportedEncodingException, XMLStreamException {
		ArgosHeader expectedArgosHeader = createExpectedArgosHeader();
		String personnummer = "196308212817";

		MuleMessage muleMessageBasedOnArgosHeader = createMockedMuleMessage(expectedArgosHeader, personnummer);
		ArgosHeader actualArgosHeader = new ArgosHeaderHelper().extractArgosHeader(muleMessageBasedOnArgosHeader);
	
		assertCompleteArgosHeader(expectedArgosHeader, actualArgosHeader);
    }

    private void assertArgosHeaderIsEmpty(ArgosHeader argosHeader) {
	    assertNotNull(argosHeader);

	    assertNull(argosHeader.getArbetsplatskod());
		assertNull(argosHeader.getArbetsplatsnamn());
		assertNull(argosHeader.getBefattningskod());
		assertNull(argosHeader.getDirectoryID());
		assertNull(argosHeader.getEfternamn());
		assertNull(argosHeader.getFornamn());
		assertNull(argosHeader.getForskrivarkod());
		assertNull(argosHeader.getHsaID());
		assertNull(argosHeader.getKatalog());
		assertNull(argosHeader.getLegitimationskod());
		assertNull(argosHeader.getOrganisationsnummer());
		assertNull(argosHeader.getPostadress());
		assertNull(argosHeader.getPostnummer());
		assertNull(argosHeader.getPostort());
		assertNull(argosHeader.getRequestId());
		assertNull(argosHeader.getRollnamn());
		assertNull(argosHeader.getSystemIp());
		assertNull(argosHeader.getSystemnamn());
		assertNull(argosHeader.getSystemversion());
		assertNull(argosHeader.getTelefonnummer());
		assertNull(argosHeader.getYrkesgrupp());
    }

    private void assertCompleteArgosHeader(ArgosHeader expectedArgosHeader, ArgosHeader actualArgosHeader) {
	    assertNotNull(actualArgosHeader);
	    assertNotNull(expectedArgosHeader);
	
	    assertEquals(expectedArgosHeader.getArbetsplatskod(), actualArgosHeader.getArbetsplatskod());
		assertEquals(expectedArgosHeader.getArbetsplatsnamn(), actualArgosHeader.getArbetsplatsnamn());
		assertEquals(expectedArgosHeader.getBefattningskod(), actualArgosHeader.getBefattningskod());
		assertEquals(expectedArgosHeader.getDirectoryID(), actualArgosHeader.getDirectoryID());
		assertEquals(expectedArgosHeader.getEfternamn(), actualArgosHeader.getEfternamn());
		assertEquals(expectedArgosHeader.getFornamn(), actualArgosHeader.getFornamn());
		assertEquals(expectedArgosHeader.getForskrivarkod(), actualArgosHeader.getForskrivarkod());
		assertEquals(expectedArgosHeader.getHsaID(), actualArgosHeader.getHsaID());
		assertEquals(expectedArgosHeader.getKatalog(), actualArgosHeader.getKatalog());
		assertEquals(expectedArgosHeader.getLegitimationskod(), actualArgosHeader.getLegitimationskod());
		assertEquals(expectedArgosHeader.getOrganisationsnummer(), actualArgosHeader.getOrganisationsnummer());
		assertEquals(expectedArgosHeader.getPostadress(), actualArgosHeader.getPostadress());
		assertEquals(expectedArgosHeader.getPostnummer(), actualArgosHeader.getPostnummer());
		assertEquals(expectedArgosHeader.getPostort(), actualArgosHeader.getPostort());
		assertEquals(expectedArgosHeader.getRequestId(), actualArgosHeader.getRequestId());
		assertEquals(expectedArgosHeader.getRollnamn(), actualArgosHeader.getRollnamn());
		assertEquals(expectedArgosHeader.getSystemIp(), actualArgosHeader.getSystemIp());
		assertEquals(expectedArgosHeader.getSystemnamn(), actualArgosHeader.getSystemnamn());
		assertEquals(expectedArgosHeader.getSystemversion(), actualArgosHeader.getSystemversion());
		assertEquals(expectedArgosHeader.getTelefonnummer(), actualArgosHeader.getTelefonnummer());
		assertEquals(expectedArgosHeader.getYrkesgrupp(), actualArgosHeader.getYrkesgrupp());
    }

    private ArgosHeader createExpectedArgosHeader() {

	String forskrivarkod = "1111129";
	String legitimationskod = "123";
	String fornamn = "Lars";
	String efternamn = "Lakare";
	String yrkesgrupp = "Lakare";
	String befattningskod = "123456";
	String arbetsplatskod = "1234567890";
	String arbetsplatsnamn = "Sjukhuset";
	String postort = "Staden";
	String postadress = "Vagen 1";
	String postnummer = "11111";
	String telefonnummer = "08-1234567";
	String requestId = "123456";
	String rollnamn = "FORSKRIVARE";
	String directoryID = "TSE6565656565-1003";
	String hsaID = "TSE6565656565-1003";
	String katalog = "HSA";
	String organisationsnummer = "1234567890";
	String systemnamn = "Melior";
	String systemversion = "1.0";
	String systemIp = "192.0.0.1";

	ArgosHeader expectedArgosHeader = new ArgosHeader(forskrivarkod, legitimationskod, fornamn, efternamn,
		yrkesgrupp, befattningskod, arbetsplatskod, arbetsplatsnamn, postort, postadress, postnummer,
		telefonnummer, requestId, rollnamn, directoryID, hsaID, katalog, organisationsnummer, systemnamn,
		systemversion, systemIp);

	return expectedArgosHeader;
    }

    private MuleMessage createMockedMuleMessage(ArgosHeader expectedArgosHeader, String personnummer)
	    throws XMLStreamException, UnsupportedEncodingException {

	String xml = getXmlPayload(expectedArgosHeader, personnummer);

	XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new StringReader(xml));

	ReversibleXMLStreamReader reversibleXMLStreamReader = new ReversibleXMLStreamReader(xmlStreamReader);

	MuleMessage msg = Mockito.mock(MuleMessage.class);
    Mockito.when(msg.getPayload()).thenReturn(reversibleXMLStreamReader);
	
	return msg;
    }

	private String getXmlPayload(ArgosHeader expectedArgosHeader, String personnummer) {
		String muleMessage = "<?xml version='1.0' encoding='UTF-8'?>"
			+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:riv:inera.se.apotekensservice:argos:1\" xmlns:add=\"http://www.w3.org/2005/08/addressing\" xmlns:urn1=\"urn:riv:se.apotekensservice:or:HamtaAktuellaOrdinationerResponder:1\">"
			+ "<soapenv:Header>" + "<add:To>1234567</add:To>" + "</soapenv:Header>" + "<soapenv:Body>"
			+ "<urn1:HamtaAktuellaOrdinationer>" + "<urn1:personnummer>" + personnummer + "</urn1:personnummer>"
			+ "</urn1:HamtaAktuellaOrdinationer>" + "</soapenv:Body>" + "</soapenv:Envelope>";

		if (expectedArgosHeader != null) {
		    muleMessage = "<?xml version='1.0' encoding='UTF-8'?>"
			    + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:riv:inera.se.apotekensservice:argos:1\" xmlns:add=\"http://www.w3.org/2005/08/addressing\" xmlns:urn1=\"urn:riv:se.apotekensservice:or:HamtaAktuellaOrdinationerResponder:1\">"
			    + "<soapenv:Header>" + "<urn:ArgosHeader>"
			    + "<urn:yrkesgrupp>" + expectedArgosHeader.getYrkesgrupp() + "</urn:yrkesgrupp>"
			    + "<urn:forskrivarkod>" + expectedArgosHeader.getForskrivarkod() + "</urn:forskrivarkod>"
			    + "<urn:legitimationskod>" + expectedArgosHeader.getLegitimationskod() + "</urn:legitimationskod>"
			    + "<urn:fornamn>" + expectedArgosHeader.getFornamn() + "</urn:fornamn>"
			    + "<urn:efternamn>" + expectedArgosHeader.getEfternamn() + "</urn:efternamn>"
			    + "<urn:befattningskod>" + expectedArgosHeader.getBefattningskod() + "</urn:befattningskod>"
			    + "<urn:arbetsplatskod>" + expectedArgosHeader.getArbetsplatskod() + "</urn:arbetsplatskod>"
			    + "<urn:arbetsplatsnamn>" + expectedArgosHeader.getArbetsplatsnamn() + "</urn:arbetsplatsnamn>"
			    + "<urn:postort>" + expectedArgosHeader.getPostort() + "</urn:postort>"
			    + "<urn:postadress>" + expectedArgosHeader.getPostadress() + "</urn:postadress>"
			    + "<urn:postnummer>" + expectedArgosHeader.getPostnummer() + "</urn:postnummer>"
			    + "<urn:telefonnummer>" + expectedArgosHeader.getTelefonnummer() + "</urn:telefonnummer>"
			    + "<urn:requestId>" + expectedArgosHeader.getRequestId() + "</urn:requestId>"
			    + "<urn:rollnamn>" + expectedArgosHeader.getRollnamn() + "</urn:rollnamn>"
			    + "<urn:directoryID>" + expectedArgosHeader.getDirectoryID() + "</urn:directoryID>"
			    + "<urn:hsaID>" + expectedArgosHeader.getHsaID() + "</urn:hsaID>"
			    + "<urn:katalog>" + expectedArgosHeader.getKatalog() + "</urn:katalog>"
			    + "<urn:organisationsnummer>" + expectedArgosHeader.getOrganisationsnummer() + "</urn:organisationsnummer>"
			    + "<urn:systemnamn>" + expectedArgosHeader.getSystemnamn() + "</urn:systemnamn>"
			    + "<urn:systemversion>" + expectedArgosHeader.getSystemversion() + "</urn:systemversion>"
			    + "<urn:systemIp>" + expectedArgosHeader.getSystemIp() + "</urn:systemIp>" + "</urn:ArgosHeader>"
			    + "<add:To>1234567</add:To>" + "</soapenv:Header>" + "<soapenv:Body>"
			    + "<urn1:HamtaAktuellaOrdinationer>" + "<urn1:personnummer>" + personnummer + "</urn1:personnummer>"
			    + "</urn1:HamtaAktuellaOrdinationer>" + "</soapenv:Body>" + "</soapenv:Envelope>";
		}
		return muleMessage;
	}
}
