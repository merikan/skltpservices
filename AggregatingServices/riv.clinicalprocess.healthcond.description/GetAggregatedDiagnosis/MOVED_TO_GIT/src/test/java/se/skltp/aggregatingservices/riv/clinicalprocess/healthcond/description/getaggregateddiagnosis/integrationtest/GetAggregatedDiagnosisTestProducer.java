package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregateddiagnosis.integrationtest;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.riv.clinicalprocess.healthcond.description.getdiagnosis.v2.GetDiagnosisResponderInterface;
import se.riv.clinicalprocess.healthcond.description.getdiagnosisresponder.v2.GetDiagnosisResponseType;
import se.riv.clinicalprocess.healthcond.description.getdiagnosisresponder.v2.GetDiagnosisType;
import se.skltp.agp.test.producer.TestProducerDb;

// TODO: CHANGE GENERATED CODE - START
@WebService(serviceName = "GetDiagnosisResponderService", portName = "GetDiagnosisResponderPort", targetNamespace = "urn:riv:clinicalprocess:healthcond:description:GetDiagnosis:2:rivtabp21", name = "GetDiagnosisInteraction")
// TODO: CHANGE GENERATED CODE - START
public class GetAggregatedDiagnosisTestProducer implements GetDiagnosisResponderInterface {

	private static final Logger log = LoggerFactory.getLogger(GetAggregatedDiagnosisTestProducer.class);

	private TestProducerDb testDb;
	public void setTestDb(TestProducerDb testDb) {
		this.testDb = testDb;
	}

	@Override
	public GetDiagnosisResponseType getDiagnosis(String logicalAddress, GetDiagnosisType request) {
		log.info("### Virtual service for GetDiagnosis call the source system with logical address: {} and patientId: {}", logicalAddress, request.getPatientId().getId());

		GetDiagnosisResponseType response = (GetDiagnosisResponseType)testDb.processRequest(logicalAddress, request.getPatientId().getId());
        if (response == null) {
        	// Return an empty response object instead of null if nothing is found
        	response = new GetDiagnosisResponseType();
        }

		log.info("### Virtual service got {} booknings in the reply from the source system with logical address: {} and patientId: {}", new Object[] {response.getDiagnosis().size(), logicalAddress, request.getPatientId().getId()});

		// We are done
        return response;
	}
}