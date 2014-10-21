package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.actoutcome.getaggregatedlaboratoryorderoutcome;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;

import se.riv.clinicalprocess.healthcond.actoutcome.getlaboratoryoutcomeresponder.v3.GetLaboratoryOrderOutcomeResponseType;
import se.riv.clinicalprocess.healthcond.actoutcome.getlaboratoryoutcomeresponder.v3.ObjectFactory;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;
import se.skltp.agp.service.api.QueryObject;
import se.skltp.agp.service.api.ResponseListFactory;

public class ResponseListFactoryImpl implements ResponseListFactory {

	private static final Logger log = LoggerFactory.getLogger(ResponseListFactoryImpl.class);
	private static final JaxbUtil jaxbUtil = new JaxbUtil(GetLaboratoryOrderOutcomeResponseType.class, ProcessingStatusType.class);
	private static final ObjectFactory OF = new ObjectFactory();
	
	@Override
	public String getXmlFromAggregatedResponse(QueryObject queryObject, List<Object> aggregatedResponseList) {
		GetLaboratoryOrderOutcomeResponseType aggregatedResponse = new GetLaboratoryOrderOutcomeResponseType();

	    for (Object object : aggregatedResponseList) {
	    	GetLaboratoryOrderOutcomeResponseType response = (GetLaboratoryOrderOutcomeResponseType)object;
			aggregatedResponse.getLaboratoryOrderOutcome().addAll(response.getLaboratoryOrderOutcome());
		}

	    if (log.isInfoEnabled()) {
    		String subjectOfCareId = queryObject.getFindContent().getRegisteredResidentIdentification();
        	log.info("Returning {} aggregated laboratory order outcome for subject of care id {}", aggregatedResponse.getLaboratoryOrderOutcome().size() ,subjectOfCareId);
        }
        
        // Since the class GetRequestActivitiesResponseType don't have an @XmlRootElement annotation
        // we need to use the ObjectFactory to add it.
        return jaxbUtil.marshal(OF.createGetLaboratoryOrderOutcomeResponse(aggregatedResponse));
	}
}