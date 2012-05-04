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
package se.riv.crm.scheduling.getcancelledandrebooked.v1;

import javax.jws.WebService;

import org.w3.wsaddressing10.AttributedURIType;

import se.riv.crm.scheduling.getcancelledandrebooked.v1.GetCancelledAndRebookedresponderInterface;
import se.riv.crm.scheduling.getcancelledandrebooked.v1.GetCancelledAndRebookedType;
import se.riv.crm.scheduling.getcancelledandrebooked.v1.GetCancelledAndRebookedResponseType;

@WebService(
		serviceName = "GetCancelledAndRebookedResponderService", 
		endpointInterface="se.riv.crm.scheduling.getcancelledandrebooked.v1.GetCancelledAndRebookedresponderInterface", 
		portName = "GetCancelledAndRebookedResponderPort", 
		targetNamespace = "urn:riv:crm:scheduling:GetCancelledAndRebooked:1:rivtabp20",
		wsdlLocation = "schemas/interactions/GetCancelledAndRebookedInteraction/GetCancelledAndRebookedInteraction_1.0_RIVTABP20.wsdl")
public class ProducerImpl implements GetCancelledAndRebookedresponderInterface {

	public GetCancelledAndRebookedResponseType getCancelledAndRebooked(final AttributedURIType logicalAddress, final GetCancelledAndRebookedType parameters) {
		GetCancelledAndRebookedResponseType response = new GetCancelledAndRebookedResponseType();
		return response;
	}
}
