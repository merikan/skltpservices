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
