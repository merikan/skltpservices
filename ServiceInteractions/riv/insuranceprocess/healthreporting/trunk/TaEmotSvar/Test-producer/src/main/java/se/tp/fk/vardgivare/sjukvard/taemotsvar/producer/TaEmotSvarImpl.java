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
package se.tp.fk.vardgivare.sjukvard.taemotsvar.producer;

import javax.jws.WebService;

import org.w3.wsaddressing10.AttributedURIType;

import se.fk.vardgivare.sjukvard.taemotsvar.v1.rivtabp20.TaEmotSvarResponderInterface;
import se.fk.vardgivare.sjukvard.taemotsvarresponder.v1.TaEmotSvarResponseType;
import se.fk.vardgivare.sjukvard.taemotsvarresponder.v1.TaEmotSvarType;

@WebService(
		serviceName = "TaEmotSvarResponderService", 
		endpointInterface="se.fk.vardgivare.sjukvard.taemotsvar.v1.rivtabp20.TaEmotSvarResponderInterface", 
		portName = "TaEmotSvarResponderPort", 
		targetNamespace = "urn:riv:fk:vardgivare:sjukvard:TaEmotSvar:1:rivtabp20",
		wsdlLocation = "schemas/TaEmotSvarInteraction_0.9_rivtabp20.wsdl")
public class TaEmotSvarImpl implements TaEmotSvarResponderInterface {

	public TaEmotSvarResponseType taEmotSvar(
			AttributedURIType logicalAddress, TaEmotSvarType parameters) {
		try {
			TaEmotSvarResponseType response = new TaEmotSvarResponseType();
			
			if (parameters.getFKSKLTaEmotSvarAnrop().getPatient().getFornamn().equalsIgnoreCase("Mats")) {
				throw new RuntimeException("Bad name!");
			}

			System.out.println("response sent!");
			return response;
		} catch (RuntimeException e) {
			System.out.println("Error occured: " + e);
			throw e;
		}
	}
}