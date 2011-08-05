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
package se.riv.inera.ior.v1;

import java.net.MalformedURLException;
import java.net.URL;

import org.w3.wsaddressing10.AttributedURIType;

import se.riv.inera.ior.hamtalokaltproduktsortiment.v1.HamtaLokaltProduktsortimentResponderInterface;
import se.riv.inera.ior.hamtalokaltproduktsortiment.v1.HamtaLokaltProduktsortimentResponderService;
import se.riv.inera.ior.hamtalokaltproduktsortiment.v1.HamtaLokaltProduktsortimentResponseType;
import se.riv.inera.ior.hamtalokaltproduktsortiment.v1.HamtaLokaltProduktsortimentType;

public final class Consumer {

	public static void main(final String[] args) {
		// Setup ssl info for the initial ?wsdl lookup...
		System.setProperty("javax.net.ssl.keyStore","../../certs/consumer.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "password");
		System.setProperty("javax.net.ssl.keyStoreType", "jks");
		System.setProperty("javax.net.ssl.trustStore", "../../certs/truststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "password");

		final String adress = "https://localhost:21000/vp/HamtaLokaltProduktsortiment/1/rivtabp20";
		System.out.println("Consumer connecting to "  + adress);
		
		final String p = callService(adress, "Test");
		System.out.println("Returned: " + p);
	}

	public static String callService(final String serviceAddress, final String logicalAddresss) {
		
		final HamtaLokaltProduktsortimentResponderInterface service = new HamtaLokaltProduktsortimentResponderService(
			Consumer.createEndpointUrlFromServiceAddress(serviceAddress)).getHamtaLokaltProduktsortimentResponderPort();

		final AttributedURIType logicalAddressHeader = new AttributedURIType();
		logicalAddressHeader.setValue(logicalAddresss);

		final HamtaLokaltProduktsortimentType request = new HamtaLokaltProduktsortimentType();
		
		/*
		 * 1. Set any data to the request
		 */
	
		/*
		 * 2. Call operation on the service
		 */
		final HamtaLokaltProduktsortimentResponseType result = service.hamtaLokaltProduktsortiment(logicalAddressHeader, request);
		
		/*
		 * 3. Any syso's to verify response
		 */
		
		/*
		 * 4. Return
		 */
		return result.toString();
	}

	public static URL createEndpointUrlFromServiceAddress(final String serviceAddress) {
		try {
			return new URL(serviceAddress + "?wsdl");
		} catch (MalformedURLException e) {
			throw new RuntimeException("Malformed URL Exception: " + e.getMessage());
		}
	}
}
