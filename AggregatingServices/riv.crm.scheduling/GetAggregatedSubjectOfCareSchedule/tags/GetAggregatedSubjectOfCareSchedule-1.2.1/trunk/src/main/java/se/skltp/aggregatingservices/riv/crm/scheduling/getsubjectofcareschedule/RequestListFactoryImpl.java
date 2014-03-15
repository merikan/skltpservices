package se.skltp.aggregatingservices.riv.crm.scheduling.getsubjectofcareschedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.riv.crm.scheduling.getsubjectofcarescheduleresponder.v1.GetSubjectOfCareScheduleType;
import se.riv.interoperability.headers.v1.ActorType;
import se.riv.interoperability.headers.v1.ActorTypeEnum;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentResponseType;
import se.skltp.agp.riv.itintegration.engagementindex.v1.EngagementType;
import se.skltp.agp.service.api.QueryObject;
import se.skltp.agp.service.api.RequestListFactory;

public class RequestListFactoryImpl implements RequestListFactory {

	private static final Logger log = LoggerFactory.getLogger(RequestListFactoryImpl.class);

	@Override
	public List<Object[]> createRequestList(QueryObject qo, FindContentResponseType src) {

		FindContentResponseType eiResp = (FindContentResponseType)src;
		List<EngagementType> inEngagements = eiResp.getEngagement();
		
		log.info("Got {} hits in the engagement index", inEngagements.size());

		// Since we are using the GetSubjectOfCareSchedule that returns all bookings from a logical-address in one call we can reduce multiple hits in the index for the same logical-address to lower the number of calls required
		Map<String, String> uniqueLogicalAddresses = new HashMap<String, String>();
		for (EngagementType inEng : inEngagements) {
			uniqueLogicalAddresses.put(inEng.getLogicalAddress(), inEng.getRegisteredResidentIdentification());
		}

		// Prepare the result of the transformation as a list of request-payloads, 
		// one payload for each unique logical-address from the Set uniqueLogicalAddresses,
		// each payload built up as an object-array according to the JAXB-signature for the method in the service interface
		List<Object[]> reqList = new ArrayList<Object[]>();
		
		for (Entry<String, String> entry : uniqueLogicalAddresses.entrySet()) {

			String logicalAdress = entry.getKey();
			String subjectOfCare = entry.getValue();

			log.info("Calling source system using logical address {} for subject of care id {}", logicalAdress, subjectOfCare);

			// Get the Actor from from the query object, see QueryObjectFactoryImpl for how it is set
			ActorType actor = (ActorType)qo.getExtraArg();

			GetSubjectOfCareScheduleType request = new GetSubjectOfCareScheduleType();
			request.setHealthcareFacility(logicalAdress);
			request.setSubjectOfCare(subjectOfCare);

			Object[] reqArr = new Object[] {logicalAdress, actor, request};
			
			reqList.add(reqArr);
		}

		log.debug("Transformed payload: {}", reqList);

		return reqList;
	}

}
