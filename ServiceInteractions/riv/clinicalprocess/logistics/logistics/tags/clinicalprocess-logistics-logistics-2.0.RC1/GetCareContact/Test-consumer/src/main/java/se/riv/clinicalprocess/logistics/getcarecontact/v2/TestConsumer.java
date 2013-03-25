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
package se.riv.clinicalprocess.logistics.getcarecontact.v2;

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

import se.riv.clinicalprocess.logistics.getcarecontact.v2.rivtabp21.GetCareContactResponderInterface;
import se.riv.clinicalprocess.logistics.getcarecontact.v2.rivtabp21.GetCareContactResponderService;
import se.riv.clinicalprocess.logistics.getcarecontactresponder.v2.GetCareContactResponseType;
import se.riv.clinicalprocess.logistics.getcarecontactresponder.v2.GetCareContactType;
import se.riv.clinicalprocess.logistics.v2.DatePeriodType;
import se.riv.clinicalprocess.logistics.v2.PatientIdType;

public final class TestConsumer {

    private static final String LOGICAL_ADDRESS = "Test";

	private static final String LOGISK_ADDRESS = "/GetCareContact/2/rivtabp21";

	// private static String host = "192.168.25.40:20000/vp";

    //private static String host = "test1.esb.ntjp.se/vp";
    private static String host = "localhost:20000/testproducer";

	public static void main(String[] args) {

		if (args.length > 0) {
			host = args[0];
		}

		String endpointAddress = "https://" + host + LOGISK_ADDRESS;
		System.out.println("Consumer connecting to " + endpointAddress);
		String p = callService2(endpointAddress, "Test");
		System.out.println("Returned: " + p);
	}

	public static String callService2(final String serviceAddress, final String logicalAddresss) {

		try {
			// Get URL to wsdl file
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			URL wsdlUrl = loader
					.getResource("schemas/interactions/GetCareContactInteraction/GetCareContactInteraction_2.0_RIVTABP21.wsdl");

            GetCareContactResponderService service = new GetCareContactResponderService(wsdlUrl);
            GetCareContactResponderInterface serviceInterface = service
					.getGetCareContactResponderPort();

			// Set web service server url
			BindingProvider provider = (BindingProvider) serviceInterface;
			provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceAddress);

            GetCareContactType request = new GetCareContactType();
            PatientIdType patientIdType = new PatientIdType();
            patientIdType.setId("1212121212");
            patientIdType.setType("1.2.752.129.2.1.3.1");

            request.setPatientId(patientIdType);

            DatePeriodType datePeriodType = new DatePeriodType();
            datePeriodType.setStart("20130113");
            datePeriodType.setEnd("20130213");
            request.setTimePeriod(datePeriodType);

			Client client = ClientProxy.getClient(serviceInterface);
			HTTPConduit http = (HTTPConduit) client.getConduit();

			HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
			httpClientPolicy.setConnectionTimeout(36000);
			httpClientPolicy.setAllowChunking(false);
			httpClientPolicy.setReceiveTimeout(32000);
			http.setClient(httpClientPolicy);

			TLSClientParameters tlsCP = setUpTlsClientParams();
			http.setTlsClientParameters(tlsCP);

            GetCareContactResponseType response = serviceInterface.getCareContact(LOGICAL_ADDRESS, request);

			return ("GetCareContact response=" + response.getCareContact());

		} catch (Throwable ex) {
			System.out.println("Exception={}" + ex.getMessage());
			throw new RuntimeException(ex); // TODO: Define other exception
		}
	}

	private static TLSClientParameters setUpTlsClientParams() throws Exception {
        KeyStore trustStore = KeyStore.getInstance("JKS");
		String trustStoreLoc = "certs/truststore-consumer.jks";
		String trustPassword = "password";
		trustStore.load(new FileInputStream(trustStoreLoc), trustPassword.toCharArray());

		String keyPassword = "password";
		KeyStore keyStore = KeyStore.getInstance("JKS");
		String keyStoreLoc = "certs/consumer.jks";
		keyStore.load(new FileInputStream(keyStoreLoc), keyPassword.toCharArray());

		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(trustStore);

		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(keyStore, keyPassword.toCharArray());

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
