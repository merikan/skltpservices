/**
 * Copyright (c) 2013 Center for eHalsa i samverkan (CeHis).
 * 							<http://cehis.se/>
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
package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getdiagnosis.v2;

import riv.clinicalprocess.healthcond.description._2.DiagnosisBodyType
import riv.clinicalprocess.healthcond.description._2.DiagnosisType
import riv.clinicalprocess.healthcond.description._2.HealthcareProfessionalType
import riv.clinicalprocess.healthcond.description._2.PatientSummaryHeaderType
import riv.clinicalprocess.healthcond.description._2.PersonIdType
import riv.clinicalprocess.healthcond.description._2.RelatedDiagnosisType
import riv.clinicalprocess.healthcond.description.enums._2.DiagnosisTypeEnum
import se.riv.clinicalprocess.healthcond.description.getdiagnosisresponder.v2.GetDiagnosisType

public class ResponseBuilder {

	public DiagnosisType createResponse(GetDiagnosisType parameters, String systemId) {
		
		DiagnosisType diagnosis = new DiagnosisType();
		diagnosis.diagnosisHeader = new PatientSummaryHeaderType()
		diagnosis.diagnosisHeader.patientId = new PersonIdType()
		diagnosis.diagnosisHeader.accountableHealthcareProfessional = new HealthcareProfessionalType()
		diagnosis.diagnosisBody = new DiagnosisBodyType()
		
		def doscumentId = UUID.randomUUID().toString()
		
		//Header
		diagnosis.diagnosisHeader.patientId.id = parameters.patientId.id
		diagnosis.diagnosisHeader.patientId.type = parameters.patientId.type
		
		diagnosis.diagnosisHeader.documentId = doscumentId
		diagnosis.diagnosisHeader.sourceSystemHSAId = parameters?.sourceSystemHSAId
		diagnosis.diagnosisHeader.accountableHealthcareProfessional.authorTime = new Date().format("yyyyMMdd")
		//End Header
		
		//Body
		diagnosis.diagnosisBody.typeOfDiagnosis = DiagnosisTypeEnum.HUVUDDIAGNOS
		
		RelatedDiagnosisType relatedDiagnos = new RelatedDiagnosisType()
		relatedDiagnos.documentId = doscumentId
		diagnosis.diagnosisBody.relatedDiagnosis = [relatedDiagnos]
		//End Body
		
		
		return diagnosis;
	}
}
