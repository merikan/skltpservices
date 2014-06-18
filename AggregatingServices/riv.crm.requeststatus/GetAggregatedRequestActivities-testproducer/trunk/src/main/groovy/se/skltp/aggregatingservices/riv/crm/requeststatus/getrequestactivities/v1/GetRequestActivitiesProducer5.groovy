package se.skltp.aggregatingservices.riv.crm.requeststatus.getrequestactivities.v1;

import javax.jws.WebService;

import se.riv.crm.requeststatus.getrequestactivities.v1.rivtabp21.GetRequestActivitiesResponderInterface;
import se.riv.crm.requeststatus.getrequestactivitiesresponder.v1.GetRequestActivitiesResponseType;
import se.riv.crm.requeststatus.getrequestactivitiesresponder.v1.GetRequestActivitiesType;


@WebService
public class GetRequestActivitiesProducer5 implements GetRequestActivitiesResponderInterface {

	@Override
	public GetRequestActivitiesResponseType getRequestActivities(
			String logicalAddress, GetRequestActivitiesType parameters) {
			
		return new GetRequestActivitiesResponseType();
	}
}
