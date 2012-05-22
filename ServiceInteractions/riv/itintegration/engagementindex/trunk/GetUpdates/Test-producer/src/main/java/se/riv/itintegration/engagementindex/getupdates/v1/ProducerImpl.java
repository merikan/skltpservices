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
package se.riv.itintegration.engagementindex.getupdates.v1;

import java.util.Date;

import javax.jws.WebService;

import riv.itintegration.engagementindex._1.EngagementType;
import se.riv.itintegration.engagementindex.getupdates.v1.rivtabp21.GetUpdatesResponderInterface;
import se.riv.itintegration.engagementindex.getupdatesresponder.v1.GetUpdatesResponseType;
import se.riv.itintegration.engagementindex.getupdatesresponder.v1.GetUpdatesType;
import se.riv.itintegration.engagementindex.getupdatesresponder.v1.RegisteredResidentEngagementType;

@WebService(serviceName = "GetUpdatesResponderService", endpointInterface = "se.riv.itintegration.engagementindex.getupdates.v1.rivtabp21.GetUpdatesResponderInterface", portName = "GetUpdatesResponderPort", targetNamespace = "urn:riv:itintegration:engagementindex:GetUpdates:1:rivtabp21", wsdlLocation = "schemas/interactions/GetUpdatesInteraction/GetUpdatesInteraction_1.0_RIVTABP21.wsdl")
public class ProducerImpl implements GetUpdatesResponderInterface {

	@Override
	public GetUpdatesResponseType getUpdates(String arg0, GetUpdatesType arg1) {
		GetUpdatesResponseType getUpdatesResponseType = new GetUpdatesResponseType();
		getUpdatesResponseType.setResponseIsComplete(true);
		
		String serviceDomain = arg1.getServiceDomain();
		
		getUpdatesResponseType.getRegisteredResidentEngagement().add(createRegisteredResidentEngagementType(serviceDomain, "196911202163"));
		return getUpdatesResponseType;
	}

	private RegisteredResidentEngagementType createRegisteredResidentEngagementType(String registeredResidentIdentification, String serviceDomain) {
		RegisteredResidentEngagementType engagementType = new RegisteredResidentEngagementType();
		engagementType.getEngagement().add(createEngagementType(registeredResidentIdentification, serviceDomain));
		engagementType.setRegisteredResidentIdentification(registeredResidentIdentification);
		return engagementType;
	}

	private EngagementType createEngagementType(String registeredResidentIdentification, String serviceDomain) {

		EngagementType engagementType = new EngagementType();
		engagementType.setBusinessObjectInstanceIdentifier("");
		engagementType.setCategorization("");
		engagementType.setCreationTime("");
		engagementType.setLogicalAddress("");
		engagementType.setMostRecentContent("");
		engagementType.setOwner("");
		engagementType.setRegisteredResidentIdentification(registeredResidentIdentification);
		engagementType.setServiceDomain(serviceDomain);
		engagementType.setSourceSystem("");
		engagementType.setUpdateTime(new Date().toString());

		return engagementType;
	}

	

}
