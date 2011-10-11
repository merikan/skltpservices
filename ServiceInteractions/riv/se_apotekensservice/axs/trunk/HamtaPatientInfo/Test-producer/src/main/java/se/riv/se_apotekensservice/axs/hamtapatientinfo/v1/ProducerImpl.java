package se.riv.se_apotekensservice.axs.hamtapatientinfo.v1;

/**
 * Copyright 2009 Sjukvardsradgivningen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public

 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the

 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,

 *   Boston, MA 02111-1307  USA
 */

import javax.jws.WebService;

import org.w3c.addressing.v1.AttributedURIType;

import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.inera.se.apotekensservice.axs.hamtapatientinfo.v1.rivtabp20.ApplicationException;
import se.riv.inera.se.apotekensservice.axs.hamtapatientinfo.v1.rivtabp20.HamtaPatientInfoResponderInterface;
import se.riv.inera.se.apotekensservice.axs.hamtapatientinfo.v1.rivtabp20.SystemException;
import se.riv.se.apotekensservice.axs.hamtapatientinforesponder.v1.HamtaPatientInfoRequestType;
import se.riv.se.apotekensservice.axs.hamtapatientinforesponder.v1.HamtaPatientInfoResponseType;

@WebService(serviceName = "HamtaPatientInfoResponderService", endpointInterface = "se.riv.inera.se.apotekensservice.axs.hamtapatientinfo.v1.rivtabp20.HamtaPatientInfoResponderInterface", portName = "HamtaPatientInfoResponderPort", targetNamespace = "urn:riv:inera:se.apotekensservice:axs:HamtaPatientInfo:1:rivtabp20", wsdlLocation = "schemas/interactions/HamtaPatientInfoInteraction/HamtaPatientInfoInteraction_1.0_rivtabp20.wsdl")
public class ProducerImpl implements HamtaPatientInfoResponderInterface {

    @Override
    public HamtaPatientInfoResponseType hamtaPatientInfo(HamtaPatientInfoRequestType arg0, AttributedURIType arg1,
	    ArgosHeaderType arg2) throws ApplicationException, SystemException {

	HamtaPatientInfoResponseType responseType = new HamtaPatientInfoResponseType();
	responseType.setFinnsOrdination(true);

	return responseType;
    }

}
