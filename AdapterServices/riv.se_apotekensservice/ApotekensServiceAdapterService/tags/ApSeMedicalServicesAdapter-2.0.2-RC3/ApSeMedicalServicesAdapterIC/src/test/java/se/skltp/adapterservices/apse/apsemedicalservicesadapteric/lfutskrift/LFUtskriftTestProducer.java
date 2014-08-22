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
package se.skltp.adapterservices.apse.apsemedicalservicesadapteric.lfutskrift;

import javax.jws.WebService;

import org.w3c.addressing.v1.AttributedURIType;

import riv.se_apotekensservice.lf._1.PatientResponse;
import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.inera.se.apotekensservice.lf.lfutskrift.v1.rivtabp20.ApplicationException;
import se.riv.inera.se.apotekensservice.lf.lfutskrift.v1.rivtabp20.LFUtskriftResponderInterface;
import se.riv.inera.se.apotekensservice.lf.lfutskrift.v1.rivtabp20.SystemException;
import se.riv.se.apotekensservice.lf.lfutskriftresponder.v1.LFUtskriftRequestType;
import se.riv.se.apotekensservice.lf.lfutskriftresponder.v1.LFUtskriftResponseType;

@WebService(serviceName = "LFUtskriftResponderService", 
			endpointInterface = "se.riv.inera.se.apotekensservice.lf.lfutskrift.v1.rivtabp20.LFUtskriftResponderInterface", 
			portName = "LFUtskriftResponderPort", targetNamespace = "urn:riv:inera:se.apotekensservice:lf:LFUtskrift:1:rivtabp20", 
			wsdlLocation = "TD_APSE_LF_1_0_0_R/interactions/LFUtskriftInteraction/LFUtskriftInteraction_1.0_rivtabp20.wsdl")
public class LFUtskriftTestProducer implements LFUtskriftResponderInterface {
	
	public static final String CITIZENREQUEST = "188803099368";
	public static final String ORGANIZATIONREQUEST = "191704109279";

	@Override
	public LFUtskriftResponseType lfUtskrift(LFUtskriftRequestType parameters,
			AttributedURIType logicalAddress, ArgosHeaderType argosHeader)
			throws SystemException, ApplicationException {
		
		/*
		 * ArgosHeader is removed by TicketMachine and can not be accessed by
		 * the producer. TODO: Is there any way we can verify that this is a
		 * citizen request, we need the saml ticket in the soap header?
		 */

		return createResponse(parameters.getPersonnummer());
	}

	private LFUtskriftResponseType createResponse(String ssn) {
		LFUtskriftResponseType response = new LFUtskriftResponseType();
		response.setPatient(createPatientResponse(ssn));
		return response;
	}

	private PatientResponse createPatientResponse(String ssn) {
		PatientResponse response = new PatientResponse();
		response.setAvliden(true);
		response.setEfternamn("Andersson");
		response.setFornamn("Agda");
		response.setPersonnummer(ssn);
		return response;
	}

}
