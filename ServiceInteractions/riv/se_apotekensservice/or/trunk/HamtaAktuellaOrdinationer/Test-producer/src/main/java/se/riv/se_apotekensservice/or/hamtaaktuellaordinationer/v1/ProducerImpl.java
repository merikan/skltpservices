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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.wsaddressing10.AttributedURIType;

import riv.inera.se_apotekensservice.or.hamtaaktuellaordinationer._1.rivtabp20.ApplicationException;
import riv.inera.se_apotekensservice.or.hamtaaktuellaordinationer._1.rivtabp20.HamtaAktuellaOrdinationerResponderInterface;
import riv.inera.se_apotekensservice.or.hamtaaktuellaordinationer._1.rivtabp20.SystemException;
import riv.inera_se_apotekensservice.argos._1.ArgosHeaderType;
import riv.se_apotekensservice.or.hamtaaktuellaordinationerresponder._1.HamtaAktuellaOrdinationerRequestType;
import riv.se_apotekensservice.or.hamtaaktuellaordinationerresponder._1.HamtaAktuellaOrdinationerResponseType;

@WebService(serviceName = "HamtaAktuellaOrdinationerResponderService", endpointInterface = "riv.inera.se_apotekensservice.or.hamtaaktuellaordinationer._1.rivtabp20.HamtaAktuellaOrdinationerResponderInterface", portName = "HamtaAktuellaOrdinationerResponderPort", targetNamespace = "urn:riv:inera:se.apotekensservice:or:HamtaAktuellaOrdinationer:1:rivtabp20", wsdlLocation = "schemas/interactions/HamtaAktuellaOrdinationerInteraction/HamtaAktuellaOrdinationerInteraction_1.0_rivtabp20.wsdl")
public class ProducerImpl implements HamtaAktuellaOrdinationerResponderInterface {

    @Override
    public HamtaAktuellaOrdinationerResponseType hamtaAktuellaOrdinationer(
	    HamtaAktuellaOrdinationerRequestType parameters, AttributedURIType logicalAddress,
	    ArgosHeaderType argosHeader) throws ApplicationException, SystemException {

	System.out.println("Personnr: " + parameters.getPersonnummer());
	System.out.println("Arbetplatskod: " + argosHeader.getArbetsplatskod());
	System.out.println("Arbetsplatsnamn: " + argosHeader.getArbetsplatsnamn());
	System.out.println("Befattningskod: " + argosHeader.getBefattningskod());
	System.out.println("Efternamn: " + argosHeader.getEfternamn());
	System.out.println("Fšrnamn: " + argosHeader.getFornamn());
	System.out.println("Forskrivarkod: " + argosHeader.getForskrivarkod());
	System.out.println("HsaId: " + argosHeader.getHsaID());
	System.out.println("Katalog: " + argosHeader.getKatalog());
	System.out.println("Legitimationskod: " + argosHeader.getLegitimationskod());
	System.out.println("Organisationsnummer: " + argosHeader.getOrganisationsnummer());
	System.out.println("Postadress: " + argosHeader.getPostadress());
	System.out.println("Postnummer: " + argosHeader.getPostnummer());
	System.out.println("Postort: " + argosHeader.getPostort());
	System.out.println("Requestid: " + argosHeader.getRequestId());
	System.out.println("Rollnamn: " + argosHeader.getRollnamn());
	System.out.println("SystemIp: " + argosHeader.getSystemIp());
	System.out.println("Systemnamn: " + argosHeader.getSystemnamn());
	System.out.println("Systemversion: " + argosHeader.getSystemversion());
	System.out.println("Telefonnummer: " + argosHeader.getTelefonnummer());
	System.out.println("Yrkesgrupp: " + argosHeader.getYrkesgrupp());

	HamtaAktuellaOrdinationerResponseType response = new HamtaAktuellaOrdinationerResponseType();
	return response;
    }
}
