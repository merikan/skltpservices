package se.riv.crm.scheduling.getsubjectofcareschedule.v1;

import javax.jws.WebService

import riv.interoperability.headers._1.ActorType
import se.riv.crm.scheduling.getsubjectofcareschedule.v1.rivtabp21.GetSubjectOfCareScheduleResponderInterface
import se.riv.crm.scheduling.getsubjectofcarescheduleresponder.v1.GetSubjectOfCareScheduleResponseType
import se.riv.crm.scheduling.getsubjectofcarescheduleresponder.v1.GetSubjectOfCareScheduleType

@WebService(
		serviceName = "GetSubjectOfCareScheduleResponderService", 
		endpointInterface="se.riv.crm.scheduling.getsubjectofcareschedule.v1.rivtabp21.GetSubjectOfCareScheduleResponderInterface", 
		portName = "GetSubjectOfCareScheduleResponderPort", 
		targetNamespace = "urn:riv:crm:scheduling:GetSubjectOfCareSchedule:1:rivtabp21",
		wsdlLocation = "interactions/GetSubjectOfCareScheduleInteraction/GetSubjectOfCareScheduleInteraction_1.1_RIVTABP21.wsdl")
public class GetSubjectOfCareScheduleProducer3 implements GetSubjectOfCareScheduleResponderInterface {
	
	@Override
	public GetSubjectOfCareScheduleResponseType getSubjectOfCareSchedule(
			String logicalAddress, ActorType actor,
			GetSubjectOfCareScheduleType request) {
			//Let it sleep for a while so that the scheduling aggregating service gets mad at us and moves on.
			println "Producer 3 is sleeping in order to provoke a read time out for aggregated services"
			Thread.currentThread().sleep(6000)
	}
}