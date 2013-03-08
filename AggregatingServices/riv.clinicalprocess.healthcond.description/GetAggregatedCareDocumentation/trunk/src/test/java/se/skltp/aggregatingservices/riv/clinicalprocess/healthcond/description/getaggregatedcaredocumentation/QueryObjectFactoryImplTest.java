package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedcaredocumentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;

import se.riv.clinicalprocess.healthcond.description.getcaredocumentationresponder.v2.GetCareDocumentationType;
import se.riv.clinicalprocess.healthcond.description.getcaredocumentationresponder.v2.ObjectFactory;
import se.riv.clinicalprocess.healthcond.description.v2.PatientIdType;
import se.skltp.agp.service.api.QueryObject;

public class QueryObjectFactoryImplTest {

    private static final String CATEGORIZATION = "voo";
    private static final String SERVICE_DOMAIN = "riv:clinicalprocess:healthcond:description";
    private static final String RR_ID = "1212121212"; 
    
    @Test
    public void createQueryObject() throws Exception{
        QueryObjectFactoryImpl objectFactory = new QueryObjectFactoryImpl();
        objectFactory.setEiCategorization(CATEGORIZATION);
        objectFactory.setEiServiceDomain(SERVICE_DOMAIN);
        
        GetCareDocumentationType getCareDoc = new GetCareDocumentationType();
        PatientIdType patientId = new PatientIdType();
        patientId.setId(RR_ID);
        getCareDoc.setPatientId(patientId);
        
        ObjectFactory of = new ObjectFactory();
        JAXBContext jaxbContext = JAXBContext.newInstance(GetCareDocumentationType.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = dbf.newDocumentBuilder().newDocument();
        marshaller.marshal(of.createGetCareDocumentation(getCareDoc), doc);
        QueryObject queryObj = objectFactory.createQueryObject(doc);
        
        assertNotNull(queryObj.getFindContent());
        assertEquals(SERVICE_DOMAIN, queryObj.getFindContent().getServiceDomain());
        assertEquals(CATEGORIZATION, queryObj.getFindContent().getCategorization());
        assertEquals(RR_ID, queryObj.getFindContent().getRegisteredResidentIdentification());
    }
    
}
