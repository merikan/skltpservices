package se.skl.takecare.takecareintegrationcomponent.getalltimetypes;

import static se.skl.takecare.takecareintegrationcomponent.TakeCareIntegrationComponentMuleServer.getAddress;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.refapps.sd.sample.schema.v1.Sample;
import org.soitoolkit.refapps.sd.sample.schema.v1.SampleResponse;
import org.soitoolkit.refapps.sd.sample.wsdl.v1.Fault;
import org.soitoolkit.refapps.sd.sample.wsdl.v1.SampleInterface;
import org.soitoolkit.refapps.sd.sample.wsdl.v1.SampleService;

public class GetAllTimeTypesTestConsumer {

	private static final Logger log = LoggerFactory.getLogger(GetAllTimeTypesTestConsumer.class);

	private SampleInterface _service = null;

	public static void main(String[] args) throws Fault {
		String serviceAddress = getAddress("GETALLTIMETYPES_INBOUND_URL");
		String personnummer = "1234567890";

		GetAllTimeTypesTestConsumer consumer = new GetAllTimeTypesTestConsumer(serviceAddress);
		SampleResponse response = consumer.callService(personnummer);
		log.info("Returned value = " + response.getValue());
	}
	
	public GetAllTimeTypesTestConsumer(String serviceAddress) {
        try {
            URL url =  new URL(serviceAddress + "?wsdl");
            _service = new SampleService(url).getSamplePort();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed URL Exception: " + e.getMessage());
        }
	}
	
	public SampleResponse callService(String id) throws Fault {
		log.debug("Calling sample-soap-service with id = {}", id);
		Sample request = new Sample();
		request.setId(id);
		return _service.sample(request);
	}
}