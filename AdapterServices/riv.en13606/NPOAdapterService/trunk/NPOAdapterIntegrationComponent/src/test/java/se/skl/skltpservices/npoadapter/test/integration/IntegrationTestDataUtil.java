/**
 * Copyright (c) 2014 Inera AB, <http://inera.se/>
 *
 * This file is part of SKLTP.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package se.skl.skltpservices.npoadapter.test.integration;

import riv.clinicalprocess.healthcond.description.getcaredocumentationresponder._2.GetCareDocumentationType;
import riv.clinicalprocess.healthcond.description.getdiagnosisresponder._2.GetDiagnosisResponseType;
import riv.clinicalprocess.healthcond.description.getdiagnosisresponder._2.GetDiagnosisType;
import riv.clinicalprocess.logistics.logistics.getcarecontactsresponder._2.GetCareContactsType;
import se.skl.skltpservices.npoadapter.test.stub.EhrExtractWS;

import java.util.UUID;

public final class IntegrationTestDataUtil {
	private static final String SOURCE_SYSTEM_HSA_ID = UUID.randomUUID().toString();
	private static final String STRING_TEST_DATA_1 = UUID.randomUUID().toString();
	private static final String STRING_TEST_DATA_2 = UUID.randomUUID().toString();
	private static final String STRING_TEST_DATA_3 = UUID.randomUUID().toString();
	private static final String STRING_TEST_DATA_4 = UUID.randomUUID().toString();
	
	public static final int TRIGGER_INFO_MESSAGE = 0;
	public static final int TRIGGER_WARNING_MESSAGE = 1;
	public static final int TRIGGER_ERROR_MESSAGE = 2;
	public static final int NO_TRIGGER = 3;
	
	public static GetCareDocumentationType createGetCareDocumentationType(int triggerType) {
		final GetCareDocumentationType type = new GetCareDocumentationType();
		type.setPatientId(docPersonIdType(triggerType));
		type.setSourceSystemHSAid(SOURCE_SYSTEM_HSA_ID);
		type.setTimePeriod(docDatePeriodType());
		return type;
	}
	
	public static GetCareContactsType createGetCareContactsType(int triggerType) {
		final GetCareContactsType type = new GetCareContactsType();
		type.setPatientId(conPersonIdType(triggerType));
		type.setSourceSystemHSAId(SOURCE_SYSTEM_HSA_ID);
		type.setTimePeriod(conDatePeriodType());
		return type;
	}
	
	public static GetDiagnosisType createGetDiagnosisType(int triggerType) {
		final GetDiagnosisType type = new GetDiagnosisType();
		type.setPatientId(docPersonIdType(triggerType));
		type.setSourceSystemHSAId(SOURCE_SYSTEM_HSA_ID);
		type.setTimePeriod(docDatePeriodType());
		return type;
	}
	
	private static riv.clinicalprocess.healthcond.description._2.PersonIdType docPersonIdType(int triggerType) {
		final riv.clinicalprocess.healthcond.description._2.PersonIdType personIdType = new riv.clinicalprocess.healthcond.description._2.PersonIdType();
		personIdType.setId(personId(triggerType));
		personIdType.setType(STRING_TEST_DATA_2);
		return personIdType;
	}
	
	private static riv.clinicalprocess.healthcond.description._2.DatePeriodType docDatePeriodType() {
		final riv.clinicalprocess.healthcond.description._2.DatePeriodType datePeriodType = new riv.clinicalprocess.healthcond.description._2.DatePeriodType();
		datePeriodType.setEnd(STRING_TEST_DATA_3);
		datePeriodType.setStart(STRING_TEST_DATA_4);
		return datePeriodType;
	}
	
	private static riv.clinicalprocess.logistics.logistics._2.PersonIdType conPersonIdType(int triggerType) {
		final riv.clinicalprocess.logistics.logistics._2.PersonIdType personIdType = new riv.clinicalprocess.logistics.logistics._2.PersonIdType();
		personIdType.setId(personId(triggerType));
		personIdType.setType(STRING_TEST_DATA_2);
		return personIdType;
	}
	
	private static riv.clinicalprocess.logistics.logistics._2.DatePeriodType conDatePeriodType() {
		final riv.clinicalprocess.logistics.logistics._2.DatePeriodType datePeriodType = new riv.clinicalprocess.logistics.logistics._2.DatePeriodType();
		datePeriodType.setEnd(STRING_TEST_DATA_3);
		datePeriodType.setStart(STRING_TEST_DATA_4);
		return datePeriodType;
	}
	
	private static String personId(int triggerType) {
		switch(triggerType) {
		case TRIGGER_ERROR_MESSAGE:
			return EhrExtractWS.PATIENT_ID_TRIGGER_ERROR;
		case TRIGGER_INFO_MESSAGE:
			return EhrExtractWS.PATIENT_ID_TRIGGER_INFO;
		case TRIGGER_WARNING_MESSAGE:
			return EhrExtractWS.PATIENT_ID_TRIGGER_WARNING;
		default:
			return STRING_TEST_DATA_1;		
		}
	}
}
