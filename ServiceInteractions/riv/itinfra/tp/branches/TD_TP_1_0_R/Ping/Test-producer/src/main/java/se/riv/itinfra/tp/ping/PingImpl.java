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
package se.riv.itinfra.tp.ping;

import javax.jws.WebService;

import org.w3.wsaddressing10.AttributedURIType;

import se.riv.itinfra.tp.ping.v1.rivtabp20.PingResponderInterface;
import se.riv.itinfra.tp.pingresponder.v1.PingRequestType;
import se.riv.itinfra.tp.pingresponder.v1.PingResponseType;

@WebService(
		serviceName = "PingResponderService", 
		endpointInterface="se.riv.itinfra.tp.ping.v1.rivtabp20.PingResponderInterface", 
		portName = "PingResponderPort", 
		targetNamespace = "urn:riv:itinfra:tp:Ping:1:rivtabp20",
		wsdlLocation = "schemas/PingInteraction_1.0_rivtabp20.wsdl")
public class PingImpl implements PingResponderInterface {

	public PingResponseType ping(AttributedURIType logicalAddress, PingRequestType parameters) {
//		try {
			PingResponseType response = new PingResponseType();

			response.setPingUt("Ping response from: " + parameters.getPingIn());			
			return response;
//		} catch (RuntimeException e) {
//			System.out.println("Error occured: " + e);
//			throw e;
//		}
	}
}