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

import javax.jws.WebService

import se.riv.clinicalprocess.healthcond.description.getdiagnosis.v2.GetDiagnosisResponderInterface
import se.riv.clinicalprocess.healthcond.description.getdiagnosisresponder.v2.GetDiagnosisResponseType
import se.riv.clinicalprocess.healthcond.description.getdiagnosisresponder.v2.GetDiagnosisType


@WebService
public class Producer4 implements GetDiagnosisResponderInterface {

	@Override
	public GetDiagnosisResponseType getDiagnosis(
			String logicalAddress, GetDiagnosisType parameters) {
			
		println "Throwing runtimeexception to simulate that the producer 4 can not respond properly!"
		throw new RuntimeException("This Exception is just the symptom of Producer4 simulating its inability to respond")
	}
}
