package se.skl.tp.ticket;

import static org.junit.matchers.JUnitMatchers.containsString;

import org.junit.Assert;
import org.junit.Test;

import se.skl.tp.ticket.argos.ArgosHeader;
import se.skl.tp.ticket.exception.TicketMachineException;

public class TicketMachineTest {

	@Test
	public void testProduceCompleteSamlTicketFromArgosHeader() throws TicketMachineException {

//		String rollnamn = "LEG_VARDPERSONAL";
//		String yrkesgrupp = "SJ";
//		String efternamn = "Sjuksyrra";
//		String legitimationskod = "971000";
//		String befattningskod = "";
//		String fornamn = "Lars";
//		String arbetsplatsnamn = "VC Test";
//		String postort = "Staden";
//		String postadress = "Vagen 1";
//		String postnummer = "11111";
//		String telefonnummer = "0987654321";
//		String requestId = "524525252523";
//		String hsaID = "SE11111111-1003";
//		String directoryID = "SE11111111-1003";
//		String katalog = "HSA";
//		String organisationsnummer = "232100-0206";
//		String systemnamn = "Pascal";
//		String systemversion = "1.0";
//		String systemIp = "127.0.0.1";
//		String arbetsplatskod = "01100103010";
//		String forskrivarkod = "";
		
		String rollnamn = "FORSKRIVARE";
		String yrkesgrupp = "LK";
		String efternamn = "Måns";
		String legitimationskod = "";
		String befattningskod = "";
		String fornamn = "Easy";
		String arbetsplatsnamn = "Medicin";
		String postort = "Stockholm";
		String postadress = "vägen";
		String postnummer = "12345";
		String telefonnummer = "0810000000";
		String requestId = "334434343";
		String hsaID = "SE2321000131-P000000099915";
		String directoryID = "SE11111111-1003";
		String katalog = "HSA";
		String organisationsnummer = "556559-4230";
		String systemnamn = "Pascal";
		String systemversion = "1.1.5";
		String systemIp = "213.203.15.112";
		String arbetsplatskod = "01100103010";
		String forskrivarkod = "9001132";
		
		ArgosHeader argosHeader = new ArgosHeader(forskrivarkod, legitimationskod, fornamn, efternamn, yrkesgrupp,
				befattningskod, arbetsplatskod, arbetsplatsnamn, postort, postadress, postnummer, telefonnummer,
				requestId, rollnamn, directoryID, hsaID, katalog, organisationsnummer, systemnamn, systemversion,
				systemIp);

		String ticket = new TicketMachine().produceSamlTicket(argosHeader);
		System.out.println(ticket);

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
