package se.riv.ehr.patientconsent.accesscontrol.checkconsent.v1;

/**
 * Copyright 2012 Sjukvardsradgivningen
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

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import se.riv.ehr.patientconsent.accesscontrol.checkconsent.v1.rivtabp21.CheckConsentResponderInterface;
import se.riv.ehr.patientconsent.accesscontrol.checkconsentresponder.v1.CheckConsentRequestType;
import se.riv.ehr.patientconsent.accesscontrol.checkconsentresponder.v1.CheckConsentResponseType;
import se.riv.ehr.patientconsent.v1.AssertionTypeType;
import se.riv.ehr.patientconsent.v1.CheckResultType;

@WebService(serviceName = "CheckConsentResponderService", portName = "CheckConsentResponderPort", name = "CheckConsentInteraction")
public class CheckConsentImpl implements CheckConsentResponderInterface {

	@Override
	@WebResult(name = "CheckConsentResponse", targetNamespace = "urn:riv:ehr:patientconsent:accesscontrol:CheckConsentResponder:1", partName = "parameters")
	@WebMethod(operationName = "CheckConsent", action = "urn:riv:ehr:patientconsent:accesscontrol:CheckConsentResponder:1:CheckConsent")
	public CheckConsentResponseType checkConsent(
			@WebParam(partName = "LogicalAddress", name = "LogicalAddress", targetNamespace = "urn:riv:itintegration:registry:1", header = true) String arg0,
			@WebParam(partName = "parameters", name = "CheckConsentRequest", targetNamespace = "urn:riv:ehr:patientconsent:accesscontrol:CheckConsentResponder:1") CheckConsentRequestType arg1) {
		
		CheckConsentResponseType response = new CheckConsentResponseType();
		CheckResultType resultType = new CheckResultType();
		resultType.setHasConsent(true);
		resultType.setAssertionType(AssertionTypeType.CONSENT);
		response.setCheckResultType(resultType);
		return response;
	}

}
