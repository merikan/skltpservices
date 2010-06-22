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
package se.skl.riv.fk.vardgivare.sjukvard.taemotlakarintyg.producer;

import javax.jws.WebService;

import org.w3.wsaddressing10.AttributedURIType;

import se.fk.vardgivare.sjukvard.taemotlakarintyg.v1.rivtabp20.TaEmotLakarintygResponderInterface;
import se.fk.vardgivare.sjukvard.taemotlakarintygresponder.v1.TaEmotLakarintygResponseType;
import se.fk.vardgivare.sjukvard.taemotlakarintygresponder.v1.TaEmotLakarintygType;

@WebService(
		serviceName = "TaEmotLakarintygResponderService", 
		endpointInterface="se.fk.vardgivare.sjukvard.taemotlakarintyg.v1.rivtabp20.TaEmotLakarintygResponderInterface", 
		portName = "TaEmotLakarintygResponderPort", 
		targetNamespace = "urn:riv:fk:vardgivare:sjukvard:TaEmotLakarintyg:1:rivtabp20",
		wsdlLocation = "schemas/TaEmotLakarintygInteraction_1.0_rivtabp20.wsdl")
public class TaEmotLakarintygImpl implements TaEmotLakarintygResponderInterface {

	public TaEmotLakarintygResponseType taEmotLakarintyg(
			AttributedURIType logicalAddress, TaEmotLakarintygType parameters) {
		try {
			TaEmotLakarintygResponseType response = new TaEmotLakarintygResponseType();
			
			System.out.println("response sent!");
			return response;
		} catch (RuntimeException e) {
			System.out.println("Error occured: " + e);
			throw e;
		}
	}
}