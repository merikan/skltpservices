package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.basic.getaggregatedobservation.integrationtest;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.util.ThreadSafeSimpleDateFormat;

import se.riv.clinicalprocess.healthcond.basic.getobservationresponder.v1.GetObservationResponseType;
import se.riv.clinicalprocess.healthcond.basic.v1.CVType;
import se.riv.clinicalprocess.healthcond.basic.v1.CareGiverType;
import se.riv.clinicalprocess.healthcond.basic.v1.CareUnitType;
import se.riv.clinicalprocess.healthcond.basic.v1.IIType;
import se.riv.clinicalprocess.healthcond.basic.v1.LegalAuthenticatorType;
import se.riv.clinicalprocess.healthcond.basic.v1.LocationType;
import se.riv.clinicalprocess.healthcond.basic.v1.ObservationType;
import se.riv.clinicalprocess.healthcond.basic.v1.PatientType;
import se.riv.clinicalprocess.healthcond.basic.v1.PerformerRoleType;
import se.riv.clinicalprocess.healthcond.basic.v1.PersonType;
import se.riv.clinicalprocess.healthcond.basic.v1.SourceSystemType;
import se.riv.clinicalprocess.healthcond.basic.v1.TimePeriodType;
import se.skltp.agp.test.producer.TestProducerDb;

public class GetAggregatedObservationTestProducerDb extends TestProducerDb {
	
	ThreadSafeSimpleDateFormat sdf = new ThreadSafeSimpleDateFormat("YYYYMMddHHmmss");

	private static final Logger log = LoggerFactory.getLogger(GetAggregatedObservationTestProducerDb.class);
	
	@Override
	public Object processRequest(String logicalAddress,
			String registeredResidentId) {
		
		/*
		 *  Return an error-message if error logical address HSA-ID-6 is called.
		 *  
		 *  This case was added to be able to test the case when a system returns an error for a certain
		 *  RR_ID and another system returns a OK response. This case should probably be added to the
		 *  agp test common TestProducerDb, but for know its included here to test the concept.
		 */
		if (TEST_LOGICAL_ADDRESS_6.equals(logicalAddress)) {
			throw new RuntimeException("Logical address to trigger exception was called: " + logicalAddress);
		}
		
		return super.processRequest(logicalAddress, registeredResidentId);
	}

	@Override
	public Object createResponse(Object... responseItems) {
		log.debug("Creates a response with {} items", responseItems);
		GetObservationResponseType response = new GetObservationResponseType();
		for (int i = 0; i < responseItems.length; i++) {
            // TODO: CHANGE GENERATED SAMPLE CODE - START
			response.getObservation().add((ObservationType)responseItems[i]);
            // TODO: CHANGE GENERATED SAMPLE CODE - END
		}
		return response;
	}
	
	@Override
	public Object createResponseItem(String logicalAddress, String registeredResidentId, String businessObjectId, String time) {
		
		if (log.isDebugEnabled()) {
			log.debug("Created one response item for logical-address {}, registeredResidentId {} and businessObjectId {}",
				new Object[] {logicalAddress, registeredResidentId, businessObjectId});
		}

        // TODO: CHANGE GENERATED SAMPLE CODE - START
		ObservationType observation = new ObservationType();
		observation.setApprovedForPatient(true);
		observation.setDescription("Fritextbeskrivning av observationen");
		observation.setId(createId("1.2.752.129.2.1.2.1", logicalAddress));
		observation.setLegalAuthenticator(createLegalAuthenticator());
		observation.setLocation(createLocation("Provtagningsgatan 9"));
		observation.setMethod(createCVType());
		observation.setPatient(createPatient(registeredResidentId));
		observation.setPerformerRole(createPerformerRole());
		observation.setSourceSystem(createSourceSystem());
		observation.setTargetSite(createCVType());
		observation.setTime(createTimeType());
		observation.setType(createCVType());
		observation.setValue(createCVType());
		observation.setValueNegation(false);
		
		return observation;
        // TODO: CHANGE GENERATED SAMPLE CODE - END

	}
	
	private TimePeriodType createTimeType() {
		TimePeriodType period = new TimePeriodType();
		period.setEnd(sdf.format(new Date()));
		period.setStart(sdf.format(new Date()));
		return period;
	}

	private PerformerRoleType createPerformerRole() {
		PerformerRoleType performerRole = new PerformerRoleType();
		performerRole.setCareUnit(createCareUnit());
		performerRole.setCode(createCVType());
		performerRole.setId(createId("1.2.752.129.2.1.4.1", "HSA-ID-1"));
		performerRole.setPerson(createPerson());
		return performerRole;
	}

	private PersonType createPerson() {
		PersonType person = new PersonType();
		person.setId(createId("1.2.752.129.2.1.3.1", "1912121212"));
		return person;
	}

	private CareUnitType createCareUnit() {
		CareUnitType careUnitType = new CareUnitType();
		careUnitType.setCareGiver(createCareGiver());
		careUnitType.setId(createId("1.2.752.129.2.1.4.1", "HSA-ID-1"));
		careUnitType.setName("Kråkans vårdcentral");
		return careUnitType;
	}

	private CareGiverType createCareGiver() {
		CareGiverType careGiver = new CareGiverType();
		careGiver.setId(createId("1.2.752.129.2.1.4.1", "HSA-ID-1"));
		careGiver.setName("Vårdgivare 19");
		return careGiver;
	}

	private LegalAuthenticatorType createLegalAuthenticator() {
		LegalAuthenticatorType authenticator = new LegalAuthenticatorType();
		authenticator.setId(createId("1.2.752.129.2.1.4.1", "HSA-ID-1"));
		authenticator.setName("Kalle Kula");
		authenticator.setTime(sdf.format(new Date()));
		return authenticator;
	}
	
	private LocationType createLocation(String name) {
		LocationType locationType = new LocationType();
		locationType.setId(createId("1.2.752.129.2.1.4.1", "HSA-ID"));
		locationType.setName(name);
		return locationType;
	}
	
	private CVType createCVType() {
		CVType cvType = new CVType();
		cvType.setCode("CODE");
		cvType.setCodeSystem("CODESYSTEM");
		return cvType;
	}
	
	private PatientType createPatient(String registeredResidentId) {
		PatientType patientType = new PatientType();
		patientType.setId(createPatientId(registeredResidentId));
		patientType.setDateOfBirth(registeredResidentId.substring(0, 8));
		return patientType;
	}
	
	private IIType createPatientId(String registeredResidentId) {
		return createId("1.2.752.129.2.1.3.1", registeredResidentId);
	}
	
	private SourceSystemType createSourceSystem() {
		SourceSystemType sourceSystemType = new SourceSystemType();
		sourceSystemType.setId(createId("1.2.752.129.2.1.4.1", "HSA-ID-1"));
		return sourceSystemType;
	}

	private IIType createId(String root, String extension) {
		IIType iiType = new IIType();
		iiType.setRoot(root);
		iiType.setExtension(extension);
		return iiType;
	}
}