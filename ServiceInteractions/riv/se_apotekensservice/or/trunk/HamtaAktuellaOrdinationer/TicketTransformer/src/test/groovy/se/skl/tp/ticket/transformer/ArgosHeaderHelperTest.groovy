package se.skl.tp.ticket.transformer;

import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamReader

import org.mule.DefaultMuleMessage
import org.mule.api.MuleMessage
import org.mule.module.xml.stax.ReversibleXMLStreamReader

public class ArgosHeaderHelperTest extends GroovyTestCase{

    public void testEmptyArgosHeaderIsCreatedWhenNoInfoIsProvided() {
	ArgosHeader expectedArgosHeader = null
	String personnummer = '196308212817'
	MuleMessage muleMessageBasedOnArgosHeader = createMockedMuleMessage(expectedArgosHeader, personnummer);
	ArgosHeader actualArgosHeader = new ArgosHeaderHelper().extractArgosHeader(muleMessageBasedOnArgosHeader);

	assertArgosHeaderIsEmpty(actualArgosHeader)
    }

    public void testExtractArgosHeaderWithAllInformation() {
	ArgosHeader expectedArgosHeader = createExpectedArgosHeader();
	String personnummer = '196308212817'
	MuleMessage muleMessageBasedOnArgosHeader = createMockedMuleMessage(expectedArgosHeader, personnummer);
	ArgosHeader actualArgosHeader = new ArgosHeaderHelper().extractArgosHeader(muleMessageBasedOnArgosHeader);

	assertCompleteArgosHeader(expectedArgosHeader, actualArgosHeader);
    }
    
    private void assertArgosHeaderIsEmpty(ArgosHeader argosHeader){
	assert argosHeader != null
	assert argosHeader.arbetsplatskod == null
	assert argosHeader.arbetsplatsnamn == null
	assert argosHeader.befattningskod == null
	assert argosHeader.directoryID == null
	assert argosHeader.efternamn == null
	assert argosHeader.fornamn == null
	assert argosHeader.forskrivarkod == null
	assert argosHeader.hsaID == null
	assert argosHeader.katalog == null
	assert argosHeader.legitimationskod == null
	assert argosHeader.organisationsnummer == null
	assert argosHeader.postadress == null
	assert argosHeader.postnummer == null
	assert argosHeader.postort == null
	assert argosHeader.requestId == null
	assert argosHeader.rollnamn == null
	assert argosHeader.systemIp == null
	assert argosHeader.systemnamn == null
	assert argosHeader.systemversion == null
	assert argosHeader.telefonnummer == null
	assert argosHeader.yrkesgrupp == null
    }
    
    private void assertCompleteArgosHeader(ArgosHeader expectedArgosHeader, ArgosHeader actualArgosHeader) {
	assert actualArgosHeader != null
	assert expectedArgosHeader.arbetsplatskod == actualArgosHeader.arbetsplatskod
	assert expectedArgosHeader.arbetsplatsnamn == actualArgosHeader.arbetsplatsnamn
	assert expectedArgosHeader.befattningskod == actualArgosHeader.befattningskod
	assert expectedArgosHeader.directoryID == actualArgosHeader.directoryID
	assert expectedArgosHeader.efternamn == actualArgosHeader.efternamn
	assert expectedArgosHeader.fornamn == actualArgosHeader.fornamn
	assert expectedArgosHeader.forskrivarkod == actualArgosHeader.forskrivarkod
	assert expectedArgosHeader.hsaID == actualArgosHeader.hsaID
	assert expectedArgosHeader.katalog == actualArgosHeader.katalog
	assert expectedArgosHeader.legitimationskod == actualArgosHeader.legitimationskod
	assert expectedArgosHeader.organisationsnummer == actualArgosHeader.organisationsnummer
	assert expectedArgosHeader.postadress == actualArgosHeader.postadress
	assert expectedArgosHeader.postnummer == actualArgosHeader.postnummer
	assert expectedArgosHeader.postort == actualArgosHeader.postort
	assert expectedArgosHeader.requestId == actualArgosHeader.requestId
	assert expectedArgosHeader.rollnamn == actualArgosHeader.rollnamn
	assert expectedArgosHeader.systemIp == actualArgosHeader.systemIp
	assert expectedArgosHeader.systemnamn == actualArgosHeader.systemnamn
	assert expectedArgosHeader.systemversion == actualArgosHeader.systemversion
	assert expectedArgosHeader.telefonnummer == actualArgosHeader.telefonnummer
	assert expectedArgosHeader.yrkesgrupp == actualArgosHeader.yrkesgrupp
    }

    private ArgosHeader createExpectedArgosHeader(){

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

    private MuleMessage createMockedMuleMessage(ArgosHeader expectedArgosHeader, String personnummer) {

	def muleMessage = """<?xml version='1.0' encoding='UTF-8'?>
	    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:urn="urn:riv:inera.se.apotekensservice:argos:1" xmlns:add="http://www.w3.org/2005/08/addressing" xmlns:urn1="urn:riv:se.apotekensservice:or:HamtaAktuellaOrdinationerResponder:1">
		    <soapenv:Header>
		       <add:To>1234567</add:To>
		    </soapenv:Header>
		    <soapenv:Body>
		       <urn1:HamtaAktuellaOrdinationer>
			  <urn1:personnummer>${personnummer}</urn1:personnummer>
		       </urn1:HamtaAktuellaOrdinationer>
		    </soapenv:Body>
	    </soapenv:Envelope>"""


	if(expectedArgosHeader != null){
	    muleMessage = """<?xml version='1.0' encoding='UTF-8'?>
		<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:urn="urn:riv:inera.se.apotekensservice:argos:1" xmlns:add="http://www.w3.org/2005/08/addressing" xmlns:urn1="urn:riv:se.apotekensservice:or:HamtaAktuellaOrdinationerResponder:1">
			<soapenv:Header>
			   <urn:ArgosHeader>
			      <urn:yrkesgrupp>${expectedArgosHeader.yrkesgrupp}</urn:yrkesgrupp>
			      <urn:forskrivarkod>${expectedArgosHeader.forskrivarkod}</urn:forskrivarkod>
			      <urn:legitimationskod>${expectedArgosHeader.legitimationskod}</urn:legitimationskod>
			      <urn:fornamn>${expectedArgosHeader.fornamn}</urn:fornamn>
			      <urn:efternamn>${expectedArgosHeader.efternamn}</urn:efternamn>
			      <urn:befattningskod>${expectedArgosHeader.befattningskod}</urn:befattningskod>
			      <urn:arbetsplatskod>${expectedArgosHeader.arbetsplatskod}</urn:arbetsplatskod>
			      <urn:arbetsplatsnamn>${expectedArgosHeader.arbetsplatsnamn}</urn:arbetsplatsnamn>
			      <urn:postort>${expectedArgosHeader.postort}</urn:postort>
			      <urn:postadress>${expectedArgosHeader.postadress}</urn:postadress>
			      <urn:postnummer>${expectedArgosHeader.postnummer}</urn:postnummer>
			      <urn:telefonnummer>${expectedArgosHeader.telefonnummer}</urn:telefonnummer>
			      <urn:requestId>${expectedArgosHeader.requestId}</urn:requestId>
			      <urn:rollnamn>${expectedArgosHeader.rollnamn}</urn:rollnamn>
			      <urn:directoryID>${expectedArgosHeader.directoryID}</urn:directoryID>
			      <urn:hsaID>${expectedArgosHeader.hsaID}</urn:hsaID>
			      <urn:katalog>${expectedArgosHeader.katalog}</urn:katalog>
			      <urn:organisationsnummer>${expectedArgosHeader.organisationsnummer}</urn:organisationsnummer>
			      <urn:systemnamn>${expectedArgosHeader.systemnamn}</urn:systemnamn>
			      <urn:systemversion>${expectedArgosHeader.systemversion}</urn:systemversion>
			      <urn:systemIp>${expectedArgosHeader.systemIp}</urn:systemIp>
			   </urn:ArgosHeader>
			   <add:To>1234567</add:To>
			</soapenv:Header>
			<soapenv:Body>
			   <urn1:HamtaAktuellaOrdinationer>
			      <urn1:personnummer>${personnummer}</urn1:personnummer>
			   </urn1:HamtaAktuellaOrdinationer>
			</soapenv:Body>
		</soapenv:Envelope>"""
	}

	InputStream is = new ByteArrayInputStream(muleMessage.getBytes('UTF-8'))
	XMLInputFactory factory = XMLInputFactory.newInstance();
	XMLStreamReader parser = factory.createXMLStreamReader(is,'UTF-8');
	ReversibleXMLStreamReader reversibleXMLStreamReader = new ReversibleXMLStreamReader(parser)
	MuleMessage msg = new DefaultMuleMessage((Object)reversibleXMLStreamReader)
	return msg
    }
}
