package se.skltp.aggregatingservices.riv.crm.requeststatus.getrequestactivities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Ignore;

import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentResponseType;
import se.skltp.agp.service.api.QueryObject;
import se.skltp.agp.service.api.RequestListFactory;

public class RequestListFactoryTest {

	private RequestListFactory testObject = new RequestListFactoryImpl();
	
	@Test
	@Ignore
	public void testQueryObjectFactory() {
		
		QueryObject qo = null;
		FindContentResponseType src = null;
		testObject.createRequestList(qo, src);
		assertEquals("expected", "actual");
	}
}
