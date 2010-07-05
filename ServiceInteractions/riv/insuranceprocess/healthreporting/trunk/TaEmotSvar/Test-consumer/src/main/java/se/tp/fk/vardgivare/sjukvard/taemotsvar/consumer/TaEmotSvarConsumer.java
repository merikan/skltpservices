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
package se.tp.fk.vardgivare.sjukvard.taemotsvar.consumer;

import java.net.MalformedURLException;
import java.net.URL;

import org.w3.wsaddressing10.AttributedURIType;

import se.fk.vardgivare.sjukvard.taemotlakarintyg.v1.rivtabp20.TaEmotLakarintygResponderInterface;
import se.fk.vardgivare.sjukvard.taemotlakarintyg.v1.rivtabp20.TaEmotLakarintygResponderService;
import se.fk.vardgivare.sjukvard.taemotlakarintygresponder.v1.TaEmotLakarintygResponseType;
import se.fk.vardgivare.sjukvard.taemotlakarintygresponder.v1.TaEmotLakarintygType;
import se.fk.vardgivare.sjukvard.v1.Adressering;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg;
import se.fk.vardgivare.sjukvard.v1.TaEmotLakarintyg;

public final class TaEmotSvarConsumer {

	// Use this one to connect via Virtualiseringsplattformen
	private static final String LOGISK_ADDRESS = "/TaEmotLakarintyg_Service";

	// Use this one to connect directly (just for test)

	public static void main(String[] args) {
		String host = "localhost:19000/virtuell";
		if (args.length > 0) {
			host = args[0];
		}

		// Setup ssl info for the initial ?wsdl lookup...
		System.setProperty("javax.net.ssl.keyStore","../certs/consumer.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "password");
		System.setProperty("javax.net.ssl.trustStore","../certs/truststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "password");

		String adress = "https://" + host + LOGISK_ADDRESS;
		System.out.println("Consumer connecting to " + adress);
		String p = callTaEmotLakarintyg("RIV TA BP2.0 Ref App OK", adress,"Test HSA-ID");
		System.out.println("Returned: " + p);
	}

	public static String callTaEmotLakarintyg(String id, String serviceAddress,
			String logicalAddresss) {

		TaEmotLakarintygResponderInterface service = new TaEmotLakarintygResponderService(
				createEndpointUrlFromServiceAddress(serviceAddress)).getTaEmotLakarintygResponderPort();

		AttributedURIType logicalAddressHeader = new AttributedURIType();
		logicalAddressHeader.setValue(logicalAddresss);

		TaEmotLakarintygType request = new TaEmotLakarintygType();
		
		// Simple Intyg
		TaEmotLakarintyg fkSklTELA = new TaEmotLakarintyg();
		request.setFKSKLTaEmotLakarintygAnrop(fkSklTELA);
		fkSklTELA.setAdressering(getAdressering());
		fkSklTELA.setLakarintyg(getSimpleLakarintyg());
		
		try {
			TaEmotLakarintygResponseType result = service.taEmotLakarintyg(logicalAddressHeader, request);

			if (result != null) {
				return ("Result OK");
			} else {
				return ("Result Error!");				
			}

		} catch (Exception ex) {
			System.out.println("Exception=" + ex.getMessage());
			return null;
		}
	}

	private static Adressering getAdressering() {
		Adressering adressering = new Adressering();
		
		
		return adressering;
	}
	private static Lakarintyg getSimpleLakarintyg() {
		Lakarintyg lakarintyg = new Lakarintyg();
		return lakarintyg;
	}

	private static Lakarintyg getComplexLakarintyg() {
		Lakarintyg lakarintyg = new Lakarintyg();
		return lakarintyg;
	}
	
	public static URL createEndpointUrlFromServiceAddress(String serviceAddress) {
		try {
			return new URL(serviceAddress + "?wsdl");
		} catch (MalformedURLException e) {
			throw new RuntimeException("Malformed URL Exception: "
					+ e.getMessage());
		}
	}
}
