package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregateddiagnosis.integrationtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import riv.clinicalprocess.healthcond.description._2.DiagnosisBodyType;
import riv.clinicalprocess.healthcond.description._2.DiagnosisType;
import riv.clinicalprocess.healthcond.description._2.PatientSummaryHeaderType;
import riv.clinicalprocess.healthcond.description._2.PersonIdType;
import se.riv.clinicalprocess.healthcond.description.getdiagnosisresponder.v2.GetDiagnosisResponseType;
import se.skltp.agp.test.producer.TestProducerDb;

public class GetAggregatedDiagnosisTestProducerDb extends TestProducerDb {

	private static final Logger log = LoggerFactory.getLogger(GetAggregatedDiagnosisTestProducerDb.class);

	@Override
	public Object createResponse(Object... responseItems) {
		log.debug("Creates a response with {} items", responseItems);
		GetDiagnosisResponseType response = new GetDiagnosisResponseType();
		for (int i = 0; i < responseItems.length; i++) {
			response.getDiagnosis().add((DiagnosisType)responseItems[i]);
		}
		return response;
	}
	
	@Override
	public Object createResponseItem(String logicalAddress, String registeredResidentId, String businessObjectId, String time) {
		
		if (log.isDebugEnabled()) {
			log.debug("Created one response item for logical-address {}, registeredResidentId {} and businessObjectId {}",
				new Object[] {logicalAddress, registeredResidentId, businessObjectId});
		}

		DiagnosisType response = new DiagnosisType();

		// TODO: CHANGE GENERATED CODE - START
		DiagnosisBodyType bodyType = new DiagnosisBodyType();
		response.setDiagnosisBody(bodyType);
		
		PatientSummaryHeaderType headerType = new PatientSummaryHeaderType();
		
		PersonIdType personIdType = new PersonIdType();
		personIdType.setId(registeredResidentId);
		personIdType.setType("1.2.752.129.2.1.3.1");
		
		headerType.setSourceSystemHSAId(logicalAddress);
		
		headerType.setPatientId(personIdType);
		response.setDiagnosisHeader(headerType);
		
//		response.setCareUnit(logicalAddress);
//		response.setSubjectOfCareId(registeredResidentId);
//		response.setSenderRequestId(businessObjectId);
		// TODO: CHANGE GENERATED CODE - END
		
		return response;
	}
}