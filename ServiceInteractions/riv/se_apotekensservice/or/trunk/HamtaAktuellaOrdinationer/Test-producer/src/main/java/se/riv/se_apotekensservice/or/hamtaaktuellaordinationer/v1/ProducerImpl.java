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
package se.riv.se_apotekensservice.or.hamtaaktuellaordinationer.v1;

import javax.jws.WebService;

import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.inera.se.apotekensservice.or.hamtaaktuellaordinationer.v1.rivtabp20.HamtaAktuellaOrdinationerResponderInterface;
import se.riv.se.apotekensservice.or.hamtaaktuellaordinationerresponder.v1.HamtaAktuellaOrdinationerRequestType;
import se.riv.se.apotekensservice.or.hamtaaktuellaordinationerresponder.v1.HamtaAktuellaOrdinationerResponseType;

@WebService(serviceName = "HamtaAktuellaOrdinationerResponderService", endpointInterface = "se.riv.inera.se.apotekensservice.or.hamtaaktuellaordinationer.v1.rivtabp20.HamtaAktuellaOrdinationerResponderInterface", portName = "HamtaAktuellaOrdinationerResponderPort", targetNamespace = "urn:riv:inera:se.apotekensservice:or:HamtaAktuellaOrdinationer:1:rivtabp20", wsdlLocation = "schemas/interactions/HamtaAktuellaOrdinationerInteraction/HamtaAktuellaOrdinationerInteraction_1.0_rivtabp20.wsdl")
public class ProducerImpl implements HamtaAktuellaOrdinationerResponderInterface {

    @Override
    public HamtaAktuellaOrdinationerResponseType hamtaAktuellaOrdinationer(
	    HamtaAktuellaOrdinationerRequestType parameters, org.w3c.addressing.v1.AttributedURIType logicalAddress,
	    ArgosHeaderType argosHeader)
	    throws se.riv.inera.se.apotekensservice.or.hamtaaktuellaordinationer.v1.rivtabp20.ApplicationException,
	    se.riv.inera.se.apotekensservice.or.hamtaaktuellaordinationer.v1.rivtabp20.SystemException {

	HamtaAktuellaOrdinationerResponseType response = new HamtaAktuellaOrdinationerResponseType();
	return response;
    }

}
