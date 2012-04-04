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
package se.skl.tp.clinicalprocess.requestworkflow.processrequest.mockservice;

import javax.jws.WebService;
import lombok.extern.slf4j.Slf4j;
import se.riv.clinicalprocess.requestworkflow.processrequest.v1.ProcessRequestResponderInterface;
import se.riv.clinicalprocess.requestworkflow.processrequest.v1.ProcessRequestResponseType;
import se.riv.clinicalprocess.requestworkflow.processrequest.v1.ProcessRequestType;
import se.riv.clinicalprocess.requestworkflow.v1.ResultCodeEnum;

/**
 * Validation class that will certify a webservice call made for a question regarding a medical certificate.. We will check mandatory/optional fields and all other declared rules.
 * @author matsek
 *
 */

@WebService(
		serviceName = "ReceiveMedicalCertificateQuestionResponderService", 
		endpointInterface="se.skl.riv.insuranceprocess.healthreporting.receivemedicalcertificatequestion.v1.rivtabp20.ReceiveMedicalCertificateQuestionResponderInterface", 
		portName = "ReceiveMedicalCertificateQuestionResponderPort", 
		targetNamespace = "urn:riv:insuranceprocess:healthreporting:ReceiveMedicalCertificateQuestion:1:rivtabp20",
		wsdlLocation = "schemas/vard/interactions/ReceiveMedicalCertificateQuestionInteraction/ReceiveMedicalCertificateQuestionInteraction_1.0_rivtabp20.wsdl")
@Slf4j
public class MockServiceImpl implements ProcessRequestResponderInterface {

    @Override
    public ProcessRequestResponseType processRequest(String string, ProcessRequestType parameters) {
		ProcessRequestResponseType outResponse = new ProcessRequestResponseType();
		outResponse.setResultCode(ResultCodeEnum.OK);
        outResponse.setComment("MockServiceImpl");

		try {
			// Send a new Answer before answering to this request
			MockServiceAnswer autoanswer = new MockServiceAnswer(parameters.getRequest());
			autoanswer.start();
					
			return outResponse;
		} catch (Exception e) {
			outResponse.setComment(e.getMessage());
			outResponse.setResultCode(ResultCodeEnum.ERROR);
			log.error("Autosvar exception: " + e.getMessage());
			return outResponse;
		}

    }
}