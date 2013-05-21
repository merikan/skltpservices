package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistory.integrationtest;

import static se.skltp.agp.test.producer.TestProducerDb.TEST_RR_ID_ONE_HIT;

import javax.xml.ws.Holder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistory.v2.rivtabp21.GetMaternityMedicalHistoryResponderInterface;
import se.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistoryresponder.v2.GetMaternityMedicalHistoryResponseType;
import se.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistoryresponder.v2.GetMaternityMedicalHistoryType;
import se.riv.clinicalprocess.healthcond.actoutcome.v2.PatientIdType;
import se.skltp.aggregatingservices.MaternityMedicalHistoryMuleServer;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;
import se.skltp.agp.test.consumer.AbstractTestConsumer;
import se.skltp.agp.test.consumer.SoapHeaderCxfInterceptor;

public class MaternityMedicalHistoryTestConsumer extends AbstractTestConsumer<GetMaternityMedicalHistoryResponderInterface>{

    private static final Logger log = LoggerFactory.getLogger(MaternityMedicalHistoryTestConsumer.class);

    public static void main(String[] args) {
        log.info("URL: " + MaternityMedicalHistoryMuleServer.getAddress("SERVICE_INBOUND_URL"));
        String serviceAddress = MaternityMedicalHistoryMuleServer.getAddress("SERVICE_INBOUND_URL");
        String personnummer = TEST_RR_ID_ONE_HIT;

        MaternityMedicalHistoryTestConsumer consumer = new MaternityMedicalHistoryTestConsumer(serviceAddress, SAMPLE_ORIGINAL_CONSUMER_HSAID);
        Holder<GetMaternityMedicalHistoryResponseType> responseHolder = new Holder<GetMaternityMedicalHistoryResponseType>();
        Holder<ProcessingStatusType> processingStatusHolder = new Holder<ProcessingStatusType>();
        long now = System.currentTimeMillis();
        consumer.callService("logical-adress", personnummer, processingStatusHolder, responseHolder);
        log.info("Returned #maternity medical record= " + responseHolder.value.getMaternityMedicalRecord().size() + " in " + (System.currentTimeMillis() - now) + " ms.");	
    }

    public MaternityMedicalHistoryTestConsumer(String serviceAddress, String originalConsumerHsaId) {
        // Setup a web service proxy for communication using HTTPS with Mutual Authentication
        super(GetMaternityMedicalHistoryResponderInterface.class, serviceAddress, originalConsumerHsaId); 
    }

    public void callService(String logicalAddress, String id, Holder<ProcessingStatusType> processingStatusHolder, Holder<GetMaternityMedicalHistoryResponseType> responseHolder) {
        log.debug("Calling GetAggregatedMaternityMedicalHistory-soap-service with id = {}", id);

        GetMaternityMedicalHistoryType request = new GetMaternityMedicalHistoryType();
        PatientIdType patientId = new PatientIdType();
        patientId.setId(id);
        request.setPatientId(patientId);

        GetMaternityMedicalHistoryResponseType response = _service.getMaternityMedicalHistory(logicalAddress, request);
        responseHolder.value = response;

        processingStatusHolder.value = SoapHeaderCxfInterceptor.getLastFoundProcessingStatus();
    }
}