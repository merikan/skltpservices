package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.basic.getaggregatedobservation.integrationtest;

import static se.skltp.agp.test.producer.TestProducerDb.TEST_RR_ID_ONE_HIT;

import javax.xml.ws.Holder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.riv.clinicalprocess.healthcond.basic.getobservation.v1.rivtabp21.GetObservationResponderInterface;
import se.riv.clinicalprocess.healthcond.basic.getobservationresponder.v1.GetObservationResponseType;
import se.riv.clinicalprocess.healthcond.basic.getobservationresponder.v1.GetObservationType;
import se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.basic.getaggregatedobservation.GetAggregatedObservationMuleServer;
import se.skltp.agp.test.consumer.AbstractTestConsumer;
import se.skltp.agp.test.consumer.SoapHeaderCxfInterceptor;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;

public class GetAggregatedObservationTestConsumer extends AbstractTestConsumer<GetObservationResponderInterface> {

	private static final Logger log = LoggerFactory.getLogger(GetAggregatedObservationTestConsumer.class);

	public static void main(String[] args) {
		String serviceAddress = GetAggregatedObservationMuleServer.getAddress("SERVICE_INBOUND_URL");
		String personnummer = TEST_RR_ID_ONE_HIT;

		GetAggregatedObservationTestConsumer consumer = new GetAggregatedObservationTestConsumer(serviceAddress, SAMPLE_SENDER_ID, SAMPLE_ORIGINAL_CONSUMER_HSAID);
		Holder<GetObservationResponseType> responseHolder = new Holder<GetObservationResponseType>();
		Holder<ProcessingStatusType> processingStatusHolder = new Holder<ProcessingStatusType>();

		consumer.callService("logical-adress", personnummer, processingStatusHolder, responseHolder);


        // TODO: CHANGE GENERATED SAMPLE CODE - START
        if (1==1) throw new UnsupportedOperationException("Not yet implemented");
        /*

		log.info("Returned #timeslots = " + responseHolder.value.getRequestActivity().size());

		*/
        // TODO: CHANGE GENERATED SAMPLE CODE - END

	}

	public GetAggregatedObservationTestConsumer(String serviceAddress, String senderId, String originalConsumerHsaId) {
	    
		// Setup a web service proxy for communication using HTTPS with Mutual Authentication
		super(GetObservationResponderInterface.class, serviceAddress, senderId, originalConsumerHsaId);
	}

	public void callService(String logicalAddress, String registeredResidentId, Holder<ProcessingStatusType> processingStatusHolder, Holder<GetObservationResponseType> responseHolder) {

		log.debug("Calling GetObservation-soap-service with Registered Resident Id = {}", registeredResidentId);
		
		GetObservationType request = new GetObservationType();


        // TODO: CHANGE GENERATED SAMPLE CODE - START
        if (1==1) throw new UnsupportedOperationException("Not yet implemented");
        /*

		request.setSubjectOfCareId(registeredResidentId);

        */
        // TODO: CHANGE GENERATED SAMPLE CODE - END

		GetObservationResponseType response = _service.getObservation(logicalAddress, request);
		responseHolder.value = response;
		
		processingStatusHolder.value = SoapHeaderCxfInterceptor.getLastFoundProcessingStatus();
	}
}