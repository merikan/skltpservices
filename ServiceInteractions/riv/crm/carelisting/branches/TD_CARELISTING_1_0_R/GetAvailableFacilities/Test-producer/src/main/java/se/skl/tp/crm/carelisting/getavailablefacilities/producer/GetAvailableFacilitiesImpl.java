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
package se.skl.tp.crm.carelisting.getavailablefacilities.producer;

import javax.jws.WebService;

import org.w3.wsaddressing10.AttributedURIType;

import se.skl.riv.crm.carelisting.v1.Facility;
import se.skl.riv.crm.carelisting.v1.rivtabp20.getavailablefacilities.GetAvailableFacilitiesResponderInterface;
import se.skl.riv.crm.carelisting.v1.rivtabp20.getavailablefacilities.TechnicalException;
import se.skl.riv.crm.carelistingresponder.v1.getavailablefacilities.GetAvailableFacilitiesRequestType;
import se.skl.riv.crm.carelistingresponder.v1.getavailablefacilities.GetAvailableFacilitiesResponseType;



@WebService(
		serviceName = "GetAvailableFacilitiesResponderService", 
		endpointInterface="se.skl.riv.crm.carelisting.v1.rivtabp20.getavailablefacilities.GetAvailableFacilitiesResponderInterface", 
		portName = "GetAvailableFacilitiesResponderPort", 
		targetNamespace = "urn:riv:crm:carelisting:GetAvailableFacilities:1:rivtabp20",
		wsdlLocation = "schemas/GetAvailableFacilitiesInteraction_1.0_rivtabp20.wsdl")
public class GetAvailableFacilitiesImpl implements GetAvailableFacilitiesResponderInterface {

	public GetAvailableFacilitiesResponseType getAvailableFacilities(
			AttributedURIType logicalAddress,
			GetAvailableFacilitiesRequestType parameters) throws TechnicalException{
		try {
			GetAvailableFacilitiesResponseType response = new GetAvailableFacilitiesResponseType();
			
			// A more complex return code can be returned depending on the request, but for now we always return 1 
			Facility facility1 = new Facility();
			facility1.setFacilityId("SE2321000016-1hz6");
			facility1.setFacilityName("Testvårdcentral A");
			facility1.setHasQueue(false);
			facility1.getSupportedListingTypes().add("HLM");
			response.getHealthcareFacilities().add(facility1);

			Facility facility2 = new Facility();
			facility2.setFacilityId("SE2321000016-1hz7");
			facility2.setFacilityName("Testvårdcentral B");
			facility2.setHasQueue(true);
			facility2.getSupportedListingTypes().add("HLM");
			response.getHealthcareFacilities().add(facility2);

			Facility facility3 = new Facility();
			facility3.setFacilityId("SE2321000016-1hz8");
			facility3.setFacilityName("Testvårdcentral C");
			facility3.setHasQueue(false);
			facility3.getSupportedListingTypes().add("HLM");
			facility3.getSupportedListingTypes().add("DSK");
			response.getHealthcareFacilities().add(facility3);

			Facility facility4 = new Facility();
			facility4.setFacilityId("SE2321000016-1hz3");
			facility4.setFacilityName("Testspecialist A");
			facility4.setHasQueue(false);
			facility4.getSupportedListingTypes().add("HLM");
			response.getHealthcareFacilities().add(facility4);
			
			Facility facility5 = new Facility();
			facility5.setFacilityId("SE2321000016-1hz4");
			facility5.setFacilityName("Testspecialist B");
			facility5.setHasQueue(false);
			facility5.getSupportedListingTypes().add("HLM");
			response.getHealthcareFacilities().add(facility5);

			Facility facility6 = new Facility();
			facility6.setFacilityId("SE2321000016-1hz5");
			facility6.setFacilityName("Testspecialist C");
			facility6.setHasQueue(false);
			facility6.getSupportedListingTypes().add("HLM");
			facility6.getSupportedListingTypes().add("BVC");
			response.getHealthcareFacilities().add(facility6);

			return response;
		} catch (RuntimeException e) {
			System.out.println("Error occured: " + e);
			throw e;
		}
	}
}