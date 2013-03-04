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
package se.riv.clinicalprocess.healthcond.getcaredocumentation.v2;

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

import se.riv.ehr.patientsummary.getcaredocumentation.v2.rivtabp21.GetCareDocumentationResponderInterface;
import se.riv.ehr.patientsummary.getcaredocumentation.v2.rivtabp21.GetCareDocumentationResponderService;
import se.riv.ehr.patientsummary.getcaredocumentationresponder.v1.GetCareDocumentationResponseType;
import se.riv.ehr.patientsummary.getcaredocumentationresponder.v1.GetCareDocumentationType;
import se.riv.ehr.patientsummary.v2.DatePeriodType;
import se.riv.ehr.patientsummary.v2.PatientIdType;

public final class TestConsumer {

    private static final String LOGICAL_ADDRESS = "Test";

	private static final String LOGISK_ADDRESS = "/GetCareDocumentation/2/rivtabp21";

	// private static String host = "192.168.25.40:20000/vp";

	private static String host = "test1.esb.ntjp.se/vp";

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
					.getResource("Schemas/schemas/interactions/GetCareDocumentationInteraction/GetCareDocumentationInteraction_2.0_RIVTABP21.wsdl");


            GetCareDocumentationResponderService service = new GetCareDocumentationResponderService(wsdlUrl);
            GetCareDocumentationResponderInterface serviceInterface = service
					.getGetCareDocumentationResponderPort();

			// Set web service server url
			BindingProvider provider = (BindingProvider) serviceInterface;
			provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceAddress);

            GetCareDocumentationType request = new GetCareDocumentationType();
            PatientIdType patientIdType = new PatientIdType();
            patientIdType.setId("patientId");
            patientIdType.setType("patientType");

            request.setPatientId(patientIdType);

            DatePeriodType datePeriodType = new DatePeriodType();
            datePeriodType.setStart("2012-01-01");
            datePeriodType.setEnd("2012-12-31");
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

            GetCareDocumentationResponseType response = serviceInterface.getCareDocumentation(LOGICAL_ADDRESS, request);

			return ("GetCareDocumentation response=" + response.getCareDocumentation());

		} catch (Throwable ex) {
			System.out.println("Exception={}" + ex.getMessage());
			throw new RuntimeException(ex); // TODO: Define other exception
		}
	}

	private static TLSClientParameters setUpTlsClientParams() throws Exception {

		KeyStore trustStore = KeyStore.getInstance("JKS");
		String trustStoreLoc = "../../certs/truststore_test.jks";
		String trustPassword = "password";
		trustStore.load(new FileInputStream(trustStoreLoc), trustPassword.toCharArray());

		String keyPassword = "password";
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		String keyStoreLoc = "../../certs/consumer.p12";
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
