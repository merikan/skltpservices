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
package se.riv.ehr.accescontrol.assertcareengagement;

import javax.jws.WebService;

import org.w3.wsaddressing10.AttributedURIType;

import se.riv.ehr.accesscontrol.assertcareengagement.v1.rivtabp20.AssertCareEngagementResponderInterface;
import se.riv.ehr.accesscontrol.assertcareengagementresponder.v1.AssertCareEngagementResponseType;
import se.riv.ehr.accesscontrol.assertcareengagementresponder.v1.AssertCareEngagementType;

@WebService(
		serviceName = "AssertCareEngagementResponderService", 
		endpointInterface="se.riv.ehr.accesscontrol.assertcareengagement.v1.rivtabp20.AssertCareEngagementResponderInterface", 
		portName = "AssertCareEngagementResponderPort", 
		targetNamespace = "urn:riv:ehr:accesscontrol:AssertCareEngagement:1:rivtabp20",
		wsdlLocation = "schemas/interactions/AssertCareEngagementInteraction/AssertCareEngagementInteraction_1.0_RIVTABP20.wsdl")
public class AssertCareEngagementImpl implements AssertCareEngagementResponderInterface {

	public AssertCareEngagementResponseType assertCareEngagement( AttributedURIType logicalAddress, AssertCareEngagementType parameters) {
		AssertCareEngagementResponseType response = new AssertCareEngagementResponseType();

		response.setHasCareEngagement(false);
		
		if (parameters.getPerformer().equalsIgnoreCase("kalle")) {
			response.setHasCareEngagement(true);
		}
		
		return response;
	}
}