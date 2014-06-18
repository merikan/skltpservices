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
package se.skltp.aggregatingservices.riv.crm.requeststatus.getrequestactivities.v1;

import static se.skltp.aggregatingservices.riv.crm.requeststatus.getrequestactivities.v1.Producer.AGDA_ANDERSSON;
import static se.skltp.aggregatingservices.riv.crm.requeststatus.getrequestactivities.v1.Producer.LABAN_MEIJER;
import static se.skltp.aggregatingservices.riv.crm.requeststatus.getrequestactivities.v1.Producer.ULLA_ALM;

import java.util.List;

import javax.jws.WebService;

import se.riv.crm.requeststatus.getrequestactivities.v1.rivtabp21.GetRequestActivitiesResponderInterface;
import se.riv.crm.requeststatus.getrequestactivitiesresponder.v1.GetRequestActivitiesResponseType;
import se.riv.crm.requeststatus.getrequestactivitiesresponder.v1.GetRequestActivitiesType;
import se.riv.crm.requeststatus.v1.RequestActivityType;


@WebService
public class GetRequestActivitiesProducer2 implements GetRequestActivitiesResponderInterface {

	@Override
	public GetRequestActivitiesResponseType getRequestActivities(
			String logicalAddress, GetRequestActivitiesType parameters) {
				
		GetRequestActivitiesBuilder builder = new GetRequestActivitiesBuilder()
		GetRequestActivitiesResponseType response = new GetRequestActivitiesResponseType()
		
		def createResult = {
			response.requestActivity.add(builder.createRequestActivityType(parameters, "HSAPRODUCER2"))
		}
		
		String subjectOfCareId = parameters.subjectOfCareId;

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
