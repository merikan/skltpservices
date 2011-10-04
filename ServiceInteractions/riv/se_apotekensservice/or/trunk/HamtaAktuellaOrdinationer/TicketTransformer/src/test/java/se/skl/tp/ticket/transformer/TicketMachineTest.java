package se.skl.tp.ticket.transformer;

import static org.junit.matchers.JUnitMatchers.containsString;

import org.junit.Assert;
import org.junit.Test;

public class TicketMachineTest {

    @Test
    public void testProduceCompleteSamlTicketFromArgosHeader() {

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

	ArgosHeader argosHeader = new ArgosHeader(forskrivarkod, legitimationskod, fornamn, efternamn, yrkesgrupp,
		befattningskod, arbetsplatskod, arbetsplatsnamn, postort, postadress, postnummer, telefonnummer,
		requestId, rollnamn, directoryID, hsaID, katalog, organisationsnummer, systemnamn, systemversion,
		systemIp);

	String ticket = new TicketMachine().produceSamlTicket(argosHeader);

	Assert.assertThat(ticket, containsString("<saml2:Issuer>pascalonline</saml2:Issuer>"));

    }

    @Test
    public void testApplyWsSecurityInformationToSamlTicket() {

	String samlTicket = "<saml2:Assertion xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\""
		+ "ID=\"_c4cda1f1c805c68a1a0c499ec6e34381\" IssueInstant=\"2011-09-28T06:32:56.395Z\""
		+ "Version=\"2.0\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">"
		+ "<saml2:Issuer>pascalonline</saml2:Issuer></saml2:Assertion>"
		+ "<saml2:Assertion xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\""
		+ "ID=\"_c4cda1f1c805c68a1a0c499ec6e34381\" IssueInstant=\"2011-09-28T06:32:56.395Z\""
		+ "Version=\"2.0\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">"
		+ "<saml2:Issuer>pascalonline</saml2:Issuer></saml2:Assertion>";

	String ticket = new TicketMachine().applyWsSecurityToSamlTicket(samlTicket);

	Assert.assertNotNull(ticket);

	Assert.assertThat(
		ticket,
		containsString("<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">"));

	Assert.assertThat(ticket, containsString("</wsse:Security>"));
    }

    @Test
    public void testApplyWsSecurityEvenWhenSamlTicketDoesNoteExist() {
	String samlTicket = null;
	String samlTicketWithWsSecurity = new TicketMachine().applyWsSecurityToSamlTicket(samlTicket);

	Assert.assertThat(
		samlTicketWithWsSecurity,
		containsString("<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"></wsse:Security>"));
    }
}
