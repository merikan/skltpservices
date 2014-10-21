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

import static se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.actoutcome.getlaboratoryorderoutcome.v3.Producer.AGDA_ANDERSSON
import static se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.actoutcome.getlaboratoryorderoutcome.v3.Producer.LABAN_MEIJER
import static se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.actoutcome.getlaboratoryorderoutcome.v3.Producer.ULLA_ALM

import javax.jws.WebService

import se.riv.clinicalprocess.healthcond.actoutcome.enums.v3.ResultCodeEnum
import se.riv.clinicalprocess.healthcond.actoutcome.getlaboratoryoutcome.v3.rivtabp21.GetLaboratoryOrderOutcomeResponderInterface
import se.riv.clinicalprocess.healthcond.actoutcome.getlaboratoryoutcomeresponder.v3.GetLaboratoryOrderOutcomeResponseType
import se.riv.clinicalprocess.healthcond.actoutcome.getlaboratoryoutcomeresponder.v3.GetLaboratoryOrderOutcomeType
import se.riv.clinicalprocess.healthcond.actoutcome.v3.ResultType


@WebService
public class Producer2 implements GetLaboratoryOrderOutcomeResponderInterface {

	@Override
	public GetLaboratoryOrderOutcomeResponseType getLaboratoryOrderOutcome(
			String logicalAddress, GetLaboratoryOrderOutcomeType parameters) {
			
		ResponseBuilder builder = new ResponseBuilder()
		
		GetLaboratoryOrderOutcomeResponseType response = new GetLaboratoryOrderOutcomeResponseType()
		response.result = new ResultType()
		response.result.resultCode = ResultCodeEnum.OK
		response.result.logId = UUID.randomUUID().toString()
		response.result.message = "Ett meddelande från HSAPRODUCER2 till användaren"
		
		def createResult = {
			response.laboratoryOrderOutcome.add(builder.createResponse(parameters, "HSAPRODUCER2"))
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
