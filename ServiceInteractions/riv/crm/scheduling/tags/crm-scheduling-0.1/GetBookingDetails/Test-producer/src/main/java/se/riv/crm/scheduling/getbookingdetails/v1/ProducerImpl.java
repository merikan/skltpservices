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
package se.riv.crm.scheduling.getbookingdetails.v1;

import javax.jws.WebService;

import org.w3.wsaddressing10.AttributedURIType;

import se.riv.crm.scheduling.getbookingdetails.v1.GetBookingDetailsResponderInterface;
import se.riv.crm.scheduling.getbookingdetails.v1.GetBookingDetailsType;
import se.riv.crm.scheduling.getbookingdetails.v1.GetBookingDetailsResponseType;

@WebService(
		serviceName = "GetBookingDetailsResponderService", 
		endpointInterface="se.riv.crm.scheduling.getbookingdetails.v1.GetBookingDetailsResponderInterface", 
		portName = "GetBookingDetailsResponderPort", 
		targetNamespace = "urn:riv:crm:scheduling:GetBookingDetails:1:rivtabp20",
		wsdlLocation = "schemas/interactions/GetBookingDetailsInteraction/GetBookingDetailsInteraction_1.0_rivtabp20.wsdl")
public class ProducerImpl implements GetBookingDetailsResponderInterface {

	public GetBookingDetailsResponseType getBookingDetails(final AttributedURIType logicalAddress, final GetBookingDetailsType parameters) {
		GetBookingDetailsResponseType response = new GetBookingDetailsResponseType();
		return response;
	}
}
