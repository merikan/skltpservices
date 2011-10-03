package se.skl.tp.ticket.transformer;


public class TicketMachineTest extends GroovyTestCase {

    public void testProduceCompleteSamlTicketFromArgosHeader() {

	def forskrivarkod = '1111129'
	def legitimationskod = ''
	def fornamn = 'Lars'
	def efternamn = 'LŠkare'
	def yrkesgrupp = 'LŠkare'
	def befattningskod = '123456'
	def arbetsplatskod = '1234567890'
	def arbetsplatsnamn = 'Sjukhuset'
	def postort = 'Staden'
	def postadress = 'VŠgen 1'
	def postnummer = '11111'
	def telefonnummer = '08-1234567'
	def requestId = '123456'
	def rollnamn = 'FORSKRIVARE'
	def directoryID = 'TSE6565656565-1003'
	def hsaID = 'TSE6565656565-1003'
	def katalog = 'HSA'
	def organisationsnummer = '1234567890'
	def systemnamn = 'Melior'
	def systemversion = '1.0'
	def systemIp = '192.0.0.1'

	def argosHeader = new ArgosHeader(forskrivarkod,legitimationskod,fornamn, efternamn,yrkesgrupp,befattningskod, arbetsplatskod, arbetsplatsnamn, postort, postadress,
		postnummer, telefonnummer, requestId, rollnamn,directoryID,hsaID,katalog,organisationsnummer,systemnamn, systemversion,systemIp)

	def ticket = new TicketMachine().produceSamlTicket(argosHeader)

	assert ticket.contains('<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">')
	assert ticket.contains('<saml2:Issuer>pascalonline</saml2:Issuer>')
	assert ticket.contains('</wsse:Security>')
    }

    public void testApplyWsSecurityInformationToSamlTicket() {

	String samlTicket = """<saml2:Assertion xmlns:saml2="urn:oasis:names:tc:SAML:2.0:assertion"
	ID="_c4cda1f1c805c68a1a0c499ec6e34381" IssueInstant="2011-09-28T06:32:56.395Z"
	Version="2.0" xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<saml2:Issuer>pascalonline</saml2:Issuer></saml2:Assertion>
	<saml2:Assertion xmlns:saml2="urn:oasis:names:tc:SAML:2.0:assertion"
	ID="_c4cda1f1c805c68a1a0c499ec6e34381" IssueInstant="2011-09-28T06:32:56.395Z"
	Version="2.0" xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<saml2:Issuer>pascalonline</saml2:Issuer></saml2:Assertion>"""

	def samlTicketWithWsSecurity = new TicketMachine().applyWsSecurityToSamlTicket(samlTicket)

	assert samlTicketWithWsSecurity.contains('<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">')
	assert samlTicketWithWsSecurity.contains('</wsse:Security>')
    }

    public void testApplyWsSecurityEvenWhenSamlTicketDoesNoteExist() {
	String samlTicket = null
	def samlTicketWithWsSecurity = new TicketMachine().applyWsSecurityToSamlTicket(samlTicket)
	assert samlTicketWithWsSecurity == '<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"></wsse:Security>'
    }
}
