package se.skltp.aggregatingservices.riv.crm.requeststatus.getrequestactivities.v1;

import static se.skltp.aggregatingservices.riv.crm.requeststatus.getrequestactivities.v1.Producer.AGDA_ANDERSSON;
import static se.skltp.aggregatingservices.riv.crm.requeststatus.getrequestactivities.v1.Producer.FRIDA_KRANSTEGE;
import static se.skltp.aggregatingservices.riv.crm.requeststatus.getrequestactivities.v1.Producer.LABAN_MEIJER;
import static se.skltp.aggregatingservices.riv.crm.requeststatus.getrequestactivities.v1.Producer.ULLA_ALM;

import java.util.List;

import javax.jws.WebService;

import se.riv.crm.requeststatus.getrequestactivities.v1.rivtabp21.GetRequestActivitiesResponderInterface;
import se.riv.crm.requeststatus.getrequestactivitiesresponder.v1.GetRequestActivitiesResponseType;
import se.riv.crm.requeststatus.getrequestactivitiesresponder.v1.GetRequestActivitiesType;
import se.riv.crm.requeststatus.v1.RequestActivityType;

@WebService
public class GetRequestActivitiesProducer1 implements GetRequestActivitiesResponderInterface {
	
	@Override
	public GetRequestActivitiesResponseType getRequestActivities(
			String logicalAddress, GetRequestActivitiesType parameters) {
		
		GetRequestActivitiesBuilder builder = new GetRequestActivitiesBuilder()
		GetRequestActivitiesResponseType response = new GetRequestActivitiesResponseType()
		
		def createResult = {
			response.requestActivity.add(builder.createRequestActivityType(parameters, "HSAPRODUCER1"))
		}
		
		String subjectOfCareId = parameters.subjectOfCareId;

		if (AGDA_ANDERSSON == subjectOfCareId) {
			1.times(createResult)
		} else if (LABAN_MEIJER == subjectOfCareId) {
			2.times(createResult)
		} else if (ULLA_ALM == subjectOfCareId) {
			3.times(createResult)
		} else if (FRIDA_KRANSTEGE == subjectOfCareId) {
			2.times(createResult)
		}
		
		return response
	}
}
