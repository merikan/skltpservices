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

import java.net.MalformedURLException;
import java.net.URL;

import org.w3.wsaddressing10.AttributedURIType;

import se.riv.itinfra.tp.ping.v1.rivtabp20.PingResponderInterface;
import se.riv.itinfra.tp.ping.v1.rivtabp20.PingResponderService;
import se.riv.itinfra.tp.pingresponder.v1.PingRequestType;
import se.riv.itinfra.tp.pingresponder.v1.PingResponseType;

public final class PingConsumer {

	// Use this one to connect via Virtualiseringsplattformen
	private static final String LOGISK_ADDRESS = "/Ping/1/rivtabp20";
	// Use this one to connect directly (just for test)

	public static void main(String[] args) {
		String host = "vp.prod.tp.cybercom.carelink.sjunet.org:20000/vp";
//		String host = "localhost:21000/vp";
		if (args.length > 0) {
			host = args[0];
		}

		// Setup ssl info for the initial ?wsdl lookup...
		System.setProperty("javax.net.ssl.keyStore","../../SITHScerts/tk_qa_auth.p12");
		System.setProperty("javax.net.ssl.keyStorePassword", "PRPWkJ9mBr");
		System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
		System.setProperty("javax.net.ssl.trustStore", "../../SITHScerts/truststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "password");

//		System.setProperty("javax.net.ssl.keyStore","../certs/consumer.jks");
//		System.setProperty("javax.net.ssl.keyStorePassword", "password");
//		System.setProperty("javax.net.ssl.keyStoreType", "jks");
//		System.setProperty("javax.net.ssl.trustStore", "../certs/truststore.jks");
//		System.setProperty("javax.net.ssl.trustStorePassword", "password");

		String adress = "https://" + host + LOGISK_ADDRESS;
		System.out.println("Consumer connecting to "  + adress);
		String p = callService(adress, "Ping");
		System.out.println("Returned: " + p);
	}

	public static String callService(String serviceAddress, String logicalAddresss) {
		
		PingResponderInterface service = new PingResponderService(
			createEndpointUrlFromServiceAddress(serviceAddress)).getPingResponderPort();

		AttributedURIType logicalAddressHeader = new AttributedURIType();
		logicalAddressHeader.setValue(logicalAddresss);

		PingRequestType request = new PingRequestType();
		
		// The below request is created to mimic the sample xml-message
		request.setPingIn("Pinging!");
	
		try {
		    PingResponseType result = service.ping(logicalAddressHeader, request);

		    return ("Ping response=" + result.getPingUt());

		} catch (Exception ex) {
			System.out.println("Exception=" + ex.getMessage());
			return null;
		}
	}

	public static URL createEndpointUrlFromServiceAddress(String serviceAddress) {
		try {
			return new URL(serviceAddress + "?wsdl");
		} catch (MalformedURLException e) {
			throw new RuntimeException("Malformed URL Exception: " + e.getMessage());
		}
	}
}
