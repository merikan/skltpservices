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
package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.actoutcome.getlaboratoryorderoutcome.v3;

import se.riv.clinicalprocess.healthcond.actoutcome.getlaboratoryoutcomeresponder.v3.GetLaboratoryOrderOutcomeType;
import se.riv.clinicalprocess.healthcond.actoutcome.v3.HealthcareProfessionalType;
import se.riv.clinicalprocess.healthcond.actoutcome.v3.LaboratoryOrderOutcomeType;
import se.riv.clinicalprocess.healthcond.actoutcome.v3.LaboratoryOrderOutcomeBodyType;
import se.riv.clinicalprocess.healthcond.actoutcome.v3.OrderType;
import se.riv.clinicalprocess.healthcond.actoutcome.v3.OrgUnitType;
import se.riv.clinicalprocess.healthcond.actoutcome.v3.PatientSummaryHeaderType;
import se.riv.clinicalprocess.healthcond.actoutcome.v3.PersonIdType;

public class ResponseBuilder {

	public LaboratoryOrderOutcomeType createResponse(GetLaboratoryOrderOutcomeType parameters, String systemId) {
		
		//Header
		LaboratoryOrderOutcomeType labOrderOutcome = new LaboratoryOrderOutcomeType()
		labOrderOutcome.laboratoryOrderOutcomeHeader = new PatientSummaryHeaderType()
		labOrderOutcome.laboratoryOrderOutcomeHeader.accountableHealthcareProfessional = new HealthcareProfessionalType()
		labOrderOutcome.laboratoryOrderOutcomeHeader.accountableHealthcareProfessional.healthcareProfessionalOrgUnit = new OrgUnitType();
		
		labOrderOutcome.laboratoryOrderOutcomeHeader.documentId = UUID.randomUUID().toString()
		labOrderOutcome.laboratoryOrderOutcomeHeader.sourceSystemHSAId = parameters?.sourceSystemHSAId
		labOrderOutcome.laboratoryOrderOutcomeHeader.documentTime = new Date().format("YYYYMMDDhhmmss")
		
		labOrderOutcome.laboratoryOrderOutcomeHeader.patientId = new PersonIdType()
		labOrderOutcome.laboratoryOrderOutcomeHeader.patientId.id = parameters.patientId.id
		labOrderOutcome.laboratoryOrderOutcomeHeader.patientId.type = parameters.patientId.type
		
		labOrderOutcome.laboratoryOrderOutcomeHeader.accountableHealthcareProfessional.authorTime = new Date().format("YYYYMMDDhhmmss")
		labOrderOutcome.laboratoryOrderOutcomeHeader.accountableHealthcareProfessional.healthcareProfessionalHSAId = 'HSA-1234567890'
		labOrderOutcome.laboratoryOrderOutcomeHeader.accountableHealthcareProfessional.healthcareProfessionalOrgUnit.orgUnitHSAId = 'HSA-1234567890'
		
		labOrderOutcome.laboratoryOrderOutcomeHeader.approvedForPatient = true
		
		//End Header
		
		//Body
		labOrderOutcome.laboratoryOrderOutcomeBody = new LaboratoryOrderOutcomeBodyType()
		labOrderOutcome.laboratoryOrderOutcomeBody.resultType = 'DEF'
		labOrderOutcome.laboratoryOrderOutcomeBody.registrationTime = new Date().format("YYYYMMDDhhmmss")
		labOrderOutcome.laboratoryOrderOutcomeBody.discipline = 'Klinisk kemi'
		
		labOrderOutcome.laboratoryOrderOutcomeBody.order = new OrderType()
		labOrderOutcome.laboratoryOrderOutcomeBody.order.orderId = UUID.randomUUID().toString()
		//End Body
		
		
		return labOrderOutcome;
	}
}
