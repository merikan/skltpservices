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
package se.riv.eservicesupply.eoffering.v1;

import javax.jws.WebService;

import se.riv.eservicesupply.eoffering.getavailableeservices.v1.GetAvailableEServicesResponseType;
import se.riv.eservicesupply.eoffering.getavailableeservices.v1.GetAvailableEServicesType;
import se.riv.eservicesupply.eoffering.getavailableeservices.v1.rivtabp21.GetAvailableEServicesResponderInterface;

@WebService(
		serviceName = "GetAvailableEServicesResponderService", 
		endpointInterface="se.riv.eservicesupply.eoffering.getavailableeservices.v1.rivtabp21.GetAvailableEServicesResponderInterface", 
		portName = "GetAvailableEServicesResponderPort", 
		targetNamespace = "urn:riv:eservicesupply:eoffering:GetAvailableEServices:1:rivtabp21",
		wsdlLocation = "schemas/interactions/GetAvailableEServicesInteraction/GetAvailableEServicesInteraction_1.0_RIVTABP21.wsdl")
public class ProducerImpl implements GetAvailableEServicesResponderInterface {

	public GetAvailableEServicesResponseType getAvailableEServices(
			String logicalAddress, GetAvailableEServicesType parameters) {
		GetAvailableEServicesResponseType response = new GetAvailableEServicesResponseType();
		return response;
	}
}
