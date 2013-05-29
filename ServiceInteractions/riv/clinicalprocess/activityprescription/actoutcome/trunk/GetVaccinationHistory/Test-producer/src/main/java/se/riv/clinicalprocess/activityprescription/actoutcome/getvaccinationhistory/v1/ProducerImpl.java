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
package se.riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistory.v1;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import se.riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistory.v1.rivtabp21.GetVaccinationHistoryResponderInterface;
import se.riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v1.GetVaccinationHistoryResponseType;
import se.riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v1.GetVaccinationHistoryType;
import se.riv.clinicalprocess.activityprescription.actoutcome.v1.AuthorType;
import se.riv.clinicalprocess.activityprescription.actoutcome.v1.PatientIdType;
import se.riv.clinicalprocess.activityprescription.actoutcome.v1.PatientSummaryHeaderType;
import se.riv.clinicalprocess.activityprescription.actoutcome.v1.RegistrationRecordType;
import se.riv.clinicalprocess.activityprescription.actoutcome.v1.VaccinationMedicalRecordBodyType;
import se.riv.clinicalprocess.activityprescription.actoutcome.v1.VaccinationMedicalRecordType;

@WebService(
		serviceName = "GetVaccinationHistoryResponderService",
        endpointInterface= "se.riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistory.v1.rivtabp21.GetVaccinationHistoryResponderInterface",
        portName = "GetVaccinationHistoryResponderPort",
		targetNamespace = "urn:riv:clinicalprocess:activityprescription:actoutcome:GetVaccinationHistory:1:rivtabp21",
		wsdlLocation = "interactions/GetVaccinationHistoryInteraction/GetVaccinationHistoryInteraction_1.0_RIVTABP21.wsdl")
public class ProducerImpl implements GetVaccinationHistoryResponderInterface {

    @Override
    @WebResult(name = "GetVaccinationHistoryResponse", targetNamespace = "urn:riv:clinicalprocess:activityprescription:actoutcome:GetVaccinationHistoryResponder:1", partName = "parameters")
    @WebMethod(operationName = "GetVaccinationHistory", action = "urn:riv:clinicalprocess:activityprescription:actoutcome:GetVaccinationHistoryResponder:2:GetVaccinationHistory")
    public GetVaccinationHistoryResponseType getVaccinationHistory(
            @WebParam(partName = "LogicalAddress", name = "LogicalAddress", targetNamespace = "urn:riv:itintegration:registry:1", header = true) String logicalAddress,
            @WebParam(partName = "parameters", name = "GetVaccinationHistory", targetNamespace = "urn:riv:clinicalprocess:activityprescription:actoutcome:GetVaccinationHistoryResponder:1") GetVaccinationHistoryType parameters) {

        VaccinationMedicalRecordType record = new VaccinationMedicalRecordType();
        PatientSummaryHeaderType header = new PatientSummaryHeaderType();
        PatientIdType patientId = new PatientIdType();
        patientId.setId(parameters.getPatientId().getId());
        patientId.setType("1.2.752.129.2.1.3.1");
        header.setPatientId(patientId);
        header.setApprovedForPatient(true);
        header.setSourceSystemHSAid(logicalAddress);
        header.setCareContactId("businessObjectId");
        header.setDocumentTime("20130528121010");;
        
        // TODO: Set stuff

        VaccinationMedicalRecordBodyType body = new VaccinationMedicalRecordBodyType();
        
        RegistrationRecordType regRecord = new RegistrationRecordType();
        // TODO: Set stuff
        
        body.setRegistrationRecord(regRecord);
        
        AuthorType author = new AuthorType();
        author.setCareUnitHSAid(logicalAddress); 
        author.setAuthorOrgUnitName("Vårdcentralen Kusten, Kärna"); 
        
        header.setAuthor(author);
        record.setVaccinationMedicalRecordHeader(header);
        record.setVaccinationMedicalRecordBody(body);
        
        GetVaccinationHistoryResponseType response = new GetVaccinationHistoryResponseType();
        response.getVaccinationMedicalRecord().add(record);
        return response;
    }

}
