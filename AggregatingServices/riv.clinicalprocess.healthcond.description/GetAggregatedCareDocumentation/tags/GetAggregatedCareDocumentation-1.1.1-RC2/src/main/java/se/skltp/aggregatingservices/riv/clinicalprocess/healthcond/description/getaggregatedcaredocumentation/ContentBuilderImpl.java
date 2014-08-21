package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedcaredocumentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;

/* FIXME
import se.riv.clinicalprocess.healthcond.description.getcaredocumentationresponder.v2.GetCareDocumentationResponseType;
import se.riv.clinicalprocess.healthcond.description.getcaredocumentationresponder.v2.ObjectFactory;
import se.riv.clinicalprocess.healthcond.description.v2.CareDocumentationType;
import se.skltp.agp.cache.Content;
import se.skltp.agp.cache.ContentBuilder; */

public class ContentBuilderImpl { //FIXME: implements ContentBuilder {

    private static final Logger log = LoggerFactory.getLogger(ContentBuilderImpl.class);
    
    /*FIXME
    private static final JaxbUtil jaxbUtil = new JaxbUtil(GetCareDocumentationResponseType.class);
    
    private static final ObjectFactory objectFactory = new ObjectFactory();
    
    @Override
    public String aggregateContent(List<Content> body) {
        GetCareDocumentationResponseType aggregatedResponse = new GetCareDocumentationResponseType();

        for (Content content: body) {
            GetCareDocumentationResponseType response = (GetCareDocumentationResponseType)jaxbUtil.unmarshal(content.getBody());
            aggregatedResponse.getCareDocumentation().addAll(response.getCareDocumentation());
        }
        
        return jaxbUtil.marshal(objectFactory.createGetCareDocumentationResponse(aggregatedResponse));
    }

    @Override
    public List<Content> splitContent(String body) {
        GetCareDocumentationResponseType fullResponse = (GetCareDocumentationResponseType)jaxbUtil.unmarshal(body);
        
        Map<String, List<CareDocumentationType>> careDocMap = new HashMap<String, List<CareDocumentationType>>();
        for(CareDocumentationType careDoc: fullResponse.getCareDocumentation()){
            String key = careDoc.getCareDocumentationHeader().getSourceSystemHSAid();
            if(!careDocMap.containsKey(key)){
                careDocMap.put(key,new LinkedList<CareDocumentationType>());
            }
            careDocMap.get(key).add(careDoc);
        }
        
        List<Content> splittedResponse = new ArrayList<Content>(careDocMap.size());
        for(Map.Entry<String, List<CareDocumentationType>> careDocEntry: careDocMap.entrySet()){
            GetCareDocumentationResponseType response = new GetCareDocumentationResponseType();
            response.getCareDocumentation().addAll(careDocEntry.getValue());
            Content content = new Content();
            content.setLogicalAddress(careDocEntry.getKey());
            content.setBody(jaxbUtil.marshal(objectFactory.createGetCareDocumentationResponse(response)));
            splittedResponse.add(content);
        }
        return splittedResponse;
    }
     */
}
