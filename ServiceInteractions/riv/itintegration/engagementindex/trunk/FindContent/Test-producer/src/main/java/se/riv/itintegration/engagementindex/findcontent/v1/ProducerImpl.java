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
package se.riv.itintegration.engagementindex.findcontent.v1;

import java.util.Date;

import javax.jws.WebService;

import riv.itintegration.engagementindex._1.EngagementType;
import se.riv.itintegration.engagementindex.findcontent.v1.rivtabp21.FindContentResponderInterface;
import se.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentResponseType;
import se.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentType;

@WebService(serviceName = "FindContentResponderService", endpointInterface = "se.riv.itintegration.engagementindex.findcontent.v1.rivtabp21.FindContentResponderInterface", portName = "FindContentResponderPort", targetNamespace = "urn:riv:itintegration:engagementindex:FindContent:1:rivtabp21", wsdlLocation = "schemas/interactions/FindContentInteraction/FindContentInteraction_1.0_RIVTABP21.wsdl")
public class ProducerImpl implements FindContentResponderInterface {

	@Override
	public FindContentResponseType findContent(String arg0, FindContentType findContent) {

		FindContentResponseType reponse = new FindContentResponseType();
		reponse.getEngagement().add(createEngagementType(findContent));

		return reponse;
	}

	private EngagementType createEngagementType(FindContentType findContent) {

		EngagementType engagementType = new EngagementType();
		engagementType.setBusinessObjectInstanceIdentifier(findContent.getBusinessObjectInstanceIdentifier());
		engagementType.setCategorization("Booking");
		engagementType.setCreationTime(new Date().toString());
		engagementType.setLogicalAddress(findContent.getLogicalAddress());
		engagementType.setMostRecentContent(findContent.getMostRecentContent());
		engagementType.setOwner(findContent.getOwner());
		engagementType.setRegisteredResidentIdentification("191111111111");
		engagementType.setServiceDomain("riv:crm:scheduling");
		engagementType.setSourceSystem(findContent.getSourceSystem());
		engagementType.setUpdateTime(new Date().toString());

		return engagementType;
	}

}
