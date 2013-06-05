package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedcaredocumentation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;

import se.riv.clinicalprocess.healthcond.description.getcaredocumentationresponder.v2.GetCareDocumentationResponseType;
import se.riv.clinicalprocess.healthcond.description.getcaredocumentationresponder.v2.ObjectFactory;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;
import se.skltp.agp.service.api.QueryObject;
import se.skltp.agp.service.api.ResponseListFactory;

public class ResponseListFactoryImpl implements ResponseListFactory {

    private static final Logger log = LoggerFactory.getLogger(ResponseListFactoryImpl.class);
    private static final JaxbUtil jaxbUtil = new JaxbUtil(GetCareDocumentationResponseType.class, ProcessingStatusType.class);
    private static final ObjectFactory OF = new ObjectFactory();

    @Override
    public String getXmlFromAggregatedResponse(QueryObject queryObject, List<Object> aggregatedResponseList) {
        GetCareDocumentationResponseType aggregatedResponse = new GetCareDocumentationResponseType();

        for (Object object : aggregatedResponseList) {
            GetCareDocumentationResponseType response = (GetCareDocumentationResponseType)object;
            aggregatedResponse.getCareDocumentation().addAll(response.getCareDocumentation());
        }

        if (log.isInfoEnabled()) {
            String subjectOfCareId = queryObject.getFindContent().getRegisteredResidentIdentification();
            log.info("Returning {} aggregated care documentation for subject of care id {}", aggregatedResponse.getCareDocumentation().size() ,subjectOfCareId);
        }

        // Since the class GetCareDocumentationResponseType don't have an @XmlRootElement annotation
        // we need to use the ObjectFactory to add it.
        return jaxbUtil.marshal(OF.createGetCareDocumentationResponse(aggregatedResponse));
    }
}