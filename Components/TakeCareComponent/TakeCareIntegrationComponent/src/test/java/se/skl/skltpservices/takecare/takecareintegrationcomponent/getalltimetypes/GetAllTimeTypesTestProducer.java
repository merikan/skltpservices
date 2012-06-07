package se.skl.takecare.takecareintegrationcomponent.getalltimetypes;

import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.soitoolkit.commons.mule.util.RecursiveResourceBundle;



import javax.jws.WebService;

import org.soitoolkit.refapps.sd.sample.schema.v1.Sample;
import org.soitoolkit.refapps.sd.sample.schema.v1.SampleResponse;
import org.soitoolkit.refapps.sd.sample.wsdl.v1.Fault;
import org.soitoolkit.refapps.sd.sample.wsdl.v1.SampleInterface;

@WebService(serviceName = "sampleService", portName = "samplePort", targetNamespace = "urn:org.soitoolkit.refapps.sd.sample.wsdl:v1", name = "sampleService")
public class GetAllTimeTypesTestProducer implements SampleInterface {

	public static final String TEST_ID_OK               = "1234567890";
	public static final String TEST_ID_FAULT_INVALID_ID = "-1";
	public static final String TEST_ID_FAULT_TIMEOUT    = "0";
	
	private static final Logger log = LoggerFactory.getLogger(GetAllTimeTypesTestProducer.class);
    private static final RecursiveResourceBundle rb = new RecursiveResourceBundle("TakeCareIntegrationComponent-config");
	private static final long SERVICE_TIMOUT_MS = Long.parseLong(rb.getString("SERVICE_TIMEOUT_MS"));

	public SampleResponse sample(Sample request) throws Fault {

		log.info("GetAllTimeTypesTestProducer received the request: {}", request);

		String id = request.getId();

		// Return an error-message if invalid id
		if (TEST_ID_FAULT_INVALID_ID.equals(id)) {
			throw new Fault("Invalid Id: " + id);
		}

		// Force a timeout if zero Id
        if (TEST_ID_FAULT_TIMEOUT.equals(id)) {
	    	try {
				Thread.sleep(SERVICE_TIMOUT_MS + 1000);
			} catch (InterruptedException e) {}
        }

        // Produce the response
		SampleResponse response = new SampleResponse();
		response.setValue("Value" + id);
		return response;
	}
}


