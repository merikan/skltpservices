package se.skl.skltpservices.adapter.apse.hamtaaktuellaordinationer;

import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.addressing.v1.AttributedURIType;

import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.inera.se.apotekensservice.or.hamtaaktuellaordinationer.v1.rivtabp20.ApplicationException;
import se.riv.inera.se.apotekensservice.or.hamtaaktuellaordinationer.v1.rivtabp20.HamtaAktuellaOrdinationerResponderInterface;
import se.riv.inera.se.apotekensservice.or.hamtaaktuellaordinationer.v1.rivtabp20.HamtaAktuellaOrdinationerResponderService;
import se.riv.inera.se.apotekensservice.or.hamtaaktuellaordinationer.v1.rivtabp20.SystemException;
import se.riv.se.apotekensservice.or.hamtaaktuellaordinationerresponder.v1.HamtaAktuellaOrdinationerRequestType;
import se.riv.se.apotekensservice.or.hamtaaktuellaordinationerresponder.v1.HamtaAktuellaOrdinationerResponseType;

public class HamtaAllaAktuellaOrdinationerTestConsumer {

	private HamtaAktuellaOrdinationerResponderInterface service;

	public HamtaAllaAktuellaOrdinationerTestConsumer(String endpointAdress) {

		URL url = createEndpointUrlFromServiceAddress(endpointAdress);
		service = new HamtaAktuellaOrdinationerResponderService(url).getHamtaAktuellaOrdinationerResponderPort();

	}

	public HamtaAktuellaOrdinationerResponseType requestIncludingCompleteArgosInformation(String socialSecurityNumber,
			String to) throws ApplicationException, SystemException {
		ArgosHeaderType argosHeader = createCompleteArgosHeader();
		AttributedURIType logicalAddress = createLogicalAddress(to);
		HamtaAktuellaOrdinationerRequestType requestSocialSecurityNumber = createSocialSecurityNumberRequest(socialSecurityNumber);
		return service.hamtaAktuellaOrdinationer(requestSocialSecurityNumber, logicalAddress, argosHeader);
	}

	private HamtaAktuellaOrdinationerRequestType createSocialSecurityNumberRequest(String socialSecurityNumber) {
		HamtaAktuellaOrdinationerRequestType aktuellaOrdinationerRequest = new HamtaAktuellaOrdinationerRequestType();
		aktuellaOrdinationerRequest.setPersonnummer(socialSecurityNumber);
		return aktuellaOrdinationerRequest;
	}

	private AttributedURIType createLogicalAddress(String logicalAddress) {
		AttributedURIType logicalAddressType = new AttributedURIType();
		logicalAddressType.setValue(logicalAddress);
		return logicalAddressType;
	}

	private ArgosHeaderType createCompleteArgosHeader() {
		ArgosHeaderType argosHeader = new ArgosHeaderType();
		argosHeader.setArbetsplatskod("1234567890");
		argosHeader.setArbetsplatsnamn("Sjukhuset");
		argosHeader.setBefattningskod("123456");
		argosHeader.setEfternamn("L�kare");
		argosHeader.setFornamn("Lars");
		argosHeader.setForskrivarkod("1111129");
		argosHeader.setHsaID("TSE6565656565-1003");
		argosHeader.setKatalog("HSA");
		argosHeader.setLegitimationskod("1");
		argosHeader.setOrganisationsnummer("1234567890");
		argosHeader.setPostadress("Gatan 1");
		argosHeader.setPostnummer("11111");
		argosHeader.setPostort("Staden");
		argosHeader.setRequestId("123456");
		argosHeader.setRollnamn("FORSKRIVARE");
		argosHeader.setSystemIp("192.0.0.1");
		argosHeader.setSystemnamn("Melior");
		argosHeader.setSystemversion("1.0");
		argosHeader.setTelefonnummer("08-1234567");
		argosHeader.setYrkesgrupp("LK");
		return argosHeader;
	}

	private static URL createEndpointUrlFromServiceAddress(String serviceAddress) {
		try {
			return new URL(serviceAddress + "?wsdl");
		} catch (MalformedURLException e) {
			throw new RuntimeException("Malformed URL Exception: " + e.getMessage());
		}
	}

}
