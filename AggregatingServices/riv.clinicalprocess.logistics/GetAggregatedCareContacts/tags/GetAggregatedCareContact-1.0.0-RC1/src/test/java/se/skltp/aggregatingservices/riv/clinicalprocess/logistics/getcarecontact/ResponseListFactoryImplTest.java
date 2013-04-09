package se.skltp.aggregatingservices.riv.clinicalprocess.logistics.getcarecontact;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;

import se.riv.clinicalprocess.logistics.getcarecontactresponder.v2.GetCareContactResponseType;
import se.riv.clinicalprocess.logistics.v2.CareContactType;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentType;
import se.skltp.agp.service.api.QueryObject;

public class ResponseListFactoryImplTest {
    
    private static final JaxbUtil jaxbUtil = new JaxbUtil(GetCareContactResponseType.class, ProcessingStatusType.class);
    
    @Test
    public void getXmlFromAggregatedResponse(){
        FindContentType fc = new FindContentType();     
        fc.setRegisteredResidentIdentification("1212121212");
        QueryObject queryObject = new QueryObject(fc, null);
        List<Object> responseList = new ArrayList<Object>(2);
        responseList.add(createGetCareContactResponse());
        responseList.add(createGetCareContactResponse());
        ResponseListFactoryImpl responseListFactory = new ResponseListFactoryImpl();
        
        String responseXML = responseListFactory.getXmlFromAggregatedResponse(queryObject, responseList);
        GetCareContactResponseType response = (GetCareContactResponseType)jaxbUtil.unmarshal(responseXML);
        assertEquals(2, response.getCareContact().size());
    }
    
    private GetCareContactResponseType createGetCareContactResponse(){
        GetCareContactResponseType getCareDocResponse = new GetCareContactResponseType();
        getCareDocResponse.getCareContact().add(new CareContactType());
        return getCareDocResponse;
    }
}
