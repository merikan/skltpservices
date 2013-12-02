package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedcaredocumentation.integrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static se.skltp.aggregatingservices.CareDocumentationMuleServer.getAddress;
import static se.skltp.agp.riv.interoperability.headers.v1.CausingAgentEnum.VIRTUALIZATION_PLATFORM;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_BO_ID_MANY_HITS_1;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_BO_ID_MANY_HITS_2;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_BO_ID_MANY_HITS_3;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_BO_ID_ONE_HIT;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_LOGICAL_ADDRESS_1;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_LOGICAL_ADDRESS_2;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_LOGICAL_ADDRESS_3;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_RR_ID_FAULT_INVALID_ID;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_RR_ID_MANY_HITS;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_RR_ID_ONE_HIT;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_RR_ID_ZERO_HITS;

import java.util.List;

import javax.xml.ws.Holder;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.util.RecursiveResourceBundle;

import se.riv.clinicalprocess.healthcond.description.getcaredocumentationresponder.v2.GetCareDocumentationResponseType;
import se.riv.clinicalprocess.healthcond.description.v2.CareDocumentationType;
import se.riv.clinicalprocess.healthcond.description.v2.DatePeriodType;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusRecordType;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;
import se.skltp.agp.test.consumer.AbstractAggregateIntegrationTest;
import se.skltp.agp.test.consumer.ExpectedTestData;
import se.skltp.agp.test.producer.EngagemangsindexTestProducerLogger;
import se.skltp.agp.test.producer.TestProducerLogger;

public class CareDocumentationIntegrationTest extends AbstractAggregateIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(CareDocumentationIntegrationTest.class);

    private static final RecursiveResourceBundle rb = new RecursiveResourceBundle("GetAggregatedCareDocumentation-config");
    private static final String SKLTP_HSA_ID = rb.getString("SKLTP_HSA_ID");

    private static final String LOGICAL_ADDRESS = "logical-address";
    private static final String EXPECTED_ERR_TIMEOUT_MSG = "Read timed out";
    private static final String EXPECTED_ERR_INVALID_ID_MSG = "Invalid Id: " + TEST_RR_ID_FAULT_INVALID_ID;;
    private static final String DEFAULT_SERVICE_ADDRESS = getAddress("SERVICE_INBOUND_URL");

    protected String getConfigResources() {
        return 
                "soitoolkit-mule-jms-connector-activemq-embedded.xml," + 
                "GetAggregatedCareDocumentation-common.xml," +
                //			"aggregating-services-common.xml," + 
                //			"aggregating-service.xml," +
                "teststub-services/engagemangsindex-teststub-service.xml," + 
                "teststub-services/service-producer-teststub-service.xml";
    }

    /**
     * Perform a test that is expected to return zero hits
     */
    @Test
    public void test_ok_zero_hits() {
        doTest(TEST_RR_ID_ZERO_HITS, 0);		
    }

    /**
     * Perform a test that is expected to return one hit with data from one source system
     */
    @Test
    public void test_ok_one_hit() {

        List<ProcessingStatusRecordType> statusList = doTest(TEST_RR_ID_ONE_HIT, 1, new ExpectedTestData(TEST_BO_ID_ONE_HIT, TEST_LOGICAL_ADDRESS_1));

        assertProcessingStatusDataFromSource(statusList.get(0), TEST_LOGICAL_ADDRESS_1);
    }

    /**
     * Perform a test that is expected to return three hit with data from two source systems and one source system that cause a timeout
     */
    @Test
    public void test_ok_many_hits_with_partial_timeout() {

        // Setup call and verify the response, expect one booking from source #1, two from source #2 and a timeout from source #3
        List<ProcessingStatusRecordType> statusList = doTest(TEST_RR_ID_MANY_HITS, 3, 
                new ExpectedTestData(TEST_BO_ID_MANY_HITS_1, TEST_LOGICAL_ADDRESS_1),
                new ExpectedTestData(TEST_BO_ID_MANY_HITS_2, TEST_LOGICAL_ADDRESS_2),
                new ExpectedTestData(TEST_BO_ID_MANY_HITS_3, TEST_LOGICAL_ADDRESS_2));

        // Verify the Processing Status, expect ok from source system #1 and #2 but a timeout from #3
        assertProcessingStatusDataFromSource(statusList.get(0), TEST_LOGICAL_ADDRESS_1);
        assertProcessingStatusDataFromSource(statusList.get(1), TEST_LOGICAL_ADDRESS_2);
        assertProcessingStatusNoDataSynchFailed(statusList.get(2), TEST_LOGICAL_ADDRESS_3, VIRTUALIZATION_PLATFORM, EXPECTED_ERR_TIMEOUT_MSG);
    }

    /**
     * Perform a test that is expected to casue the source system to fail with its processing
     */
    @Test
    public void test_fault_invalidInput() throws Exception {

        List<ProcessingStatusRecordType> statusList = doTest(TEST_RR_ID_FAULT_INVALID_ID, 1);

        // Verify the Processing Status, expect a processing failure from the source system
        assertProcessingStatusNoDataSynchFailed(statusList.get(0), TEST_LOGICAL_ADDRESS_1, VIRTUALIZATION_PLATFORM, EXPECTED_ERR_INVALID_ID_MSG);
    }

    @Ignore @Test
    public void test_ok_caching() {
        long   expectedProcessingTime = getTestDb().getProcessingTime(TEST_LOGICAL_ADDRESS_1);

        long ts = System.currentTimeMillis();
        
        List<ProcessingStatusRecordType> statusList = doTest(TEST_RR_ID_ONE_HIT, 1, "consumer1", null, new ExpectedTestData(TEST_BO_ID_ONE_HIT, TEST_LOGICAL_ADDRESS_1));
        ts = System.currentTimeMillis() - ts;
        assertProcessingStatusDataFromSource(statusList.get(0), TEST_LOGICAL_ADDRESS_1);
        assertTrue("Expected a long processing time (i.e. a non cached response)", ts > expectedProcessingTime);
        
        ts = System.currentTimeMillis();
        statusList = doTest(TEST_RR_ID_ONE_HIT, 1, "consumer1", null, new ExpectedTestData(TEST_BO_ID_ONE_HIT, TEST_LOGICAL_ADDRESS_1));
        ts = System.currentTimeMillis() - ts;
        assertProcessingStatusDataFromCache(statusList.get(0), TEST_LOGICAL_ADDRESS_1);
        assertTrue("Expected a short processing time (i.e. a cached response)", ts < expectedProcessingTime);
        
        ts = System.currentTimeMillis();
        statusList = doTest(TEST_RR_ID_ONE_HIT, 1, "consumer2", null, new ExpectedTestData(TEST_BO_ID_ONE_HIT, TEST_LOGICAL_ADDRESS_1));
        ts = System.currentTimeMillis() - ts;
        assertProcessingStatusDataFromSource(statusList.get(0), TEST_LOGICAL_ADDRESS_1);
        assertTrue("Expected a long processing time (i.e. a non cached response)", ts > expectedProcessingTime);
    }
    

    @Ignore @Test
    public void test_ok_caching_many_hits() {
        long   expectedProcessingTime = getTestDb().getProcessingTime(TEST_LOGICAL_ADDRESS_1);

        long ts = System.currentTimeMillis();
        
        
        List<ProcessingStatusRecordType> statusList = doTest(TEST_RR_ID_MANY_HITS, 3, 
                new ExpectedTestData(TEST_BO_ID_MANY_HITS_1, TEST_LOGICAL_ADDRESS_1),
                new ExpectedTestData(TEST_BO_ID_MANY_HITS_2, TEST_LOGICAL_ADDRESS_2),
                new ExpectedTestData(TEST_BO_ID_MANY_HITS_3, TEST_LOGICAL_ADDRESS_2));

        // Verify the Processing Status, expect ok from source system #1 and #2 but a timeout from #3
        assertProcessingStatusDataFromSource(statusList.get(0), TEST_LOGICAL_ADDRESS_1);
        assertProcessingStatusDataFromSource(statusList.get(1), TEST_LOGICAL_ADDRESS_2);
        assertProcessingStatusNoDataSynchFailed(statusList.get(2), TEST_LOGICAL_ADDRESS_3, VIRTUALIZATION_PLATFORM, EXPECTED_ERR_TIMEOUT_MSG);
        ts = System.currentTimeMillis() - ts;
        assertTrue("Expected a long processing time (i.e. a non cached response)", ts > expectedProcessingTime);
        
        ts = System.currentTimeMillis();
        statusList = doTest(TEST_RR_ID_MANY_HITS, 3, 
                new ExpectedTestData(TEST_BO_ID_MANY_HITS_1, TEST_LOGICAL_ADDRESS_1),
                new ExpectedTestData(TEST_BO_ID_MANY_HITS_2, TEST_LOGICAL_ADDRESS_2),
                new ExpectedTestData(TEST_BO_ID_MANY_HITS_3, TEST_LOGICAL_ADDRESS_2));
        ts = System.currentTimeMillis() - ts;
        assertProcessingStatusDataFromCache(statusList.get(0), TEST_LOGICAL_ADDRESS_1);
        assertProcessingStatusDataFromCache(statusList.get(1), TEST_LOGICAL_ADDRESS_2);
        assertProcessingStatusNoDataSynchFailed(statusList.get(2), TEST_LOGICAL_ADDRESS_3, VIRTUALIZATION_PLATFORM, EXPECTED_ERR_TIMEOUT_MSG);
        assertTrue("Expected a short processing time (i.e. a cached response)", ts < expectedProcessingTime);
    }

    @Ignore @Test
    public void test_non_cachable() {
        DatePeriodType datePeriod = new DatePeriodType();
        datePeriod.setStart("20110101");
        datePeriod.setEnd("20130201");
        
        List<ProcessingStatusRecordType> statusList = doTest(TEST_RR_ID_ONE_HIT, 1, "consumer1", datePeriod, new ExpectedTestData(TEST_BO_ID_ONE_HIT, TEST_LOGICAL_ADDRESS_1));
        assertProcessingStatusDataFromSource(statusList.get(0), TEST_LOGICAL_ADDRESS_1);
        
        statusList = doTest(TEST_RR_ID_ONE_HIT, 1, "consumer1", datePeriod, new ExpectedTestData(TEST_BO_ID_ONE_HIT, TEST_LOGICAL_ADDRESS_1));
        assertProcessingStatusDataFromSource(statusList.get(0), TEST_LOGICAL_ADDRESS_1);
    }

    /**
     * Helper method for performing a call to the aggregating service and perform some common validations of the result
     * 
     * @param registeredResidentId
     * @param expectedProcessingStatusSize
     * @param testData
     * @return
     */
    private List<ProcessingStatusRecordType> doTest(String registeredResidentId, int expectedProcessingStatusSize, ExpectedTestData... testData) {
        return doTest(registeredResidentId, expectedProcessingStatusSize, CareDocumentationTestConsumer.SAMPLE_ORIGINAL_CONSUMER_HSAID, null, testData);
    }
    
    /**
     * Helper method for performing a call to the aggregating service and perform some common validations of the result
     * 
     * @param registeredResidentId
     * @param expectedProcessingStatusSize
     * @param testData
     * @return
     */
    private List<ProcessingStatusRecordType> doTest(String registeredResidentId, int expectedProcessingStatusSize, String serviceConsumer, DatePeriodType datePeriod, ExpectedTestData... testData) {

        // Setup and perform the call to the web service
        CareDocumentationTestConsumer consumer = new CareDocumentationTestConsumer(DEFAULT_SERVICE_ADDRESS, serviceConsumer);
        Holder<GetCareDocumentationResponseType> responseHolder = new Holder<GetCareDocumentationResponseType>();
        Holder<ProcessingStatusType> processingStatusHolder = new Holder<ProcessingStatusType>();
        consumer.callService(LOGICAL_ADDRESS, registeredResidentId, datePeriod, processingStatusHolder, responseHolder);

        // Verify the response size and content
        GetCareDocumentationResponseType response = responseHolder.value;
        int expextedResponseSize = testData.length;
        assertEquals(expextedResponseSize, response.getCareDocumentation().size());

        for (int i = 0; i < testData.length; i++) {
            CareDocumentationType responseElement = response.getCareDocumentation().get(i);
            assertEquals(registeredResidentId, responseElement.getCareDocumentationHeader().getPatientId().getId());		
            assertEquals(testData[i].getExpectedBusinessObjectId(), responseElement.getCareDocumentationHeader().getDocumentId());		
            assertEquals(testData[i].getExpectedLogicalAddress(), responseElement.getCareDocumentationHeader().getAccountableHealthcareProfessional().getHealthcareProfessionalCareUnitHSAId());
        }

        // Verify the size of the processing status and return it for further analysis
        ProcessingStatusType statusList = processingStatusHolder.value;
        assertEquals(expectedProcessingStatusSize, statusList.getProcessingStatusList().size());
        
        // Verify that correct "x-rivta-original-serviceconsumer-hsaid" http header was passed to the engagement index
        assertEquals(SKLTP_HSA_ID, EngagemangsindexTestProducerLogger.getLastOriginalConsumer());
        
        // Verify that correct "x-rivta-original-serviceconsumer-hsaid" http header was passed to the service producer,
        // given that a service producer was called
        if (expectedProcessingStatusSize > 0) {
                assertEquals(serviceConsumer, TestProducerLogger.getLastOriginalConsumer());
        }
        
        return statusList.getProcessingStatusList();
    }

}