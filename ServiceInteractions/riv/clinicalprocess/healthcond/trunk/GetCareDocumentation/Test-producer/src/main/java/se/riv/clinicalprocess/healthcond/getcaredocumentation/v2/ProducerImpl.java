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
package se.riv.clinicalprocess.healthcond.getcaredocumentation.v2;

import se.riv.clinicalprocess.healthcond.description.enums.v2.ClinicalDocumentNoteCodeEnum;
import se.riv.clinicalprocess.healthcond.description.getcaredocumentation.v2.rivtabp21.GetCareDocumentationResponderInterface;
import se.riv.clinicalprocess.healthcond.description.getcaredocumentationresponder.v1.GetCareDocumentationResponseType;
import se.riv.clinicalprocess.healthcond.description.getcaredocumentationresponder.v1.GetCareDocumentationType;
import se.riv.clinicalprocess.healthcond.description.v2.*;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;


@WebService(
		serviceName = "GetCareDocumentationResponderService",
        endpointInterface= "se.riv.clinicalprocess.healthcond.description.getcaredocumentation.v2.rivtabp21.GetCareDocumentationResponderInterface",
        portName = "GetCareDocumentationResponderPort",
		targetNamespace = "urn:riv:clinicalprocess:healthcond:description:GetCareDocumentation:2:rivtabp21",
		wsdlLocation = "schemas/interactions/GetCareDocumentationInteraction/GetCareDocumentationInteraction_2.0_RIVTABP21.wsdl")
public class ProducerImpl implements GetCareDocumentationResponderInterface {

    @Override
    public GetCareDocumentationResponseType getCareDocumentation(@WebParam(partName = "LogicalAddress", name = "LogicalAddress", targetNamespace = "urn:riv:itintegration:registry:1", header = true) String s, @WebParam(partName = "parameters", name = "GetCareDocumentation", targetNamespace = "urn:riv:clinicalprocess:healthcond:description:GetCareDocumentationResponder:2") GetCareDocumentationType getCareDocumentationType) {
        GetCareDocumentationResponseType responseType = new GetCareDocumentationResponseType();

        List<CareDocumentationType> careDocumentation = responseType.getCareDocumentation();
        CareDocumentationType documentationType = new CareDocumentationType();
        CareDocumentationBodyType body = new CareDocumentationBodyType();
        ClinicalDocumentNoteType note = new ClinicalDocumentNoteType();

        note.setClinicalDocumentNoteCode(ClinicalDocumentNoteCodeEnum.UTR);
        note.setClinicalDocumentNoteText("clinicalDocumentNoteText");
        note.setClinicalDocumentNoteTitle("clinicalDocumentNoteTitle");

        body.setClinicalDocumentNote(note);

        MultimediaType multimediaType = new MultimediaType();
        multimediaType.setId("MM1");
        multimediaType.setMediaType("image/jpeg");
        multimediaType.setValue("default".getBytes());
        multimediaType.setReference("Reference");
        body.getMultimediaEntry().add(multimediaType);

        documentationType.setCareDocumentationBody(body);

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

        LegalAuthenticatorType legalAuthenticatorType = new LegalAuthenticatorType();
        legalAuthenticatorType.setLegalAuthenticatorHSAid("legalAuthenticatorHSAid");
        legalAuthenticatorType.setSignatureTime("20130213121419");
        header.setLegalAuthenticator(legalAuthenticatorType);

        PatientIdType patientIdType = new PatientIdType();
        patientIdType.setId("1212121212");
        patientIdType.setType("1.2.752.129.2.1.3.1");
        header.setPatientId(patientIdType);

        documentationType.setCareDocumentationHeader(header);
        responseType.getCareDocumentation().add(documentationType);

        return responseType;
    }
}
