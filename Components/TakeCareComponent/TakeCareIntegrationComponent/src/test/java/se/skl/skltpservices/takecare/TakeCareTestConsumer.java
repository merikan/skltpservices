package se.skl.skltpservices.takecare;

import java.net.URL;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBusFactory;

import se.skl.skltpservices.takecare.takecareintegrationcomponent.getalltimetypes.GetAllTimeTypesTestConsumer;

public class TakeCareTestConsumer {

	protected static void initHttpsCommunication() {
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
