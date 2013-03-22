package se.skltp.aggregatingservices.riv.clinicalprocess.logistics.getcarecontact;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;

import se.riv.clinicalprocess.logistics.getcarecontactresponder.v2.GetCareContactResponseType;
import se.riv.clinicalprocess.logistics.getcarecontactresponder.v2.ObjectFactory;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;
import se.skltp.agp.service.api.QueryObject;
import se.skltp.agp.service.api.ResponseListFactory;

public class ResponseListFactoryImpl implements ResponseListFactory {

    private static final Logger log = LoggerFactory.getLogger(ResponseListFactoryImpl.class);
    private static final JaxbUtil jaxbUtil = new JaxbUtil(GetCareContactResponseType.class, ProcessingStatusType.class);
    private static final ObjectFactory OF = new ObjectFactory();

    @Override
    public String getXmlFromAggregatedResponse(QueryObject queryObject, List<Object> aggregatedResponseList) {
        GetCareContactResponseType aggregatedResponse = new GetCareContactResponseType();

        for (Object object : aggregatedResponseList) {
            GetCareContactResponseType response = (GetCareContactResponseType)object;
            aggregatedResponse.getCareContact().addAll(response.getCareContact());
        }

        if (log.isInfoEnabled()) {
            String subjectOfCareId = queryObject.getFindContent().getRegisteredResidentIdentification();
            log.info("Returning {} aggregated care contact for subject of care id {}", aggregatedResponse.getCareContact().size() ,subjectOfCareId);
        }

        // Since the class GetCareContactResponseType don't have an @XmlRootElement annotation
        // we need to use the ObjectFactory to add it.
        return jaxbUtil.marshal(OF.createGetCareContactResponse(aggregatedResponse));
    }
}