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
package se.skl.tp.crm.carelisting.getlistingtypes.producer;

import javax.jws.WebService;

import org.w3.wsaddressing10.AttributedURIType;

import se.skl.riv.crm.carelisting.v1.rivtabp20.getlistingtypes.GetListingTypesResponderInterface;
import se.skl.riv.crm.carelisting.v1.rivtabp20.getlistingtypes.PersonNotFoundException;
import se.skl.riv.crm.carelisting.v1.rivtabp20.getlistingtypes.TechnicalException;
import se.skl.riv.crm.carelistingresponder.v1.getlistingtypes.GetListingTypesRequestType;
import se.skl.riv.crm.carelistingresponder.v1.getlistingtypes.GetListingTypesResponseType;

@WebService(
		serviceName = "GetListingTypesResponderService", 
		endpointInterface="se.skl.riv.crm.carelisting.v1.rivtabp20.getlistingtypes.GetListingTypesResponderInterface", 
		portName = "GetListingTypesResponderPort", 
		targetNamespace = "urn:riv:crm:carelisting:GetListingTypes:1:rivtabp20",
		wsdlLocation = "schemas/GetListingTypesInteraction_1.0_rivtabp20.wsdl")
public class GetListingTypesImpl implements GetListingTypesResponderInterface {

	public GetListingTypesResponseType getListingTypes(
			AttributedURIType logicalAddress,
			GetListingTypesRequestType parameters) throws TechnicalException, PersonNotFoundException {
		try {
			GetListingTypesResponseType response = new GetListingTypesResponseType();

			if (parameters.getPersonId().equalsIgnoreCase("20011231-2384")) {
				response.getListingType().add("HLM");
				response.getListingType().add("BVC");			
			} else if(parameters.getPersonId().equalsIgnoreCase("19121212-1212")) {
				response.getListingType().add("HLM");
			} else if(parameters.getPersonId().equalsIgnoreCase("19301229-9263")) {
				response.getListingType().add("HLM");
				response.getListingType().add("DSK");				
			} else {
				response.getListingType().add("ListingType1");
				response.getListingType().add("ListingType2");				
			}
			
			return response;
		} catch (RuntimeException e) {
			System.out.println("Error occured: " + e);
			throw e;
		}
	}
}