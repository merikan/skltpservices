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
package se.skl.tp.insuranceprocess.healthreporting.sendmedcertquestion.consumer;

import iso.v21090.dt.v1.II;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;

import org.w3.wsaddressing10.AttributedURIType;

import se.skl.riv.insuranceprocess.healthreporting.qa.v1.Amnetyp;
import se.skl.riv.insuranceprocess.healthreporting.qa.v1.InnehallType;
import se.skl.riv.insuranceprocess.healthreporting.qa.v1.LakarutlatandeEnkelType;
import se.skl.riv.insuranceprocess.healthreporting.qa.v1.VardAdresseringsType;
import se.skl.riv.insuranceprocess.healthreporting.sendmedicalcertificatequestion.v1.rivtabp20.SendMedicalCertificateQuestionResponderInterface;
import se.skl.riv.insuranceprocess.healthreporting.sendmedicalcertificatequestion.v1.rivtabp20.SendMedicalCertificateQuestionResponderService;
import se.skl.riv.insuranceprocess.healthreporting.sendmedicalcertificatequestionsponder.v1.QuestionToFkType;
import se.skl.riv.insuranceprocess.healthreporting.sendmedicalcertificatequestionsponder.v1.SendMedicalCertificateQuestionResponseType;
import se.skl.riv.insuranceprocess.healthreporting.sendmedicalcertificatequestionsponder.v1.SendMedicalCertificateQuestionType;
import se.skl.riv.insuranceprocess.healthreporting.v2.EnhetType;
import se.skl.riv.insuranceprocess.healthreporting.v2.HosPersonalType;
import se.skl.riv.insuranceprocess.healthreporting.v2.PatientType;
import se.skl.riv.insuranceprocess.healthreporting.v2.VardgivareType;

public final class SendMedCertQuestionConsumer {

	// Use this one to connect via Virtualiseringsplattformen
	private static final String LOGISK_ADDRESS = "/SendMedicalCertificateQuestion/1/rivtabp20";

	// Use this one to connect directly (just for test)

	public static void main(String[] args) {
		String host = "localhost:19000/vard";
		if (args.length > 0) {
			host = args[0];
		}

		// Setup ssl info for the initial ?wsdl lookup...
		System.setProperty("javax.net.ssl.keyStore","../../certs/consumer.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "password");
		System.setProperty("javax.net.ssl.trustStore","../../certs/truststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "password");
		
		String adress = "https://" + host + LOGISK_ADDRESS;
		System.out.println("Consumer connecting to " + adress);
		String p = callSendMedicalCertificateQuestion("RIV TA BP2.0 Ref App OK", adress, "FKTransform");
		System.out.println("Returned: " + p);
	}

	public static String callSendMedicalCertificateQuestion(String id, String serviceAddress,
			String logicalAddresss) {

		SendMedicalCertificateQuestionResponderInterface service = new SendMedicalCertificateQuestionResponderService(
				createEndpointUrlFromServiceAddress(serviceAddress))
				.getSendMedicalCertificateQuestionResponderPort();

		AttributedURIType logicalAddressHeader = new AttributedURIType();
		logicalAddressHeader.setValue(logicalAddresss);

		SendMedicalCertificateQuestionType request = new SendMedicalCertificateQuestionType();
				
		try {
			// Set question
			request.setQuestion(getQuestion());

			SendMedicalCertificateQuestionResponseType result = service.sendMedicalCertificateQuestion(
					logicalAddressHeader, request);

			return ("Result id = " + result.getResult().getResultCode() + ".");

		} catch (Exception ex) {
			System.out.println("Exception=" + ex.getMessage());
			return null;
		}
	}

	private static QuestionToFkType getQuestion() throws Exception {
		QuestionToFkType meddelande = new QuestionToFkType();
		
		// Avsändare
		VardAdresseringsType avsandare = new VardAdresseringsType();		
		HosPersonalType hosPersonal = new HosPersonalType();
		EnhetType enhet = new EnhetType();	
		II enhetsId = new II();
		enhetsId.setRoot("1.2.752.129.2.1.4.1");
		enhetsId.setExtension("Enköpings lasaretts HSA-ID");
		enhet.setEnhetsId(enhetsId);
		enhet.setTelefonnummer("018-611 45 30");
		enhet.setPostadress("Akademiska sjukhuset");
		enhet.setPostnummer("751 85");
		enhet.setPostort("Uppsala");
		enhet.setEnhetsnamn("Kir mott UAS/KIR");
		VardgivareType vardgivare = new VardgivareType();
		vardgivare.setVardgivarnamn("Landstinget i Uppsala");
		II vardgivareId = new II();
		vardgivareId.setRoot("1.2.752.129.2.1.4.1");
		vardgivareId.setExtension("Uppsala landstings HSA-ID");
		vardgivare.setVardgivareId(vardgivareId);
		enhet.setVardgivare(vardgivare);
		hosPersonal.setEnhet(enhet);
		hosPersonal.setFullstandigtNamn("Erik Åselius");
		II personalId = new II();
		personalId.setRoot("1.2.752.129.2.1.4.1");
		personalId.setExtension("Personal HSA-ID");
		hosPersonal.setPersonalId(personalId);
		avsandare.setHosPersonal(hosPersonal);
		meddelande.setAdressVard(avsandare);
				
		// Avsänt tidpunkt - nu
		meddelande.setAvsantTidpunkt(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));

		// Set läkarutlåtande enkel från vården
		meddelande.setVardReferensId("Referens till fråga från vården");
		LakarutlatandeEnkelType lakarutlatandeEnkel = new LakarutlatandeEnkelType();
		PatientType patient = new PatientType();
		II personId = new II();
		personId.setRoot("1.2.752.129.2.1.3.1"); // OID för samordningsnummer är 1.2.752.129.2.1.3.3.
		personId.setExtension("19430811-7094");
		patient.setPersonId(personId);
		patient.setFullstandigtNamn("Lab Testsson"); 
		lakarutlatandeEnkel.setPatient(patient);
		lakarutlatandeEnkel.setLakarutlatandeId("xxx");
		lakarutlatandeEnkel.setSigneringsTidpunkt(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		meddelande.setLakarutlatande(lakarutlatandeEnkel);
	
		// Set ämne
		meddelande.setAmne(Amnetyp.AVSTAMNINGSMOTE);
		
		// Set meddelande - fraga
		InnehallType fraga = new InnehallType();
		fraga.setMeddelandeText("Meddelandetetext");
		fraga.setSigneringsTidpunkt(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		meddelande.setFraga(fraga);
		
		return meddelande;
	}
	
	public static URL createEndpointUrlFromServiceAddress(String serviceAddress) {
		try {
			return new URL(serviceAddress + "?wsdl");
		} catch (MalformedURLException e) {
			throw new RuntimeException("Malformed URL Exception: "
					+ e.getMessage());
		}
	}
}
