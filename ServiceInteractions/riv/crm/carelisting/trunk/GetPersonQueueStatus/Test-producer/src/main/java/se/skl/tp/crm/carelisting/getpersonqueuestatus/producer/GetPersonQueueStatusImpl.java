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

package se.skl.tp.crm.carelisting.getpersonqueuestatus.producer;

import javax.jws.WebService;

import org.w3.wsaddressing10.AttributedURIType;

import se.skl.riv.crm.carelisting.v1.Facility;
import se.skl.riv.crm.carelisting.v1.PersonQueueStatus;
import se.skl.riv.crm.carelisting.v1.rivtabp20.getpersonqueuestatus.GetPersonQueueStatusResponderInterface;
import se.skl.riv.crm.carelisting.v1.rivtabp20.getpersonqueuestatus.PersonNotFoundException;
import se.skl.riv.crm.carelisting.v1.rivtabp20.getpersonqueuestatus.TechnicalException;
import se.skl.riv.crm.carelistingresponder.v1.getpersonqueuestatus.GetPersonQueueStatusRequestType;
import se.skl.riv.crm.carelistingresponder.v1.getpersonqueuestatus.GetPersonQueueStatusResponseType;

@WebService(serviceName = "GetPersonQueueStatusResponderService", 
			endpointInterface = "se.skl.riv.crm.carelisting.v1.rivtabp20.getpersonqueuestatus.GetPersonQueueStatusResponderInterface", 
			portName = "GetPersonQueueStatusResponderPort", 
			targetNamespace = "urn:riv:crm:carelisting:GetPersonQueueStatus:1:rivtabp20", 
			wsdlLocation = "schemas/GetPersonQueueStatusInteraction_1.0_rivtabp20.wsdl")
public class GetPersonQueueStatusImpl implements
		GetPersonQueueStatusResponderInterface {

	public GetPersonQueueStatusResponseType getPersonQueueStatus(
			AttributedURIType logicalAddress,
			GetPersonQueueStatusRequestType parameters)
			throws TechnicalException, PersonNotFoundException {

		GetPersonQueueStatusResponseType response = new GetPersonQueueStatusResponseType();

		if (parameters.getPersonId().equalsIgnoreCase("19121212-1212")) {
			response.setQueueStatus(PersonQueueStatus.IN_QUEUE);
			Facility facility = new Facility();
			facility.setFacilityId("SE2321000016-1hz6");
			facility.setFacilityName("Testvårdcentral A");
			facility.setHasQueue(true);
			facility.getSupportedListingTypes().add("HLM");
			
			response.setHealthcareFacility(facility);
			
		} else if (parameters.getPersonId().equalsIgnoreCase("19301229-9263")) {
			response.setQueueStatus(PersonQueueStatus.NOT_IN_QUEUE);
			
		} else {
			response.setQueueStatus(PersonQueueStatus.NOT_IN_QUEUE);
			Facility facility = new Facility();
			facility.setFacilityId("");
			facility.setFacilityName("");
			facility.setHasQueue(true);
			facility.getSupportedListingTypes().add("HLM");
			
			response.setHealthcareFacility(facility);			
		}
		
		return response;
	}

}
