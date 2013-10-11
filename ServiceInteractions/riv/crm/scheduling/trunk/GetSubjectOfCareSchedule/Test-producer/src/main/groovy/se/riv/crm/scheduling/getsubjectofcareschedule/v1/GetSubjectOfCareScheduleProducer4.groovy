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
public class GetSubjectOfCareScheduleProducer4 implements GetSubjectOfCareScheduleResponderInterface {
	
	@Override
	public GetSubjectOfCareScheduleResponseType getSubjectOfCareSchedule(
			String logicalAddress, ActorType actor,
			GetSubjectOfCareScheduleType request) {
		println "Throwing runtimeexception to simulate that the producer 4 can not respond properly!"
		throw new RuntimeException("This Exception is just the symptom of Producer4 simulating its inability to respond")
	}
}