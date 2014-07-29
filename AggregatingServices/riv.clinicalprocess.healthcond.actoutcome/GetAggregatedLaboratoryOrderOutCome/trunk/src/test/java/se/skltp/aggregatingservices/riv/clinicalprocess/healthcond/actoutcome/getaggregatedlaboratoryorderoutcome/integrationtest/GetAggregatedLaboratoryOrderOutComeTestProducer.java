package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.actoutcome.getaggregatedlaboratoryorderoutcome.integrationtest;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.riv.clinicalprocess.healthcond.actoutcome.getlaboratoryoutcome.v3.rivtabp21.GetLaboratoryOrderOutcomeResponderInterface;
import se.riv.clinicalprocess.healthcond.actoutcome.getlaboratoryoutcomeresponder.v3.GetLaboratoryOrderOutcomeResponseType;
import se.riv.clinicalprocess.healthcond.actoutcome.getlaboratoryoutcomeresponder.v3.GetLaboratoryOrderOutcomeType;
import se.skltp.agp.test.producer.TestProducerDb;

// TODO: CHANGE GENERATED CODE - START
@WebService(serviceName = "GetRequestActivitiesResponderService", portName = "GetRequestActivitiesResponderPort", targetNamespace = "urn:riv:crm:requeststatus:GetRequestActivities:1:rivtabp21", name = "GetRequestActivitiesInteraction")
// TODO: CHANGE GENERATED CODE - START
public class GetAggregatedLaboratoryOrderOutComeTestProducer implements GetLaboratoryOrderOutcomeResponderInterface {

	private static final Logger log = LoggerFactory.getLogger(GetAggregatedLaboratoryOrderOutComeTestProducer.class);

	private TestProducerDb testDb;
	public void setTestDb(TestProducerDb testDb) {
		this.testDb = testDb;
	}

	@Override
	public GetLaboratoryOrderOutcomeResponseType getLaboratoryOrderOutcome(String logicalAddress, GetLaboratoryOrderOutcomeType request) {
		log.info("### Virtual service for GetLaboratoryOrderOutcome call the source system with logical address: {} and patientId: {}", logicalAddress, request.getPatientId().getId());
		
		GetLaboratoryOrderOutcomeResponseType response = (GetLaboratoryOrderOutcomeResponseType)testDb.processRequest(logicalAddress, request.getPatientId().getId());
        if (response == null) {
        	// Return an empty response object instead of null if nothing is found
        	response = new GetLaboratoryOrderOutcomeResponseType();
        }
        
        log.info("### Virtual service got {} booknings in the reply from the source system with logical address: {} and patientId: {}", new Object[] {response.getLaboratoryOrderOutcome().size(), logicalAddress, request.getPatientId().getId()});
		
		return response;
	}
}