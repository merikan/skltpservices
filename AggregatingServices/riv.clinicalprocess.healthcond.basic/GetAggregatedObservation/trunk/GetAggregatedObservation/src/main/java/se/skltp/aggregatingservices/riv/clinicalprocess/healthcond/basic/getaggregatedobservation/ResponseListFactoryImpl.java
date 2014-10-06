package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.basic.getaggregatedobservation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;

import se.riv.clinicalprocess.healthcond.basic.getobservationresponder.v1.GetObservationResponseType;
import se.riv.clinicalprocess.healthcond.basic.getobservationresponder.v1.ObjectFactory;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;
import se.skltp.agp.service.api.QueryObject;
import se.skltp.agp.service.api.ResponseListFactory;

public class ResponseListFactoryImpl implements ResponseListFactory {

	private static final Logger log = LoggerFactory.getLogger(ResponseListFactoryImpl.class);
	private static final JaxbUtil jaxbUtil = new JaxbUtil(GetObservationResponseType.class, ProcessingStatusType.class);
	private static final ObjectFactory OF = new ObjectFactory();
	
	@Override
	public String getXmlFromAggregatedResponse(QueryObject queryObject, List<Object> aggregatedResponseList) {
		GetObservationResponseType aggregatedResponse = new GetObservationResponseType();


        // TODO: CHANGE GENERATED SAMPLE CODE - START
        if (1==1) throw new UnsupportedOperationException("Not yet implemented");
        /*

	    for (Object object : aggregatedResponseList) {
	    	GetObservationResponseType response = (GetObservationResponseType)object;
			aggregatedResponse.getRequestActivity().addAll(response.getRequestActivity());
		}

	    if (log.isInfoEnabled()) {
    		String subjectOfCareId = queryObject.getFindContent().getRegisteredResidentIdentification();
        	log.info("Returning {} aggregated remisstatus for subject of care id {}", aggregatedResponse.getRequestActivity().size() ,subjectOfCareId);
        }

        */
        // TODO: CHANGE GENERATED SAMPLE CODE - END


        // Since the class GetObservationResponseType don't have an @XmlRootElement annotation
        // we need to use the ObjectFactory to add it.
        return jaxbUtil.marshal(OF.createGetObservationResponse(aggregatedResponse));
	}
}