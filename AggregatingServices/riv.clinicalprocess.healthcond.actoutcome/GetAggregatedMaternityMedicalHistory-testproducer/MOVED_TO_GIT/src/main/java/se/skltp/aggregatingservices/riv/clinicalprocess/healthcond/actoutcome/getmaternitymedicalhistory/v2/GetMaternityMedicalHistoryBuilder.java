package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistory.v2;

import se.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistoryresponder.v2.GetMaternityMedicalHistoryType;
import se.riv.clinicalprocess.healthcond.actoutcome.v2.AuthorOtherRoleType;
import se.riv.clinicalprocess.healthcond.actoutcome.v2.AuthorType;
import se.riv.clinicalprocess.healthcond.actoutcome.v2.HealthcareProfessionalType;
import se.riv.clinicalprocess.healthcond.actoutcome.v2.LegalAuthenticatorType;
import se.riv.clinicalprocess.healthcond.actoutcome.v2.MaternityMedicalRecordBodyType;
import se.riv.clinicalprocess.healthcond.actoutcome.v2.MaternityMedicalRecordType;
import se.riv.clinicalprocess.healthcond.actoutcome.v2.OrgUnitType;
import se.riv.clinicalprocess.healthcond.actoutcome.v2.PQType;
import se.riv.clinicalprocess.healthcond.actoutcome.v2.PatientSummaryHeaderType;
import se.riv.clinicalprocess.healthcond.actoutcome.v2.PregnancyCheckupRecordType;
import se.riv.clinicalprocess.healthcond.actoutcome.v2.RegistrationRecordType;

public class GetMaternityMedicalHistoryBuilder {

	public MaternityMedicalRecordType createMaternityMedicalRecord(GetMaternityMedicalHistoryType parameters,
			String documentId, String time) {
		MaternityMedicalRecordType maternityMedicalRecord = new MaternityMedicalRecordType();
		maternityMedicalRecord.setMaternityMedicalRecordHeader(generatePatientSummaryHeader(parameters, documentId,
				time));
		maternityMedicalRecord.setMaternityMedicalRecordBody(generateRecordBody());
		return maternityMedicalRecord;
	}

	protected PQType buildPQ(String unit, double value) {
		PQType pq = new PQType();
		pq.setUnit(unit);
		pq.setValue(value);
		return pq;
	}

	protected MaternityMedicalRecordBodyType generateRecordBody() {
		MaternityMedicalRecordBodyType body = new MaternityMedicalRecordBodyType();
		RegistrationRecordType registrationRecord = new RegistrationRecordType();
		{
			registrationRecord.setLastMenstrualPeriod("20130503");
			registrationRecord.setIndicationPregnancy("20130610");
			registrationRecord.setContraceptiveDiscontinued("20130401");
			registrationRecord.setExpectedDayOfDeliveryFromLastMenstrualPeriod("20130208");
			registrationRecord.setExpectedDayOfDeliveryFromUltrasoundScan(null);
			registrationRecord.setExpectedDayOfDeliveryFromEmbryonicTransfer(null);
			// registrationRecord.setLength(value);
			registrationRecord.setWeight(buildPQ("kg", 69));
			// registrationRecord.setBodyMassIndex(value);
			// registrationRecord.setInfertility(value);
			// registrationRecord.getPreviousGravidityAndParity();
			registrationRecord.setDiseasesThrombosis(false);
			registrationRecord.setDiseasesEndocineDiseases(false);
			registrationRecord.setDiseasesRecurrentUrinaryTractInfections(false);
			registrationRecord.setDiseasesDiabetesMellitus(true);
			// registrationRecord.getMedicationDuringPregnacy();
			registrationRecord.setAssessmentAtFirstContactStandardCare(true);
		}
		body.setRegistrationRecord(registrationRecord);
		PregnancyCheckupRecordType pregnancyCheckupRecordType = new PregnancyCheckupRecordType();
		{
			pregnancyCheckupRecordType.setCompleteWeeksOfGestation(22);
			pregnancyCheckupRecordType.setWeight(buildPQ("g", 301.0));
			pregnancyCheckupRecordType.setSymphysisFundalHeight(buildPQ("cm", 20.0));
			pregnancyCheckupRecordType.setHaemoglobin(buildPQ("g/dL", 14.3));
			pregnancyCheckupRecordType.setBloodPressureSystolic(buildPQ("mm Hg", 135.2));
			pregnancyCheckupRecordType.setBloodPressureDiastolic(buildPQ("mm Hg", 83.7));
			// pregnancyCheckupRecordType.setProteinuria(value);
			// pregnancyCheckupRecordType.setGlycosuria(value);
			// pregnancyCheckupRecordType.setFetalPosition(new BigInteger("1"));
			// pregnancyCheckupRecordType.setFetalPresentation(new
			// BigInteger("1"));
			// pregnancyCheckupRecordType.setFetalHeartRate(buildPQ("bpm", 82));
			// pregnancyCheckupRecordType.setTypeOfLeave(new BigInteger("0"));
			// pregnancyCheckupRecordType.setMedicationSinceRegistration()

		}
		body.setPregnancyCheckupRecord(pregnancyCheckupRecordType);
		body.setPostDeliveryRecord(null);
		return body;
	}

	protected PatientSummaryHeaderType generatePatientSummaryHeader(GetMaternityMedicalHistoryType parameters,
			String documentId, String time) {
		PatientSummaryHeaderType header = new PatientSummaryHeaderType();
		{
			header.setDocumentId(documentId);
			header.setSourceSystemHSAId("HSA-MOCKSYS-01");
			// header.setDocumentTitle(value);
			header.setDocumentTime(time);
			header.setPatientId(parameters.getPatientId());
			AuthorType author = new AuthorType();
			{
				author.setAuthorHSAId("HSA-DR-1");
				author.setAuthorName("Ulla Bandage");
				author.setAuthorOrgUnitAddress("Bättringsvägen 1");
				author.setAuthorOrgUnitHSAid("HSA-MOLN-01");
				author.setAuthorOrgUnitName("Vårdcentralen Molnet");
				AuthorOtherRoleType authorOtherRole = new AuthorOtherRoleType();
				{
					authorOtherRole.setAuthorOtherRoleCode("123");
					authorOtherRole.setAuthorOtherRoleCodeOID("1.2.3.4.5.6.7");
				}
				author.setAuthorOtherRole(authorOtherRole);
				author.setAuthorRoleCode("112");
				author.setCareGiverHSAid("HSA-SLL-01");
				author.setCareUnitHSAid("HSA-MOLN-01");
			}
			HealthcareProfessionalType aHPT = new HealthcareProfessionalType();
			{
				aHPT.setAuthorTime("20130601130700");
				aHPT.setHealthcareProfessionalHSAId("HSAID-001");
				// aHPT.setHealthcareProfessionalName(value);
				// aHPT.setHealthcareProfessionalRoleCode(value);
				OrgUnitType orgUnit = new OrgUnitType();
				orgUnit.setOrgUnitHSAId("HSAID-001");
				orgUnit.setOrgUnitName("Plåstret AB");
				aHPT.setHealthcareProfessionalOrgUnit(orgUnit);
				aHPT.setHealthcareProfessionalCareUnitHSAId("HSAID-002");
				aHPT.setHealthcareProfessionalCareGiverHSAId("HSAID-001");

			}
			// header.setAccountableHealthcareProfessional(value);

			LegalAuthenticatorType legalAuthenticator = new LegalAuthenticatorType();
			{
				legalAuthenticator.setLegalAuthenticatorHSAId("HSA-DR-1");
				legalAuthenticator.setSignatureTime(time);
			}
			header.setLegalAuthenticator(legalAuthenticator);
			header.setApprovedForPatient(true);
			// header.setNullified(value);
			// header.setNullifiedReason(value);
			header.setCareContactId("KONTAKT-01");

		}
		return header;
	}

}
