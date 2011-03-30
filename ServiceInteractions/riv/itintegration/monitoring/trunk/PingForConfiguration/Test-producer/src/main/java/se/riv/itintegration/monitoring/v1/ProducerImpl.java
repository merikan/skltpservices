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
package se.riv.itintegration.monitoring.v1;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jws.WebService;

import org.w3.wsaddressing10.AttributedURIType;

@WebService(
		serviceName = "PingForConfigurationResponderService", 
		endpointInterface="se.riv.itintegration.monitoring.v1.PingForConfigurationResponderInterface", 
		portName = "PingForConfigurationResponderPort", 
		targetNamespace = "urn:riv:itintegration:monitoring:PingForConfiguration:1:rivtabp20",
		wsdlLocation = "schemas/interactions/PingForConfigurationInteraction/PingForConfigurationInteraction_1.0_RIVTABP20.wsdl")
public class ProducerImpl implements PingForConfigurationResponderInterface {

	public PingForConfigurationResponseType pingForConfiguration(final AttributedURIType logicalAddress, final PingForConfigurationType parameters) {
		PingForConfigurationResponseType response = new PingForConfigurationResponseType();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
		
		response.setPingDateTime(formatter.format(new Date()));
		response.setVersion("V1.0");
		
		return response;
	}
}
