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
package se.riv.crm.scheduling.getsubjectofcareschedule.v1;

import java.io.FileInputStream;
import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.ws.BindingProvider;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import se.riv.crm.scheduling.getsubjectofcareschedule.v1.rivtabp21.GetSubjectOfCareScheduleResponderInterface;
import se.riv.crm.scheduling.getsubjectofcareschedule.v1.rivtabp21.GetSubjectOfCareScheduleResponderService;
import se.riv.crm.scheduling.getsubjectofcarescheduleresponder.v1.GetSubjectOfCareScheduleResponseType;
import se.riv.crm.scheduling.getsubjectofcarescheduleresponder.v1.GetSubjectOfCareScheduleType;

public final class TestConsumer {

	private static final String LOGICAL_ADDRESS = "Test";
	
	private static String endpointAddress = "https://localhost:20001/teststub/GetSubjectOfCareSchedule/1/rivtabp21";
	private static String keystorePath = "src/main/resources/test-certs/consumer.jks";
	private static String keystoreType = "JKS";
	private static String keystorePassword = "password";
	private static String truststorePath = "src/main/resources/test-certs/truststore.jks";
	private static String truststoreType = "JKS";
	private static String truststorePassword = "password";

	public static void main(String[] args) {
		
		if (args.length > 0) {
		      endpointAddress = args[0];
		      keystorePath = args[1];
		      keystoreType = args[2];
		      keystorePassword = args[3];
		      truststorePath = args[4];
		      truststoreType = args[5];
		      truststorePassword = args[6]; 
		}

		System.out.println("Consumer connecting to " + endpointAddress);
		String p = callService2(endpointAddress, LOGICAL_ADDRESS);
		System.out.println("Returned: " + p);
	}

	public static String callService2(String serviceAddress, String logicalAddresss) {

		try {
			// Get URL to wsdl file
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			URL wsdlUrl = loader
					.getResource("interactions/GetSubjectOfCareScheduleInteraction/GetSubjectOfCareScheduleInteraction_1.1_RIVTABP21.wsdl");

			GetSubjectOfCareScheduleResponderService service = new GetSubjectOfCareScheduleResponderService(wsdlUrl);
			GetSubjectOfCareScheduleResponderInterface serviceInterface = service
					.getGetSubjectOfCareScheduleResponderPort();

			// Set web service server url
			BindingProvider provider = (BindingProvider) serviceInterface;
			provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceAddress);

			GetSubjectOfCareScheduleType request = new GetSubjectOfCareScheduleType();
			request.setHealthcareFacility("HSA-VKK123");
			request.setSubjectOfCare("19751026-6849");

			Client client = ClientProxy.getClient(serviceInterface);
			HTTPConduit http = (HTTPConduit) client.getConduit();

			HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
			httpClientPolicy.setConnectionTimeout(36000);
			httpClientPolicy.setAllowChunking(false);
			httpClientPolicy.setReceiveTimeout(32000);
			http.setClient(httpClientPolicy);

			TLSClientParameters tlsCP = setUpTlsClientParams();
			http.setTlsClientParameters(tlsCP);

			GetSubjectOfCareScheduleResponseType response = serviceInterface.getSubjectOfCareSchedule(LOGICAL_ADDRESS,
					null, request);

			return ("GetSubjectOfCareSchedule response=" + response.getTimeslotDetail());

		} catch (Throwable ex) {
			System.out.println("Exception={}" + ex.getMessage());
			throw new RuntimeException(ex); // TODO: Define other exception
		}
	}

	private static TLSClientParameters setUpTlsClientParams() throws Exception {

		KeyStore trustStore = KeyStore.getInstance(truststoreType);
		trustStore.load(new FileInputStream(truststorePath), truststorePassword.toCharArray());

		KeyStore keyStore = KeyStore.getInstance(keystoreType);
		keyStore.load(new FileInputStream(keystorePath), keystorePassword.toCharArray());

		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(trustStore);

		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(keyStore, keystorePassword.toCharArray());

		TLSClientParameters tlsCP = new TLSClientParameters();
		tlsCP.setTrustManagers(tmf.getTrustManagers());
		tlsCP.setKeyManagers(kmf.getKeyManagers());

		// The following is not recommended and would not be done in a
		// prodcution environment,
		// this is just for illustrative purpose
		tlsCP.setDisableCNCheck(true);

		return tlsCP;

	}

}
