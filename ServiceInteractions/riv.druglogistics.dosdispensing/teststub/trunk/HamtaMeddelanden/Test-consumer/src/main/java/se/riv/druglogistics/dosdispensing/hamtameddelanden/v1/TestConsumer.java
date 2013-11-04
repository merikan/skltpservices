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
package se.riv.druglogistics.dosdispensing.hamtameddelanden.v1;

import java.io.FileInputStream;
import java.net.URL;
import java.security.KeyStore;
import java.util.Random;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.ws.BindingProvider;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.w3c.addressing_1_0.AttributedURIType;

import se.riv.druglogistics.dosedispensing_1.BehorighetsinfoRequest;
import se.riv.druglogistics.dosedispensing_1.HamtaMeddelandenResponderInterface;
import se.riv.druglogistics.dosedispensing_1.HamtaMeddelandenResponderService;
import se.riv.druglogistics.dosedispensing_1.HamtaMeddelandenResponseType;
import se.riv.druglogistics.dosedispensing_1.HamtaMeddelandenType;
import se.riv.druglogistics.dosedispensing_1.IdentitetstypEnum;
import se.riv.druglogistics.dosedispensing_1.MeddelandeStatusEnum;
import se.riv.druglogistics.dosedispensing_1.MeddelandenutvalRequest;
import se.riv.druglogistics.dosedispensing_1.MeddelandetypEnum;
import se.riv.druglogistics.dosedispensing_1.PatientinfoResponse;
import se.riv.druglogistics.dosedispensing_1.YrkesKodEnum;

public final class TestConsumer {

	private static String logicalAddress = "Test";
	private static long sleepTime = 5000L;
	private static String endpointAddress = "http://localhost:2000/teststub/HamtaMeddelanden/1/rivtabp20";
	private static String glnKod = "1111111111";

	private static HamtaMeddelandenResponderInterface service = null;

	public static void main(String[] args) throws Exception {

		if (args.length > 0) {
			sleepTime = Long.parseLong(args[0]);
			endpointAddress = args[1];
			logicalAddress = args[2];
			glnKod = args[3];
		}

		long randomness = sleepTime / 3L;
		Random random = new Random();
		System.out.println("Consumer connecting to " + endpointAddress);
		service = setupService(endpointAddress, logicalAddress);
		while (true) {
			long start = System.nanoTime();
			try {
				HamtaMeddelandenResponseType responseType = callService();
			} catch (Exception e) {
				System.out.println("Error: " + e.toString());
			} finally {
				System.out.println("Time: " + (System.nanoTime() - start) / 1000000L + " ms");
			}
			Thread.sleep((long) (sleepTime - randomness + 2L * randomness * random.nextDouble()));
		}
	}

	public static HamtaMeddelandenResponseType callService() {

		AttributedURIType logicalAddressHeader = new AttributedURIType();
		logicalAddressHeader.setValue(logicalAddress);

		HamtaMeddelandenType request = new HamtaMeddelandenType();
		request.setBehorighetsinformation(createBehorighetsInfo());

		request.setGlnkod(glnKod);
		request.setMeddelandenutval(createMeddelandeutval());
		return service.hamtaMeddelanden(logicalAddressHeader, request);
	}

	private static MeddelandenutvalRequest createMeddelandeutval() {
		MeddelandenutvalRequest request = new MeddelandenutvalRequest();
		request.setForskrivarkod("9001033");
		request.setMeddelandestatus(MeddelandeStatusEnum.LO);
		request.setMeddelandetyp(MeddelandetypEnum.VG);
		request.setVardgivarespersonid("TSE165565594230-9999");
		
		request.setPatientinformation(createPatientInformation());
		
		return request;
	}

	private static PatientinfoResponse createPatientInformation() {
		PatientinfoResponse patientInfo = new PatientinfoResponse();
		patientInfo.setEfternamn("x");
		patientInfo.setFornamn("x");
		patientInfo.setIdentitetstyp(IdentitetstypEnum.P);
		//patientInfo.setKommunkod(value);
		//patientInfo.setLanskod(value);
		patientInfo.setMellannamn("x");
		patientInfo.setPersonid("190110299807");
		return patientInfo;
	}

	private static BehorighetsinfoRequest createBehorighetsInfo() {
		BehorighetsinfoRequest request = new BehorighetsinfoRequest();
		request.setFornamn("Lindsey");
		request.setEfternamn("Pärlemo");
		request.setForskrivarkod("9001033");
		request.setArbetsplatskod("20131015422");
		request.setHsaid("TSE165565594230-9999");
		request.setYrkeskod(YrkesKodEnum.LK);
		return request;
	}

	public static HamtaMeddelandenResponderInterface setupService(String serviceAddress, String logicalAddresss) {
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			URL wsdlUrl = loader
					.getResource("interactions/HamtaMeddelandenInteraction/HamtaMeddelandenInteraction_1.0_RIVTABP20.wsdl");

			HamtaMeddelandenResponderService service = new HamtaMeddelandenResponderService(wsdlUrl);
			HamtaMeddelandenResponderInterface serviceInterface = service.getHamtaMeddelandenResponderPort();

			BindingProvider provider = (BindingProvider) serviceInterface;
			provider.getRequestContext().put("javax.xml.ws.service.endpoint.address", serviceAddress);

			Client client = ClientProxy.getClient(serviceInterface);
			HTTPConduit http = (HTTPConduit) client.getConduit();

			HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
			httpClientPolicy.setConnectionTimeout(36000L);
			httpClientPolicy.setAllowChunking(false);
			httpClientPolicy.setReceiveTimeout(32000L);
			http.setClient(httpClientPolicy);

			if (serviceAddress.startsWith("https")) {
				TLSClientParameters tlsCP = setUpTlsClientParams();
				http.setTlsClientParameters(tlsCP);
			}

			return serviceInterface;
		} catch (Throwable ex) {
			System.out.println("Exception={}" + ex.getMessage());
			throw new RuntimeException(ex);
		}
	}

	private static TLSClientParameters setUpTlsClientParams() throws Exception {

		KeyStore trustStore = KeyStore.getInstance("JKS");
		String trustStoreLoc = "src/main/resources/test-certs/truststore.jks";
		String trustPassword = "password";
		trustStore.load(new FileInputStream(trustStoreLoc), trustPassword.toCharArray());

		String keyPassword = "password";
		KeyStore keyStore = KeyStore.getInstance("consumer");
		String keyStoreLoc = "src/main/resources/test-certs/consumer.jks";
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
