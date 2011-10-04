package se.skl.tp.ticket.transformer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleMessage;
import org.mule.module.xml.stax.ReversibleXMLStreamReader;

public class ArgosHeaderHelperTest {

    @Test
    public void testEmptyArgosHeaderIsCreatedWhenNoInfoIsProvided() throws UnsupportedEncodingException,
	    XMLStreamException {
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
	assert argosHeader != null;
	assert argosHeader.getArbetsplatskod() == null;
	assert argosHeader.getArbetsplatsnamn() == null;
	assert argosHeader.getBefattningskod() == null;
	assert argosHeader.getDirectoryID() == null;
	assert argosHeader.getEfternamn() == null;
	assert argosHeader.getFornamn() == null;
	assert argosHeader.getForskrivarkod() == null;
	assert argosHeader.getHsaID() == null;
	assert argosHeader.getKatalog() == null;
	assert argosHeader.getLegitimationskod() == null;
	assert argosHeader.getOrganisationsnummer() == null;
	assert argosHeader.getPostadress() == null;
	assert argosHeader.getPostnummer() == null;
	assert argosHeader.getPostort() == null;
	assert argosHeader.getRequestId() == null;
	assert argosHeader.getRollnamn() == null;
	assert argosHeader.getSystemIp() == null;
	assert argosHeader.getSystemnamn() == null;
	assert argosHeader.getSystemversion() == null;
	assert argosHeader.getTelefonnummer() == null;
	assert argosHeader.getYrkesgrupp() == null;
    }

    private void assertCompleteArgosHeader(ArgosHeader expectedArgosHeader, ArgosHeader actualArgosHeader) {

	assert actualArgosHeader != null;
	assert actualArgosHeader.getArbetsplatskod() == actualArgosHeader.getArbetsplatskod();
	assert actualArgosHeader.getArbetsplatsnamn() == actualArgosHeader.getArbetsplatsnamn();
	assert actualArgosHeader.getBefattningskod() == actualArgosHeader.getBefattningskod();
	assert actualArgosHeader.getDirectoryID() == actualArgosHeader.getDirectoryID();
	assert actualArgosHeader.getEfternamn() == actualArgosHeader.getEfternamn();
	assert actualArgosHeader.getFornamn() == actualArgosHeader.getFornamn();
	assert actualArgosHeader.getForskrivarkod() == actualArgosHeader.getForskrivarkod();
	assert actualArgosHeader.getHsaID() == actualArgosHeader.getHsaID();
	assert actualArgosHeader.getKatalog() == actualArgosHeader.getKatalog();
	assert actualArgosHeader.getLegitimationskod() == actualArgosHeader.getLegitimationskod();
	assert actualArgosHeader.getOrganisationsnummer() == actualArgosHeader.getOrganisationsnummer();
	assert actualArgosHeader.getPostadress() == actualArgosHeader.getPostadress();
	assert actualArgosHeader.getPostnummer() == actualArgosHeader.getPostnummer();
	assert actualArgosHeader.getPostort() == actualArgosHeader.getPostort();
	assert actualArgosHeader.getRequestId() == actualArgosHeader.getRequestId();
	assert actualArgosHeader.getRollnamn() == actualArgosHeader.getRollnamn();
	assert actualArgosHeader.getSystemIp() == actualArgosHeader.getSystemIp();
	assert actualArgosHeader.getSystemnamn() == actualArgosHeader.getSystemnamn();
	assert actualArgosHeader.getSystemversion() == actualArgosHeader.getSystemversion();
	assert actualArgosHeader.getTelefonnummer() == actualArgosHeader.getTelefonnummer();
	assert actualArgosHeader.getYrkesgrupp() == actualArgosHeader.getYrkesgrupp();
    }

    private ArgosHeader createExpectedArgosHeader() {

	String forskrivarkod = "1111129";
	String legitimationskod = "123";
	String fornamn = "Lars";
	String efternamn = "LŠkare";
	String yrkesgrupp = "LŠkare";
	String befattningskod = "123456";
	String arbetsplatskod = "1234567890";
	String arbetsplatsnamn = "Sjukhuset";
	String postort = "Staden";
	String postadress = "VŠgen 1";
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

	String muleMessage = "<?xml version='1.0' encoding='UTF-8'?>"
		+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:riv:inera.se.apotekensservice:argos:1\" xmlns:add=\"http://www.w3.org/2005/08/addressing\" xmlns:urn1=\"urn:riv:se.apotekensservice:or:HamtaAktuellaOrdinationerResponder:1\">"
		+ "<soapenv:Header>" + "<add:To>1234567</add:To>" + "</soapenv:Header>" + "<soapenv:Body>"
		+ "<urn1:HamtaAktuellaOrdinationer>" + "<urn1:personnummer>" + personnummer + "</urn1:personnummer>"
		+ "</urn1:HamtaAktuellaOrdinationer>" + "</soapenv:Body>" + "</soapenv:Envelope>";

	if (expectedArgosHeader != null) {
	    muleMessage = "<?xml version='1.0' encoding='UTF-8'?>"
		    + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:riv:inera.se.apotekensservice:argos:1\" xmlns:add=\"http://www.w3.org/2005/08/addressing\" xmlns:urn1=\"urn:riv:se.apotekensservice:or:HamtaAktuellaOrdinationerResponder:1\">"
		    + "<soapenv:Header>" + "<urn:ArgosHeader>"
		    + "<urn:yrkesgrupp>${expectedArgosHeader.yrkesgrupp}</urn:yrkesgrupp>"
		    + "<urn:forskrivarkod>${expectedArgosHeader.forskrivarkod}</urn:forskrivarkod>"
		    + "<urn:legitimationskod>${expectedArgosHeader.legitimationskod}</urn:legitimationskod>"
		    + "<urn:fornamn>${expectedArgosHeader.fornamn}</urn:fornamn>"
		    + "<urn:efternamn>${expectedArgosHeader.efternamn}</urn:efternamn>"
		    + "<urn:befattningskod>${expectedArgosHeader.befattningskod}</urn:befattningskod>"
		    + "<urn:arbetsplatskod>${expectedArgosHeader.arbetsplatskod}</urn:arbetsplatskod>"
		    + "<urn:arbetsplatsnamn>${expectedArgosHeader.arbetsplatsnamn}</urn:arbetsplatsnamn>"
		    + "<urn:postort>${expectedArgosHeader.postort}</urn:postort>"
		    + "<urn:postadress>${expectedArgosHeader.postadress}</urn:postadress>"
		    + "<urn:postnummer>${expectedArgosHeader.postnummer}</urn:postnummer>"
		    + "<urn:telefonnummer>${expectedArgosHeader.telefonnummer}</urn:telefonnummer>"
		    + "<urn:requestId>${expectedArgosHeader.requestId}</urn:requestId>"
		    + "<urn:rollnamn>${expectedArgosHeader.rollnamn}</urn:rollnamn>"
		    + "<urn:directoryID>${expectedArgosHeader.directoryID}</urn:directoryID>"
		    + "<urn:hsaID>${expectedArgosHeader.hsaID}</urn:hsaID>"
		    + "<urn:katalog>${expectedArgosHeader.katalog}</urn:katalog>"
		    + "<urn:organisationsnummer>${expectedArgosHeader.organisationsnummer}</urn:organisationsnummer>"
		    + "<urn:systemnamn>${expectedArgosHeader.systemnamn}</urn:systemnamn>"
		    + "<urn:systemversion>${expectedArgosHeader.systemversion}</urn:systemversion>"
		    + "<urn:systemIp>${expectedArgosHeader.systemIp}</urn:systemIp>" + "</urn:ArgosHeader>"
		    + "<add:To>1234567</add:To>" + "</soapenv:Header>" + "<soapenv:Body>"
		    + "<urn1:HamtaAktuellaOrdinationer>" + "<urn1:personnummer>${personnummer}</urn1:personnummer>"
		    + "</urn1:HamtaAktuellaOrdinationer>" + "</soapenv:Body>" + "</soapenv:Envelope>";
	}

	InputStream is = new ByteArrayInputStream(muleMessage.getBytes("UTF-8"));
	XMLInputFactory factory = XMLInputFactory.newInstance();
	XMLStreamReader parser = factory.createXMLStreamReader(is, "UTF-8");
	ReversibleXMLStreamReader reversibleXMLStreamReader = new ReversibleXMLStreamReader(parser);
	MuleMessage msg = new DefaultMuleMessage((Object) reversibleXMLStreamReader);
	return msg;
    }
}
