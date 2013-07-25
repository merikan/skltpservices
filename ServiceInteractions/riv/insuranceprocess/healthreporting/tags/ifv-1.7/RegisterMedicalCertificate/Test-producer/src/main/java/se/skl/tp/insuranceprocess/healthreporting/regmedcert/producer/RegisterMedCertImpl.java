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
package se.skl.tp.insuranceprocess.healthreporting.regmedcert.producer;

import javax.jws.WebService;

import org.w3.wsaddressing10.AttributedURIType;

import se.skl.riv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.skl.riv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.skl.riv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.skl.riv.insuranceprocess.healthreporting.v2.ErrorIdEnum;
import se.skl.riv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.skl.riv.insuranceprocess.healthreporting.v2.ResultOfCall;


@WebService(
		serviceName = "RegisterMedicalCertificateResponderService", 
		endpointInterface="se.skl.riv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface", 
		portName = "RegisterMedicalCertificateResponderPort", 
		targetNamespace = "urn:riv:insuranceprocess:healthreporting:RegisterMedicalCertificate:3:rivtabp20",
		wsdlLocation = "schemas/vard/interactions/RegisterMedicalCertificateInteraction/RegisterMedicalCertificateInteraction_3.1_rivtabp20.wsdl")
public class RegisterMedCertImpl implements RegisterMedicalCertificateResponderInterface {

	public RegisterMedicalCertificateResponseType registerMedicalCertificate(
			AttributedURIType logicalAddress,
			RegisterMedicalCertificateType parameters) {
		try {
			RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();
						
			String logiskAdress = logicalAddress.getValue();
			String name = "dummy";
			// Check if we should throw some kind of exception or simulate a timeout.
			if (    parameters != null && parameters.getLakarutlatande() != null && 
					parameters.getLakarutlatande().getPatient() != null && 
					parameters.getLakarutlatande().getPatient().getFullstandigtNamn() != null &&
					parameters.getLakarutlatande().getPatient().getFullstandigtNamn().length() > 0) {

				name = parameters.getLakarutlatande().getPatient().getFullstandigtNamn();
			}
				
			if (name.equalsIgnoreCase("Error") || logiskAdress.contains("Error") ) {
				ResultOfCall result = new ResultOfCall();
				result.setResultCode(ResultCodeEnum.ERROR);
				result.setErrorId(ErrorIdEnum.APPLICATION_ERROR);
				response.setResult(result);
				System.out.println("Returned Error for not validating!");
			} else if (name.equalsIgnoreCase("AlreadyRegistered") || logiskAdress.contains("AlreadyRegistered")) {
				ResultOfCall result = new ResultOfCall();
				result.setResultCode(ResultCodeEnum.INFO);
				result.setInfoText("Medical Certificate already registered");
				response.setResult(result);
				System.out.println("Returned Info for already registered certificate!");
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
			throw e;
		}
	}
}