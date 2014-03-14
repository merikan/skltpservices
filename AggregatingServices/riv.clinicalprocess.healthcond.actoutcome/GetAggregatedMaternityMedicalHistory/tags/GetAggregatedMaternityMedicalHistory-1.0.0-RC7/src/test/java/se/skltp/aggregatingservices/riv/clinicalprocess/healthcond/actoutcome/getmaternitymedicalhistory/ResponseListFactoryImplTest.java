package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistory;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;

import se.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistoryresponder.v2.GetMaternityMedicalHistoryResponseType;
import se.riv.clinicalprocess.healthcond.actoutcome.v2.MaternityMedicalRecordType;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentType;
import se.skltp.agp.service.api.QueryObject;

public class ResponseListFactoryImplTest {
    
    private static final JaxbUtil jaxbUtil = new JaxbUtil(GetMaternityMedicalHistoryResponseType.class, ProcessingStatusType.class);
    
    @Test
    public void getXmlFromAggregatedResponse(){
        FindContentType fc = new FindContentType();     
        fc.setRegisteredResidentIdentification("1212121212");
        QueryObject queryObject = new QueryObject(fc, null);
        List<Object> responseList = new ArrayList<Object>(2);
        responseList.add(createGetMaternityMedicalHistoryResponse());
        responseList.add(createGetMaternityMedicalHistoryResponse());
        ResponseListFactoryImpl responseListFactory = new ResponseListFactoryImpl();
        
        String responseXML = responseListFactory.getXmlFromAggregatedResponse(queryObject, responseList);
        GetMaternityMedicalHistoryResponseType response = (GetMaternityMedicalHistoryResponseType)jaxbUtil.unmarshal(responseXML);
        assertEquals(2, response.getMaternityMedicalRecord().size());
    }
    
    private GetMaternityMedicalHistoryResponseType createGetMaternityMedicalHistoryResponse(){
        GetMaternityMedicalHistoryResponseType getMaternityMedicalHistoryResponse = new GetMaternityMedicalHistoryResponseType();
        getMaternityMedicalHistoryResponse.getMaternityMedicalRecord().add(new MaternityMedicalRecordType());
        return getMaternityMedicalHistoryResponse;
    }
}
