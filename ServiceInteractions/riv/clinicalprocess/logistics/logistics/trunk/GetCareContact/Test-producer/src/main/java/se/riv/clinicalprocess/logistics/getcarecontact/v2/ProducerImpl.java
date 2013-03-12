/**
 * Copyright 2009 Sjukvardsradgivningen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public

 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the

 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,

 *   Boston, MA 02111-1307  USA
 */
package se.riv.clinicalprocess.logistics.getcarecontact.v2;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import se.riv.clinicalprocess.logistics.getcarecontact.v2.rivtabp21.GetCareContactResponderInterface;
import se.riv.clinicalprocess.logistics.getcarecontactresponder.v2.GetCareContactResponseType;
import se.riv.clinicalprocess.logistics.getcarecontactresponder.v2.GetCareContactType;
import se.riv.clinicalprocess.logistics.v2.AuthorType;
import se.riv.clinicalprocess.logistics.v2.CareContactBodyType;
import se.riv.clinicalprocess.logistics.v2.CareContactType;
import se.riv.clinicalprocess.logistics.v2.CareContactUnitType;
import se.riv.clinicalprocess.logistics.v2.PatientIdType;
import se.riv.clinicalprocess.logistics.v2.PatientSummaryHeaderType;


@WebService(
		serviceName = "GetCareDocumentationResponderService",
        endpointInterface= "se.riv.clinicalprocess.healthcond.description.getcaredocumentation.v2.rivtabp21.GetCareDocumentationResponderInterface",
        portName = "GetCareDocumentationResponderPort",
		targetNamespace = "urn:riv:clinicalprocess:healthcond:description:GetCareDocumentation:2:rivtabp21",
		wsdlLocation = "interactions/GetCareDocumentationInteraction/GetCareDocumentationInteraction_2.0_RIVTABP21.wsdl")
public class ProducerImpl implements GetCareContactResponderInterface {

    @Override
    @WebResult(name = "GetCareContactResponse", targetNamespace = "urn:riv:clinicalprocess:logistics:logistics:GetCareContactResponder:2", partName = "parameters")
    @WebMethod(operationName = "GetCareContact", action = "urn:riv:ehr:patientsummary:GetCareContactResponder:2:GetCareContact")
    public GetCareContactResponseType getCareContact(
            @WebParam(partName = "LogicalAddress", name = "LogicalAddress", targetNamespace = "urn:riv:itintegration:registry:1", header = true) String logicalAddress,
            @WebParam(partName = "parameters", name = "GetCareContact", targetNamespace = "urn:riv:clinicalprocess:logistics:logistics:GetCareContactResponder:2") GetCareContactType parameters) {
        GetCareContactResponseType responseType = new GetCareContactResponseType();
        CareContactType careContact = new CareContactType();
        
        CareContactBodyType body = new CareContactBodyType();
        body.setCareContactCode(0);
        body.setCareContactReason("reason");
        body.setCareContactStatus(0);
        body.setCareContactTime("20130213121419");
        
        CareContactUnitType unit = new CareContactUnitType();
        unit.setCareContactUnitAddress("address");
        unit.setCareContactUnitId("unitId");
        unit.setCareContactUnitName("name");
        
        body.getCareContactUnit().add(unit);
        careContact.setCareContactBody(body);
        
        PatientSummaryHeaderType header = new PatientSummaryHeaderType();
        header.setDocumentId("documentId");
        header.setDocumentTitle("documentTitle");
        header.setSourceSystem("sourceSystemHSAid");
        header.setDocumentTime("20130213121419");
        header.setApprovedForPatient(false);
        header.setCareContactId("careContactId");

        AuthorType authorType = new AuthorType();
        authorType.setAuthorTime("20130213121419");
        authorType.setAuthorHSAid("authorHSAid");
        authorType.setAuthorRoleCode("authorRoleCode");
        authorType.setAuthorName("authorName");
        authorType.setAuthorOrgUnitHSAid("authorOrgUnitHSAid");
        authorType.setAuthorOrgUnitName("careUnitName");
        authorType.setAuthorOrgUnitAddress("orgUnitAddress");
        authorType.setCareUnitHSAid("careUnitHSAid");
        authorType.setCareGiverHSAid("careGiverHSAid");
        header.setAuthor(authorType);
        
        PatientIdType patientIdType = new PatientIdType();
        patientIdType.setId("1212121212");
        patientIdType.setType("1.2.752.129.2.1.3.1");
        header.setPatientId(patientIdType);

        careContact.setCareContactHeader(header);
        responseType.getCareContact().add(careContact);

        return responseType;
    }

}
