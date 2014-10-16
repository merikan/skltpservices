package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.basic.getaggregatedobservation.integrationtest;

import static se.skltp.agp.test.producer.TestProducerDb.TEST_RR_ID_ONE_HIT;

import javax.xml.ws.Holder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.riv.clinicalprocess.healthcond.basic.getobservation.v1.rivtabp21.GetObservationResponderInterface;
import se.riv.clinicalprocess.healthcond.basic.getobservationresponder.v1.GetObservationResponseType;
import se.riv.clinicalprocess.healthcond.basic.getobservationresponder.v1.GetObservationType;
import se.riv.clinicalprocess.healthcond.basic.v1.CVType;
import se.riv.clinicalprocess.healthcond.basic.v1.IIType;
import se.riv.clinicalprocess.healthcond.basic.v1.TimePeriodType;
import se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.basic.getaggregatedobservation.GetAggregatedObservationMuleServer;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;
import se.skltp.agp.test.consumer.AbstractTestConsumer;
import se.skltp.agp.test.consumer.SoapHeaderCxfInterceptor;

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
		log.info("Returned #observations = " + responseHolder.value.getObservation().size());
        // TODO: CHANGE GENERATED SAMPLE CODE - END

	}

	public GetAggregatedObservationTestConsumer(String serviceAddress, String senderId, String originalConsumerHsaId) {
	    
		// Setup a web service proxy for communication using HTTPS with Mutual Authentication
		super(GetObservationResponderInterface.class, serviceAddress, senderId, originalConsumerHsaId);
	}
	
	public void callService(String logicalAddress, String registeredResidentId, Holder<ProcessingStatusType> processingStatusHolder, Holder<GetObservationResponseType> responseHolder){
		callService(logicalAddress, registeredResidentId, null, null, processingStatusHolder, responseHolder);
	}

	public void callService(String logicalAddress, String registeredResidentId, String starTime, String endTime, Holder<ProcessingStatusType> processingStatusHolder, Holder<GetObservationResponseType> responseHolder) {

		log.debug("Calling GetObservation-soap-service with Registered Resident Id = {}", registeredResidentId);
		
		GetObservationType request = new GetObservationType();


        // TODO: CHANGE GENERATED SAMPLE CODE - START
		request.setPatientId(createID("1.2.752.129.2.1.3.1", registeredResidentId));
		
		request.setTime(createTimePeriodType(starTime, endTime));
		
		request.setSourceSystemId(createID("1.2.752.129.2.1.4.1", logicalAddress));
       
        // TODO: CHANGE GENERATED SAMPLE CODE - END

		GetObservationResponseType response = _service.getObservation(logicalAddress, request);
		responseHolder.value = response;
		
		processingStatusHolder.value = SoapHeaderCxfInterceptor.getLastFoundProcessingStatus();
	}
	
	public void callServiceIncludeValuesInAllReqParams(String logicalAddress, String registeredResidentId, String starTime, String endTime, Holder<ProcessingStatusType> processingStatusHolder, Holder<GetObservationResponseType> responseHolder) {

		log.debug("Calling GetObservation-soap-service with Registered Resident Id = {}", registeredResidentId);
		
		GetObservationType request = new GetObservationType();

		// TODO: CHANGE GENERATED CODE - START
		
		request.setPatientId(createID("1.2.752.129.2.1.3.1", registeredResidentId));
		
		request.setTime(createTimePeriodType(starTime, endTime));
		
		request.getObservationType().add(createType());
		
		request.getObservationValue().add(createType());
		
		request.getObservationId().add(createID("1.2.752.129.2.1.2.1", "HSA-ID-1"));
		
		request.setCareGiverId(createID("1.2.752.129.2.1.4.1", "HSA-ID-1"));

		request.setSourceSystemId(createID("1.2.752.129.2.1.4.1", logicalAddress));
		
		request.setCareUnitId(createID("1.2.752.129.2.1.4.1", "HSA-ID-1"));
		
		// TODO: CHANGE GENERATED CODE - END

		GetObservationResponseType response = _service.getObservation(logicalAddress, request);
		responseHolder.value = response;
		
		processingStatusHolder.value = SoapHeaderCxfInterceptor.getLastFoundProcessingStatus();
	}

	private CVType createType() {
		CVType cvType = new CVType();
		cvType.setCode("CODE");
		cvType.setCodeSystem("CODESYSTEM");
		cvType.setCodeSystemVersion("CODESYSTEMVERSION");
		cvType.setDisplayName("DISPLAYNAME");
		return cvType;
	}

	private TimePeriodType createTimePeriodType(String starTime, String endTime) {
		TimePeriodType periodType = new TimePeriodType();
		periodType.setEnd(endTime);
		periodType.setStart(starTime);
		return periodType;
	}

	private IIType createID(String type, String value) {
		IIType iiType = new IIType();
		iiType.setRoot(type);
		iiType.setExtension(value);
		return iiType;
	}
}