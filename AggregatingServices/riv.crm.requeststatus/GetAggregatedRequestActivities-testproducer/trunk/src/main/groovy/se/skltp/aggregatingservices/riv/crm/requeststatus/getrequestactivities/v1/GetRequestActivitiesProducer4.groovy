package se.skltp.aggregatingservices.riv.crm.requeststatus.getrequestactivities.v1;

import javax.jws.WebService;

import se.riv.crm.requeststatus.getrequestactivities.v1.rivtabp21.GetRequestActivitiesResponderInterface;
import se.riv.crm.requeststatus.getrequestactivitiesresponder.v1.GetRequestActivitiesResponseType;
import se.riv.crm.requeststatus.getrequestactivitiesresponder.v1.GetRequestActivitiesType;


@WebService
public class GetRequestActivitiesProducer4 implements GetRequestActivitiesResponderInterface {

	@Override
	public GetRequestActivitiesResponseType getRequestActivities(
			String logicalAddress, GetRequestActivitiesType parameters) {
		
		System.out.println("Throwing runtimeexception to simulate that the producer 4 can not respond properly!");
		throw new RuntimeException("This Exception is just the symptom of Producer4 simulating its inability to respond");
	}
}
