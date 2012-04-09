package se.inera.pascal.ticket;

import static org.junit.matchers.JUnitMatchers.containsString;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ArgosTicketTest {

	private static ArgosTicket argosTicketUnderTest;

	@BeforeClass
	public static void setUp() throws Exception {
		argosTicketUnderTest = ArgosTicket.getInstance();
	}

	@Test
	public void happyCaseToSeeHowTicketGeneratorWorks() {

		String forskrivarkod = "1111152";
		String legitimationskod = "1111111";
		String fornamn = "Lars";
		String efternamn = "Läkare";
		String yrkesgrupp = "FORSKRIVARE";
		String befattningskod = "1111111";
		String arbetsplatskod = "4000000000001";
		String arbetsplatsnamn = "VC Test";
		String postort = "Staden";
		String postadress = "Vägen 1";
		String postnummer = "11111";
		String telefonnummer = "0987654321";
		String requestId = "12345676";
		String rollnamn = "LK";
		// String directoryID = "SE1111111111-1003";
		String hsaID = "SE1111111111-1003";
		String katalog = "HSA";
		String organisationsnummer = "111111111";
		String systemnamn = "Pascal";
		String systemversion = "1.0";
		String systemIp = "192.168.1.1";

		String ticket = argosTicketUnderTest.getTicket(forskrivarkod, legitimationskod, fornamn, efternamn, yrkesgrupp,
				befattningskod, arbetsplatskod, arbetsplatsnamn, postort, postadress, postnummer, telefonnummer,
				requestId, rollnamn, hsaID, katalog, organisationsnummer, systemnamn, systemversion, systemIp);

		Assert.assertThat(ticket, containsString("<saml2:Issuer>pascalonline</saml2:Issuer>"));

	}

	@Test
	public void forskrivarkodIsSetProperlyWhenItExistInRequest() {

		String forskrivarkod = "1111152";
		String legitimationskod = "1111111";
		String fornamn = "Lars";
		String efternamn = "Läkare";
		String yrkesgrupp = "FORSKRIVARE";
		String befattningskod = "1111111";
		String arbetsplatskod = "4000000000001";
		String arbetsplatsnamn = "VC Test";
		String postort = "Staden";
		String postadress = "Vägen 1";
		String postnummer = "11111";
		String telefonnummer = "0987654321";
		String requestId = "12345676";
		String rollnamn = "FORSKRIVARE";
		// String directoryID = "SE1111111111-1003";
		String hsaID = "SE1111111111-1003";
		String katalog = "HSA";
		String organisationsnummer = "111111111";
		String systemnamn = "Pascal";
		String systemversion = "1.0";
		String systemIp = "192.168.1.1";

		String ticket = argosTicketUnderTest.getTicket(forskrivarkod, legitimationskod, fornamn, efternamn, yrkesgrupp,
				befattningskod, arbetsplatskod, arbetsplatsnamn, postort, postadress, postnummer, telefonnummer,
				requestId, rollnamn, hsaID, katalog, organisationsnummer, systemnamn, systemversion, systemIp);

		Assert.assertThat(
				ticket,
				containsString("<saml2:Attribute Name=\"urn:apotekensservice:names:federation:attributeName:rollnamn\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"><saml2:AttributeValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">FORSKRIVARE</saml2:AttributeValue></saml2:Attribute>"));

		Assert.assertThat(
				ticket,
				containsString("<saml2:Attribute Name=\"urn:apotekensservice:names:federation:attributeName:forskrivarkod\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"><saml2:AttributeValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">1111152</saml2:AttributeValue></saml2:Attribute>"));

	}
	
	@Test
	public void arbetsplaskodAndArbetsplatsIsSetProperly() {

		String forskrivarkod = "1111152";
		String legitimationskod = "1111111";
		String fornamn = "Lars";
		String efternamn = "Läkare";
		String yrkesgrupp = "FORSKRIVARE";
		String befattningskod = "1111111";
		String arbetsplatskod = "4000000000001";
		String arbetsplatsnamn = "VC Test";
		String postort = "Staden";
		String postadress = "Vägen 1";
		String postnummer = "11111";
		String telefonnummer = "0987654321";
		String requestId = "12345676";
		String rollnamn = "FORSKRIVARE";
		// String directoryID = "SE1111111111-1003";
		String hsaID = "SE1111111111-1003";
		String katalog = "HSA";
		String organisationsnummer = "111111111";
		String systemnamn = "Pascal";
		String systemversion = "1.0";
		String systemIp = "192.168.1.1";

		String ticket = argosTicketUnderTest.getTicket(forskrivarkod, legitimationskod, fornamn, efternamn, yrkesgrupp,
				befattningskod, arbetsplatskod, arbetsplatsnamn, postort, postadress, postnummer, telefonnummer,
				requestId, rollnamn, hsaID, katalog, organisationsnummer, systemnamn, systemversion, systemIp);

		Assert.assertThat(
				ticket,
				containsString("<saml2:Attribute Name=\"urn:apotekensservice:names:federation:attributeName:arbetsplatskod\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"><saml2:AttributeValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">4000000000001</saml2:AttributeValue>"));
								
		Assert.assertThat(
				ticket,
				containsString("<saml2:Attribute Name=\"urn:apotekensservice:names:federation:attributeName:arbetsplats\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"><saml2:AttributeValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">VC Test</saml2:AttributeValue></saml2:Attribute>"));

	}

	@Test
	public void noErrorWhenOneFieldIsNull() {

		String forskrivarkod = "1111129";
		String legitimationskod = null;
		String fornamn = "Lars";
		String efternamn = "Läkare";
		String yrkesgrupp = "Läkare";
		String befattningskod = "123456";
		String arbetsplatskod = "1234567890";
		String arbetsplatsnamn = "Sjukhuset";
		String postort = "Staden";
		String postadress = "Vägen 1";
		String postnummer = "11111";
		String telefonnummer = "08-1234567";
		String requestId = "123456";
		String rollnamn = "FORSKRIVARE";
		// String directoryID = "TSE6565656565-1003";
		String hsaID = "TSE6565656565-1003";
		String katalog = "HSA";
		String organisationsnummer = "1234567890";
		String systemnamn = "Melior";
		String systemversion = "1.0";
		String systemIp = "192.0.0.1";

		String ticket = argosTicketUnderTest.getTicket(forskrivarkod, legitimationskod, fornamn, efternamn, yrkesgrupp,
				befattningskod, arbetsplatskod, arbetsplatsnamn, postort, postadress, postnummer, telefonnummer,
				requestId, rollnamn, hsaID, katalog, organisationsnummer, systemnamn, systemversion, systemIp);

		Assert.assertNotNull(ticket);

	}

	@Test
	public void minusSignInLegitimationskodIsTreatedAsNull() {

		String forskrivarkod = "1111129";
		String legitimationskod = "-";
		String fornamn = "Lars";
		String efternamn = "Läkare";
		String yrkesgrupp = "Läkare";
		String befattningskod = "123456";
		String arbetsplatskod = "1234567890";
		String arbetsplatsnamn = "Sjukhuset";
		String postort = "Staden";
		String postadress = "Vägen 1";
		String postnummer = "11111";
		String telefonnummer = "08-1234567";
		String requestId = "123456";
		String rollnamn = "FORSKRIVARE";
		// String directoryID = "TSE6565656565-1003";
		String hsaID = "TSE6565656565-1003";
		String katalog = "HSA";
		String organisationsnummer = "1234567890";
		String systemnamn = "Melior";
		String systemversion = "1.0";
		String systemIp = "192.0.0.1";

		String ticket = argosTicketUnderTest.getTicket(forskrivarkod, legitimationskod, fornamn, efternamn, yrkesgrupp,
				befattningskod, arbetsplatskod, arbetsplatsnamn, postort, postadress, postnummer, telefonnummer,
				requestId, rollnamn, hsaID, katalog, organisationsnummer, systemnamn, systemversion, systemIp);

		Assert.assertNotNull(ticket);
		Assert.assertThat(
				ticket,
				containsString("<saml2:Attribute Name=\"urn:apotekensservice:names:federation:attributeName:legitimationskod\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"><saml2:AttributeValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\"/></saml2:Attribute>"));

	}

	@AfterClass
	public static void tearDown() throws Exception {
		argosTicketUnderTest = null;
	}
}
