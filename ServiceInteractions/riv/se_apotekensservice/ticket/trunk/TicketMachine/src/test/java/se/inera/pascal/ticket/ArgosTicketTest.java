package se.inera.pascal.ticket;

import static org.junit.matchers.JUnitMatchers.containsString;
import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

public class ArgosTicketTest extends TestCase {

    @Test
    public void testHappyCaseToSeeHowTicketGeneratorWorks() {

	String forskrivarkod = "1111129";
	String legitimationskod = "";
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

	String ticket = new ArgosTicket().getTicket(forskrivarkod, legitimationskod, fornamn, efternamn, yrkesgrupp,
		befattningskod, arbetsplatskod, arbetsplatsnamn, postort, postadress, postnummer, telefonnummer,
		requestId, rollnamn, hsaID, katalog, organisationsnummer, systemnamn, systemversion, systemIp);

	Assert.assertThat(ticket, containsString("<saml2:Issuer>pascalonline</saml2:Issuer>"));

    }

    @Test
    public void testIneraCodeGivesNullpointerWhenAnyStringIsNull() {

	String forskrivarkod = "1111129";
	String legitimationskod = null;
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

	try {
	    new ArgosTicket().getTicket(forskrivarkod, legitimationskod, fornamn, efternamn, yrkesgrupp,
		    befattningskod, arbetsplatskod, arbetsplatsnamn, postort, postadress, postnummer, telefonnummer,
		    requestId, rollnamn, hsaID, katalog, organisationsnummer, systemnamn, systemversion, systemIp);
	} catch (NullPointerException e) {
	    return;
	}

	Assert.fail("Nullpointer is unfortenatly thrown when null value");

    }
}
