package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedcaredocumentation;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.riv.clinicalprocess.healthcond.description.getcaredocumentationrequest.v2.GetCareDocumentationResponderInterface;
import se.riv.clinicalprocess.healthcond.description.getcaredocumentationresponder.v2.GetCareDocumentationResponseType;
import se.riv.clinicalprocess.healthcond.description.getcaredocumentationresponder.v2.GetCareDocumentationType;
import se.skltp.agp.test.producer.TestProducerDb;

@WebService(serviceName = "GetCareDocumentationResponderService", portName = "GetCareDocumentationResponderPort", targetNamespace = "urn:riv:ehr:patientsummary:GetCareDocumentationResponder:2:rivtabp21", name = "GetCareDocumentationInteraction")
public class CareDocumentationTestProducer implements GetCareDocumentationResponderInterface {

    private static final Logger log = LoggerFactory.getLogger(CareDocumentationTestProducer.class);

    private TestProducerDb testDb;

    public void setTestDb(TestProducerDb testDb) {
        this.testDb = testDb;
    }

    @Override
    @WebResult(name = "GetCareDocumentationResponse", targetNamespace = "urn:riv:ehr:patientsummary:GetCareDocumentationResponder:2", partName = "parameters")
    @WebMethod(operationName = "GetCareDocumentation", action = "urn:riv:ehr:patientsummary:GetCareDocumentationResponder:2:GetCareDocumentation")
    public GetCareDocumentationResponseType getCareDocumentation(
            @WebParam(partName = "LogicalAddress", name = "LogicalAddress", targetNamespace = "urn:riv:itintegration:registry:1", header = true) String logicalAddress,
            @WebParam(partName = "parameters", name = "GetCareDocumentation", targetNamespace = "urn:riv:ehr:patientsummary:GetCareDocumentationResponder:2") GetCareDocumentationType request) {
        log.info("### Virtual service for GetCareDocumentation call the source system with logical address: {} and patientId: {}", logicalAddress, request.getPatientId().getId());

        GetCareDocumentationResponseType response = (GetCareDocumentationResponseType)testDb.processRequest(logicalAddress, request.getPatientId().getId());
        if (response == null) {
            // Return an empty response object instead of null if nothing is found
            response = new GetCareDocumentationResponseType();
        }

        log.info("### Virtual service got {} documents in the reply from the source system with logical address: {} and patientId: {}", new Object[] {response.getCareDocumentation().size(), logicalAddress, request.getPatientId().getId()});

        // We are done
        return response;
    }
}