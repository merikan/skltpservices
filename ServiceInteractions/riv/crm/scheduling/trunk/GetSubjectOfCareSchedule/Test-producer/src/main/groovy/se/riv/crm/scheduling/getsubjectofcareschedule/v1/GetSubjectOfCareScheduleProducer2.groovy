package se.riv.crm.scheduling.getsubjectofcareschedule.v1;

import java.text.SimpleDateFormat;

import javax.jws.WebService;

import riv.interoperability.headers._1.ActorType;
import se.riv.crm.scheduling.getsubjectofcareschedule.v1.rivtabp21.GetSubjectOfCareScheduleResponderInterface;
import se.riv.crm.scheduling.getsubjectofcarescheduleresponder.v1.GetSubjectOfCareScheduleResponseType;
import se.riv.crm.scheduling.getsubjectofcarescheduleresponder.v1.GetSubjectOfCareScheduleType;
import se.riv.crm.scheduling.v1.TimeslotType;

@WebService(
serviceName = "GetSubjectOfCareScheduleResponderService",
endpointInterface="se.riv.crm.scheduling.getsubjectofcareschedule.v1.rivtabp21.GetSubjectOfCareScheduleResponderInterface",
portName = "GetSubjectOfCareScheduleResponderPort",
targetNamespace = "urn:riv:crm:scheduling:GetSubjectOfCareSchedule:1:rivtabp21",
wsdlLocation = "interactions/GetSubjectOfCareScheduleInteraction/GetSubjectOfCareScheduleInteraction_1.1_RIVTABP21.wsdl")
public class GetSubjectOfCareScheduleProducer2 implements GetSubjectOfCareScheduleResponderInterface {

	private final static String ADAM = "195609230577"
	private final static String ERIK = "192506298112"
	private final static String CESAR = "198111176239"
	private final static String GEORG = "196603164697"
	private final static String HSAPRODUCER2 = "HSAPRODUCER2"

	@Override
	public GetSubjectOfCareScheduleResponseType getSubjectOfCareSchedule(
			String logicalAddress, ActorType actor,
			GetSubjectOfCareScheduleType request) {

		def response = new GetSubjectOfCareScheduleResponseType() //Always return a response of this type, never return null
		println "### Virtual service for GetSubjectOfCareSchedule call the source system with logical address: $logicalAddress and patientId: ${request.getSubjectOfCare()}"

		if(request.subjectOfCare == ADAM) {
			response.timeslotDetail << createTimeslot("PRODUCER2_BookingID_Adam_1", HSAPRODUCER2, ADAM)
			response.timeslotDetail << createTimeslot("PRODUCER2_BookingID_Adam_2", HSAPRODUCER2, ADAM)
		} else if(request.subjectOfCare == ERIK) {
			response.timeslotDetail << createTimeslot("PRODUCER2_BookingID_Erik_1", HSAPRODUCER2, ERIK)
		}  else if(request.subjectOfCare == CESAR) {
			response.timeslotDetail << createTimeslot("PRODUCER2_BookingID_Cesar_1", HSAPRODUCER2, CESAR)
		} else if(request.subjectOfCare == GEORG) {
			response.timeslotDetail << createTimeslot("PRODUCER2_BookingID_Georg_1", HSAPRODUCER2, GEORG)
		}

		println "### Virtual service got ${response.getTimeslotDetail().size()} booknings in the reply from the source system with logical address: $logicalAddress and patientId: ${request.subjectOfCare}"

		// We are done
		return response
	}
	def createTimeslot(def bookingID, def producer, def subjectOfCare) {
		def timeslot =  new TimeslotType()
		timeslot.with {
			bookingId = bookingID //Don't let these properties be the same or you will experience hurt
			startTimeInclusive = "2013107140145"
			endTimeExclusive = "2013107150145"
			healthcareFacility = producer
			subjectOfCare = subjectOfCare
		}

		return timeslot
	}
}