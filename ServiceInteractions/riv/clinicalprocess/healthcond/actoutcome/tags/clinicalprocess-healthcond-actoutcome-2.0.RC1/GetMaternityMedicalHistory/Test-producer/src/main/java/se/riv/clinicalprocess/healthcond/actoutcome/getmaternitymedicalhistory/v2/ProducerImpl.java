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
package se.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistory.v2;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import se.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistory.v2.rivtabp21.GetMaternityMedicalHistoryResponderInterface;
import se.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistoryresponder.v2.GetMaternityMedicalHistoryResponseType;
import se.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistoryresponder.v2.GetMaternityMedicalHistoryType;
import se.riv.clinicalprocess.healthcond.actoutcome.v2.AuthorType;
import se.riv.clinicalprocess.healthcond.actoutcome.v2.LegalAuthenticatorType;
import se.riv.clinicalprocess.healthcond.actoutcome.v2.MaternityMedicalRecordBodyType;
import se.riv.clinicalprocess.healthcond.actoutcome.v2.MaternityMedicalRecordType;
import se.riv.clinicalprocess.healthcond.actoutcome.v2.PatientIdType;
import se.riv.clinicalprocess.healthcond.actoutcome.v2.PatientSummaryHeaderType;


@WebService(
		serviceName = "GetMaternityMedicalHistoryResponderService",
        endpointInterface= "se.riv.clinicalprocess.healthcond.actoutcome.getreferraloutcome.v2.rivtabp21.GetMaternityMedicalHistoryResponderInterface",
        portName = "GetMaternityMedicalHistoryResponderPort",
		targetNamespace = "urn:riv:clinicalprocess:healthcond:actoutcome:GetMaternityMedicalHistory:2:rivtabp21",
		wsdlLocation = "interactions/GetMaternityMedicalHistoryInteraction/GetMaternityMedicalHistoryInteraction_2.0_RIVTABP21.wsdl")
public class ProducerImpl implements GetMaternityMedicalHistoryResponderInterface {

    @Override
    @WebResult(name = "GetMaternityMedicalHistoryResponse", targetNamespace = "urn:riv:clinicalprocess:healthcond:actoutcome:GetMaternityMedicalHistoryResponder:2", partName = "parameters")
    @WebMethod(operationName = "GetMaternityMedicalHistory", action = "urn:riv:clinicalprocess:healthcond:actoutcome:GetMaternityMedicalHistoryResponder:2:GetMaternityMedicalHistory")
    public GetMaternityMedicalHistoryResponseType getMaternityMedicalHistory(
            @WebParam(partName = "LogicalAddress", name = "LogicalAddress", targetNamespace = "urn:riv:itintegration:registry:1", header = true) String logicalAddress,
            @WebParam(partName = "parameters", name = "GetMaternityMedicalHistory", targetNamespace = "urn:riv:clinicalprocess:healthcond:actoutcome:GetMaternityMedicalHistoryResponder:2") GetMaternityMedicalHistoryType parameters) {
        GetMaternityMedicalHistoryResponseType responseType = new GetMaternityMedicalHistoryResponseType();
        
        MaternityMedicalRecordType record = new MaternityMedicalRecordType();
        MaternityMedicalRecordBodyType body = new MaternityMedicalRecordBodyType();
        
        // TODO: Fix body

        PatientSummaryHeaderType header = new PatientSummaryHeaderType();

        header.setDocumentId("documentId");
        header.setDocumentTitle("documentTitle");
        header.setSourceSystemHSAid("sourceSystemHSAid");
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

        LegalAuthenticatorType legalAuthenticatorType = new LegalAuthenticatorType();
        legalAuthenticatorType.setLegalAuthenticatorHSAid("legalAuthenticatorHSAid");
        legalAuthenticatorType.setSignatureTime("20130213121419");
        header.setLegalAuthenticator(legalAuthenticatorType);

        PatientIdType patientIdType = new PatientIdType();
        patientIdType.setId("1212121212");
        patientIdType.setType("1.2.752.129.2.1.3.1");
        header.setPatientId(patientIdType);

        record.setMaternityMedicalRecordHeader(header);
        record.setMaternityMedicalRecordBody(body);
        responseType.getMaternityMedicalRecord().add(record);
        
        return responseType;
    }

}
