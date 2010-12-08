package se.skl.tp.regmedcert.transformers;

import iso.v21090.dt.v1.II;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.stream.XMLStreamReader;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.config.i18n.MessageFactory;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.wsaddressing10.AttributedURIType;

import se.fk.vardgivare.sjukvard.taemotlakarintygresponder.v1.TaEmotLakarintygType;
import se.fk.vardgivare.sjukvard.v1.Adress;
import se.fk.vardgivare.sjukvard.v1.Adressering;
import se.fk.vardgivare.sjukvard.v1.Adressering.Avsandare;
import se.fk.vardgivare.sjukvard.v1.Adressering.Mottagare;
import se.fk.vardgivare.sjukvard.v1.Enhet;
import se.fk.vardgivare.sjukvard.v1.InternIdentitetsbeteckning;
import se.fk.vardgivare.sjukvard.v1.Kontaktuppgifter;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Arbetsformaga;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Arbetsformaga.Nedsattningsgrad;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Begransning;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Diagnos;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Kontakt;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Lakare;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Motivering;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Planering;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Planering.Behandling;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Prognos;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Rehabilitering;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Rekommendationer;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Rekommendationer.Rekommendation;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Resor;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Sjukdomshistoria;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Smittskydd;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Status;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Status.Basering;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Status.Basering.Ursprung;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Sysselsattning;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Upplysningar;
import se.fk.vardgivare.sjukvard.v1.Land;
import se.fk.vardgivare.sjukvard.v1.Namn;
import se.fk.vardgivare.sjukvard.v1.NationellIdentitetsbeteckning;
import se.fk.vardgivare.sjukvard.v1.Organisation;
import se.fk.vardgivare.sjukvard.v1.Patient;
import se.fk.vardgivare.sjukvard.v1.Person;
import se.fk.vardgivare.sjukvard.v1.Postadress;
import se.fk.vardgivare.sjukvard.v1.Postnummer;
import se.fk.vardgivare.sjukvard.v1.Postort;
import se.fk.vardgivare.sjukvard.v1.ReferensAdressering;
import se.fk.vardgivare.sjukvard.v1.TaEmotLakarintyg;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.AktivitetType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.Aktivitetskod;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaNedsattningType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.ArbetsuppgiftType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.FunktionstillstandType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.MedicinsktTillstandType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.Prognosangivelse;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.ReferensType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.Referenstyp;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.SysselsattningType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.TypAvFunktionstillstand;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.TypAvSysselsattning;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.VardkontaktType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.Vardkontakttyp;
import se.skl.riv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.skl.riv.insuranceprocess.healthreporting.v2.EnhetType;
import se.skl.riv.insuranceprocess.healthreporting.v2.HosPersonalType;
import se.skl.riv.insuranceprocess.healthreporting.v2.PatientType;
import se.skl.riv.insuranceprocess.healthreporting.v2.VardgivareType;


public class Vard2FkTransformer extends AbstractMessageAwareTransformer
{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public Vard2FkTransformer()
    {
        super();
        registerSourceType(Object.class);
        setReturnClass(Object.class); 
    }
    
	public Object transform(MuleMessage message, String outputEncoding) throws TransformerException {
		ResourceBundle rb = ResourceBundle.getBundle("fkdata");	    

		try {			
			// Transform the XML payload into a JAXB object
            Unmarshaller unmarshaller = JAXBContext.newInstance(RegisterMedicalCertificateType.class).createUnmarshaller();
            XMLStreamReader streamPayload = (XMLStreamReader)((Object[])message.getPayload())[1];
            RegisterMedicalCertificateType inRequest = (RegisterMedicalCertificateType)((JAXBElement)unmarshaller.unmarshal(streamPayload)).getValue();

			// Get receiver to adress from Mule property
			String receiverId = (String)message.getProperty("receiverid");
			
			// Validate incoming request
			validateRequest(inRequest);
			
			// Extract all incoming data to local variables
            String emuId = inRequest.getLakarutlatande().getLakarutlatandeId();
            String inPersonnummer = inRequest.getLakarutlatande().getPatient().getPersonId().getExtension();
            String inPatientNamn = inRequest.getLakarutlatande().getPatient().getFullstandigtNamn();
            String inLakarId = inRequest.getLakarutlatande().getSkapadAvHosPersonal().getPersonalId().getExtension();
            String inLakarNamn = inRequest.getLakarutlatande().getSkapadAvHosPersonal().getFullstandigtNamn();
            String inLakarForskrivarekod = inRequest.getLakarutlatande().getSkapadAvHosPersonal().getForskrivarkod();
            String inEnhetsId = inRequest.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getEnhetsId().getExtension();
            String inEnhetsNamn = inRequest.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getEnhetsnamn();
            String inEnhetsArbetsplatskod = inRequest.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getArbetsplatskod().getExtension();
            String inEnhetsPostAdress = inRequest.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getPostadress();
            String inEnhetsPostNummer = inRequest.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getPostnummer();
            String inEnhetsPostOrt = inRequest.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getPostort();
            String inVardgivareId = inRequest.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getVardgivare().getVardgivareId().getExtension();
            String inVardgivarNamn = inRequest.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getVardgivare().getVardgivarnamn();
            XMLGregorianCalendar inSignerades = inRequest.getLakarutlatande().getSigneringsdatum();
            XMLGregorianCalendar inSkickadesTid = inRequest.getLakarutlatande().getSkickatDatum();
            boolean inSmittskydd = findAktivitetWithCode(inRequest.getLakarutlatande().getAktivitet(), Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA) != null ? true:false;
            String inDiagnoskod = inRequest.getLakarutlatande().getMedicinsktTillstand().getTillstandskod().getCode();
            String inDiagnosBeskrivning = inRequest.getLakarutlatande().getMedicinsktTillstand().getBeskrivning();
            String inSjukdomshistoriaBeskrivning = inRequest.getLakarutlatande().getBedomtTillstand().getBeskrivning();
            FunktionstillstandType inKroppsFunktion = findFunktionsTillstandType(inRequest.getLakarutlatande().getFunktionstillstand(), TypAvFunktionstillstand.KROPPSFUNKTION);
            VardkontaktType inUndersokning = findVardkontaktTyp(inRequest.getLakarutlatande().getVardkontakt(), Vardkontakttyp.MIN_UNDERSOKNING_AV_PATIENTEN);     
            VardkontaktType inTelefonkontakt = findVardkontaktTyp(inRequest.getLakarutlatande().getVardkontakt(), Vardkontakttyp.MIN_TELEFONKONTAKT_MED_PATIENTEN);
            ReferensType inJournal = findReferensTyp(inRequest.getLakarutlatande().getReferens(), Referenstyp.JOURNALUPPGIFTER);
            ReferensType inAnnat = findReferensTyp(inRequest.getLakarutlatande().getReferens(), Referenstyp.ANNAT);
            FunktionstillstandType inAktivitetFunktion = findFunktionsTillstandType(inRequest.getLakarutlatande().getFunktionstillstand(), TypAvFunktionstillstand.AKTIVITET);
            AktivitetType kontaktAF = findAktivitetWithCode(inRequest.getLakarutlatande().getAktivitet(), Aktivitetskod.PATIENTEN_BEHOVER_FA_KONTAKT_MED_ARBETSFORMEDLINGEN);
            AktivitetType kontaktFHV = findAktivitetWithCode(inRequest.getLakarutlatande().getAktivitet(), Aktivitetskod.PATIENTEN_BEHOVER_FA_KONTAKT_MED_FORETAGSHALSOVARDEN);
            AktivitetType ovrigt = findAktivitetWithCode(inRequest.getLakarutlatande().getAktivitet(), Aktivitetskod.OVRIGT);
            AktivitetType planeradAtgardInomSjukvarden = findAktivitetWithCode(inRequest.getLakarutlatande().getAktivitet(), Aktivitetskod.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN);
            AktivitetType planeradAtgardAnnan = findAktivitetWithCode(inRequest.getLakarutlatande().getAktivitet(), Aktivitetskod.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD);
            AktivitetType arbRelRehabAktuell = findAktivitetWithCode(inRequest.getLakarutlatande().getAktivitet(), Aktivitetskod.ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL);
            AktivitetType arbRelRehabEjAktuell = findAktivitetWithCode(inRequest.getLakarutlatande().getAktivitet(), Aktivitetskod.ARBETSLIVSINRIKTAD_REHABILITERING_AR_EJ_AKTUELL);
            AktivitetType garEjAttBedommaArbRelRehab = findAktivitetWithCode(inRequest.getLakarutlatande().getAktivitet(), Aktivitetskod.GAR_EJ_ATT_BEDOMMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL);
            SysselsattningType inArbete = findTypAvSysselsattning(inAktivitetFunktion.getArbetsformaga().getSysselsattning(), TypAvSysselsattning.NUVARANDE_ARBETE);
            SysselsattningType inArbetslos = findTypAvSysselsattning(inAktivitetFunktion.getArbetsformaga().getSysselsattning(), TypAvSysselsattning.ARBETSLOSHET);
            SysselsattningType inForaldraledig = findTypAvSysselsattning(inAktivitetFunktion.getArbetsformaga().getSysselsattning(), TypAvSysselsattning.FORALDRALEDIGHET);
            ArbetsformagaNedsattningType nedsatt14del =  findArbetsformaga(inAktivitetFunktion.getArbetsformaga().getArbetsformagaNedsattning(), se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.Nedsattningsgrad.NEDSATT_MED_1_4);
            ArbetsformagaNedsattningType nedsatthalften =  findArbetsformaga(inAktivitetFunktion.getArbetsformaga().getArbetsformagaNedsattning(), se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.Nedsattningsgrad.NEDSATT_MED_1_2);
            ArbetsformagaNedsattningType nedsatt34delar =  findArbetsformaga(inAktivitetFunktion.getArbetsformaga().getArbetsformagaNedsattning(), se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.Nedsattningsgrad.NEDSATT_MED_3_4);
            ArbetsformagaNedsattningType heltNedsatt =  findArbetsformaga(inAktivitetFunktion.getArbetsformaga().getArbetsformagaNedsattning(), se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.Nedsattningsgrad.HELT_NEDSATT);
            String inMotivering = inAktivitetFunktion.getArbetsformaga().getMotivering();
            boolean inPrognosAterfaHelt = inAktivitetFunktion.getArbetsformaga().getPrognosangivelse().compareTo(Prognosangivelse.ATERSTALLAS_HELT) == 0;
            boolean inPrognosAterfaDelvis = inAktivitetFunktion.getArbetsformaga().getPrognosangivelse().compareTo(Prognosangivelse.ATERSTALLAS_DELVIS) == 0;
            boolean inPrognosEjAterfa = inAktivitetFunktion.getArbetsformaga().getPrognosangivelse().compareTo(Prognosangivelse.INTE_ATERSTALLAS) == 0;
            boolean inPrognosGarEjAttBedomma = inAktivitetFunktion.getArbetsformaga().getPrognosangivelse().compareTo(Prognosangivelse.DET_GAR_INTE_ATT_BEDOMMA) == 0;
            boolean inResorJa = findAktivitetWithCode(inRequest.getLakarutlatande().getAktivitet(), Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT) != null;
            boolean inResorNej = findAktivitetWithCode(inRequest.getLakarutlatande().getAktivitet(), Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT) != null;
            boolean inKontaktFK = findAktivitetWithCode(inRequest.getLakarutlatande().getAktivitet(), Aktivitetskod.KONTAKT_MED_FORSAKRINGSKASSAN_AR_AKTUELL) != null;
            String inKommentar = inRequest.getLakarutlatande().getKommentar();            
            
            // Create new JAXB object for the outgoing data
            TaEmotLakarintygType outRequest = new TaEmotLakarintygType();
    		TaEmotLakarintyg fkSklTELA = new TaEmotLakarintyg();
    		outRequest.setFKSKLTaEmotLakarintygAnrop(fkSklTELA);
            
			// Transform between incoming and outgoing objects
            Adressering adressering = new Adressering();
            Avsandare avsandare = new Avsandare();
            Mottagare mottagare = new Mottagare();
            Organisation avsandarOrganisation = new Organisation();
            Enhet enhet = new Enhet();
            Lakarintyg lakarintyg = new Lakarintyg();
            
            /********* Avsändare *******/ 
            // Id
            ReferensAdressering referens = new ReferensAdressering();
            referens.setValue(emuId);
            avsandare.setReferens(referens);
            adressering.setAvsandare(avsandare);
            
            // Patient
            Patient patient = new Patient();
            patient.setIdentifierare(inPersonnummer);
            patient.setNamn(inPatientNamn);
            fkSklTELA.setPatient(patient);
            
            // Personal
            Person personal = new Person();
            InternIdentitetsbeteckning iiId = new InternIdentitetsbeteckning();
            iiId.setValue(inLakarId);
            personal.setId(iiId);
            personal.setNamn(inLakarNamn);
            enhet.setPerson(personal);            
            
            // Enhet
            InternIdentitetsbeteckning iiEnhetId = new InternIdentitetsbeteckning();
            iiEnhetId.setValue(inEnhetsId);
            enhet.setId(iiEnhetId);
            Namn enhetsNamn = new Namn();
            enhetsNamn.setValue(inEnhetsNamn);
            enhet.setNamn(enhetsNamn);
            Kontaktuppgifter enhetKontaktuppgift = new Kontaktuppgifter();
            Adress enhetAdress = new Adress();
            Postadress postAdress = new Postadress();
            postAdress.setValue(inEnhetsPostAdress);
            enhetAdress.setPostadress(postAdress);
            Postnummer postnummer = new Postnummer();
            postnummer.setValue(inEnhetsPostNummer);
            enhetAdress.setPostnummer(postnummer);
            Postort postort = new Postort();
            postort.setValue(inEnhetsPostOrt);
            enhetAdress.setPostort(postort);
            Land land = new Land();
            land.setValue("Sverige");
            enhetAdress.setLand(land);
            enhetKontaktuppgift.setAdress(enhetAdress);
            enhet.setKontaktuppgifter(enhetKontaktuppgift);
            avsandarOrganisation.setEnhet(enhet);
            
            // Organisation
            InternIdentitetsbeteckning iiOrganisationsId = new InternIdentitetsbeteckning();
            iiOrganisationsId.setValue(inVardgivareId);
            avsandarOrganisation.setId(iiOrganisationsId);
            Namn organisationsNamn = new Namn();
            organisationsNamn.setValue(inVardgivarNamn);
            avsandarOrganisation.setNamn(organisationsNamn);
            avsandare.setOrganisation(avsandarOrganisation);
            adressering.setAvsandare(avsandare);
            fkSklTELA.setAdressering(adressering);

            /********* Mottagare *******/ 
            // Organisation
            Organisation mottagarOrganisation = new Organisation();
            NationellIdentitetsbeteckning mottagarNationellIdentitetsbeteckning = new NationellIdentitetsbeteckning();
            mottagarNationellIdentitetsbeteckning.setValue("202100-5521");
            mottagarOrganisation.setOrganisationsnummer(mottagarNationellIdentitetsbeteckning);
            Namn mottagarNamn = new Namn();
            mottagarNamn.setValue(rb.getString("FK"));
            mottagarOrganisation.setNamn(mottagarNamn);
            mottagare.setOrganisation(mottagarOrganisation);
            adressering.setMottagare(mottagare);
            
            // Skickades tidpunkt
            adressering.setSkickades(inSkickadesTid);
            
            /********* Fält *******/             
            // Smittskydd - Fält 1
        	Smittskydd smittskydd = new Smittskydd();
        	lakarintyg.setSmittskydd(smittskydd);		// Element should always be set!
            if (inSmittskydd) {
            	smittskydd.setBeskrivning(rb.getString("FK_SMIL"));
            }

            // Diagnos - Fält 2
            Diagnos diagnos = new Diagnos();
            lakarintyg.setDiagnos(diagnos);
            if (inDiagnoskod != null && inDiagnoskod.length() > 0) {
                diagnos.setKod(inDiagnoskod);
            }
            if (inDiagnosBeskrivning != null && inDiagnosBeskrivning.length() > 0) {
                diagnos.setBeskrivning(inDiagnosBeskrivning);            	
            }

            // Sjukdomshistoria - Fält 3
            Sjukdomshistoria sjukdomshistoria = new Sjukdomshistoria();
            lakarintyg.setSjukdomshistoria(sjukdomshistoria);
            if (inSjukdomshistoriaBeskrivning != null && inSjukdomshistoriaBeskrivning.length() > 0) {
                sjukdomshistoria.setBeskrivning(inSjukdomshistoriaBeskrivning);
            }
            
            // Status - Fält 4 - vänster
            Status status = new Status();
            status.setBeskrivning(inKroppsFunktion.getBeskrivning());
            lakarintyg.setStatus(status);

            // Ursprung - Fält 4
            Basering basering = new Basering();
            status.setBasering(basering);

            // Ursprung - Fält 4 - höger översta kryssrutan
            if (inUndersokning != null) {
                Ursprung ursprung = new Ursprung();
                basering.getUrsprung().add(ursprung);
            	ursprung.setBeskrivning(rb.getString("FK_UNDERSOKNING"));
            	ursprung.setDatum(inUndersokning.getVardkontaktstid());
            }

            // Ursprung - Fält 4 - höger näst översta kryssrutan
            if (inTelefonkontakt != null) {
                Ursprung ursprung = new Ursprung();
                basering.getUrsprung().add(ursprung);
            	ursprung.setBeskrivning("Telefonkontakt");
            	ursprung.setDatum(inTelefonkontakt.getVardkontaktstid());
            }

            // Ursprung - Fält 4 - höger näst nedersta kryssrutan
            if (inJournal != null) {
                Ursprung ursprung = new Ursprung();
                basering.getUrsprung().add(ursprung);
            	ursprung.setBeskrivning("Journaluppgifter");
            	ursprung.setDatum(inJournal.getDatum());
            }

            // Ursprung - Fält 4 - höger nedersta kryssrutan
            if (inAnnat != null) {
                Ursprung ursprung = new Ursprung();
                basering.getUrsprung().add(ursprung);
            	ursprung.setBeskrivning("Annat");
            	ursprung.setDatum(inAnnat.getDatum());
            }

            // Status - Fält 5
            Begransning begransning = new Begransning();
            lakarintyg.setBegransning(begransning);
            if (inAktivitetFunktion != null) {
            	begransning.setBeskrivning(inAktivitetFunktion.getBeskrivning());
            }
            
            // Fält 6a 
            Rekommendationer rekommendationer = new Rekommendationer();
            lakarintyg.setRekommendationer(rekommendationer);

            // Fält 6a - kryssruta 1            
            if (kontaktAF != null) {
            	Rekommendation rekommendation = new Rekommendation();
            	rekommendation.setBeskrivning(rb.getString("FK_ARBETSFORMEDLING"));
            	rekommendationer.getRekommendation().add(rekommendation);
            }
            
            // Fält 6a - kryssruta 2
            if (kontaktFHV != null) {
            	Rekommendation rekommendation = new Rekommendation();
            	rekommendation.setBeskrivning(rb.getString("FK_FTGHLSVARD"));
            	rekommendationer.getRekommendation().add(rekommendation);
            }

            // Fält 6a - kryssruta 3
            if (ovrigt != null) {
            	Rekommendation rekommendation = new Rekommendation();
            	rekommendation.setBeskrivning(rb.getString("FK_OVRIGT"));
            	rekommendation.setKommentar(ovrigt.getBeskrivning());
            	rekommendationer.getRekommendation().add(rekommendation);
            }
            
            // Fält 6b
            Planering planering = new Planering();
            lakarintyg.setPlanering(planering);
            
            // Fält 6b - kryssruta 1
            if (planeradAtgardInomSjukvarden != null) {
            	Behandling behandling = new Behandling();
            	behandling.setBeskrivning(rb.getString("FK_ATGARD"));
            	behandling.setKommentar(planeradAtgardInomSjukvarden.getBeskrivning());
            	planering.getBehandling().add(behandling);
            }

            // Fält 6b - kryssruta 2
            if (planeradAtgardAnnan != null) {
            	Behandling behandling = new Behandling();
            	behandling.setBeskrivning("Annan");
            	behandling.setKommentar(planeradAtgardAnnan.getBeskrivning());
            	planering.getBehandling().add(behandling);
            }

            // Fält 7 
        	Rehabilitering rehab = new Rehabilitering();
        	lakarintyg.setRehabilitering(rehab);
            if (arbRelRehabAktuell != null) {
            	rehab.setBeskrivning(rb.getString("FK_ARBLIV_REHAB_AKT"));
            }
            if (arbRelRehabEjAktuell != null) {
            	rehab.setBeskrivning(rb.getString("FK_ARBLIV_REHAB_EJ_AKT"));
            }
            if (garEjAttBedommaArbRelRehab != null) {
            	rehab.setBeskrivning(rb.getString("FK_BEHOV_EJ_BEDOMMA"));
            }
            
            // Fält 8a
            Sysselsattning sysselsattning = new Sysselsattning();
            lakarintyg.setSysselsattning(sysselsattning);
                                    
            // Fält 8a - kryssruta 1
            if (inArbete != null) {
            	sysselsattning.getBeskrivning().add(rb.getString("FK_ARBETAR")); 
            	sysselsattning.setArbetsuppgifter(inAktivitetFunktion.getArbetsformaga().getArbetsuppgift().getTypAvArbetsuppgift());
            }

            // Fält 8a - kryssruta 2
            if (inArbetslos != null) {
            	sysselsattning.getBeskrivning().add(rb.getString("FK_ARBETSLOS")); 
            }

            // Fält 8a - kryssruta 3
            if (inForaldraledig != null) {
            	sysselsattning.getBeskrivning().add(rb.getString("FK_FORALDRALEDIG")); 
            }

            // Fält 8b
            Arbetsformaga arbetsformaga = new Arbetsformaga();
            lakarintyg.setArbetsformaga(arbetsformaga);

            // Fält 8b - kryssruta 1
            if (nedsatt14del != null) {
            	Nedsattningsgrad nedsattningsgrad = new Nedsattningsgrad();
            	nedsattningsgrad.setBeskrivning("Nedsatt med 1/4");            	
            	nedsattningsgrad.setFranOchMed(nedsatt14del.getVaraktighetFrom());
            	nedsattningsgrad.setLangstTillOchMed(nedsatt14del.getVaraktighetTom());
            	lakarintyg.getArbetsformaga().getNedsattningsgrad().add(nedsattningsgrad);
            }

            // Fält 8b - kryssruta 2
            if (nedsatthalften != null) {
            	Nedsattningsgrad nedsattningsgrad = new Nedsattningsgrad();
            	nedsattningsgrad.setBeskrivning(rb.getString("FK_NEDSATT_HALFTEN"));
            	nedsattningsgrad.setFranOchMed(nedsatthalften.getVaraktighetFrom());
            	nedsattningsgrad.setLangstTillOchMed(nedsatthalften.getVaraktighetTom());
            	lakarintyg.getArbetsformaga().getNedsattningsgrad().add(nedsattningsgrad);
            }
            
            // Fält 8b - kryssruta 3
            if (nedsatt34delar != null) {
            	Nedsattningsgrad nedsattningsgrad = new Nedsattningsgrad();
            	nedsattningsgrad.setBeskrivning("Nedsatt med 3/4");
            	nedsattningsgrad.setFranOchMed(nedsatt34delar.getVaraktighetFrom());
            	nedsattningsgrad.setLangstTillOchMed(nedsatt34delar.getVaraktighetTom());
            	lakarintyg.getArbetsformaga().getNedsattningsgrad().add(nedsattningsgrad);
            }

            // Fält 8b - kryssruta 4
            if (heltNedsatt != null) {
            	Nedsattningsgrad nedsattningsgrad = new Nedsattningsgrad();
            	nedsattningsgrad.setBeskrivning("Helt nedsatt");
            	nedsattningsgrad.setFranOchMed(heltNedsatt.getVaraktighetFrom());
            	nedsattningsgrad.setLangstTillOchMed(heltNedsatt.getVaraktighetTom());
            	lakarintyg.getArbetsformaga().getNedsattningsgrad().add(nedsattningsgrad);
            }

            // Fält 9 - Motivering
            Motivering motivering  = new Motivering();
            lakarintyg.setMotivering(motivering);
            if (inMotivering != null && inMotivering.length() > 0) {
                motivering.setBeskrivning(inMotivering);
            }
            
            // Fält 10 - Prognosangivelse
        	Prognos prognos = new Prognos();
        	lakarintyg.setPrognos(prognos);        	
            if (inPrognosAterfaHelt) {
            	prognos.setBeskrivning(rb.getString("FK_ATERFA"));
            }
            if (inPrognosAterfaDelvis) {
            	prognos.setBeskrivning(rb.getString("FK_DELVIS_ATERFA"));
            }
            if (inPrognosEjAterfa) {
            	prognos.setBeskrivning(rb.getString("FK_EJ_ATERFA"));
            }
            if (inPrognosGarEjAttBedomma) {
            	prognos.setBeskrivning(rb.getString("FK_EJ_BEDOMA_ATERFA"));
            }
            
            // Fält 11 - kryssruta 1 och 2 Endast 1 av 2 val möjligt!
        	Resor resor = new Resor();
        	lakarintyg.setResor(resor);
            if (inResorJa) {
            	resor.setBeskrivning(rb.getString("FK_ANNAT_FARDSATT"));
            }
            if (inResorNej) {
            	resor.setBeskrivning(rb.getString("FK_ANNAT_FARDSATT_EJ"));
            }

            // Fält 12 - kryssruta 1 och 2
            Kontakt kontakt = new Kontakt();
            lakarintyg.setKontakt(kontakt);
            if (inKontaktFK) {
            	kontakt.setBeskrivning(rb.getString("FK_KONTAKT_FK"));
            }
            
            // Fält 13 - Upplysningar
            Upplysningar upplysningar = new Upplysningar();
            lakarintyg.setUpplysningar(upplysningar);
            if (inKommentar != null && inKommentar.length() > 0) {
                upplysningar.setBeskrivning(inKommentar);            	
            }
            
            // Fält 14 - 17
            Lakare lakare = new Lakare();
            lakarintyg.setLakare(lakare);

            // Fält 14 - Signeringstidpunkt
            lakare.setSignerades(inSignerades);
            
            // Fält 17
            if (inEnhetsArbetsplatskod != null && inEnhetsArbetsplatskod.length() > 0) {
            	lakare.setArbetsplatskod(inEnhetsArbetsplatskod);
            }
            if (inLakarForskrivarekod != null && inLakarForskrivarekod.length() > 0) {
            	lakare.setForskrivarkod(inLakarForskrivarekod);
            }
             
            fkSklTELA.setLakarintyg(lakarintyg);

    		AttributedURIType logicalAddressHeader = new AttributedURIType();
    		logicalAddressHeader.setValue(receiverId);

	        Object[] payloadOut = new Object[] {logicalAddressHeader, outRequest};
            
	        if (logger.isDebugEnabled()) {
	            logger.debug("transformed payload to: " + payloadOut);
	        }

	        return payloadOut;
		} catch (Exception e) {
			logger.error("Transform exception:" + e.getMessage());
			throw new TransformerException(MessageFactory.createStaticMessage(e.getMessage()));
		}
    }

	private void validateRequest(RegisterMedicalCertificateType inRequest) throws Exception {	
		// List of validation errors
		ArrayList<String> validationErrors = new ArrayList<String>();
	
		// Validate incoming request
		try {
			// Check that we got any data at all
			if (inRequest == null) {
				validationErrors.add("No RegisterMedicalCertificate found in incoming data!");
				throw new Exception();
			}
			
			// Check that we got a lakarutlatande element
			if (inRequest.getLakarutlatande() == null) {
				validationErrors.add("No Lakarutlatande element found in incoming request data!");
				throw new Exception();
			}
			
			LakarutlatandeType inLakarutlatande = inRequest.getLakarutlatande();
			
			/**
			 *  Check all meta-data, that is data not shown in the form
			 */
			
			// Check that we got an id - mandatory
			if ( inLakarutlatande.getLakarutlatandeId() == null ||
				 inLakarutlatande.getLakarutlatandeId().length() < 1 ) {
				validationErrors.add("No Lakarutlatande Id found!");				
			}
	
	        // Check skickat datum - mandatory
	        if (inLakarutlatande.getSkickatDatum() == null || !inLakarutlatande.getSkickatDatum().isValid()) {
				validationErrors.add("No or wrong skickatDatum found!");				
	        }
	
	        /**
	         * Check patient information
	         */          
	        // Check that we got a patient element 
			if ( inLakarutlatande.getPatient() == null) {
				validationErrors.add("No Patient element found!");	
				throw new Exception();
			}			
			PatientType inPatient = inLakarutlatande.getPatient();
			
	        // Check patient id - mandatory
			if (inPatient.getPersonId() == null ||	
				inPatient.getPersonId().getExtension() == null ||	
				inPatient.getPersonId().getExtension().length() < 1) {
				validationErrors.add("No Patient Id found!");								
			}
			// Check patient o.i.d.
			if (inPatient.getPersonId() == null ||	
				inPatient.getPersonId().getRoot() == null ||	
				(!inPatient.getPersonId().getRoot().equalsIgnoreCase("1.2.752.129.2.1.3.1") && !inPatient.getPersonId().getRoot().equalsIgnoreCase("1.2.752.129.2.1.3.3"))) {
					validationErrors.add("Wrong o.i.d. for Patient Id! Should be 1.2.752.129.2.1.3.1 or 1.2.752.129.2.1.3.3");								
				}
			String inPersonnummer = inPatient.getPersonId().getExtension();
	
	        // Check format on personnummer? samordningsnummer?
	        
	        // Get namn for patient - mandatory
			if (inPatient.getFullstandigtNamn() == null || inPatient.getFullstandigtNamn().length() < 1 ) {
				validationErrors.add("No Patient fullstandigtNamn elements found or set!");								
			}
			
			/**
			 * Check hälso och sjukvårds personal information. Vårdgivare, vårdenhet och läkare.
			 */
	        // Check that we got a skapadAvHosPersonal element
			if ( inLakarutlatande.getSkapadAvHosPersonal() == null) {
				validationErrors.add("No SkapadAvHosPersonal element found!");	
				throw new Exception();
			}			
	        HosPersonalType inHoSP = inLakarutlatande.getSkapadAvHosPersonal() ; 
	        
	        // Check lakar id - mandatory
	        if (inHoSP.getPersonalId() == null || 
	        	inHoSP.getPersonalId().getExtension() == null ||
	        	inHoSP.getPersonalId().getExtension().length() < 1) {
				validationErrors.add("No personal-id found!");	            	
	        }
	        // Check lakar id o.i.d.
	        if (inHoSP.getPersonalId() == null || 
	        	inHoSP.getPersonalId().getRoot() == null ||
	            !inHoSP.getPersonalId().getRoot().equalsIgnoreCase("1.2.752.129.2.1.4.1")) {
				validationErrors.add("Wrong o.i.d. for personalId! Should be 1.2.752.129.2.1.4.1");								
	        }
	        
	        // Check lakarnamn - mandatory
			if (inHoSP.getFullstandigtNamn() == null || inHoSP.getFullstandigtNamn().length() < 1 ) {
				validationErrors.add("No skapadAvHosPersonal fullstandigtNamn found.");								
			}
	
	        // Check that we got a enhet element
	        if (inHoSP.getEnhet() == null) {
				validationErrors.add("No enhet element found!");	  
				throw new Exception();
	        }
	        EnhetType inEnhet = inHoSP.getEnhet() ;
	       
	        // Check enhets id - mandatory
	        if (inEnhet.getEnhetsId() == null ||
	        	inEnhet.getEnhetsId().getExtension() == null ||
	        	inEnhet.getEnhetsId().getExtension().length() < 1) {
				validationErrors.add("No enhets-id found!");	            	
	        }
	        // Check enhets o.i.d
	        if (inEnhet.getEnhetsId() == null || 
	        	inEnhet.getEnhetsId().getRoot() == null ||
	            !inEnhet.getEnhetsId().getRoot().equalsIgnoreCase("1.2.752.129.2.1.4.1")) {
				validationErrors.add("Wrong o.i.d. for enhetsId! Should be 1.2.752.129.2.1.4.1");								
	        }
	        
	        // Check enhetsnamn - mandatory
	        if (inEnhet.getEnhetsnamn() == null || 
	        	inEnhet.getEnhetsnamn().length() < 1) {
	        	validationErrors.add("No enhetsnamn found!");	            	
	        }
	
	        // Check enhetsadress - mandatory      
			if (inEnhet.getPostadress() == null || inEnhet.getPostadress().length() < 1 ) {
				validationErrors.add("No postadress found for enhet!");								
			}
			if (inEnhet.getPostnummer() == null || inEnhet.getPostnummer().length() < 1 ) {
				validationErrors.add("No postnummer found for enhet!");								
			}
			if (inEnhet.getPostort() == null || inEnhet.getPostort().length() < 1 ) {
				validationErrors.add("No postort found for enhet!");								
			}
	
	        // Check that we got a vardgivare element
	        if (inEnhet.getVardgivare() == null) {
				validationErrors.add("No vardgivare element found!");	  
				throw new Exception();
	        }
	        VardgivareType inVardgivare = inEnhet.getVardgivare();
	       
	        // Check vardgivare id - mandatory
	        if (inVardgivare.getVardgivareId() == null ||
	        	inVardgivare.getVardgivareId().getExtension() == null ||
	        	inVardgivare.getVardgivareId().getExtension().length() < 1) {
				validationErrors.add("No vardgivare-id found!");	            	
	        }
	        // Check vardgivare o.i.d.
	        if (inVardgivare.getVardgivareId() == null || 
	        	inVardgivare.getVardgivareId().getRoot() == null ||
	            !inVardgivare.getVardgivareId().getRoot().equalsIgnoreCase("1.2.752.129.2.1.4.1")) {
	        	validationErrors.add("Wrong o.i.d. for vardgivareId! Should be 1.2.752.129.2.1.4.1");								
	        }
	
	        // Check vardgivarename - mandatory
	        if (inVardgivare.getVardgivarnamn() == null || 
	        	inVardgivare.getVardgivarnamn().length() < 1) {
				validationErrors.add("No vardgivarenamn found!");	            	
	        }
	
	        /**
	         * Check form data
	         */
	        // Fält 1 - no rule
	        boolean inSmittskydd = findAktivitetWithCode(inRequest.getLakarutlatande().getAktivitet(), Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA) != null ? true:false;
	
	        // Must be set as this element contains a lot of mandatory information
	        FunktionstillstandType inAktivitetFunktion = findFunktionsTillstandType(inLakarutlatande.getFunktionstillstand(), TypAvFunktionstillstand.AKTIVITET);
	        if (inAktivitetFunktion == null) {
				validationErrors.add("No funktionstillstand - aktivitet element found!");	
				throw new Exception();
	        }
	
	        // Declared outside as it may be used further down. 
	        ReferensType inAnnat = null;
	
	        // Many fields are optional if smittskydd is checked, if not set validate these below
	        if (!inSmittskydd) {
	            // Fält 2 - Check that we got a medicinsktTillstand element
	            if (inLakarutlatande.getMedicinsktTillstand() == null) {
	    			validationErrors.add("No medicinsktTillstand element found!");	
	    			throw new Exception();
	            }
	            // Fält 2 - Medicinskt tillstånd kod - mandatory
	            MedicinsktTillstandType medTillstand = inLakarutlatande.getMedicinsktTillstand();
	            if (medTillstand.getTillstandskod() == null ||
	            	medTillstand.getTillstandskod().getCode() == null ||
	            	medTillstand.getTillstandskod().getCode().length() < 1) {
	    			validationErrors.add("No tillstandskod in medicinsktTillstand found!");	            	
	            }
	            // Fält 2 - Medicinskt tillstånd kodsystemnamn - mandatory
	            if (medTillstand.getTillstandskod() == null || 
	            	medTillstand.getTillstandskod().getCodeSystemName() == null ||
	                !medTillstand.getTillstandskod().getCodeSystemName().equalsIgnoreCase("ICD-10")) {
	            	validationErrors.add("Wrong code system name for medicinskt tillstand - tillstandskod (diagnoskod)! Should be ICD-10");								
	            }
	            // Fält 2 - Medicinskt tillstånd beskrivning - mandatory
	            if (medTillstand.getBeskrivning() == null ||
	                medTillstand.getBeskrivning().length() < 1) {
	        		validationErrors.add("No beskrivning in medicinsktTillstand found!");	            	
	            }
	                        
	            // Fält 3 - Check that we got a bedomtTillstand element
	            if (inLakarutlatande.getBedomtTillstand() == null) {
	    			validationErrors.add("No bedomtTillstand element found!");	
	    			throw new Exception();
	            }
	            // Fält 3 - Bedömt tillstånd beskrivning - mandatory
	            if (inLakarutlatande.getBedomtTillstand().getBeskrivning() == null ||
	            	inLakarutlatande.getBedomtTillstand().getBeskrivning().length() < 1	) {
	    			validationErrors.add("No beskrivning in bedomtTillstand found!");	
	    			throw new Exception();
	            }
	 
	            // Fält 4 - vänster Check that we got a funktionstillstand - kroppsfunktion element  
	            FunktionstillstandType inKroppsFunktion = findFunktionsTillstandType(inLakarutlatande.getFunktionstillstand(), TypAvFunktionstillstand.KROPPSFUNKTION);
	            if (inKroppsFunktion == null ) {
	    			validationErrors.add("No funktionstillstand - kroppsfunktion element found!");	
	    			throw new Exception();
	        	}
	            // Fält 4 - vänster Funktionstillstand - kroppsfunktion beskrivning - mandatory  
	            if (inKroppsFunktion.getBeskrivning() == null || 
	            	inKroppsFunktion.getBeskrivning().length() < 1) {
	    			validationErrors.add("No beskrivning in funktionstillstand - kroppsfunktion found!");	
	            }
	
	            // Fält 4 - höger översta kryssrutan
	            VardkontaktType inUndersokning = findVardkontaktTyp(inLakarutlatande.getVardkontakt(), Vardkontakttyp.MIN_UNDERSOKNING_AV_PATIENTEN);     
	            
	            // Fält 4 - höger näst översta kryssrutan
	            VardkontaktType telefonkontakt = findVardkontaktTyp(inLakarutlatande.getVardkontakt(), Vardkontakttyp.MIN_TELEFONKONTAKT_MED_PATIENTEN);
	
	            // Fält 4 - höger näst nedersta kryssrutan
	            ReferensType journal = findReferensTyp(inLakarutlatande.getReferens(), Referenstyp.JOURNALUPPGIFTER);
	
	            // Fält 4 - höger nedersta kryssrutan
	            inAnnat = findReferensTyp(inLakarutlatande.getReferens(), Referenstyp.ANNAT);
	
	            // Fält 4 - höger Check that we at least got one field set
	            if (inUndersokning==null && telefonkontakt==null && journal==null && inAnnat==null) {
	    			validationErrors.add("No vardkontakt or referens element found ! At least one must be set!");
	    			throw new Exception();
	            }
	            // Fält 4 - höger - 1:a kryssrutan Check that we got a date if choice is set
	            if(inUndersokning != null && (inUndersokning.getVardkontaktstid() == null || !inUndersokning.getVardkontaktstid().isValid())) {
	    			validationErrors.add("No or wrong date for vardkontakt - min undersokning av patienten found!");                	
	            }
	            // Fält 4 - höger - 2:a kryssrutan Check that we got a date if choice is set
	            if(telefonkontakt != null && (telefonkontakt.getVardkontaktstid() == null || !telefonkontakt.getVardkontaktstid().isValid())) {
	    			validationErrors.add("No or wrong date for vardkontakt - telefonkontakt found!");                	
	            }
	            // Fält 4 - höger - 3:e kryssrutan Check that we got a date if choice is set
	            if(journal != null && (journal.getDatum() == null || !journal.getDatum().isValid())) {
	    			validationErrors.add("No or wrong date for referens - journal found!");                	
	            }
	            // Fält 4 - höger - 4:e kryssrutan Check that we got a date if choice is set
	            if(inAnnat != null && (inAnnat.getDatum() == null || !inAnnat.getDatum().isValid())) {
	    			validationErrors.add("No or wrong date for referens - annat found!");                	
	            }
	                   
	            // Fält 5 - beskrivning
	            if (inAktivitetFunktion.getBeskrivning() == null || 
	            	inAktivitetFunktion.getBeskrivning().length() < 1) {
	    			validationErrors.add("No beskrivning in funktionstillstand - aktivitet found!");	
	            }
	        
/*	          
	            // Fält 6a - kryssruta 1
	            AktivitetType kontaktAF = findAktivitetWithCode(inLakarutlatande.getAktivitet(), Aktivitetskod.PATIENTEN_BEHOVER_FA_KONTAKT_MED_ARBETSFORMEDLINGEN);
	
	            // Fält 6a - kryssruta 2
	            AktivitetType kontaktFHV = findAktivitetWithCode(inLakarutlatande.getAktivitet(), Aktivitetskod.PATIENTEN_BEHOVER_FA_KONTAKT_MED_FORETAGSHALSOVARDEN);
	
	            // Fält 6a - kryssruta 3
	            AktivitetType ovrigt = findAktivitetWithCode(inLakarutlatande.getAktivitet(), Aktivitetskod.OVRIGT);
	
	            // Fält 6b - kryssruta 1
	            AktivitetType planeradInomSjukvard = findAktivitetWithCode(inLakarutlatande.getAktivitet(), Aktivitetskod.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN);
	
	            // Fält 6b - kryssruta 2
	            AktivitetType planeradAnnat = findAktivitetWithCode(inLakarutlatande.getAktivitet(), Aktivitetskod.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD);
	
	            // Fält 6a+6b Check that at least one is checked
	            if (kontaktAF==null && kontaktFHV==null && ovrigt==null && planeradInomSjukvard==null && planeradAnnat==null) {
	    			validationErrors.add("No aktivitet element found for field 6a and 6b!Kontakt med AF, Kontakt med FHV, Ovrigt, Planerad eller pagaende behandling inom sjukvard or planerad eller pagaende annan atgard.");	
	    			throw new Exception();
	            }
	            // Fält 6a - kryssruta 3 - beskrivning                
	            if (ovrigt != null && (ovrigt.getBeskrivning() == null || ovrigt.getBeskrivning().length() < 1)) {
	    			validationErrors.add("No beskrivning in aktivitet element Ovrigt found!.");	                	
	            }
	            // Fält 6b - kryssruta 1 - beskrivning
	            if (planeradInomSjukvard != null && (planeradInomSjukvard.getBeskrivning() == null || planeradInomSjukvard.getBeskrivning().length() < 1)) {
	    			validationErrors.add("No beskrivning in aktivitet element Planerad eller pagaende behandling inom sjukvard found!.");	                	
	            }
	            // Fält 6b - kryssruta 2 - beskrivning
	            if (planeradAnnat != null && (planeradAnnat.getBeskrivning() == null || planeradAnnat.getBeskrivning().length() < 1)) {
	    			validationErrors.add("No beskrivning in aktivitet element planerad eller pagaende annan atgard found!.");	                	
	            }
*/	            
	            // Fält 7
	            AktivitetType arbRelRehabAktuell = findAktivitetWithCode(inLakarutlatande.getAktivitet(), Aktivitetskod.ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL);
	            AktivitetType arbRelRehabEjAktuell = findAktivitetWithCode(inLakarutlatande.getAktivitet(), Aktivitetskod.ARBETSLIVSINRIKTAD_REHABILITERING_AR_EJ_AKTUELL);
	            AktivitetType garEjAttBedommaArbRelRehab = findAktivitetWithCode(inLakarutlatande.getAktivitet(), Aktivitetskod.GAR_EJ_ATT_BEDOMMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL);
	            // Fält 7 - Check that at least one choice is made
	            if (arbRelRehabAktuell == null && arbRelRehabEjAktuell == null && garEjAttBedommaArbRelRehab == null) {
	    			validationErrors.add("No aktivitet element found for field 7.! Arbetslivsrehab aktuell, Arbetslivsrehab ej aktuell eller Arbetslivsrehab gar ej att bedoma.");	
	    			throw new Exception();                	
	            }
	            // Fält 7 - Check that only one choice is made
	            if ( (arbRelRehabAktuell != null && arbRelRehabEjAktuell != null && garEjAttBedommaArbRelRehab != null) ||
	            	 (arbRelRehabAktuell != null && arbRelRehabEjAktuell != null && garEjAttBedommaArbRelRehab == null) ||
	            	 (arbRelRehabAktuell != null && arbRelRehabEjAktuell == null && garEjAttBedommaArbRelRehab != null) ||
	            	 (arbRelRehabAktuell == null && arbRelRehabEjAktuell != null && garEjAttBedommaArbRelRehab != null) ) {
	    			validationErrors.add("Only one ckeckbox is allowed for field 7! Arbetslivsrehab aktuell, Arbetslivsrehab ej aktuell eller Arbetslivsrehab gar ej att bedoma.");	              	
	            }
	            
	            // Fält 8a - Check that we got a arbetsformaga element
	            if (inAktivitetFunktion.getArbetsformaga() == null) {
	    			validationErrors.add("No arbetsformaga element found for field 8a!");	
	    			throw new Exception();                	
	            }
	
	            // Fält 8a
	            SysselsattningType inArbete = findTypAvSysselsattning(inAktivitetFunktion.getArbetsformaga().getSysselsattning(), TypAvSysselsattning.NUVARANDE_ARBETE);
	            SysselsattningType inArbetslos = findTypAvSysselsattning(inAktivitetFunktion.getArbetsformaga().getSysselsattning(), TypAvSysselsattning.ARBETSLOSHET);
	            SysselsattningType inForaldraledig = findTypAvSysselsattning(inAktivitetFunktion.getArbetsformaga().getSysselsattning(), TypAvSysselsattning.FORALDRALEDIGHET);
	            // Fält 8a - Check that we at least got one choice
	            if (inArbete==null && inArbetslos==null && inForaldraledig==null) {
	    			validationErrors.add("No sysselsattning element found for field 8a! Nuvarande arbete, arbestloshet or foraldraledig should be set.");	
	    			throw new Exception();                	
	            }                
	            ArbetsuppgiftType inArbetsBeskrivning = inAktivitetFunktion.getArbetsformaga().getArbetsuppgift();
	            // Fält 8a - Check that we got a arbetsuppgift element if arbete is set                
	            if (inArbete != null && inArbetsBeskrivning == null) {
	            	validationErrors.add("No arbetsuppgift element found when arbete set in field 8a!.");	
	    			throw new Exception();                	
	            }
                // Fält 8a - 1:a kryssrutan - beskrivning
                if (inArbete != null && (inArbetsBeskrivning.getTypAvArbetsuppgift() == null || inArbetsBeskrivning.getTypAvArbetsuppgift().length() < 1)) {
                	validationErrors.add("No typAvArbetsuppgift found when arbete set in field 8a!.");	
        			throw new Exception();                	
                }
	        }
	
	        // Fält 8b - kryssruta 1
	        ArbetsformagaNedsattningType nedsatt14del =  findArbetsformaga(inAktivitetFunktion.getArbetsformaga().getArbetsformagaNedsattning(), se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.Nedsattningsgrad.NEDSATT_MED_1_4);
	
	        // Fält 8b - kryssruta 2
	        ArbetsformagaNedsattningType nedsatthalften =  findArbetsformaga(inAktivitetFunktion.getArbetsformaga().getArbetsformagaNedsattning(), se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.Nedsattningsgrad.NEDSATT_MED_1_2);
	        
	        // Fält 8b - kryssruta 3
	        ArbetsformagaNedsattningType nedsatt34delar =  findArbetsformaga(inAktivitetFunktion.getArbetsformaga().getArbetsformagaNedsattning(), se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.Nedsattningsgrad.NEDSATT_MED_3_4);
	
	        // Fält 8b - kryssruta 4
	        ArbetsformagaNedsattningType heltNedsatt =  findArbetsformaga(inAktivitetFunktion.getArbetsformaga().getArbetsformagaNedsattning(), se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.Nedsattningsgrad.HELT_NEDSATT);
	
	        // Check that we at least got one choice
	        if (nedsatt14del == null && nedsatthalften == null && nedsatt34delar == null && heltNedsatt == null) {
	        	validationErrors.add("No arbetsformaganedsattning element found 8b!.");	
				throw new Exception();                	
	        }            
	        // Fält 8b - kryssruta 1 - varaktighet From
	        if (nedsatt14del != null && (nedsatt14del.getVaraktighetFrom() == null || !nedsatt14del.getVaraktighetFrom().isValid())) {
	        	validationErrors.add("No or wrong date for nedsatt 1/4 from date found!");		
	        }
	        // Fält 8b - kryssruta 1 - varaktighet Tom
	        if (nedsatt14del != null && (nedsatt14del.getVaraktighetTom() == null || !nedsatt14del.getVaraktighetTom().isValid())) {
	        	validationErrors.add("No or wrong date for nedsatt 1/4 tom date found!");		
	        }
	        // Fält 8b - kryssruta 2 - varaktighet From
	        if (nedsatthalften != null && (nedsatthalften.getVaraktighetFrom() == null || !nedsatthalften.getVaraktighetFrom().isValid())) {
	        	validationErrors.add("No or wrong date for nedsatt 1/2 from date found!");		
	        }
	        // Fält 8b - kryssruta 2 - varaktighet Tom
	        if (nedsatthalften != null && (nedsatthalften.getVaraktighetTom() == null || !nedsatthalften.getVaraktighetTom().isValid())) {
	        	validationErrors.add("No or wrong date for nedsatt 1/2 tom date found!");		
	        }
	        // Fält 8b - kryssruta 3 - varaktighet From
	        if (nedsatt34delar != null && (nedsatt34delar.getVaraktighetFrom() == null || !nedsatt34delar.getVaraktighetFrom().isValid())) {
	        	validationErrors.add("No or wrong date for nedsatt 3/4 from date found!");		
	        }
	        // Fält 8b - kryssruta 3 - varaktighet Tom
	        if (nedsatt34delar != null && (nedsatt34delar.getVaraktighetTom() == null || !nedsatt34delar.getVaraktighetTom().isValid())) {
	        	validationErrors.add("No or wrong date for nedsatt 3/4 tom date found!");		
	        }
	        // Fält 8b - kryssruta 4 - varaktighet From
	        if (heltNedsatt != null && (heltNedsatt.getVaraktighetFrom() == null || !heltNedsatt.getVaraktighetFrom().isValid())) {
	        	validationErrors.add("No or wrong date for helt nedsatt from date found!");		
	        }
	        // Fält 8b - kryssruta 4 - varaktighet Tom
	        if (heltNedsatt != null && (heltNedsatt.getVaraktighetTom() == null || !heltNedsatt.getVaraktighetTom().isValid())) {
	        	validationErrors.add("No or wrong date for helt nedsatt tom date found!");		
	        }
	        
	        // Fält 9 - Motivering - optional
	        String inMotivering = inAktivitetFunktion.getArbetsformaga().getMotivering();
	        
	        // Fält 10 - Prognosangivelse
	        if (inAktivitetFunktion.getArbetsformaga().getPrognosangivelse() == null) {
				validationErrors.add("No prognos element found for field 10!");	
				throw new Exception();                	
	        }
	        boolean inArbetsformagaAterstallasHelt = inAktivitetFunktion.getArbetsformaga().getPrognosangivelse().compareTo(Prognosangivelse.ATERSTALLAS_HELT) == 0;
	        boolean inArbetsformagaAterstallasDelvis = inAktivitetFunktion.getArbetsformaga().getPrognosangivelse().compareTo(Prognosangivelse.ATERSTALLAS_DELVIS) == 0;
	        boolean inArbetsformagaEjAterstallas = inAktivitetFunktion.getArbetsformaga().getPrognosangivelse().compareTo(Prognosangivelse.INTE_ATERSTALLAS) == 0;
	        boolean inArbetsformagaGarEjAttBedomma = inAktivitetFunktion.getArbetsformaga().getPrognosangivelse().compareTo(Prognosangivelse.DET_GAR_INTE_ATT_BEDOMMA) == 0;
	        // Fält 10 - Prognosangivelse - Check that we at least got one choice
	        if (!inArbetsformagaAterstallasHelt && !inArbetsformagaAterstallasDelvis && !inArbetsformagaEjAterstallas && !inArbetsformagaGarEjAttBedomma) {
	        	validationErrors.add("No prognosangivelse element found for field 10.");	
				throw new Exception();                	            	
	        }
	        // If we got more then one prognoselement these will not be read as only the first is set!
	        int inPrognosCount = 0;
	        if (inArbetsformagaAterstallasHelt) {
	        	inPrognosCount++;
	        }
	        if (inArbetsformagaAterstallasDelvis) {
	        	inPrognosCount++;
	        }
	        if (inArbetsformagaEjAterstallas) {
	        	inPrognosCount++;
	        }
	        if (inArbetsformagaGarEjAttBedomma) {
	        	inPrognosCount++;
	        }
	        // Fält 10 - Prognosangivelse - Check that we only got one choice            
	        if (inPrognosCount > 2) {
	            validationErrors.add("Only one prognosangivelse should be set for field 10.");	
	        }            
	        
	        // Fält 11 - optional
	        AktivitetType inForandratRessatt = findAktivitetWithCode(inLakarutlatande.getAktivitet(), Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT);
	        AktivitetType inEjForandratRessatt = findAktivitetWithCode(inLakarutlatande.getAktivitet(), Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT);
	        // Fält 11 - If set only one should be set
	        if (inForandratRessatt != null && inEjForandratRessatt != null) {
	            validationErrors.add("Only one forandrat ressatt could be set for field 11.");	
	        }
	        
	        // Fält 12 - kryssruta 1 - optional
	        AktivitetType inKontaktFKAktuell = findAktivitetWithCode(inRequest.getLakarutlatande().getAktivitet(), Aktivitetskod.KONTAKT_MED_FORSAKRINGSKASSAN_AR_AKTUELL);
	        
	        // Fält 13 - Upplysningar - optional
	        // If field 4 annat satt or field 10 går ej att bedömma is set then field 13 should contain data.
			String kommentar = inRequest.getLakarutlatande().getKommentar();
	        if ( (inAnnat!=null || inArbetsformagaGarEjAttBedomma) && (kommentar == null || kommentar.length() < 1) ){
	            validationErrors.add("Upplysningar should contain data as field 4 or fields 10 is checked.");	            	
	        }
	
	        // Fält 14 - Signeringstidpunkt
	        if (inLakarutlatande.getSigneringsdatum() == null || !inLakarutlatande.getSigneringsdatum().isValid()) {
	            validationErrors.add("Signeringsdatum must be set (14)");	            	
	        }
	        
	        // Fält 17 - arbetsplatskod - Check that we got an element
	        if (inEnhet.getArbetsplatskod() == null ) {
				validationErrors.add("No Arbetsplatskod element found!");	
				throw new Exception();
	        }
	        II inArbetsplatskod = inEnhet.getArbetsplatskod();
	        // Fält 17 arbetsplatskod id            
	        if (inArbetsplatskod.getExtension() == null || 
	        	inArbetsplatskod.getExtension().length() < 1) {
	            validationErrors.add("Arbetsplatskod for enhet not found!");	            	            	
	        }
	        // Fält 17 arbetsplatskod o.i.d.
	        if (inArbetsplatskod.getRoot() == null || 
	            !inArbetsplatskod.getRoot().equalsIgnoreCase("1.2.752.29.4.71") ) {
				validationErrors.add("Wrong o.i.d. for arbetsplatskod! Should be 1.2.752.29.4.71");								
	        }
	        
			// Check if we got any validation errors that not caused an Exception
			if (validationErrors.size() > 0) {
				logger.error("Validate exception:" + getValidationErrors(validationErrors));
				throw new Exception();
			} 
			
			// No validation errors!           
		} catch (Exception e) {
			throw new Exception(getValidationErrors(validationErrors));
		}
	}

	private String getValidationErrors(ArrayList<String> validationErrors) {
		int i = 1;
		StringBuffer validationString = new StringBuffer();
		Iterator<String> iterValidationErrors = validationErrors.iterator();
		validationString.append("Validation error " + i++ + ":");
		validationString.append((String)iterValidationErrors.next());
		while (iterValidationErrors.hasNext()) {
			validationString.append("\n\rValidation error " + i++ + ":");
			validationString.append((String)iterValidationErrors.next());
		}
		return validationString.toString();
	}

	private AktivitetType findAktivitetWithCode(List<AktivitetType> aktiviteter, Aktivitetskod aktivitetskod) {
		AktivitetType foundAktivitet = null;
		if (aktiviteter != null) {
			for (int i = 0; i< aktiviteter.size(); i++) {
				AktivitetType listAktivitet = (AktivitetType)aktiviteter.get(i);
				if (listAktivitet.getAktivitetskod() != null && listAktivitet.getAktivitetskod().compareTo(aktivitetskod) == 0) {
					foundAktivitet = listAktivitet;
					break;
				}
			}	
		}
		return foundAktivitet;
	}
	
	private FunktionstillstandType findFunktionsTillstandType(List<FunktionstillstandType> funktionstillstand, TypAvFunktionstillstand funktionstillstandsTyp) {
		FunktionstillstandType foundFunktionstillstand = null;
		if (funktionstillstand != null) {
			for (int i = 0; i< funktionstillstand.size(); i++) {
				FunktionstillstandType listFunktionstillstand = (FunktionstillstandType)funktionstillstand.get(i);
				if (listFunktionstillstand.getTypAvFunktionstillstand() != null && listFunktionstillstand.getTypAvFunktionstillstand().compareTo(funktionstillstandsTyp) == 0) {
					foundFunktionstillstand = listFunktionstillstand;
					break;
				}
			}		
		}
		return foundFunktionstillstand;
	}
	
	private VardkontaktType findVardkontaktTyp(List<VardkontaktType> vardkontakter, Vardkontakttyp vardkontaktTyp) {
		VardkontaktType foundVardkontaktType = null;
		if (vardkontakter != null) {
			for (int i = 0; i< vardkontakter.size(); i++) {
				VardkontaktType listVardkontakter = (VardkontaktType)vardkontakter.get(i);
				if (listVardkontakter.getVardkontakttyp() != null && listVardkontakter.getVardkontakttyp().compareTo(vardkontaktTyp) == 0) {
					foundVardkontaktType = listVardkontakter;
					break;
				}
			}
		}
		return foundVardkontaktType;
	}
	
	private ReferensType findReferensTyp(List<ReferensType> referenser, Referenstyp referensTyp) {
		ReferensType foundReferensType = null;
		if (referenser != null) {
			for (int i = 0; i< referenser.size(); i++) {
				ReferensType listReferenser = (ReferensType)referenser.get(i);
				if (listReferenser.getReferenstyp() != null && listReferenser.getReferenstyp().compareTo(referensTyp) == 0) {
					foundReferensType = listReferenser;
					break;
				}
			}	
		}
		return foundReferensType;
	}	
	
	private SysselsattningType findTypAvSysselsattning(List<SysselsattningType> sysselsattning, TypAvSysselsattning sysselsattningsTyp) {
		SysselsattningType foundSysselsattningType = null;
		if (sysselsattning != null) {
			for (int i = 0; i< sysselsattning.size(); i++) {
				SysselsattningType listSysselsattning = (SysselsattningType)sysselsattning.get(i);
				if (listSysselsattning.getTypAvSysselsattning() != null && listSysselsattning.getTypAvSysselsattning().compareTo(sysselsattningsTyp) == 0) {
					foundSysselsattningType = listSysselsattning;
					break;
				}
			}
		}
		return foundSysselsattningType;
	}	
	
	private ArbetsformagaNedsattningType findArbetsformaga(List<ArbetsformagaNedsattningType> arbetsformaga, se.skl.riv.insuranceprocess.healthreporting.mu7263.v3.Nedsattningsgrad arbetsformagaNedsattningTyp) {
		ArbetsformagaNedsattningType foundArbetsformagaType = null;
		if (arbetsformaga != null) {
			for (int i = 0; i< arbetsformaga.size(); i++) {
				ArbetsformagaNedsattningType listArbetsformaga = (ArbetsformagaNedsattningType)arbetsformaga.get(i);
				if (listArbetsformaga.getNedsattningsgrad() != null && listArbetsformaga.getNedsattningsgrad().compareTo(arbetsformagaNedsattningTyp) == 0) {
					foundArbetsformagaType = listArbetsformaga;
					break;
				}
			}	
		}
		return foundArbetsformagaType;
	}		
}