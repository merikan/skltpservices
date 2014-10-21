package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.actoutcome.getaggregatedlaboratoryorderoutcome.integrationtest;

import static se.skltp.agp.test.producer.TestProducerDb.TEST_RR_ID_ONE_HIT;

import javax.xml.ws.Holder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.riv.clinicalprocess.healthcond.actoutcome.getlaboratoryoutcome.v3.rivtabp21.GetLaboratoryOrderOutcomeResponderInterface;
import se.riv.clinicalprocess.healthcond.actoutcome.getlaboratoryoutcomeresponder.v3.GetLaboratoryOrderOutcomeResponseType;
import se.riv.clinicalprocess.healthcond.actoutcome.getlaboratoryoutcomeresponder.v3.GetLaboratoryOrderOutcomeType;
import se.riv.clinicalprocess.healthcond.actoutcome.v3.PersonIdType;
import se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.actoutcome.getaggregatedlaboratoryorderoutcome.GetAggregatedLaboratoryOrderOutComeMuleServer;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;
import se.skltp.agp.test.consumer.AbstractTestConsumer;
import se.skltp.agp.test.consumer.SoapHeaderCxfInterceptor;

public class GetAggregatedLaboratoryOrderOutComeTestConsumer extends AbstractTestConsumer<GetLaboratoryOrderOutcomeResponderInterface> {

	private static final Logger log = LoggerFactory.getLogger(GetAggregatedLaboratoryOrderOutComeTestConsumer.class);

	public static void main(String[] args) {
		String serviceAddress = GetAggregatedLaboratoryOrderOutComeMuleServer.getAddress("SERVICE_INBOUND_URL");
		String personnummer = TEST_RR_ID_ONE_HIT;

		GetAggregatedLaboratoryOrderOutComeTestConsumer consumer = new GetAggregatedLaboratoryOrderOutComeTestConsumer(serviceAddress, SAMPLE_SENDER_ID, SAMPLE_ORIGINAL_CONSUMER_HSAID);
		Holder<GetLaboratoryOrderOutcomeResponseType> responseHolder = new Holder<GetLaboratoryOrderOutcomeResponseType>();
		Holder<ProcessingStatusType> processingStatusHolder = new Holder<ProcessingStatusType>();

		consumer.callService("logical-adress", personnummer, processingStatusHolder, responseHolder);
		log.info("Returned #timeslots = " + responseHolder.value.getLaboratoryOrderOutcome().size());
	}

	public GetAggregatedLaboratoryOrderOutComeTestConsumer(String serviceAddress, String senderId, String originalConsumerHsaId) {
	    
		// Setup a web service proxy for communication using HTTPS with Mutual Authentication
		super(GetLaboratoryOrderOutcomeResponderInterface.class, serviceAddress, senderId, originalConsumerHsaId);
	}

	public void callService(String logicalAddress, String registeredResidentId, Holder<ProcessingStatusType> processingStatusHolder, Holder<GetLaboratoryOrderOutcomeResponseType> responseHolder) {

		log.debug("Calling GetRequestActivities-soap-service with Registered Resident Id = {}", registeredResidentId);
		
		GetLaboratoryOrderOutcomeType request = new GetLaboratoryOrderOutcomeType();

		// TODO: CHANGE GENERATED CODE - START
		PersonIdType personIdType = new PersonIdType();
		personIdType.setId(registeredResidentId);
		personIdType.setType("1.2.752.129.2.1.3.1");//Fältregler GetLaboratoryOrderOutcome från TKB
		request.setPatientId(personIdType);
		// TODO: CHANGE GENERATED CODE - END


		GetLaboratoryOrderOutcomeResponseType response = _service.getLaboratoryOrderOutcome(logicalAddress, request);
		responseHolder.value = response;
		
		processingStatusHolder.value = SoapHeaderCxfInterceptor.getLastFoundProcessingStatus();
	}
}