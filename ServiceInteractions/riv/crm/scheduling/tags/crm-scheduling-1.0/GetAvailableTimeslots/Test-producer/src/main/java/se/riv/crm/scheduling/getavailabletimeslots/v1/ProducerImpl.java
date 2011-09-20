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
package se.riv.crm.scheduling.getavailabletimeslots.v1;

import javax.jws.WebService;

import org.w3.wsaddressing10.AttributedURIType;

import se.riv.crm.scheduling.getavailabletimeslots.v1.GetAvailableTimeslotsResponderInterface;
import se.riv.crm.scheduling.getavailabletimeslots.v1.GetAvailableTimeslotsResponseType;
import se.riv.crm.scheduling.getavailabletimeslots.v1.GetAvailableTimeslotsType;

@WebService(
		serviceName = "GetAvailableTimeslotsResponderService", 
		endpointInterface="se.riv.crm.scheduling.getavailabletimeslots.v1.GetAvailableTimeslotsResponderInterface", 
		portName = "GetAvailableTimeslotsResponderPort", 
		targetNamespace = "urn:riv:crm:scheduling:GetAvailableTimeslots:1:rivtabp20",
		wsdlLocation = "schemas/interactions/GetAvailableTimeslotsInteraction/GetAvailableTimeslotsInteraction_1.0_rivtabp20.wsdl")
public class ProducerImpl implements GetAvailableTimeslotsResponderInterface {

	public GetAvailableTimeslotsResponseType getAvailableTimeslots(final AttributedURIType logicalAddress, final GetAvailableTimeslotsType parameters) {
		GetAvailableTimeslotsResponseType response = new GetAvailableTimeslotsResponseType();
		return response;
	}
}
