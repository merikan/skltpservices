package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.basic.getaggregatedobservation.integrationtest;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.riv.clinicalprocess.healthcond.basic.getobservation.v1.rivtabp21.GetObservationResponderInterface;
import se.riv.clinicalprocess.healthcond.basic.getobservationresponder.v1.GetObservationResponseType;
import se.riv.clinicalprocess.healthcond.basic.getobservationresponder.v1.GetObservationType;
import se.skltp.agp.test.producer.TestProducerDb;

@WebService(serviceName = "GetObservationResponderService", portName = "GetObservationResponderPort", targetNamespace = "urn:riv:clinicalprocess:healthcond:basic:GetObservation:1:rivtabp21", name = "GetObservationInteraction")
public class GetAggregatedObservationTestProducer implements GetObservationResponderInterface {

	private static final Logger log = LoggerFactory.getLogger(GetAggregatedObservationTestProducer.class);

	private TestProducerDb testDb;
	public void setTestDb(TestProducerDb testDb) {
		this.testDb = testDb;
	}

	public GetObservationResponseType getObservation(String logicalAddress, GetObservationType request) {
		GetObservationResponseType response = null;


        // TODO: CHANGE GENERATED SAMPLE CODE - START
        if (1==1) throw new UnsupportedOperationException("Not yet implemented");
        /*

		log.info("### Virtual service for GetObservation call the source system with logical address: {} and patientId: {}", logicalAddress, request.getSubjectOfCareId());

		response = (GetObservationResponseType)testDb.processRequest(logicalAddress, request.getSubjectOfCareId());
        if (response == null) {
        	// Return an empty response object instead of null if nothing is found
        	response = new GetObservationResponseType();
        }

		log.info("### Virtual service got {} booknings in the reply from the source system with logical address: {} and patientId: {}", new Object[] {response.getRequestActivity().size(), logicalAddress, request.getSubjectOfCareId()});

        */
        // TODO: CHANGE GENERATED SAMPLE CODE - END


		// We are done
        return response;
	}
}