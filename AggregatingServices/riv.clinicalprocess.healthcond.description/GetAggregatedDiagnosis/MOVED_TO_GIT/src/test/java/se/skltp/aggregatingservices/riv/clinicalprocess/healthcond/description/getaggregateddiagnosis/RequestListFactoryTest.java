package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregateddiagnosis;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import se.skltp.agp.service.api.RequestListFactory;

public class RequestListFactoryTest {
	
	private RequestListFactory testObject = new RequestListFactoryImpl();
	
	@Test
	public void isPartOf(){

		assertTrue(new RequestListFactoryImpl().isPartOf("UNIT2", "UNIT2"));
		
		assertTrue(new RequestListFactoryImpl().isPartOf(null, "UNIT2"));
		
		assertTrue(new RequestListFactoryImpl().isPartOf("", "UNIT2"));
	}
	
	@Test
	public void isNotPartOf(){
		assertFalse(new RequestListFactoryImpl().isPartOf("UNIT2", "UNIT3"));
		assertFalse(new RequestListFactoryImpl().isPartOf("UNIT2", null));
	}
	
}
