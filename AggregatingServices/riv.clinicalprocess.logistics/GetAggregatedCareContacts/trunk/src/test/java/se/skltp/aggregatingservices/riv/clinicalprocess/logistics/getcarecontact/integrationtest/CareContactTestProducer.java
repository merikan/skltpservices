package se.skltp.aggregatingservices.riv.clinicalprocess.logistics.getcarecontact.integrationtest;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.riv.clinicalprocess.logistics.getcarecontactrequest.v2.GetCareContactResponderInterface;
import se.riv.clinicalprocess.logistics.getcarecontactresponder.v2.GetCareContactResponseType;
import se.riv.clinicalprocess.logistics.getcarecontactresponder.v2.GetCareContactType;
import se.skltp.agp.test.producer.TestProducerDb;

@WebService(serviceName = "GetCareContactResponderService", portName = "GetCareContactResponderPort", targetNamespace = "urn:riv:clinicalprocess.logistics.logistics:GetCareContactResponder:2:rivtabp21", name = "GetCareContactInteraction")
public class CareContactTestProducer implements GetCareContactResponderInterface {

    private static final Logger log = LoggerFactory.getLogger(CareContactTestProducer.class);

    private TestProducerDb testDb;

    public void setTestDb(TestProducerDb testDb) {
        this.testDb = testDb;
    }

    @Override
    @WebResult(name = "GetCareContactResponse", targetNamespace = "urn:riv:clinicalprocess:logistics:logistics:GetCareContactResponder:2", partName = "parameters")
    @WebMethod(operationName = "GetCareContact", action = "urn:riv:ehr:patientsummary:GetCareContactResponder:2:GetCareContact")
    public GetCareContactResponseType getCareContact(
            @WebParam(partName = "LogicalAddress", name = "LogicalAddress", targetNamespace = "urn:riv:itintegration:registry:1", header = true) String logicalAddress,
            @WebParam(partName = "parameters", name = "GetCareContact", targetNamespace = "urn:riv:clinicalprocess:logistics:logistics:GetCareContactResponder:2") GetCareContactType request) {
        log.info("### Virtual service for GetCareContact call the source system with logical address: {} and patientId: {}", logicalAddress, request.getPatientId().getId());

        GetCareContactResponseType response = (GetCareContactResponseType)testDb.processRequest(logicalAddress, request.getPatientId().getId());
        if (response == null) {
            // Return an empty response object instead of null if nothing is found
            response = new GetCareContactResponseType();
        }

        log.info("### Virtual service got {} documents in the reply from the source system with logical address: {} and patientId: {}", new Object[] {response.getCareContact().size(), logicalAddress, request.getPatientId().getId()});

        // We are done
        return response;
    }

}