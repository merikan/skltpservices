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
package se.skltp.adapterservices.apse.apsemedicalservicesadapteric.hamtapatientinfo;

import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.addressing.v1.AttributedURIType;

import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.inera.se.apotekensservice.axs.hamtapatientinfo.v1.rivtabp20.HamtaPatientInfoResponderInterface;
import se.riv.inera.se.apotekensservice.axs.hamtapatientinfo.v1.rivtabp20.HamtaPatientInfoResponderService;
import se.riv.se.apotekensservice.axs.hamtapatientinforesponder.v1.HamtaPatientInfoRequestType;
import se.riv.se.apotekensservice.axs.hamtapatientinforesponder.v1.HamtaPatientInfoResponseType;

public class HamtaPatientInfoTestConsumer {

	private HamtaPatientInfoResponderInterface service;

	public HamtaPatientInfoTestConsumer(String endpointAdress) {

		URL url = createEndpointUrlFromServiceAddress(endpointAdress);
		service = new HamtaPatientInfoResponderService(url).getHamtaPatientInfoResponderPort();
	}

	public HamtaPatientInfoResponseType requestIncludingCompleteArgosInformation(String socialSecurityNumber, String to)
			throws se.riv.inera.se.apotekensservice.axs.hamtapatientinfo.v1.rivtabp20.ApplicationException,
			se.riv.inera.se.apotekensservice.axs.hamtapatientinfo.v1.rivtabp20.SystemException {
		ArgosHeaderType argosHeader = createCompleteArgosHeader();
		AttributedURIType logicalAddress = createLogicalAddress(to);
		HamtaPatientInfoRequestType requestSocialSecurityNumber = createSocialSecurityNumberRequest(socialSecurityNumber);
		return service.hamtaPatientInfo(requestSocialSecurityNumber, logicalAddress, argosHeader);
	}

	private HamtaPatientInfoRequestType createSocialSecurityNumberRequest(String socialSecurityNumber) {
		HamtaPatientInfoRequestType aktuellaOrdinationerRequest = new HamtaPatientInfoRequestType();
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
		argosHeader.setEfternamn("Läkare");
		argosHeader.setFornamn("Lars");
		argosHeader.setForskrivarkod("1111129");
		argosHeader.setHsaID("TSE6565656565-1003");
		argosHeader.setKatalog("HSA");
		argosHeader.setLegitimationskod("1");
		argosHeader.setOrganisationsnummer("1234567890");
		argosHeader.setPostadress("Vägen 1");
		argosHeader.setPostnummer("11111");
		argosHeader.setPostort("Staden");
		argosHeader.setRequestId("123456");
		argosHeader.setRollnamn("FORSKRIVARE");
		argosHeader.setSystemIp("192.0.0.1");
		argosHeader.setSystemnamn("Melior");
		argosHeader.setSystemversion("1.0");
		argosHeader.setTelefonnummer("08-1234567");
		argosHeader.setYrkesgrupp("Läkare");
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
