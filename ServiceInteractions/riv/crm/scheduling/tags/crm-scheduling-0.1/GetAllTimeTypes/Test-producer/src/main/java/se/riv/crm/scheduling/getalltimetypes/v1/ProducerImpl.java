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
package se.riv.crm.scheduling.getalltimetypes.v1;

import javax.jws.WebService;

import org.w3.wsaddressing10.AttributedURIType;

import se.riv.crm.scheduling.getalltimetypes.v1.GetAllTimeTypesResponderInterface;
import se.riv.crm.scheduling.getalltimetypes.v1.GetAllTimeTypesResponseType;
import se.riv.crm.scheduling.getalltimetypes.v1.GetAllTimeTypesType;
import se.riv.crm.scheduling.v1.TimeTypeType;

@WebService(
		serviceName = "GetAllTimeTypesResponderService", 
		endpointInterface="se.riv.crm.scheduling.getalltimetypes.v1.GetAllTimeTypesResponderInterface", 
		portName = "GetAllTimeTypesResponderPort", 
		targetNamespace = "urn:riv:crm:scheduling:GetAllTimeTypes:1:rivtabp20",
		wsdlLocation = "schemas/interactions/GetAllTimeTypesInteraction/GetAllTimeTypesInteraction_1.0_RIVTABP20.wsdl")
public class ProducerImpl implements GetAllTimeTypesResponderInterface {

	public GetAllTimeTypesResponseType getAllTimeTypes(final AttributedURIType logicalAddress, final GetAllTimeTypesType parameters) {
		GetAllTimeTypesResponseType response = new GetAllTimeTypesResponseType();
		
		TimeTypeType timeType = new TimeTypeType();
		timeType.setTimeTypeId("1122");
		timeType.setTimeTypeName("Akut");
		
		response.getListOfTimeTypes().add(timeType);
		
		return response;
	}
}
