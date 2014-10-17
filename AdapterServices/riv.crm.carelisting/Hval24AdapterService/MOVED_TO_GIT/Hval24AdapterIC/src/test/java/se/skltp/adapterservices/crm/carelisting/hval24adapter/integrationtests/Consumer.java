/**
 * Copyright (c) 2014, Inera AB <http://www.inera.se/>. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package se.skltp.adapterservices.crm.carelisting.hval24adapter.integrationtests;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import org.w3.wsaddressing10.AttributedURIType;

import se.skl.riv.crm.carelisting.v1.Listing;
import se.skl.riv.crm.carelisting.v1.rivtabp20.getlisting.GetListingResponderInterface;
import se.skl.riv.crm.carelisting.v1.rivtabp20.getlisting.GetListingResponderService;
import se.skl.riv.crm.carelisting.v1.rivtabp20.getlisting.PersonNotFound;
import se.skl.riv.crm.carelisting.v1.rivtabp20.getlisting.TechnicalException;
import se.skl.riv.crm.carelistingresponder.v1.getlisting.GetListingRequestType;
import se.skl.riv.crm.carelistingresponder.v1.getlisting.GetListingResponseType;

public class Consumer {
	
	private GetListingResponderInterface _service = null;

	public static void main(String[] args) throws PersonNotFound, TechnicalException {
		
		ResourceBundle rb     = ResourceBundle.getBundle("hval");
		String serviceAddress = rb.getString("carelisting.url");
    	String personId       = "196210083611";
    	String logicalAddress = "01";

		if (args.length > 0) {
			serviceAddress = args[0];
		}

		if (args.length > 1) {
			personId = args[1];
		}

		executeTestCall(logicalAddress, personId, serviceAddress);
	}

	private static void executeTestCall(String logicalAddress, String personId, String serviceAddress) throws PersonNotFound, TechnicalException {
		System.out.println("Consumer connecting to "  + serviceAddress);
		Consumer consumer = new Consumer(serviceAddress);
		GetListingResponseType response = consumer.callService(logicalAddress, personId);

		long ts = System.currentTimeMillis();
		response = consumer.callService(logicalAddress, personId);
		ts = System.currentTimeMillis() - ts;
		printResult(response, ts);
	}

	private static void printResult(GetListingResponseType repsonse, long ts) {		
		System.out.println("Returned ( in " + ts + " ms.): " + 
				"\n PersonId: "      + repsonse.getSubjectOfCare().getPersonId() + 
				"\n Facility-Enhetskod: "      + repsonse.getSubjectOfCare().getListing().get(0).getHealthcareFacility().getFacilityId() + 
				"\n Listningstyp: "      + repsonse.getSubjectOfCare().getListing().get(0).getListingType() + 
				"\n Listningsdatum: "      + repsonse.getSubjectOfCare().getListing().get(0).getValidFromDate() ); 
	}
	
	public Consumer(String serviceAddress) {
		_service = new GetListingResponderService(
			createEndpointUrlFromServiceAddress(serviceAddress)).getGetListingResponderPort();
	}
	
	public GetListingResponseType callService(String logicalAddress, String personId) throws PersonNotFound, TechnicalException {

		GetListingRequestType request = new GetListingRequestType();
		request.setPersonId(personId);
			
		AttributedURIType logicalAddressType = new AttributedURIType();
		logicalAddressType.setValue(logicalAddress);

		GetListingResponseType reply = _service.getListing(logicalAddressType, request);

		return reply;
	}

	private URL createEndpointUrlFromServiceAddress(String serviceAddress) {
		try {
			return new URL(serviceAddress + "?wsdl");
		} catch (MalformedURLException e) {
			throw new RuntimeException("Malformed URL Exception: " + e.getMessage());
		}
	}
}