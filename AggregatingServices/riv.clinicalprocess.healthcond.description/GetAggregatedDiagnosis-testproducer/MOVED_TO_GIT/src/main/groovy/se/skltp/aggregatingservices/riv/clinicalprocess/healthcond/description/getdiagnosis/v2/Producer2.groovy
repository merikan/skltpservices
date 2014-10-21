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

import static se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getdiagnosis.v2.Producer.AGDA_ANDERSSON
import static se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getdiagnosis.v2.Producer.LABAN_MEIJER
import static se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getdiagnosis.v2.Producer.ULLA_ALM

import javax.jws.WebService

import se.riv.clinicalprocess.healthcond.description.getdiagnosis.v2.GetDiagnosisResponderInterface
import se.riv.clinicalprocess.healthcond.description.getdiagnosisresponder.v2.GetDiagnosisResponseType
import se.riv.clinicalprocess.healthcond.description.getdiagnosisresponder.v2.GetDiagnosisType


@WebService
public class Producer2 implements GetDiagnosisResponderInterface {

	@Override
	public GetDiagnosisResponseType getDiagnosis(
			String logicalAddress, GetDiagnosisType parameters) {
			
		ResponseBuilder builder = new ResponseBuilder()
		GetDiagnosisResponseType response = new GetDiagnosisResponseType()
		
		def createResult = {
			response.diagnosis.add(builder.createResponse(parameters, "HSAPRODUCER2"))
		}
		
		String subjectOfCareId = parameters.patientId.id;

		if (AGDA_ANDERSSON == subjectOfCareId) {
			2.times(createResult)
		} else if (LABAN_MEIJER == subjectOfCareId) {
			1.times(createResult)
		} else if (ULLA_ALM == subjectOfCareId) {
			1.times(createResult)
		} 
		
		return response;
	}
}
