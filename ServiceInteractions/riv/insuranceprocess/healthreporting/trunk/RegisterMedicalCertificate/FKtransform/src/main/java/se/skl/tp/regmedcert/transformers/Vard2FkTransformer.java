package se.skl.tp.regmedcert.transformers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.stream.XMLStreamReader;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.wsaddressing10.AttributedURIType;

import se.fk.vardgivare.sjukvard.taemotlakarintygresponder.v1.TaEmotLakarintygType;
import se.fk.vardgivare.sjukvard.v1.Adress;
import se.fk.vardgivare.sjukvard.v1.Adressering;
import se.fk.vardgivare.sjukvard.v1.Enhet;
import se.fk.vardgivare.sjukvard.v1.InternIdentitetsbeteckning;
import se.fk.vardgivare.sjukvard.v1.Kontaktuppgifter;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg;
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
import se.fk.vardgivare.sjukvard.v1.Adressering.Avsandare;
import se.fk.vardgivare.sjukvard.v1.Adressering.Mottagare;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Arbetsformaga;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Begransning;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Diagnos;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Kontakt;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Lakare;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Motivering;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Planering;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Prognos;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Rehabilitering;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Rekommendationer;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Resor;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Sjukdomshistoria;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Smittskydd;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Status;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Sysselsattning;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Upplysningar;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Arbetsformaga.Nedsattningsgrad;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Planering.Behandling;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Rekommendationer.Rekommendation;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Status.Basering;
import se.fk.vardgivare.sjukvard.v1.Lakarintyg.Status.Basering.Ursprung;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.AktivitetType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.Aktivitetskod;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.ArbetsformagaNedsattningType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.FunktionstillstandType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.Prognosangivelse;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.ReferensType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.Referenstyp;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.SysselsattningType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.TypAvFunktionstillstand;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.TypAvSysselsattning;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.VardkontaktType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.Vardkontakttyp;
import se.skl.riv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v2.RegisterMedicalCertificateType;


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
			
			// Extract all incoming data to local variables
            String emuId = inRequest.getLakarutlatande().getLakarutlatandeId();
            String inPersonnummer = inRequest.getLakarutlatande().getPatient().getPersonId().getExtension();
            String inFornamn = inRequest.getLakarutlatande().getPatient().getFornamn();
            String inEfternamn = inRequest.getLakarutlatande().getPatient().getEfternamn();
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
            ArbetsformagaNedsattningType nedsatt14del =  findArbetsformaga(inAktivitetFunktion.getArbetsformaga().getArbetsformagaNedsattning(), se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.Nedsattningsgrad.NEDSATT_MED_1_2);
            ArbetsformagaNedsattningType nedsatthalften =  findArbetsformaga(inAktivitetFunktion.getArbetsformaga().getArbetsformagaNedsattning(), se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.Nedsattningsgrad.NEDSATT_MED_1_2);
            ArbetsformagaNedsattningType nedsatt34delar =  findArbetsformaga(inAktivitetFunktion.getArbetsformaga().getArbetsformagaNedsattning(), se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.Nedsattningsgrad.NEDSATT_MED_3_4);
            ArbetsformagaNedsattningType heltNedsatt =  findArbetsformaga(inAktivitetFunktion.getArbetsformaga().getArbetsformagaNedsattning(), se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.Nedsattningsgrad.HELT_NEDSATT);
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
            patient.setFornamn(inFornamn);
            patient.setEfternamn(inEfternamn);
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

            // Fält 7 - validera endast ett val!
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
            	nedsattningsgrad.setBeskrivning(rb.getString("FK_NEDSATT_HALFTEN"));            	
            	nedsattningsgrad.setFranOchMed(nedsatthalften.getVaraktighetFrom());
            	nedsattningsgrad.setLangstTillOchMed(nedsatthalften.getVaraktighetTom());
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
            
            // Fält 10 - Prognosangivelse - Validera endast 1 av 4 val giltigt
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

            // Fält 12 - kryssruta 1 och 2 Endast 1 av 2 val möjligt!
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
			throw new RuntimeException(e);
		}
    }
	
	private AktivitetType findAktivitetWithCode(List aktiviteter, Aktivitetskod aktivitetskod) {
		AktivitetType foundAktivitet = null;
		for (int i = 0; i< aktiviteter.size(); i++) {
			AktivitetType listAktivitet = (AktivitetType)aktiviteter.get(i);
			if (listAktivitet.getAktivitetskod() != null && listAktivitet.getAktivitetskod().compareTo(aktivitetskod) == 0) {
				foundAktivitet = listAktivitet;
				break;
			}
		}		
		return foundAktivitet;
	}
	
	private FunktionstillstandType findFunktionsTillstandType(List funktionstillstand, TypAvFunktionstillstand funktionstillstandsTyp) {
		FunktionstillstandType foundFunktionstillstand = null;
		for (int i = 0; i< funktionstillstand.size(); i++) {
			FunktionstillstandType listFunktionstillstand = (FunktionstillstandType)funktionstillstand.get(i);
			if (listFunktionstillstand.getTypAvFunktionstillstand() != null && listFunktionstillstand.getTypAvFunktionstillstand().compareTo(funktionstillstandsTyp) == 0) {
				foundFunktionstillstand = listFunktionstillstand;
				break;
			}
		}		
		return foundFunktionstillstand;
	}

	private VardkontaktType findVardkontaktTyp(List vardkontakter, Vardkontakttyp vardkontaktTyp) {
		VardkontaktType foundVardkontaktType = null;
		for (int i = 0; i< vardkontakter.size(); i++) {
			VardkontaktType listVardkontakter = (VardkontaktType)vardkontakter.get(i);
			if (listVardkontakter.getVardkontakttyp() != null && listVardkontakter.getVardkontakttyp().compareTo(vardkontaktTyp) == 0) {
				foundVardkontaktType = listVardkontakter;
				break;
			}
		}		
		return foundVardkontaktType;
	}
	
	private ReferensType findReferensTyp(List referenser, Referenstyp referensTyp) {
		ReferensType foundReferensType = null;
		for (int i = 0; i< referenser.size(); i++) {
			ReferensType listReferenser = (ReferensType)referenser.get(i);
			if (listReferenser.getReferenstyp() != null && listReferenser.getReferenstyp().compareTo(referensTyp) == 0) {
				foundReferensType = listReferenser;
				break;
			}
		}		
		return foundReferensType;
	}	

	private SysselsattningType findTypAvSysselsattning(List sysselsattning, TypAvSysselsattning sysselsattningsTyp) {
		SysselsattningType foundSysselsattningType = null;
		for (int i = 0; i< sysselsattning.size(); i++) {
			SysselsattningType listSysselsattning = (SysselsattningType)sysselsattning.get(i);
			if (listSysselsattning.getTypAvSysselsattning() != null && listSysselsattning.getTypAvSysselsattning().compareTo(sysselsattningsTyp) == 0) {
				foundSysselsattningType = listSysselsattning;
				break;
			}
		}		
		return foundSysselsattningType;
	}	

	private ArbetsformagaNedsattningType findArbetsformaga(List arbetsformaga, se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.Nedsattningsgrad arbetsformagaNedsattningTyp) {
		ArbetsformagaNedsattningType foundArbetsformagaType = null;
		for (int i = 0; i< arbetsformaga.size(); i++) {
			ArbetsformagaNedsattningType listArbetsformaga = (ArbetsformagaNedsattningType)arbetsformaga.get(i);
			if (listArbetsformaga.getNedsattningsgrad() != null && listArbetsformaga.getNedsattningsgrad().compareTo(arbetsformagaNedsattningTyp) == 0) {
				foundArbetsformagaType = listArbetsformaga;
				break;
			}
		}		
		return foundArbetsformagaType;
	}	
	
	private XMLGregorianCalendar getDate(String stringDate) throws Exception{

		try {
			GregorianCalendar fromDate = new GregorianCalendar();
			DateFormat dfm = new SimpleDateFormat("yyyyMMdd");
			Date date = dfm.parse(stringDate);
			fromDate.setTime(date);
			return (DatatypeFactory.newInstance().newXMLGregorianCalendar(fromDate));
		} catch (DatatypeConfigurationException e) {
			throw new Exception(e.getMessage());
		} catch (ParseException pe) {
			throw new Exception(pe.getMessage());
		}
	}
	
}