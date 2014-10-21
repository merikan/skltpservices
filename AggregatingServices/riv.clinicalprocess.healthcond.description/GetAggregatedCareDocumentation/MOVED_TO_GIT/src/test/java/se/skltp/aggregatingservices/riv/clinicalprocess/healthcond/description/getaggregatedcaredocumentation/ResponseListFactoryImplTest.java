package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedcaredocumentation;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import org.soitoolkit.commons.mule.jaxb.JaxbUtil;

import se.riv.clinicalprocess.healthcond.description.getcaredocumentationresponder.v2.GetCareDocumentationResponseType;
import se.riv.clinicalprocess.healthcond.description.v2.CareDocumentationType;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentType;
import se.skltp.agp.service.api.QueryObject;

public class ResponseListFactoryImplTest {
    
    private static final JaxbUtil jaxbUtil = new JaxbUtil(GetCareDocumentationResponseType.class, ProcessingStatusType.class);
    
    @Test
    public void getXmlFromAggregatedResponse(){
        FindContentType fc = new FindContentType();     
        fc.setRegisteredResidentIdentification("1212121212");
        QueryObject queryObject = new QueryObject(fc, null);
        List<Object> responseList = new ArrayList<Object>(2);
        responseList.add(createGetCareDocumentationResponse());
        responseList.add(createGetCareDocumentationResponse());
        ResponseListFactoryImpl responseListFactory = new ResponseListFactoryImpl();
        
        String responseXML = responseListFactory.getXmlFromAggregatedResponse(queryObject, responseList);
        GetCareDocumentationResponseType response = (GetCareDocumentationResponseType)jaxbUtil.unmarshal(responseXML);
        assertEquals(2, response.getCareDocumentation().size());
    }
    
    private GetCareDocumentationResponseType createGetCareDocumentationResponse(){
        GetCareDocumentationResponseType getCareDocResponse = new GetCareDocumentationResponseType();
        getCareDocResponse.getCareDocumentation().add(new CareDocumentationType());
        return getCareDocResponse;
    }
}
