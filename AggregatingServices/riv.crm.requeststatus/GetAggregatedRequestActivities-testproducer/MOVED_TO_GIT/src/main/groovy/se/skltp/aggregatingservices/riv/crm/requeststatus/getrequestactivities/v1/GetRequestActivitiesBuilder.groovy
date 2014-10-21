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

import se.riv.crm.requeststatus.getrequestactivitiesresponder.v1.GetRequestActivitiesType;
import se.riv.crm.requeststatus.v1.RequestActivityType;
import se.riv.crm.requeststatus.v1.ResultCodeEnum;

public class GetRequestActivitiesBuilder {

	public RequestActivityType createRequestActivityType(GetRequestActivitiesType parameters, String systemId) {
		
		RequestActivityType requestActivity = new RequestActivityType();
		
		requestActivity.subjectOfCareId = parameters.subjectOfCareId
		
		if(parameters?.typeOfRequest.size > 0){
			requestActivity.typeOfRequest = parameters.typeOfRequest.get(0)
		}else{
			requestActivity.typeOfRequest = '2';
		}
		
		requestActivity.logicalSystemId = systemId
		requestActivity.statusCode = ResultCodeEnum.OK
		requestActivity.eventTime = new Date().format("YYYYMMDDhhmmss")
		requestActivity.requestMedium = '3' //Elektroniskt
		requestActivity.receivingOrganizationalUnitDescription = 'Närakuten +468 398 72, Solna Torg 3, 171 45 SOLNA.'
		requestActivity.requestIssuedByOrganizationalUnitDescription = 'Närakuten +468 398 72, Solna Torg 3, 171 45 SOLNA.'
		
		requestActivity.senderRequestId = UUID.randomUUID().toString()
		return requestActivity;
	}
}
