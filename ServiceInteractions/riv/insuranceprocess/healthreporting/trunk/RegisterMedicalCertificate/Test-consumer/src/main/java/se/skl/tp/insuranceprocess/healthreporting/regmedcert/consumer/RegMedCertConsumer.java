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
package se.skl.tp.insuranceprocess.healthreporting.regmedcert.consumer;

import iso.v21090.dt.v1.CD;
import iso.v21090.dt.v1.II;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.w3.wsaddressing10.AttributedURIType;

import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.AktivitetType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.Aktivitetskod;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaNedsattningType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.ArbetsuppgiftType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.BedomtTillstandType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.FunktionstillstandType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.MedicinsktTillstandType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.Nedsattningsgrad;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.Prognosangivelse;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.SysselsattningType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.TypAvFunktionstillstand;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.TypAvSysselsattning;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.VardkontaktType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.Vardkontakttyp;
import se.skl.riv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.skl.riv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderService;
import se.skl.riv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.skl.riv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.skl.riv.insuranceprocess.healthreporting.v2.EnhetType;
import se.skl.riv.insuranceprocess.healthreporting.v2.HosPersonalType;
import se.skl.riv.insuranceprocess.healthreporting.v2.PatientType;
import se.skl.riv.insuranceprocess.healthreporting.v2.VardgivareType;


public final class RegMedCertConsumer {

	// Use this one to connect via Virtualiseringsplattformen
	private static final String LOGISK_ADDRESS = "/RegisterMedicalCertificate/2/rivtabp20";

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
		String p = callRegisterMedicalCertificate("RIV TA BP2.0 Ref App OK", adress, "FKTransform");
		System.out.println("Returned: " + p);
	}

	public static String callRegisterMedicalCertificate(String id, String serviceAddress,
			String logicalAddresss) {

		RegisterMedicalCertificateResponderInterface service = new RegisterMedicalCertificateResponderService(
				createEndpointUrlFromServiceAddress(serviceAddress))
				.getRegisterMedicalCertificateResponderPort();

		AttributedURIType logicalAddressHeader = new AttributedURIType();
		logicalAddressHeader.setValue(logicalAddresss);

		RegisterMedicalCertificateType request = new RegisterMedicalCertificateType();
				
		try {
			// Läkarintyg
			request.setLakarutlatande(getComplexLakarintyg());

			RegisterMedicalCertificateResponseType result = service.registerMedicalCertificate(
					logicalAddressHeader, request);

			return ("Result id = " + result.getResult().getResultCode() + ".");

		} catch (Exception ex) {
			System.out.println("Exception=" + ex.getMessage());
			return null;
		}
	}
	
	private static LakarutlatandeType getSimpleLakarintyg() throws Exception {
		LakarutlatandeType lakarutlatande = new LakarutlatandeType();
		
		// Id -- Metadata
		UUID uuid = UUID.randomUUID();
		lakarutlatande.setLakarutlatandeId(uuid.toString());

		// Typ av utlåtande -- Metadata rubrik
		lakarutlatande.setTypAvUtlatande("Medicinskt underlag för bedömning av förmåga att arbeta vid sjukdom");
		
		// Skickat datum och tid -- Nu!
		lakarutlatande.setSkickatDatum(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		
		// Patientdata -- Längst upp till höger på MU
		PatientType patient = new PatientType();
		II personId = new II();
		personId.setRoot("1.2.752.129.2.1.3.1"); // OID för samordningsnummer är 1.2.752.129.2.1.3.3.
		personId.setExtension("19430811-7094");
		patient.setPersonId(personId);
		patient.setFullstandigtNamn("Mats Testsson"); 
		lakarutlatande.setPatient(patient);

		// Skapad av HoS personal -- Längst upp till vänster på MU och/eller fält 16 och fält 17 
		HosPersonalType hosPersonal = new HosPersonalType();
		EnhetType enhet = new EnhetType();	
		II enhetsId = new II();
		enhetsId.setRoot("1.2.752.129.2.1.4.1");
		enhetsId.setExtension("Enköpings lasaretts HSA-ID");
		enhet.setEnhetsId(enhetsId);
		enhet.setTelefonnummer("018-611 45 30");
//		enhet.setAdress("");  // Ej satt!
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
		lakarutlatande.setSkapadAvHosPersonal(hosPersonal);

		// Aktivitet - Fält 1 - ej ifyllt
//		AktivitetType aktivitet_falt1 = new AktivitetType();
//		aktivitet_falt1.setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
//		aktivitet_falt1.setBeskrivning("Patienten har gulsot!");
//		lakarutlatande.getAktivitet().add(aktivitet_falt1);

		// Medicinskt tillstånd - Fält 2
		MedicinsktTillstandType medicinsktTillstand = new MedicinsktTillstandType();
		medicinsktTillstand.setBeskrivning("Akut sinuit med biverkningar");
		CD medicinsktTillstandKod = new CD();
		medicinsktTillstandKod.setCode("J01");
		medicinsktTillstandKod.setCodeSystemName("ICD-10"); // Enbart ICD-10 !	
		medicinsktTillstand.setTillstandskod(medicinsktTillstandKod);
		lakarutlatande.setMedicinsktTillstand(medicinsktTillstand);

		// Bedömt tillstånd - Fält 3
		BedomtTillstandType bedommtTillstand = new BedomtTillstandType();
		bedommtTillstand.setBeskrivning("Förkylning, nästäppa, halsont");
		lakarutlatande.setBedomtTillstand(bedommtTillstand);

		// Funktionstillstånd - Fält 4 vänster
		FunktionstillstandType funktionstillstand_falt4 = new FunktionstillstandType();
		funktionstillstand_falt4.setTypAvFunktionstillstand(TypAvFunktionstillstand.KROPPSFUNKTION);
		funktionstillstand_falt4.setBeskrivning("Kraftig förtätning bihålor bilateralt");
		lakarutlatande.getFunktionstillstand().add(funktionstillstand_falt4);
		
		// Vårdkontakt - Fält 4 höger och översta kryssrutan
		VardkontaktType vardkontakt_falt4_kryss1 = new VardkontaktType();
		vardkontakt_falt4_kryss1.setVardkontaktstid(getDate("2010-01-26"));
		vardkontakt_falt4_kryss1.setVardkontakttyp(Vardkontakttyp.MIN_UNDERSOKNING_AV_PATIENTEN);
		lakarutlatande.getVardkontakt().add(vardkontakt_falt4_kryss1);

		// Vårdkontakt - Fält 4 höger och näst översta kryssrutan - ej ifyllt!
//		VardkontaktType vardkontakt_falt4_kryss2 = new VardkontaktType();
//		vardkontakt_falt4_kryss2.setVardkontaktstid(getDate("20101012"));
//		vardkontakt_falt4_kryss2.setVardkontakttyp(Vardkontakttyp.MIN_TELEFONKONTAKT_MED_PATIENTEN);
//		lakarutlatande.getVardkontakt().add(vardkontakt_falt4_kryss2);

//		// Referens - Fält 4 höger näst nedersta kryssrutan ej ifylld!
//		ReferensType referensJournaluppgift = new ReferensType();
//		referensJournaluppgift.setDatum(getDate("20101012"));
//		referensJournaluppgift.setReferenstyp(Referenstyp.JOURNALUPPGIFTER);
//		lakarutlatande.getReferens().add(referensJournaluppgift);
//
//		// Referens - Fält 4 höger nedersta kryssrutan ej ifylld!
//		ReferensType referensAnnat = new ReferensType();
//		referensJournaluppgift.setDatum(getDate("20101012"));
//		referensJournaluppgift.setReferenstyp(Referenstyp.ANNAT);
//		lakarutlatande.getReferens().add(referensAnnat);
		
		// Funktionstillstånd - Fält 5
		FunktionstillstandType funktionstillstand_falt5 = new FunktionstillstandType();
		funktionstillstand_falt5.setTypAvFunktionstillstand(TypAvFunktionstillstand.AKTIVITET);
		funktionstillstand_falt5.setBeskrivning("Bör avstå från att träffa andra människor då patienten är i ett smittsamt stadium av sjukdomen.");
		lakarutlatande.getFunktionstillstand().add(funktionstillstand_falt5);

		// Aktivitet - Fält 6a kan vara ett eller alla kryss!
		AktivitetType aktivitet_falt6_3 = new AktivitetType();
		aktivitet_falt6_3.setAktivitetskod(Aktivitetskod.OVRIGT);
		aktivitet_falt6_3.setBeskrivning("Näsdroppar x 4");
		lakarutlatande.getAktivitet().add(aktivitet_falt6_3);

		// Aktivitet - Fält 6b kan vara ett eller två kryss!
		
		// Aktivitet - Fält 7 kan vara endast en av flera val!
		AktivitetType aktivitet_falt7 = new AktivitetType();
		aktivitet_falt7.setAktivitetskod(Aktivitetskod.ARBETSLIVSINRIKTAD_REHABILITERING_AR_EJ_AKTUELL);
		lakarutlatande.getAktivitet().add(aktivitet_falt7);

		// Instans av arbetsförmåga som håller flera fält definieras
		ArbetsformagaType arbetsformaga = new ArbetsformagaType();
	
		// Nuvarande sysselsättning - Fält 8a välj 1 eller 2 eller 3
		SysselsattningType sysselsattning = new SysselsattningType();
		sysselsattning.setTypAvSysselsattning(TypAvSysselsattning.NUVARANDE_ARBETE);
		ArbetsuppgiftType arbetsuppgift = new ArbetsuppgiftType();
		arbetsuppgift.setTypAvArbetsuppgift("Administrativa sysslor av journalsystem inom landstinget");

		// Bedömning arbetsförmåga - Fält 8b mellan 1 till 4 bedömningar kan göras med olika tidsintervall
		ArbetsformagaNedsattningType arbetsformagaNedsattning = new ArbetsformagaNedsattningType();
		arbetsformagaNedsattning.setNedsattningsgrad(Nedsattningsgrad.HELT_NEDSATT);
		arbetsformagaNedsattning.setVaraktighetFrom(getDate("2010-01-26"));
		arbetsformagaNedsattning.setVaraktighetTom(getDate("2010-02-08"));		
		arbetsformaga.getArbetsformagaNedsattning().add(arbetsformagaNedsattning);

		// Arbetsförmåga kommentar - Fält 9 - ej ifyllt
//		arbetsformaga.setMotivering("");

		// Prognos - Fält 10
		arbetsformaga.setPrognosangivelse(Prognosangivelse.ATERSTALLAS_HELT);

		// Lägg till instansierad data
		arbetsformaga.getSysselsattning().add(sysselsattning);
		arbetsformaga.setArbetsuppgift(arbetsuppgift);
		funktionstillstand_falt5.setArbetsformaga(arbetsformaga);
		
		// Åtgärd resor- Fält 11
		AktivitetType aktivitet_falt11 = new AktivitetType();
		aktivitet_falt11.setAktivitetskod(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT);
		lakarutlatande.getAktivitet().add(aktivitet_falt11);

		// Kontakt med FK - Fält 12 Ingen kontakt önskas
		
		// Kommentar - fält 13
		lakarutlatande.setKommentar("Snabbt övergående förkylning.");

		// Signeringsdatum - fält 14
		lakarutlatande.setSigneringsdatum(getDate("2010-01-28"));

		// Arbetsplatskod - Fält 17
		II arbetsplatskod = new II();
		arbetsplatskod.setRoot("1.2.752.29.4.71");
		arbetsplatskod.setExtension("Arbetsplatskoden");
		lakarutlatande.getSkapadAvHosPersonal().getEnhet().setArbetsplatskod(arbetsplatskod);		
		
		return lakarutlatande;
	}

	private static LakarutlatandeType getComplexLakarintyg() throws Exception {
		LakarutlatandeType lakarutlatande = new LakarutlatandeType();
		
		// Id -- Metadata
		UUID uuid = UUID.randomUUID();
		lakarutlatande.setLakarutlatandeId(uuid.toString());

		// Typ av utlåtande -- Metadata rubrik
		lakarutlatande.setTypAvUtlatande("Medicinskt underlag för bedömning av förmåga att arbeta vid sjukdom");

		// Skickat datum och tid -- Nu!
		lakarutlatande.setSkickatDatum(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		
		// Patientdata -- Längst upp till höger på MU
		PatientType patient = new PatientType();
		II personId = new II();
		personId.setRoot("1.2.752.129.2.1.3.1"); // OID för samordningsnummer är 1.2.752.129.2.1.3.3.
		personId.setExtension("19430811-7094");
		patient.setPersonId(personId);
		patient.setFullstandigtNamn("Lab Testsson"); 
		lakarutlatande.setPatient(patient);

		// Skapad av HoS personal -- Längst upp till vänster på MU och/eller fält 16 och fält 17 
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
		lakarutlatande.setSkapadAvHosPersonal(hosPersonal);

		// Aktivitet - Fält 1 - ej ifyllt
//		AktivitetType aktivitet_falt1 = new AktivitetType();
//		aktivitet_falt1.setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
//		aktivitet_falt1.setBeskrivning("Patienten har gulsot!");
//		lakarutlatande.getAktivitet().add(aktivitet_falt1);

		// Medicinskt tillstånd - Fält 2
		MedicinsktTillstandType medicinsktTillstand = new MedicinsktTillstandType();
		medicinsktTillstand.setBeskrivning("Sjukdomar i muskuloskeletala systemet och bindväven");
		CD medicinsktTillstandKod = new CD();
		medicinsktTillstandKod.setCode("M25");
		medicinsktTillstandKod.setCodeSystemName("ICD-10"); // Enbart ICD-10 !			
		medicinsktTillstand.setTillstandskod(medicinsktTillstandKod);
		lakarutlatande.setMedicinsktTillstand(medicinsktTillstand);

		// Bedömt tillstånd - Fält 3
		BedomtTillstandType bedommtTillstand = new BedomtTillstandType();
		bedommtTillstand.setBeskrivning("Patient med hastigt uppkommen smärta och rörelseinskränkning i höger knä.Behandlas initialt med analgetika och steroider, samt ortos. Sjukgymnastik x 3 per vecka");
		lakarutlatande.setBedomtTillstand(bedommtTillstand);

		// Funktionstillstånd - Fält 4 vänster
		FunktionstillstandType funktionstillstand_falt4 = new FunktionstillstandType();
		funktionstillstand_falt4.setTypAvFunktionstillstand(TypAvFunktionstillstand.KROPPSFUNKTION);
		funktionstillstand_falt4.setBeskrivning("Svullnad och värmeökning. Smärta vid manipulation.");
		lakarutlatande.getFunktionstillstand().add(funktionstillstand_falt4);
		
		// Vårdkontakt - Fält 4 höger och översta kryssrutan
		VardkontaktType vardkontakt_falt4_kryss1 = new VardkontaktType();
		vardkontakt_falt4_kryss1.setVardkontaktstid(getDate("2010-01-27"));
		vardkontakt_falt4_kryss1.setVardkontakttyp(Vardkontakttyp.MIN_UNDERSOKNING_AV_PATIENTEN);
		lakarutlatande.getVardkontakt().add(vardkontakt_falt4_kryss1);

		// Vårdkontakt - Fält 4 höger och näst översta kryssrutan - ej ifyllt!
//		VardkontaktType vardkontakt_falt4_kryss2 = new VardkontaktType();
//		vardkontakt_falt4_kryss2.setVardkontaktstid(getDate("2010-01-27"));
//		vardkontakt_falt4_kryss2.setVardkontakttyp(Vardkontakttyp.MIN_TELEFONKONTAKT_MED_PATIENTEN);
//		lakarutlatande.getVardkontakt().add(vardkontakt_falt4_kryss2);

//		// Referens - Fält 4 höger näst nedersta kryssrutan ej ifylld!
//		ReferensType referensJournaluppgift = new ReferensType();
//		referensJournaluppgift.setDatum(getDate("2010-01-27"));
//		referensJournaluppgift.setReferenstyp(Referenstyp.JOURNALUPPGIFTER);
//		lakarutlatande.getReferens().add(referensJournaluppgift);
//
//		// Referens - Fält 4 höger nedersta kryssrutan ej ifylld!
//		ReferensType referensAnnat = new ReferensType();
//		referensJournaluppgift.setDatum(getDate("2010-01-27"));
//		referensJournaluppgift.setReferenstyp(Referenstyp.ANNAT);
//		lakarutlatande.getReferens().add(referensAnnat);

		// Funktionstillstånd - Fält 5
		FunktionstillstandType funktionstillstand_falt5 = new FunktionstillstandType();
		funktionstillstand_falt5.setTypAvFunktionstillstand(TypAvFunktionstillstand.AKTIVITET);
		funktionstillstand_falt5.setBeskrivning("Patienten arbetar som postiljon i distrikt med höghus, sjukdomen omöjliggör gång i trappor.");
		lakarutlatande.getFunktionstillstand().add(funktionstillstand_falt5);

		// Aktivitet - Fält 6a kan vara ett eller alla kryss!
		AktivitetType aktivitet_falt6a_3 = new AktivitetType();
		aktivitet_falt6a_3.setAktivitetskod(Aktivitetskod.OVRIGT);
		aktivitet_falt6a_3.setBeskrivning("Undvika full belastning av knäleden.");
		lakarutlatande.getAktivitet().add(aktivitet_falt6a_3);

		// Aktivitet - Fält 6b kan vara ett eller två kryss!
		AktivitetType aktivitet_falt6b_1 = new AktivitetType();
		aktivitet_falt6b_1.setAktivitetskod(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN);
		aktivitet_falt6b_1.setBeskrivning("Given ordination sjukgymnastik x 3 per vecka. Väntar på artroskopi. Fortsatt poliklinisk kontakt.");
		lakarutlatande.getAktivitet().add(aktivitet_falt6b_1);
		
		// Aktivitet - Fält 7 kan vara endast en av flera val!
		AktivitetType aktivitet_falt7 = new AktivitetType();
		aktivitet_falt7.setAktivitetskod(Aktivitetskod.ARBETSLIVSINRIKTAD_REHABILITERING_AR_EJ_AKTUELL);
		lakarutlatande.getAktivitet().add(aktivitet_falt7);

		// Instans av arbetsförmåga som håller flera fält definieras
		ArbetsformagaType arbetsformaga = new ArbetsformagaType();
	
		// Nuvarande sysselsättning - Fält 8 övre delen här välj 1 av 3
		SysselsattningType sysselsattning = new SysselsattningType();
		sysselsattning.setTypAvSysselsattning(TypAvSysselsattning.NUVARANDE_ARBETE);
		ArbetsuppgiftType arbetsuppgift = new ArbetsuppgiftType();
		arbetsuppgift.setTypAvArbetsuppgift("Brevbärare");

		// Bedömning arbetsförmåga - Fält 8 nedre delen Mellan 1 till 4 bedömningar kan göras med olika tidsintervall
		ArbetsformagaNedsattningType arbetsformagaNedsattning1 = new ArbetsformagaNedsattningType();
		arbetsformagaNedsattning1.setNedsattningsgrad(Nedsattningsgrad.NEDSATT_MED_1_2);
		arbetsformagaNedsattning1.setVaraktighetFrom(getDate("2010-02-16"));
		arbetsformagaNedsattning1.setVaraktighetTom(getDate("2010-03-01"));
		arbetsformaga.getArbetsformagaNedsattning().add(arbetsformagaNedsattning1);

		ArbetsformagaNedsattningType arbetsformagaNedsattning2 = new ArbetsformagaNedsattningType();
		arbetsformagaNedsattning2.setNedsattningsgrad(Nedsattningsgrad.HELT_NEDSATT);
		arbetsformagaNedsattning2.setVaraktighetFrom(getDate("2010-01-28"));
		arbetsformagaNedsattning2.setVaraktighetTom(getDate("2010-02-15"));
		arbetsformaga.getArbetsformagaNedsattning().add(arbetsformagaNedsattning2);
		
		// Arbetsförmåga kommentar - Fält 9
		arbetsformaga.setMotivering("Patientens besvär beror med all säkerhet på förslitningar i knäleden, vilket kan bekräftas vid beställd men inte genomförd artroskopi. Om förändringarna är omfattande måste ytterligare åtgärder vidtas, men patientens besvär kommer inte att upphöra förrän dessa åtgärder är vidtagna.");

		// Prognos - Fält 10
		arbetsformaga.setPrognosangivelse(Prognosangivelse.ATERSTALLAS_DELVIS);

		// Lägg till instansierad data
		arbetsformaga.getSysselsattning().add(sysselsattning);
		arbetsformaga.setArbetsuppgift(arbetsuppgift);
		funktionstillstand_falt5.setArbetsformaga(arbetsformaga);
		
		// Åtgärd resor- Fält 11
		AktivitetType aktivitet_falt11 = new AktivitetType();
		aktivitet_falt11.setAktivitetskod(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT);
		lakarutlatande.getAktivitet().add(aktivitet_falt11);

		// Kontakt med FK - Fält 12 Ingen kontakt önskas
		
		// Kommentar - fält 13
		lakarutlatande.setKommentar("Beroende på vilka åtgärder som kan bli aktuella vid kliniska fynd under artroskopiundersökningen, så ser prognosen för patienten olika ut. En mera korrekt bedömning av återgång till arbete kan göras först efter denna undersöking.");

		// Signeringsdatum - fält 14
		lakarutlatande.setSigneringsdatum(getDate("2010-01-28"));

		// Arbetsplatskod - Fält 17
		II arbetsplatskod = new II();
		arbetsplatskod.setRoot("1.2.752.29.4.71");
		arbetsplatskod.setExtension("Arbetsplatskoden");
		lakarutlatande.getSkapadAvHosPersonal().getEnhet().setArbetsplatskod(arbetsplatskod);		

		return lakarutlatande;
	}

	private static XMLGregorianCalendar getDate(String stringDate) throws Exception{

		try {
			GregorianCalendar fromDate = new GregorianCalendar();
			DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
			dfm.setLenient(false);
			Date date = dfm.parse(stringDate);
			fromDate.setTime(date);
			return (DatatypeFactory.newInstance().newXMLGregorianCalendar(fromDate));
		} catch (DatatypeConfigurationException e) {
			throw new Exception(e.getMessage());
		} catch (ParseException pe) {
			throw new Exception(pe.getMessage());
		}
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
