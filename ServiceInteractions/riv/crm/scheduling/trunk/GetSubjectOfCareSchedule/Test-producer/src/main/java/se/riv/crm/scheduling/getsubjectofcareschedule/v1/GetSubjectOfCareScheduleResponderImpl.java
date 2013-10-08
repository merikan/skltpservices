/**
 * Copyright 2009 Sjukvardsradgivningen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public

 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the

 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,

 *   Boston, MA 02111-1307  USA
 */
package se.riv.crm.scheduling.getsubjectofcareschedule.v1;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.jws.WebService;

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
public class GetSubjectOfCareScheduleResponderImpl implements GetSubjectOfCareScheduleResponderInterface {
	
	static final SimpleDateFormat timeFformat = new SimpleDateFormat("yyyyMMddHHmmss");

	@Override
	public GetSubjectOfCareScheduleResponseType getSubjectOfCareSchedule(
			String logialAdress, riv.interoperability.headers._1.ActorType actor,
			GetSubjectOfCareScheduleType request) {
		GetSubjectOfCareScheduleResponseType careScheduleResponseType = new GetSubjectOfCareScheduleResponseType();
		careScheduleResponseType.getTimeslotDetail().add(crateTimeslotType());
		careScheduleResponseType.getTimeslotDetail().add(crateTimeslotType());
		return careScheduleResponseType;
	}
	
	private TimeslotType crateTimeslotType() {
		TimeslotType timeslotType = new TimeslotType();
		timeslotType.setBookingId(UUID.randomUUID().toString());
		timeslotType.setCareTypeID("CareTypeID");
		timeslotType.setCareTypeName("CareTypeName");
		timeslotType.setEndTimeExclusive(timeFformat.format(new Date()));
		timeslotType.setHealthcareFacility("HaltcareFacility");
		timeslotType.setHealthcareFacilityName("HaltcareFacilityName");
		timeslotType.setPerformer("Performer");
		timeslotType.setPerformerName("PerformerName");
		timeslotType.setPurpose("Purpose");
		timeslotType.setReason("Reason");
		timeslotType.setResourceID("ResourceID");
		timeslotType.setResourceName("ResourceName");
		timeslotType.setStartTimeInclusive(timeFformat.format(new Date()));
		timeslotType.setSubjectOfCare("SubjectOfCare");
		timeslotType.setTimeTypeID("TimeTypeID");
		timeslotType.setTimeTypeName("TimeTypeName");
		return timeslotType;
	}
}
