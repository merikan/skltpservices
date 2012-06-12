package se.skl.skltpservices.takecare.takecareintegrationcomponent.getalltimetypes;

import static se.skl.skltpservices.takecare.takecareintegrationcomponent.TakeCareIntegrationComponentMuleServer.getAddress;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.refapps.sd.sample.wsdl.v1.Fault;
import org.w3.wsaddressing10.AttributedURIType;

import se.riv.crm.scheduling.getalltimetypes.v1.GetAllTimeTypesResponderInterface;
import se.riv.crm.scheduling.getalltimetypes.v1.GetAllTimeTypesResponderService;
import se.riv.crm.scheduling.getalltimetypes.v1.GetAllTimeTypesResponseType;
import se.riv.crm.scheduling.getalltimetypes.v1.GetAllTimeTypesType;

public class GetAllTimeTypesTestConsumer {

	private static final Logger log = LoggerFactory.getLogger(GetAllTimeTypesTestConsumer.class);

	private GetAllTimeTypesResponderInterface _service = null;

	public static void main(String[] args) throws Fault {
		String serviceAddress = getAddress("GETALLTIMETYPES_INBOUND_URL");
		String healthcareFacility = "HSA-VKK123";

		GetAllTimeTypesTestConsumer consumer = new GetAllTimeTypesTestConsumer(serviceAddress);
		GetAllTimeTypesResponseType response = consumer.callService(healthcareFacility);
		log.info("Returned value = " + response.getListOfTimeTypes());
	}

	public GetAllTimeTypesTestConsumer(String serviceAddress) {
		try {
			initHttpsCommunication();
			URL url = new URL(serviceAddress + "?wsdl");
			_service = new GetAllTimeTypesResponderService(url).getGetAllTimeTypesResponderPort();
		} catch (MalformedURLException e) {
			throw new RuntimeException("Malformed URL Exception: " + e.getMessage());
		}
	}

	public GetAllTimeTypesResponseType callService(String healthcareFacility) throws Fault {
		log.debug("Calling sample-soap-service with healthcareFacility", healthcareFacility);

		GetAllTimeTypesType request = new GetAllTimeTypesType();
		request.setHealthcareFacility(healthcareFacility);
		String senderId = "1";

		return _service.getAllTimeTypes(new AttributedURIType(), request);
	}

	private static void initHttpsCommunication() {
		System.setProperty("javax.net.ssl.keyStore", "src/test/resources/test-certs/consumer.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "password");
		System.setProperty("javax.net.ssl.trustStore", "src/test/resources/test-certs/truststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "password");

		SpringBusFactory bf = new SpringBusFactory();
		URL busFile = GetAllTimeTypesTestConsumer.class.getClassLoader().getResource(
				"TakeCareTestConsumer-cxf-config.xml");
		Bus bus = bf.createBus(busFile.toString());
		SpringBusFactory.setDefaultBus(bus);
	}

}