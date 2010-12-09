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
package se.skl.tp.insuranceprocess.healthreporting.sendmedcertanswer.consumer;

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
import se.skl.riv.insuranceprocess.healthreporting.sendmedicalcertificateanswer.v1.rivtabp20.SendMedicalCertificateAnswerResponderInterface;
import se.skl.riv.insuranceprocess.healthreporting.sendmedicalcertificateanswer.v1.rivtabp20.SendMedicalCertificateAnswerResponderService;
import se.skl.riv.insuranceprocess.healthreporting.sendmedicalcertificateanswerresponder.v1.AnswerToFkType;
import se.skl.riv.insuranceprocess.healthreporting.sendmedicalcertificateanswerresponder.v1.SendMedicalCertificateAnswerResponseType;
import se.skl.riv.insuranceprocess.healthreporting.sendmedicalcertificateanswerresponder.v1.SendMedicalCertificateAnswerType;
import se.skl.riv.insuranceprocess.healthreporting.v2.EnhetType;
import se.skl.riv.insuranceprocess.healthreporting.v2.HosPersonalType;
import se.skl.riv.insuranceprocess.healthreporting.v2.PatientType;
import se.skl.riv.insuranceprocess.healthreporting.v2.VardgivareType;

public final class SendMedCertAnswerConsumer {

	// Use this one to connect via Virtualiseringsplattformen
	private static final String LOGISK_ADDRESS = "/SendMedicalCertificateAnswer/1/rivtabp20";

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
		String p = callSendMedicalCertificateAnswer("RIV TA BP2.0 Ref App OK", adress, "FKTransform");
		System.out.println("Returned: " + p);
	}

	public static String callSendMedicalCertificateAnswer(String id, String serviceAddress,
			String logicalAddresss) {

		SendMedicalCertificateAnswerResponderInterface service = new SendMedicalCertificateAnswerResponderService(
				createEndpointUrlFromServiceAddress(serviceAddress)).getSendMedicalCertificateAnswerResponderPort();

		AttributedURIType logicalAddressHeader = new AttributedURIType();
		logicalAddressHeader.setValue(logicalAddresss);

		SendMedicalCertificateAnswerType request = new SendMedicalCertificateAnswerType();
		
		try {
			// Set an answer 
			request.setAnswer(getAnswer());

			SendMedicalCertificateAnswerResponseType result = service.sendMedicalCertificateAnswer(logicalAddressHeader, request);

			return ("Result id = " + result.getResult().getResultCode() + ".");

		} catch (Exception ex) {
			System.out.println("Exception=" + ex.getMessage());
			return null;
		}
	}

	private static AnswerToFkType getAnswer() throws Exception {
		AnswerToFkType meddelande = new AnswerToFkType();
		
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
		meddelande.setVardReferensId("Referens till svaret i vården");
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
	
		// Set Försäkringskassans id
		meddelande.setFkReferensId("Referens till FKs fråga");

		// Set ämne
		meddelande.setAmne(Amnetyp.KONTAKT);
		
		// Set meddelande	
		InnehallType svar = new InnehallType();
		svar.setMeddelandeText("Meddelandetext");
		svar.setSigneringsTidpunkt(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		meddelande.setSvar(svar);

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
