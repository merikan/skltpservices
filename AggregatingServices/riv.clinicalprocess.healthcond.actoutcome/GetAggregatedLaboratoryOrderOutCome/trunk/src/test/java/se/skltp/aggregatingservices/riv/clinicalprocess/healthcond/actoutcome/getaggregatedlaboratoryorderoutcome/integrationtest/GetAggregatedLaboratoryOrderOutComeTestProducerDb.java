package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.actoutcome.getaggregatedlaboratoryorderoutcome.integrationtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.riv.clinicalprocess.healthcond.actoutcome.getlaboratoryoutcomeresponder.v3.GetLaboratoryOrderOutcomeResponseType;
import se.riv.clinicalprocess.healthcond.actoutcome.v3.LaboratoryOrderOutcomeBodyType;
import se.riv.clinicalprocess.healthcond.actoutcome.v3.LaboratoryOrderOutcomeType;
import se.riv.clinicalprocess.healthcond.actoutcome.v3.PatientSummaryHeaderType;
import se.riv.clinicalprocess.healthcond.actoutcome.v3.PersonIdType;
import se.skltp.agp.test.producer.TestProducerDb;

public class GetAggregatedLaboratoryOrderOutComeTestProducerDb extends TestProducerDb {

	private static final Logger log = LoggerFactory.getLogger(GetAggregatedLaboratoryOrderOutComeTestProducerDb.class);

	@Override
	public Object createResponse(Object... responseItems) {
		log.debug("Creates a response with {} items", responseItems);
		GetLaboratoryOrderOutcomeResponseType response = new GetLaboratoryOrderOutcomeResponseType();
		for (int i = 0; i < responseItems.length; i++) {
			response.getLaboratoryOrderOutcome().add((LaboratoryOrderOutcomeType)responseItems[i]);
		}
		return response;
	}
	
	@Override
	public Object createResponseItem(String logicalAddress, String registeredResidentId, String businessObjectId, String time) {
		
		if (log.isDebugEnabled()) {
			log.debug("Created one response item for logical-address {}, registeredResidentId {} and businessObjectId {}",
				new Object[] {logicalAddress, registeredResidentId, businessObjectId});
		}

		// TODO: CHANGE GENERATED CODE - START
		LaboratoryOrderOutcomeType response = new LaboratoryOrderOutcomeType();
		response.setLaboratoryOrderOutcomeBody(new LaboratoryOrderOutcomeBodyType());
		
		PatientSummaryHeaderType headerType = new PatientSummaryHeaderType();
		PersonIdType personIdType = new PersonIdType();
		personIdType.setId(registeredResidentId);
		personIdType.setType("1.2.752.129.2.1.3.1");
		headerType.setSourceSystemHSAId(logicalAddress);
		headerType.setPatientId(personIdType);
		
		response.setLaboratoryOrderOutcomeHeader(headerType);
		
		//response.setCareUnit(logicalAddress);
		//response.setSubjectOfCareId(registeredResidentId);
		//response.setSenderRequestId(businessObjectId);

		// TODO: CHANGE GENERATED CODE - END
		
		return response;
	}
}