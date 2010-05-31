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
package se.skl.tp.crm.carelisting.createlisting.producer;

import javax.jws.WebService;

import org.w3.wsaddressing10.AttributedURIType;

import se.skl.riv.crm.carelisting.v1.rivtabp20.createlisting.CreateListingResponderInterface;
import se.skl.riv.crm.carelisting.v1.rivtabp20.createlisting.InvalidFacilityException;
import se.skl.riv.crm.carelisting.v1.rivtabp20.createlisting.PersonNotFoundException;
import se.skl.riv.crm.carelisting.v1.rivtabp20.createlisting.TechnicalException;
import se.skl.riv.crm.carelistingresponder.v1.createlisting.CreateListingRequestType;
import se.skl.riv.crm.carelistingresponder.v1.createlisting.CreateListingResponseType;

@WebService(serviceName = "CreateListingResponderService",
		endpointInterface = "se.skl.riv.crm.carelisting.v1.rivtabp20.createlisting.CreateListingResponderInterface", 
		portName = "CreateListingResponderPort", 
		targetNamespace = "urn:riv:crm:carelisting:CreateListing:1:rivtabp20", 
		wsdlLocation = "schemas/CreateListingInteraction_1.0_rivtabp20.wsdl")
public class CreateListingImpl implements CreateListingResponderInterface {

	public CreateListingResponseType createListing(
			AttributedURIType logicalAddress,
			CreateListingRequestType parameters)
			throws InvalidFacilityException, TechnicalException,
			PersonNotFoundException {
		
		CreateListingResponseType response = new CreateListingResponseType();
		
		if (parameters.getPersonId().equalsIgnoreCase("111")) {
			throw new PersonNotFoundException("Not found i producer!");
		} else if (parameters.getPersonId().equalsIgnoreCase("222")) {
			throw new RuntimeException("Runtime producer exception!");
		}
		
		if (parameters.getHealthcareFacility().equalsIgnoreCase("SE2321000016-1hz7") &&
			parameters.isAddToQueue() == false	) {
			response.setSuccess(false);
			response.setSystemCode("XX");
			response.setComment("Det är kö på denna enhet och man vill ej läggas till om det var kö.");
			
		} else {
			response.setSuccess(true);
			response.setSystemCode("YY");
			response.setComment("Comment");		
		}

		return response;
	}

}