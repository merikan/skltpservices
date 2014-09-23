package se.skltp.aggregatingservices.riv.crm.requeststatus.getrequestactivities;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.Ignore;
import org.soitoolkit.commons.mule.util.ThreadSafeSimpleDateFormat;

import se.riv.crm.requeststatus.getrequestactivitiesresponder.v1.GetRequestActivitiesType;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentResponseType;
import se.skltp.agp.riv.itintegration.engagementindex.v1.EngagementType;
import se.skltp.agp.service.api.QueryObject;
import se.skltp.agp.service.api.RequestListFactory;
import static se.skltp.agp.test.producer.TestProducerDb.*;

public class RequestListFactoryTest {
	
	private static final String RONTGEN = "1";
	private static final String LABB = "2";
	private static final String ALLMAN = "4";
	private static final String FYSIOLOG = "10";
	
	private static final ThreadSafeSimpleDateFormat df = new ThreadSafeSimpleDateFormat("YYYYMMDDhhmmss");

	private RequestListFactory testObject = new RequestListFactoryImpl();
	
	@Test
	@Ignore
	public void testQueryObjectFactory() {
		
		QueryObject qo = null;
		FindContentResponseType src = null;
		testObject.createRequestList(qo, src);
		assertEquals("expected", "actual");
	}
	
	@Test
	public void nullCategoryMeansAllCategoriesAreCorrect(){
		boolean result = new RequestListFactoryImpl().isCorrectCategory(null, RONTGEN);
		assertTrue(result);
	}
	
	@Test
	public void emptyCategoryMeansAllCategoriesAreCorrect(){
		List<String> reqTypeOfRequestList = new ArrayList<String>();
		boolean result = new RequestListFactoryImpl().isCorrectCategory(reqTypeOfRequestList, RONTGEN);
		assertTrue(result);
	}
	
	@Test
	public void whenExactCorrectCategoryReturnTrue(){
		List<String> reqTypeOfRequestList = Arrays.asList(RONTGEN);
		boolean result = new RequestListFactoryImpl().isCorrectCategory(reqTypeOfRequestList, RONTGEN);
		assertTrue(result);
	}
	
	@Test
	public void atLeastOneCorrectCategoryReturnTrue(){
		List<String> reqTypeOfRequestList = Arrays.asList(RONTGEN,LABB,ALLMAN,FYSIOLOG);
		boolean result = new RequestListFactoryImpl().isCorrectCategory(reqTypeOfRequestList, RONTGEN);
		assertTrue(result);
	}
	
	@Test
	public void whenNoExactCorrectCategoryReturnFalse(){
		List<String> reqTypeOfRequestList = Arrays.asList(LABB);
		boolean result = new RequestListFactoryImpl().isCorrectCategory(reqTypeOfRequestList, RONTGEN);
		assertFalse(result);
	}
	
	@Test
	public void whenNoCorrectCategoryInListReturnFalse(){
		List<String> reqTypeOfRequestList = Arrays.asList(LABB,ALLMAN,FYSIOLOG);
		boolean result = new RequestListFactoryImpl().isCorrectCategory(reqTypeOfRequestList, RONTGEN);
		assertFalse(result);
	}
	
	@Test
	public void createRequestList(){
		FindContentResponseType findContentResponse = new FindContentResponseType();
		findContentResponse.getEngagement().add(createEngagement(TEST_LOGICAL_ADDRESS_1, TEST_RR_ID_MANY_HITS_NO_ERRORS));
		findContentResponse.getEngagement().add(createEngagement(TEST_LOGICAL_ADDRESS_2, TEST_RR_ID_MANY_HITS_NO_ERRORS));
		
		GetRequestActivitiesType originalRequest = new GetRequestActivitiesType();
		originalRequest.setSubjectOfCareId(TEST_RR_ID_MANY_HITS_NO_ERRORS);
		originalRequest.getCareUnitId().add("CAREUNITID_1");
		originalRequest.getTypeOfRequest().add(FYSIOLOG);
		
		QueryObject qo = new QueryObject(null, originalRequest);
		
		List<Object[]> requestList = new RequestListFactoryImpl().createRequestList(qo, findContentResponse);
		
		String sourceSystem1 = (String)requestList.get(0)[0];
		String sourceSystem2 = (String)requestList.get(1)[0];
		assertEquals(TEST_LOGICAL_ADDRESS_2, sourceSystem1);
		assertEquals(TEST_LOGICAL_ADDRESS_1, sourceSystem2);
		
		GetRequestActivitiesType request1 = (GetRequestActivitiesType)requestList.get(0)[1];
		GetRequestActivitiesType request2 = (GetRequestActivitiesType)requestList.get(1)[1];
		
		assertOriginalRequestParamsArePropagated(originalRequest, request1);
		assertOriginalRequestParamsArePropagated(originalRequest, request2);
	}

	private void assertOriginalRequestParamsArePropagated(GetRequestActivitiesType originalRequest, GetRequestActivitiesType request) {
		assertEquals(originalRequest.getCareUnitId().get(0), request.getCareUnitId().get(0));
		assertEquals(originalRequest.getFromDate(), request.getFromDate());
		assertEquals(originalRequest.getSubjectOfCareId(), request.getSubjectOfCareId());
		assertEquals(originalRequest.getToDate(), request.getToDate());
		assertEquals(originalRequest.getTypeOfRequest().get(0), request.getTypeOfRequest().get(0));
	}

	private EngagementType createEngagement(String sourceSystem, String rrid) {
		EngagementType engagement = new EngagementType();
		engagement.setLogicalAddress(sourceSystem);
		engagement.setSourceSystem(sourceSystem);
		engagement.setCategorization(FYSIOLOG);
		engagement.setDataController("DATACONTROLLER");
		
		engagement.setMostRecentContent(df.format(new Date()));
		engagement.setUpdateTime(null);
		engagement.setCreationTime(null);
		
		engagement.setOwner("OWNER");
		engagement.setRegisteredResidentIdentification(rrid);
		engagement.setServiceDomain("riv:crm:requeststatus");
		
		
		engagement.setBusinessObjectInstanceIdentifier(null);
		engagement.setClinicalProcessInterestId(null);
		return engagement;
	}	
}
