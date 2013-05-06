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

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import se.riv.clinicalprocess.logistics.getcarecontacts.v2.rivtabp21.GetCareContactsResponderInterface;
import se.riv.clinicalprocess.logistics.getcarecontactsresponder.v2.GetCareContactsResponseType;
import se.riv.clinicalprocess.logistics.getcarecontactsresponder.v2.GetCareContactsType;
import se.riv.clinicalprocess.logistics.v2.CareContactBodyType;
import se.riv.clinicalprocess.logistics.v2.CareContactType;
import se.riv.clinicalprocess.logistics.v2.CareContactUnitType;
import se.riv.clinicalprocess.logistics.v2.HealthcareProfessionalType;
import se.riv.clinicalprocess.logistics.v2.PatientIdType;
import se.riv.clinicalprocess.logistics.v2.PatientSummaryHeaderType;
import se.riv.clinicalprocess.logistics.v2.TimePeriodType;


@WebService(
		serviceName = "GetCareContactsResponderService",
        endpointInterface= "se.riv.clinicalprocess.logistics.logistics.getcarecontacts.v2.rivtabp21.GetCareContactsResponderInterface",
        portName = "GetCareContactsResponderPort",
		targetNamespace = "urn:riv:clinicalprocess:logistics:logistics:GetCareContacts:2:rivtabp21",
		wsdlLocation = "interactions/GetCareContactInteraction/GetCareContactsInteraction_2.0_RIVTABP21.wsdl")
public class ProducerImpl implements GetCareContactsResponderInterface {

    @Override
    @WebResult(name = "GetCareContactResponse", targetNamespace = "urn:riv:clinicalprocess:logistics:logistics:GetCareContactsResponder:2", partName = "parameters")
    @WebMethod(operationName = "GetCareContacts", action = "urn:riv:ehr:patientsummary:GetCareContactsResponder:2:GetCareContacts")
    public GetCareContactsResponseType getCareContacts(
            @WebParam(partName = "LogicalAddress", name = "LogicalAddress", targetNamespace = "urn:riv:itintegration:registry:1", header = true) String logicalAddress,
            @WebParam(partName = "parameters", name = "GetCareContacts", targetNamespace = "urn:riv:clinicalprocess:logistics:logistics:GetCareContactsResponder:2") GetCareContactsType parameters) {
        GetCareContactsResponseType responseType = new GetCareContactsResponseType();
        CareContactType careContact = new CareContactType();
        
        CareContactBodyType body = new CareContactBodyType();
        body.setCareContactCode(0);
        body.setCareContactReason("reason");
        body.setCareContactStatus(0);
        
        TimePeriodType timePeriod = new TimePeriodType();
        timePeriod.setEnd("20130213121419");
        timePeriod.setStart("20130113121419");
        body.setCareContactTimePeriod(timePeriod);
        
        CareContactUnitType unit = new CareContactUnitType();
        unit.setCareContactUnitAddress("address");
        unit.setCareContactUnitId("unitId");
        unit.setCareContactUnitName("name");
        
        body.getCareContactUnit().add(unit);
        careContact.setCareContactBody(body);
        
        PatientSummaryHeaderType header = new PatientSummaryHeaderType();
        header.setCareContactId("careContactId");
        header.setSourceSystemHSAid("sourceSystemHSAid");
        header.setDocumentTime("20130213121419");
        header.setApprovedForPatient(false);
        header.setCareContactId("careContactId");

        HealthcareProfessionalType authorType = new HealthcareProfessionalType();
        authorType.setHealthcareProfessionalHsaId("authorHSAid");
        authorType.setHealthcareProfessionalRoleCode("authorRoleCode");
        authorType.setHealthcareProfessionalName("authorName");
        authorType.setHealthcareProfessionalCareUnitHSAid("careUnitHSAid");
        authorType.setHealthcareProfessionalCareGiverHSAid("careGiverHSAid");
        header.setAccountableHealthcareProfessional(authorType);
        
        PatientIdType patientIdType = new PatientIdType();
        patientIdType.setId("1212121212");
        patientIdType.setType("1.2.752.129.2.1.3.1");
        header.setPatientId(patientIdType);

        careContact.setCareContactHeader(header);
        responseType.getCareContact().add(careContact);

        return responseType;
    }

}
