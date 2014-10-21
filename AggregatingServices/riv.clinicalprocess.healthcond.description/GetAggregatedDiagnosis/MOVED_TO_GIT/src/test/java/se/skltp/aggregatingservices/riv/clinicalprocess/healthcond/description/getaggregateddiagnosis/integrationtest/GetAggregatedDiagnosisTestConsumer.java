package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregateddiagnosis.integrationtest;

import static se.skltp.agp.test.producer.TestProducerDb.TEST_RR_ID_ONE_HIT;

import javax.xml.ws.Holder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import riv.clinicalprocess.healthcond.description._2.PersonIdType;
import se.riv.clinicalprocess.healthcond.description.getdiagnosis.v2.GetDiagnosisResponderInterface;
import se.riv.clinicalprocess.healthcond.description.getdiagnosisresponder.v2.GetDiagnosisResponseType;
import se.riv.clinicalprocess.healthcond.description.getdiagnosisresponder.v2.GetDiagnosisType;
import se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregateddiagnosis.GetAggregatedDiagnosisMuleServer;
import se.skltp.agp.test.consumer.AbstractTestConsumer;
import se.skltp.agp.test.consumer.SoapHeaderCxfInterceptor;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;

public class GetAggregatedDiagnosisTestConsumer extends AbstractTestConsumer<GetDiagnosisResponderInterface> {

	private static final Logger log = LoggerFactory.getLogger(GetAggregatedDiagnosisTestConsumer.class);

	public static void main(String[] args) {
		String serviceAddress = GetAggregatedDiagnosisMuleServer.getAddress("SERVICE_INBOUND_URL");
		String personnummer = TEST_RR_ID_ONE_HIT;

		GetAggregatedDiagnosisTestConsumer consumer = new GetAggregatedDiagnosisTestConsumer(serviceAddress, SAMPLE_SENDER_ID, SAMPLE_ORIGINAL_CONSUMER_HSAID);
		Holder<GetDiagnosisResponseType> responseHolder = new Holder<GetDiagnosisResponseType>();
		Holder<ProcessingStatusType> processingStatusHolder = new Holder<ProcessingStatusType>();

		consumer.callService("logical-adress", personnummer, processingStatusHolder, responseHolder);
		log.info("Returned #timeslots = " + responseHolder.value.getDiagnosis().size());
	}

	public GetAggregatedDiagnosisTestConsumer(String serviceAddress, String senderId, String originalConsumerHsaId) {
	    
		// Setup a web service proxy for communication using HTTPS with Mutual Authentication
		super(GetDiagnosisResponderInterface.class, serviceAddress, senderId, originalConsumerHsaId);
	}

	public void callService(String logicalAddress, String registeredResidentId, Holder<ProcessingStatusType> processingStatusHolder, Holder<GetDiagnosisResponseType> responseHolder) {

		log.debug("Calling GetRequestActivities-soap-service with Registered Resident Id = {}", registeredResidentId);
		
		GetDiagnosisType request = new GetDiagnosisType();

		// TODO: CHANGE GENERATED CODE - START
		PersonIdType personIdType = new PersonIdType();
		personIdType.setType("1.2.752.129.2.1.3.1"); //Fältregler GetDiagnosis från TKB
		personIdType.setId(registeredResidentId);
		
		request.setPatientId(personIdType);
		
		// TODO: CHANGE GENERATED CODE - END


		GetDiagnosisResponseType response = _service.getDiagnosis(logicalAddress, request);
		responseHolder.value = response;
		
		processingStatusHolder.value = SoapHeaderCxfInterceptor.getLastFoundProcessingStatus();
	}
}