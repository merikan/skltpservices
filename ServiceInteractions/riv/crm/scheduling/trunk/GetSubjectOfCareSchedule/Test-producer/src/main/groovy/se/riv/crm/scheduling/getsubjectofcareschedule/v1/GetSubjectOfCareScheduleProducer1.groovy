package se.riv.crm.scheduling.getsubjectofcareschedule.v1;

import javax.jws.WebService
import riv.interoperability.headers._1.ActorType
import se.riv.crm.scheduling.getsubjectofcareschedule.v1.rivtabp21.GetSubjectOfCareScheduleResponderInterface
import se.riv.crm.scheduling.getsubjectofcarescheduleresponder.v1.GetSubjectOfCareScheduleResponseType
import se.riv.crm.scheduling.getsubjectofcarescheduleresponder.v1.GetSubjectOfCareScheduleType
import se.riv.crm.scheduling.v1.TimeslotType

@WebService(
		serviceName = "GetSubjectOfCareScheduleResponderService", 
		endpointInterface="se.riv.crm.scheduling.getsubjectofcareschedule.v1.rivtabp21.GetSubjectOfCareScheduleResponderInterface", 
		portName = "GetSubjectOfCareScheduleResponderPort", 
		targetNamespace = "urn:riv:crm:scheduling:GetSubjectOfCareSchedule:1:rivtabp21",
		wsdlLocation = "interactions/GetSubjectOfCareScheduleInteraction/GetSubjectOfCareScheduleInteraction_1.1_RIVTABP21.wsdl")
public class GetSubjectOfCareScheduleProducer1 implements GetSubjectOfCareScheduleResponderInterface {
	
	private final static String ADAM = "195609230577"
	private final static String ERIK = "192506298112"
	private final static String HSAPRODUCER1 = "HSAPRODUCER1"
	
	@Override
	public GetSubjectOfCareScheduleResponseType getSubjectOfCareSchedule(
			String logicalAddress, ActorType actor,
			GetSubjectOfCareScheduleType request) {
			
		def response = new GetSubjectOfCareScheduleResponseType() //Always return a response of this type, never return null
		println "### Virtual service for GetSubjectOfCareSchedule call the source system with logical address: $logicalAddress and patientId: ${request.getSubjectOfCare()}" 
		
		if(request.subjectOfCare == ADAM) {
			//Create a Timeslot and it to the response timeslotDetail list
			def timeslot = new TimeslotType()	
			timeslot.with {
				bookingId = "PRODUCER1_BookingID_Adam_1"
				startTimeInclusive = "2013107140145"
				endTimeExclusive = "2013107150145"
				healthcareFacility = HSAPRODUCER1
				subjectOfCare = ADAM
			}
			response.timeslotDetail << timeslot
		} else if(request.subjectOfCare == ERIK) {
				//Create a Timeslot and it to the response timeslotDetail list
				def timeslot = new TimeslotType()
				timeslot.with {
					bookingId = "PRODUCER1_BookingID_Erik_1"
					startTimeInclusive = "2013107140145"
					endTimeExclusive = "2013107150145"
					healthcareFacility = HSAPRODUCER1
					subjectOfCare = ERIK
				}
				response.timeslotDetail << timeslot
				
				timeslot = new TimeslotType()
				timeslot.with {
					bookingId = "PRODUCER1_BookingID_Erik_2"
					startTimeInclusive = "2013107140145"
					endTimeExclusive = "2013107150145"
					healthcareFacility = HSAPRODUCER1
					subjectOfCare = ERIK
				}
				response.timeslotDetail << timeslot
				
				timeslot = new TimeslotType()
				timeslot.with {
					bookingId = "PRODUCER1_BookingID_Erik_3"
					startTimeInclusive = "2013107140145"
					endTimeExclusive = "2013107150145"
					healthcareFacility = HSAPRODUCER1
					subjectOfCare = ERIK
				}
				response.timeslotDetail << timeslot
		}
		
        println "### Virtual service got ${response.getTimeslotDetail().size()} booknings in the reply from the source system with logical address: $logicalAddress and patientId: ${request.subjectOfCare}"

		// We are done
        return response
	}
}