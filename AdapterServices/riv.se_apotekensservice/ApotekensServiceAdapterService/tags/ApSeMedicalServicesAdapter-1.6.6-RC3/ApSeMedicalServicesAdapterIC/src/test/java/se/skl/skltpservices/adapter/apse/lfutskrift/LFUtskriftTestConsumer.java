/**
 * Copyright (c) 2013 Center for eHalsa i samverkan (CeHis).
 * 							<http://cehis.se/>
 *
 * This file is part of SKLTP.
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package se.skl.skltpservices.adapter.apse.lfutskrift;

import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.addressing.v1.AttributedURIType;

import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.inera.se.apotekensservice.lf.lfutskrift.v1.rivtabp20.ApplicationException;
import se.riv.inera.se.apotekensservice.lf.lfutskrift.v1.rivtabp20.LFUtskriftResponderInterface;
import se.riv.inera.se.apotekensservice.lf.lfutskrift.v1.rivtabp20.LFUtskriftResponderService;
import se.riv.inera.se.apotekensservice.lf.lfutskrift.v1.rivtabp20.SystemException;
import se.riv.se.apotekensservice.lf.lfutskriftresponder.v1.LFUtskriftRequestType;
import se.riv.se.apotekensservice.lf.lfutskriftresponder.v1.LFUtskriftResponseType;

public class LFUtskriftTestConsumer {

	private LFUtskriftResponderInterface service;

	public LFUtskriftTestConsumer(String endpointAdress) {

		URL url = createEndpointUrlFromServiceAddress(endpointAdress);
		service = new LFUtskriftResponderService(url)
				.getLFUtskriftResponderPort();
	}

	public LFUtskriftResponseType citicenRequest(LFUtskriftRequestType request,
			String to, ArgosHeaderType argosHeader) throws SystemException, ApplicationException {

		AttributedURIType logicalAddress = createLogicalAddress(to);
		return service.lfUtskrift(request, logicalAddress, argosHeader);
	}

	public LFUtskriftResponseType organizationRequest(
			LFUtskriftRequestType request, String to, ArgosHeaderType argosHeader) throws SystemException,
			ApplicationException {

		AttributedURIType logicalAddress = createLogicalAddress(to);
		return service.lfUtskrift(request, logicalAddress, argosHeader);
	}

	private AttributedURIType createLogicalAddress(String logicalAddress) {
		AttributedURIType logicalAddressType = new AttributedURIType();
		logicalAddressType.setValue(logicalAddress);
		return logicalAddressType;
	}

	private static URL createEndpointUrlFromServiceAddress(String serviceAddress) {
		try {
			return new URL(serviceAddress + "?wsdl");
		} catch (MalformedURLException e) {
			throw new RuntimeException("Malformed URL Exception: "
					+ e.getMessage());
		}
	}

}
