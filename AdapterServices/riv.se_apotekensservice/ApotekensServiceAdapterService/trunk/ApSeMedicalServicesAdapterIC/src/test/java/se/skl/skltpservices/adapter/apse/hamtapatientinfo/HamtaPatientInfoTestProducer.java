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
package se.skl.skltpservices.adapter.apse.hamtapatientinfo;

import javax.jws.WebService;

import org.w3c.addressing.v1.AttributedURIType;

import riv.se_apotekensservice.axs._1.ApoteksinformationResponse;
import riv.se_apotekensservice.axs._1.PatientInformationResponse;
import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.inera.se.apotekensservice.axs.hamtapatientinfo.v1.rivtabp20.ApplicationException;
import se.riv.inera.se.apotekensservice.axs.hamtapatientinfo.v1.rivtabp20.HamtaPatientInfoResponderInterface;
import se.riv.inera.se.apotekensservice.axs.hamtapatientinfo.v1.rivtabp20.SystemException;
import se.riv.se.apotekensservice.axs.hamtapatientinforesponder.v1.HamtaPatientInfoRequestType;
import se.riv.se.apotekensservice.axs.hamtapatientinforesponder.v1.HamtaPatientInfoResponseType;

@WebService(serviceName = "HamtaPatientInfoResponderService", endpointInterface = "se.riv.inera.se.apotekensservice.axs.hamtapatientinfo.v1.rivtabp20.HamtaPatientInfoResponderInterface", portName = "HamtaPatientInfoResponderPort", targetNamespace = "urn:riv:inera:se.apotekensservice:axs:HamtaPatientInfo:1:rivtabp20", wsdlLocation = "schemas/interactions/HamtaPatientInfoInteraction/HamtaPatientInfoInteraction_1.0_rivtabp20.wsdl")
public class HamtaPatientInfoTestProducer implements HamtaPatientInfoResponderInterface {

    public HamtaPatientInfoResponseType hamtaPatientInfo(HamtaPatientInfoRequestType arg0, AttributedURIType arg1,
	    ArgosHeaderType arg2) throws ApplicationException, SystemException {
	HamtaPatientInfoResponseType response = new HamtaPatientInfoResponseType();
	String ssn = arg0.getPersonnummer();
	
	response.setApotek(createApoteksInformationResponse(ssn));
	response.setPatientInformation(createPatientInformationResponse());
	response.setFinnsOrdination(true);
	response.setDosproducent(ssn);
	return response;
    }

    private ApoteksinformationResponse createApoteksInformationResponse(String ssn) {
	ApoteksinformationResponse apoteksinformationResponse = new ApoteksinformationResponse();
	apoteksinformationResponse.setAktorsnamn("Aktörsnamn");
	return null;
    }

    private PatientInformationResponse createPatientInformationResponse() {
	PatientInformationResponse informationResponse = new PatientInformationResponse();
	informationResponse.setOmradeskod("Områdeskod");
	return null;
    }

}
