package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.basic.getaggregatedobservation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import se.riv.clinicalprocess.healthcond.basic.getobservationresponder.v1.GetObservationResponseType;
import se.riv.clinicalprocess.healthcond.basic.v1.IIType;
import se.riv.clinicalprocess.healthcond.basic.v1.ObservationType;
import se.riv.clinicalprocess.healthcond.basic.v1.PatientType;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentType;
import se.skltp.agp.service.api.QueryObject;
import se.skltp.agp.service.api.ResponseListFactory;
import se.skltp.agp.test.producer.TestProducerDb;


public class ResponseListFactoryTest {

	private ResponseListFactory testObject = new ResponseListFactoryImpl();
	
	@Test
	public void testQueryObjectFactory() throws Exception{
		
		QueryObject queryObject = createQueryObject(TestProducerDb.TEST_RR_ID_MANY_HITS);
		List<Object> aggregatedResponseList = createAggregatedResponseList(TestProducerDb.TEST_RR_ID_MANY_HITS);
		
		String actualXml = testObject.getXmlFromAggregatedResponse(queryObject, aggregatedResponseList);
		String expectedXml = getFile("testfiles/responseListFactoryTest.xml");
		
		assertEquals(expectedXml, actualXml);
	}

	private List<Object> createAggregatedResponseList(String rrId) {
		List<Object> aggregatedResponseList = new ArrayList<Object>();
		aggregatedResponseList.add(createResponseType(rrId));
		aggregatedResponseList.add(createResponseType(rrId));
		return aggregatedResponseList;
	}

	private QueryObject createQueryObject(String rrID) {
		FindContentType findContentType = new FindContentType();
		findContentType.setRegisteredResidentIdentification(rrID);
		QueryObject queryObject = new QueryObject(findContentType, new Object());
		return queryObject;
	}

	private Object createResponseType(String rrId) {
		GetObservationResponseType responseType = new GetObservationResponseType();
		responseType.getObservation().add(createObservation(rrId));
		return responseType;
	}

	private ObservationType createObservation(String rrId) {
		ObservationType observation = new ObservationType();
		observation.setPatient(createPatient(rrId));
		return observation;
	}

	private PatientType createPatient(String rrId) {
		PatientType patient = new PatientType();
		patient.setId(createID(rrId));
		return patient;
	}

	private IIType createID(String rrId) {
		IIType id = new IIType();
		id.setExtension(rrId);
		id.setRoot("1.2.752.129.2.1.3.1");
		return id;
	}
	
	private String getFile(String fileName) throws Exception{
		ClassLoader classLoader = getClass().getClassLoader();
		return IOUtils.toString(classLoader.getResourceAsStream(fileName));  	 
	}
}
