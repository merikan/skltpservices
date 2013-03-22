package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedcaredocumentation.integrationtest;

import static se.skltp.agp.test.producer.TestProducerDb.TEST_RR_ID_ONE_HIT;

import javax.xml.ws.Holder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.riv.clinicalprocess.healthcond.description.getcaredocumentationrequest.v2.GetCareDocumentationResponderInterface;
import se.riv.clinicalprocess.healthcond.description.getcaredocumentationresponder.v2.GetCareDocumentationResponseType;
import se.riv.clinicalprocess.healthcond.description.getcaredocumentationresponder.v2.GetCareDocumentationType;
import se.riv.clinicalprocess.healthcond.description.v2.PatientIdType;
import se.skltp.aggregatingservices.CareDocumentationMuleServer;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;
import se.skltp.agp.test.consumer.AbstractTestConsumer;
import se.skltp.agp.test.consumer.SoapHeaderCxfInterceptor;

public class CareDocumentationTestConsumer extends AbstractTestConsumer<GetCareDocumentationResponderInterface>{

    private static final Logger log = LoggerFactory.getLogger(CareDocumentationTestConsumer.class);

    public static void main(String[] args) {
        log.info("URL: " + CareDocumentationMuleServer.getAddress("SERVICE_INBOUND_URL"));
        String serviceAddress = CareDocumentationMuleServer.getAddress("SERVICE_INBOUND_URL");
        String personnummer = TEST_RR_ID_ONE_HIT;

        CareDocumentationTestConsumer consumer = new CareDocumentationTestConsumer(serviceAddress, SAMPLE_ORIGINAL_CONSUMER_HSAID);
        Holder<GetCareDocumentationResponseType> responseHolder = new Holder<GetCareDocumentationResponseType>();
        Holder<ProcessingStatusType> processingStatusHolder = new Holder<ProcessingStatusType>();
        long now = System.currentTimeMillis();
        consumer.callService("logical-adress", personnummer, processingStatusHolder, responseHolder);
        log.info("Returned #care documentation = " + responseHolder.value.getCareDocumentation().size() + " in " + (System.currentTimeMillis() - now) + " ms.");	
    }

    public CareDocumentationTestConsumer(String serviceAddress, String originalConsumerHsaId) {
        // Setup a web service proxy for communication using HTTPS with Mutual Authentication
        super(GetCareDocumentationResponderInterface.class, serviceAddress, originalConsumerHsaId); 
    }

    public void callService(String logicalAddress, String id, Holder<ProcessingStatusType> processingStatusHolder, Holder<GetCareDocumentationResponseType> responseHolder) {
        log.debug("Calling GetRequestActivities-soap-service with id = {}", id);

        GetCareDocumentationType request = new GetCareDocumentationType();
        PatientIdType patientId = new PatientIdType();
        patientId.setId(id);
        request.setPatientId(patientId);

        GetCareDocumentationResponseType response = _service.getCareDocumentation(logicalAddress, request);
        responseHolder.value = response;

        processingStatusHolder.value = SoapHeaderCxfInterceptor.getLastFoundProcessingStatus();
    }
}