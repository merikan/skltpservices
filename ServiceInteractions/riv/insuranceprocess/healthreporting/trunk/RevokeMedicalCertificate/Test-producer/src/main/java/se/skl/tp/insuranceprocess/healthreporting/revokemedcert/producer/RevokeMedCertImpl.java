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
package se.skl.tp.insuranceprocess.healthreporting.revokemedcert.producer;

import javax.jws.WebService;

import org.w3.wsaddressing10.AttributedURIType;

import se.skl.riv.insuranceprocess.healthreporting.revokemedicalcertificate.v1.rivtabp20.RevokeMedicalCertificateResponderInterface;
import se.skl.riv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateRequestType;
import se.skl.riv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateResponseType;
import se.skl.riv.insuranceprocess.healthreporting.v2.ErrorIdEnum;
import se.skl.riv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.skl.riv.insuranceprocess.healthreporting.v2.ResultOfCall;


@WebService(
		serviceName = "RevokeMedicalCertificateResponderService", 
		endpointInterface="se.skl.riv.insuranceprocess.healthreporting.revokemedicalcertificate.v1.rivtabp20.RevokeMedicalCertificateResponderInterface", 
		portName = "RevokeMedicalCertificateResponderPort", 
		targetNamespace = "urn:riv:insuranceprocess:healthreporting:RevokeMedicalCertificate:1:rivtabp20",
		wsdlLocation = "schemas/vard/interactions/RevokeMedicalCertificateInteraction/RevokeMedicalCertificateInteraction_1.0_rivtabp20.wsdl")
public class RevokeMedCertImpl implements RevokeMedicalCertificateResponderInterface {

	public RevokeMedicalCertificateResponseType revokeMedicalCertificate(
			AttributedURIType logicalAddress,
			RevokeMedicalCertificateRequestType parameters) {
		try {
			RevokeMedicalCertificateResponseType response = new RevokeMedicalCertificateResponseType();
			String logiskAdress = logicalAddress.getValue();
			String name = "dummy";
			// Check if we should throw some kind of exception or simulate a timeout.
			if (    parameters != null && parameters.getRevoke().getLakarutlatande() != null && 
					parameters.getRevoke().getLakarutlatande().getPatient() != null && 
					parameters.getRevoke().getLakarutlatande().getPatient().getFullstandigtNamn() != null &&
					parameters.getRevoke().getLakarutlatande().getPatient().getFullstandigtNamn().length() > 0) {

				name = parameters.getRevoke().getLakarutlatande().getPatient().getFullstandigtNamn();
			}
				
			if (name.equalsIgnoreCase("Error") || logiskAdress.contains("Error") ) {
				ResultOfCall result = new ResultOfCall();
				result.setResultCode(ResultCodeEnum.ERROR);
				result.setErrorId(ErrorIdEnum.APPLICATION_ERROR);
				response.setResult(result);
				System.out.println("Returned Error for not validating!");
			} else if (name.equalsIgnoreCase("NoCertificate") || logiskAdress.contains("NoCertificate")) {
				ResultOfCall result = new ResultOfCall();
				result.setResultCode(ResultCodeEnum.ERROR);
				result.setErrorId(ErrorIdEnum.APPLICATION_ERROR);
				result.setErrorText("No Medical Certificate found that could be sent");
				response.setResult(result);
				System.out.println("Returned Error for medical certificate not found!");
			} else if (name.equalsIgnoreCase("AlreadyRevoked") || logiskAdress.contains("AlreadyRevoked")) {
				ResultOfCall result = new ResultOfCall();
				result.setResultCode(ResultCodeEnum.INFO);
				result.setInfoText("Medical Certificate already revoke");
				response.setResult(result);
				System.out.println("Returned Info for already revoked certificate!");
			} else if (name.equalsIgnoreCase("Exception") || logiskAdress.contains("Exception")) {
				System.out.println("Returned Exception for not validating!");
				throw new RuntimeException("Exception called");
			} else if (name.equalsIgnoreCase("Timeout") || logiskAdress.contains("Timeout")) {
				System.out.println("Returned Timeout for not validating!");
				Thread.currentThread();
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				ResultOfCall resCall = new ResultOfCall();
				resCall.setResultCode(ResultCodeEnum.OK);
				response.setResult(resCall);				
				System.out.println("Returned OK for not validating!");
			}

			return response;
		} catch (RuntimeException e) {
			System.out.println("Error occured: " + e);
			throw e;
		}
	}
}