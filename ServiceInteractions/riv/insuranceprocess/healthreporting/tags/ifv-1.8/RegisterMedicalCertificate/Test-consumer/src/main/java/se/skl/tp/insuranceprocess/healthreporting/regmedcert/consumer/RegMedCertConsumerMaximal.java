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
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.ReferensType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.Referenstyp;
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


public final class RegMedCertConsumerMaximal {

	// Use this one to connect via Virtualiseringsplattformen
	private static final String LOGISK_ADDRESS = "/RegisterMedicalCertificate/3/rivtabp20";

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
			request.setLakarutlatande(getLakarintyg());

			RegisterMedicalCertificateResponseType result = service.registerMedicalCertificate(
					logicalAddressHeader, request);

			return ("Result id = " + result.getResult().getResultCode() + ".");

		} catch (Exception ex) {
			System.out.println("Exception=" + ex.getMessage());
			return null;
		}
	}
	
	private static LakarutlatandeType getLakarintyg() throws Exception {
		LakarutlatandeType lakarutlatande = new LakarutlatandeType();
		
		// Id -- Metadata
		UUID uuid = UUID.randomUUID();
		lakarutlatande.setLakarutlatandeId(uuid.toString());

		// Typ av utlåtande -- Metadata rubrik
		lakarutlatande.setTypAvUtlatande("Läkarintyg enligt 3 kap, 8 § lagen (1962:381) om allmän försäkring");

		// Skickat datum och tid -- Nu!
		lakarutlatande.setSkickatDatum(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		
		// Patientdata -- Längst upp till höger på MU
		PatientType patient = new PatientType();
		II personId = new II();
		personId.setRoot("1.2.752.129.2.1.3.1"); // OID för samordningsnummer är 1.2.752.129.2.1.3.3.
		personId.setExtension("19121212-1212");
		patient.setPersonId(personId);
		patient.setFullstandigtNamn("Test Testorsson"); 
		lakarutlatande.setPatient(patient);

		// Skapad av HoS personal -- Längst upp till vänster på MU och/eller fält 16 och fält 17 
		HosPersonalType hosPersonal = new HosPersonalType();
		EnhetType enhet = new EnhetType();	
		II enhetsId = new II();
		enhetsId.setRoot("1.2.752.129.2.1.4.1");
		enhetsId.setExtension("Sundsvall lasaretts HSA-ID");
		enhet.setEnhetsId(enhetsId);
		enhet.setTelefonnummer("060-1818000");
		enhet.setPostadress("Lasarettsvägen 13");
		enhet.setPostnummer("85150");
		enhet.setPostort("Sundsvall");
		enhet.setEnhetsnamn("Kir mott");
		VardgivareType vardgivare = new VardgivareType();
		vardgivare.setVardgivarnamn("Landstinget Norrland");
		II vardgivareId = new II();
		vardgivareId.setRoot("1.2.752.129.2.1.4.1");
		vardgivareId.setExtension("Norrlands landstings HSA-ID");
		vardgivare.setVardgivareId(vardgivareId);
		enhet.setVardgivare(vardgivare);
		hosPersonal.setEnhet(enhet);
		hosPersonal.setFullstandigtNamn("Ewa Sundholm");
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
		medicinsktTillstand.setBeskrivning("Klämskada på överarm");
		CD medicinsktTillstandKod = new CD();
		medicinsktTillstandKod.setCode("S47");
		medicinsktTillstandKod.setCodeSystemName("ICD-10"); // Enbart ICD-10 !			
		medicinsktTillstand.setTillstandskod(medicinsktTillstandKod);
		lakarutlatande.setMedicinsktTillstand(medicinsktTillstand);

		// Bedömt tillstånd - Fält 3
		BedomtTillstandType bedommtTillstand = new BedomtTillstandType();
		bedommtTillstand.setBeskrivning("Patienten klämde höger överarm vid olycka i hemmet. Problemen har pågått en längre tid.");
		lakarutlatande.setBedomtTillstand(bedommtTillstand);

		// Funktionstillstånd - Fält 4 vänster
		FunktionstillstandType funktionstillstand_falt4 = new FunktionstillstandType();
		funktionstillstand_falt4.setTypAvFunktionstillstand(TypAvFunktionstillstand.KROPPSFUNKTION);
		funktionstillstand_falt4.setBeskrivning("Kraftigt nedsatt rörlighet i överarmen pga skadan. Böj- och sträckförmågan är mycket dålig. Smärtar vid rörelse vilket ger att patienten inte kan använda armen särkilt mycket.");
		lakarutlatande.getFunktionstillstand().add(funktionstillstand_falt4);
		
		// Vårdkontakt - Fält 4 höger och översta kryssrutan
		VardkontaktType vardkontakt_falt4_kryss1 = new VardkontaktType();
		vardkontakt_falt4_kryss1.setVardkontaktstid(getDate("2011-01-26"));
		vardkontakt_falt4_kryss1.setVardkontakttyp(Vardkontakttyp.MIN_UNDERSOKNING_AV_PATIENTEN);
		lakarutlatande.getVardkontakt().add(vardkontakt_falt4_kryss1);

		// Vårdkontakt - Fält 4 höger och näst översta kryssrutan
		VardkontaktType vardkontakt_falt4_kryss2 = new VardkontaktType();
		vardkontakt_falt4_kryss2.setVardkontaktstid(getDate("2011-01-12"));
		vardkontakt_falt4_kryss2.setVardkontakttyp(Vardkontakttyp.MIN_TELEFONKONTAKT_MED_PATIENTEN);
		lakarutlatande.getVardkontakt().add(vardkontakt_falt4_kryss2);

		// Referens - Fält 4 höger näst nedersta kryssrutan ej ifylld!
		ReferensType referensJournaluppgift = new ReferensType();
		referensJournaluppgift.setDatum(getDate("2010-01-14"));
		referensJournaluppgift.setReferenstyp(Referenstyp.JOURNALUPPGIFTER);
		lakarutlatande.getReferens().add(referensJournaluppgift);

		// Referens - Fält 4 höger nedersta kryssrutan ej ifylld!
		ReferensType referensAnnat = new ReferensType();
		referensJournaluppgift.setDatum(getDate("2010-01-24"));
		referensJournaluppgift.setReferenstyp(Referenstyp.ANNAT);
		lakarutlatande.getReferens().add(referensAnnat);

		// Funktionstillstånd - Fält 5
		FunktionstillstandType funktionstillstand_falt5 = new FunktionstillstandType();
		funktionstillstand_falt5.setTypAvFunktionstillstand(TypAvFunktionstillstand.AKTIVITET);
		funktionstillstand_falt5.setBeskrivning("Patienten bör/kan inte använda armen förrän skadan läkt. Skadan förvärras vid för tidigt påtvingad belastning. Patienten kan inte lyfta armen utan den ska hållas riktad nedåt och i fast läge så mycket som möjligt under tiden för läkning.");
		lakarutlatande.getFunktionstillstand().add(funktionstillstand_falt5);

		// Aktivitet - Fält 6a kan vara ett eller alla kryss!
		AktivitetType aktivitet_falt6a_1 = new AktivitetType();
		aktivitet_falt6a_1.setAktivitetskod(Aktivitetskod.PATIENTEN_BEHOVER_FA_KONTAKT_MED_ARBETSFORMEDLINGEN);
		lakarutlatande.getAktivitet().add(aktivitet_falt6a_1);

		AktivitetType aktivitet_falt6a_2 = new AktivitetType();
		aktivitet_falt6a_2.setAktivitetskod(Aktivitetskod.PATIENTEN_BEHOVER_FA_KONTAKT_MED_FORETAGSHALSOVARDEN);
		lakarutlatande.getAktivitet().add(aktivitet_falt6a_2);

		AktivitetType aktivitet_falt6a_3 = new AktivitetType();
		aktivitet_falt6a_3.setAktivitetskod(Aktivitetskod.OVRIGT);
		aktivitet_falt6a_3.setBeskrivning("När skadan förbättrats rekommenderas muskeluppbyggande sjukgymnastik");
		lakarutlatande.getAktivitet().add(aktivitet_falt6a_3);

		// Aktivitet - Fält 6b kan vara ett eller två kryss!
		AktivitetType aktivitet_falt6b_1 = new AktivitetType();
		aktivitet_falt6b_1.setAktivitetskod(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN);
		aktivitet_falt6b_1.setBeskrivning("Utreds om operation är nödvändig");
		lakarutlatande.getAktivitet().add(aktivitet_falt6b_1);

		AktivitetType aktivitet_falt6b_2 = new AktivitetType();
		aktivitet_falt6b_2.setAktivitetskod(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD);
		aktivitet_falt6b_2.setBeskrivning("Patienten ansvarar för att armen hålls i stillhet");
		lakarutlatande.getAktivitet().add(aktivitet_falt6b_2);
		
		// Aktivitet - Fält 7 kan vara endast en av flera val!
		AktivitetType aktivitet_falt7 = new AktivitetType();
		aktivitet_falt7.setAktivitetskod(Aktivitetskod.GAR_EJ_ATT_BEDOMMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL);
		lakarutlatande.getAktivitet().add(aktivitet_falt7);

		// Instans av arbetsförmåga som håller flera fält definieras
		ArbetsformagaType arbetsformaga = new ArbetsformagaType();
	
		// Nuvarande sysselsättning - Fält 8 övre delen här välj 1, 2 eller 3
		SysselsattningType sysselsattning1 = new SysselsattningType();
		sysselsattning1.setTypAvSysselsattning(TypAvSysselsattning.NUVARANDE_ARBETE);
		ArbetsuppgiftType arbetsuppgift = new ArbetsuppgiftType();
		arbetsuppgift.setTypAvArbetsuppgift("Dirigent. Dirigerar en större orkester på deltid");

		SysselsattningType sysselsattning2 = new SysselsattningType();
		sysselsattning2.setTypAvSysselsattning(TypAvSysselsattning.ARBETSLOSHET);

		SysselsattningType sysselsattning3 = new SysselsattningType();
		sysselsattning3.setTypAvSysselsattning(TypAvSysselsattning.FORALDRALEDIGHET);
		
		// Bedömning arbetsförmåga - Fält 8 nedre delen Mellan 1 till 4 bedömningar kan göras med olika tidsintervall
		ArbetsformagaNedsattningType arbetsformagaNedsattning1 = new ArbetsformagaNedsattningType();
		arbetsformagaNedsattning1.setNedsattningsgrad(Nedsattningsgrad.NEDSATT_MED_1_4);
		arbetsformagaNedsattning1.setVaraktighetFrom(getDate("2011-04-01"));
		arbetsformagaNedsattning1.setVaraktighetTom(getDate("2011-05-31"));
		arbetsformaga.getArbetsformagaNedsattning().add(arbetsformagaNedsattning1);

		ArbetsformagaNedsattningType arbetsformagaNedsattning2 = new ArbetsformagaNedsattningType();
		arbetsformagaNedsattning2.setNedsattningsgrad(Nedsattningsgrad.NEDSATT_MED_1_2);
		arbetsformagaNedsattning2.setVaraktighetFrom(getDate("2011-03-07"));
		arbetsformagaNedsattning2.setVaraktighetTom(getDate("2011-03-31"));
		arbetsformaga.getArbetsformagaNedsattning().add(arbetsformagaNedsattning2);

		ArbetsformagaNedsattningType arbetsformagaNedsattning3 = new ArbetsformagaNedsattningType();
		arbetsformagaNedsattning3.setNedsattningsgrad(Nedsattningsgrad.NEDSATT_MED_3_4);
		arbetsformagaNedsattning3.setVaraktighetFrom(getDate("2011-02-14"));
		arbetsformagaNedsattning3.setVaraktighetTom(getDate("2011-03-06"));
		arbetsformaga.getArbetsformagaNedsattning().add(arbetsformagaNedsattning3);

		ArbetsformagaNedsattningType arbetsformagaNedsattning4 = new ArbetsformagaNedsattningType();
		arbetsformagaNedsattning4.setNedsattningsgrad(Nedsattningsgrad.HELT_NEDSATT);
		arbetsformagaNedsattning4.setVaraktighetFrom(getDate("2011-01-26"));
		arbetsformagaNedsattning4.setVaraktighetTom(getDate("2011-02-13"));
		arbetsformaga.getArbetsformagaNedsattning().add(arbetsformagaNedsattning4);

		// Arbetsförmåga kommentar - Fält 9
		arbetsformaga.setMotivering("Skadan har förvärrats vid varje tillfälle patienten använt armen. Måste hållas i total stillhet tills läkningsprocessen kommit en bit på väg. Eventuellt kan utredning visa att operation är nödvändig för att läka skadan.");

		// Prognos - Fält 10
		arbetsformaga.setPrognosangivelse(Prognosangivelse.DET_GAR_INTE_ATT_BEDOMMA);

		// Lägg till instansierad data
		arbetsformaga.getSysselsattning().add(sysselsattning1);
		arbetsformaga.getSysselsattning().add(sysselsattning2);
		arbetsformaga.getSysselsattning().add(sysselsattning3);
		arbetsformaga.setArbetsuppgift(arbetsuppgift);
		funktionstillstand_falt5.setArbetsformaga(arbetsformaga);
		
		// Åtgärd resor- Fält 11
		AktivitetType aktivitet_falt11 = new AktivitetType();
		aktivitet_falt11.setAktivitetskod(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT);
		lakarutlatande.getAktivitet().add(aktivitet_falt11);

		// Kontakt med FK - Fält 12 
		AktivitetType aktivitet_falt12 = new AktivitetType();
		aktivitet_falt12.setAktivitetskod(Aktivitetskod.KONTAKT_MED_FORSAKRINGSKASSAN_AR_AKTUELL);
		lakarutlatande.getAktivitet().add(aktivitet_falt12);
		
		// Kommentar - fält 13
		lakarutlatande.setKommentar("Prognosen för patienten är god. Han kommer att kunna återgå till sitt arbete efter genomförd behandling.");

		// Signeringsdatum - fält 14
		lakarutlatande.setSigneringsdatum(getDate("2011-01-26"));

		// Arbetsplatskod - Fält 17
		II arbetsplatskod = new II();
		arbetsplatskod.setRoot("1.2.752.29.4.71");
		arbetsplatskod.setExtension("123456789011");
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
