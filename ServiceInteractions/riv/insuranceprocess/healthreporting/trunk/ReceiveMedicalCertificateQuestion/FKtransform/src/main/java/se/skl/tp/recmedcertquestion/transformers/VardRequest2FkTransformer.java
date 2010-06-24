package se.skl.tp.recmedcertquestion.transformers;

import iso.v21090.dt.v1.II;

import java.util.List;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.stream.XMLStreamReader;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.wsaddressing10.AttributedURIType;

import se.fk.vardgivare.sjukvard.taemotfragaresponder.v1.TaEmotFragaType;
import se.fk.vardgivare.sjukvard.v1.Adress;
import se.fk.vardgivare.sjukvard.v1.Adressering;
import se.fk.vardgivare.sjukvard.v1.Amne;
import se.fk.vardgivare.sjukvard.v1.Enhet;
import se.fk.vardgivare.sjukvard.v1.Falt;
import se.fk.vardgivare.sjukvard.v1.InternIdentitetsbeteckning;
import se.fk.vardgivare.sjukvard.v1.Kontaktuppgifter;
import se.fk.vardgivare.sjukvard.v1.Lakarintygsreferens;
import se.fk.vardgivare.sjukvard.v1.Namn;
import se.fk.vardgivare.sjukvard.v1.Organisation;
import se.fk.vardgivare.sjukvard.v1.Patient;
import se.fk.vardgivare.sjukvard.v1.Person;
import se.fk.vardgivare.sjukvard.v1.Postadress;
import se.fk.vardgivare.sjukvard.v1.Postnummer;
import se.fk.vardgivare.sjukvard.v1.Postort;
import se.fk.vardgivare.sjukvard.v1.TaEmotFraga;
import se.fk.vardgivare.sjukvard.v1.Telefon;
import se.fk.vardgivare.sjukvard.v1.Adressering.Avsandare;
import se.fk.vardgivare.sjukvard.v1.Adressering.Mottagare;
import se.skl.riv.insuranceprocess.healthreporting.qa.v1.AdresseringsType;
import se.skl.riv.insuranceprocess.healthreporting.qa.v1.Amnetyp;
import se.skl.riv.insuranceprocess.healthreporting.qa.v1.KompletteringType;
import se.skl.riv.insuranceprocess.healthreporting.qa.v1.LakarutlatandeEnkelType;
import se.skl.riv.insuranceprocess.healthreporting.qa.v1.Meddelandetyp;
import se.skl.riv.insuranceprocess.healthreporting.receivemedicalcertificatequestionsponder.v1.ReceiveMedicalCertificateQuestionType;
import se.skl.riv.insuranceprocess.healthreporting.v1.EnhetType;
import se.skl.riv.insuranceprocess.healthreporting.v1.HosPersonalType;
import se.skl.riv.insuranceprocess.healthreporting.v1.OrganisationType;
import se.skl.riv.insuranceprocess.healthreporting.v1.PatientType;
import se.skl.riv.insuranceprocess.healthreporting.v1.VardgivareType;

public class VardRequest2FkTransformer extends AbstractMessageAwareTransformer
{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public VardRequest2FkTransformer()
    {
        super();
        registerSourceType(Object.class);
        setReturnClass(Object.class); 
    }
    
	public Object transform(MuleMessage message, String outputEncoding) throws TransformerException {
		ResourceBundle rb = ResourceBundle.getBundle("fkdata");	    

		try {			
			// Transform the XML payload into a JAXB object
            Unmarshaller unmarshaller = JAXBContext.newInstance(ReceiveMedicalCertificateQuestionType.class).createUnmarshaller();
            XMLStreamReader streamPayload = (XMLStreamReader)((Object[])message.getPayload())[1];
            ReceiveMedicalCertificateQuestionType inRequest = (ReceiveMedicalCertificateQuestionType)((JAXBElement)unmarshaller.unmarshal(streamPayload)).getValue();
    		
			// Get receiver to adress from Mule property
			String receiverId = (String)message.getProperty("receiverid");			

			// Create new JAXB object for the outgoing data
			TaEmotFragaType outRequest = new TaEmotFragaType();
    		TaEmotFraga outTaEmotFraga = new TaEmotFraga();
    		outRequest.setFKSKLTaEmotFragaAnrop(outTaEmotFraga);

			// Transform between incoming and outgoing objects
    		// Avsändare - Vården
    		AdresseringsType inAvsandare = inRequest.getQuestion().getAvsandare();
    		HosPersonalType inHoSPersonalAvsandare = inAvsandare.getHosPersonal();
    		EnhetType inEnhetAvsandare = inHoSPersonalAvsandare.getEnhet();
    		VardgivareType inVardgivareAvsandare = inEnhetAvsandare.getVardgivare();
    		
    		Avsandare outAvsandare = new Avsandare();
    		Adressering outAdressering = new Adressering();
    		outAdressering.setAvsandare(outAvsandare);
    		outTaEmotFraga.setAdressering(outAdressering);

    		Organisation outOrganisationAvsandare = new Organisation();
    		InternIdentitetsbeteckning outOrganisationIdAvsandare = new InternIdentitetsbeteckning();
    		outOrganisationIdAvsandare.setValue(inVardgivareAvsandare.getVardgivareId().getExtension()); 		
    		outOrganisationAvsandare.setId(outOrganisationIdAvsandare);
    		Namn outOrganisationNamnAvsandare = new Namn();
    		outOrganisationNamnAvsandare.setValue(inVardgivareAvsandare.getVardgivarnamn());
    		outOrganisationAvsandare.setNamn(outOrganisationNamnAvsandare);
    		outAvsandare.setOrganisation(outOrganisationAvsandare);
    		
    		Enhet outEnhetAvsandare = new Enhet();
    		InternIdentitetsbeteckning outEnhetIdAvsandare = new InternIdentitetsbeteckning();
    		outEnhetIdAvsandare.setValue(inEnhetAvsandare.getEnhetsId().getExtension());
    		outEnhetAvsandare.setId(outEnhetIdAvsandare);
    		Namn outEnhetNamnAvsandare = new Namn();
    		outEnhetNamnAvsandare.setValue(inEnhetAvsandare.getEnhetsnamn());
    		outEnhetAvsandare.setNamn(outEnhetNamnAvsandare);
    		Kontaktuppgifter outEnhetKontaktuppgifterAvsandare = new Kontaktuppgifter();
    		Adress outEnhetAdressAvsandare = new Adress();
    		Postadress outEnhetPostadressAvsandare = new Postadress();
    		outEnhetPostadressAvsandare.setValue(inEnhetAvsandare.getPostadress());
    		outEnhetAdressAvsandare.setPostadress(outEnhetPostadressAvsandare);
    		Postnummer outEnhetPostnummerAvsandare = new Postnummer();
    		outEnhetPostnummerAvsandare.setValue(inEnhetAvsandare.getPostnummer());
    		outEnhetAdressAvsandare.setPostnummer(outEnhetPostnummerAvsandare);
    		Postort outEnhetPostortAvsandare = new Postort();
    		outEnhetPostortAvsandare.setValue(inEnhetAvsandare.getPostort());
    		outEnhetAdressAvsandare.setPostort(outEnhetPostortAvsandare);
    		outEnhetKontaktuppgifterAvsandare.setAdress(outEnhetAdressAvsandare);
    		Telefon outEnhetTelefonAvsandare = new Telefon();
    		outEnhetTelefonAvsandare.setValue(inEnhetAvsandare.getTelefonnummer());
    		outEnhetKontaktuppgifterAvsandare.setTelefon(outEnhetTelefonAvsandare);
    		outEnhetAvsandare.setKontaktuppgifter(outEnhetKontaktuppgifterAvsandare);
    		outOrganisationAvsandare.setEnhet(outEnhetAvsandare);

    		Person outPersonAvsandare = new Person();
    		InternIdentitetsbeteckning outPersonIdAvsandare = new InternIdentitetsbeteckning();
    		outPersonIdAvsandare.setValue(inHoSPersonalAvsandare.getPersonalId().getExtension());
    		outPersonAvsandare.setId(outPersonIdAvsandare);
    		outPersonAvsandare.setFornamn(inHoSPersonalAvsandare.getFornamn());
    		outPersonAvsandare.setEfternamn(inHoSPersonalAvsandare.getEfternamn());
    		outEnhetAvsandare.setPerson(outPersonAvsandare);
    		
    		// Mottagare - FK
    		AdresseringsType inMottagare = inRequest.getQuestion().getMottagare();
    		OrganisationType inOrganisationMottagare = inMottagare.getOrganisation();
    		
    		Mottagare outMottagare = new Mottagare();
    		outAdressering.setMottagare(outMottagare);
    		Organisation outOrganisationMottagare = new Organisation();
    		outMottagare.setOrganisation(outOrganisationMottagare);
    		
    		Namn outOrganisationNamnMottagare = new Namn();
    		outOrganisationNamnMottagare.setValue(rb.getString(""));
    		outOrganisationMottagare.setNamn(outOrganisationNamnMottagare);
    		InternIdentitetsbeteckning outOrganisationIdMottagare = new InternIdentitetsbeteckning();
    		outOrganisationIdMottagare.setValue("");
    		outOrganisationMottagare.setId(outOrganisationIdMottagare);    		
    		
//    		// Avsänt tidpunkt
//    		XMLGregorianCalendar inSkickades = inRequest.getFKSKLTaEmotFragaAnrop().getAdressering().getSkickades();
//    		outMeddelande.setAvsantTidpunkt(inSkickades);
//    		
//    		// Set läkarutlåtande enkel från vården
//    		Lakarintygsreferens inLakarutlatande = inRequest.getFKSKLTaEmotFragaAnrop().getLakarintyg();
//    		Patient inPatient = inRequest.getFKSKLTaEmotFragaAnrop().getPatient();
//    		
//    		LakarutlatandeEnkelType outLakarutlatandeEnkel = new LakarutlatandeEnkelType();
//    		PatientType outPatient = new PatientType();
//    		II outPersonId = new II();
//    		outPersonId.setRoot("1.2.752.129.2.1.3.1"); // OID för samordningsnummer är 1.2.752.129.2.1.3.3.
//    		outPersonId.setExtension(inPatient.getIdentifierare());
//    		outPatient.setPersonId(outPersonId);
//    		// Hur göra med fullständigt namn?
//    		outPatient.setFornamn(inPatient.getFornamn()); 
//    		outPatient.setEfternamn(inPatient.getEfternamn());
//    		outLakarutlatandeEnkel.setPatient(outPatient);
//    		outLakarutlatandeEnkel.setLakarutlatandeId(inLakarutlatande.getReferens());
//    		// Skall det vara avsänt tidpunkt eller signerades tidpunkt?
//    		outLakarutlatandeEnkel.setAvsantTidpunkt(inLakarutlatande.getSignerades());
//    		outMeddelande.setLakarutlatande(outLakarutlatandeEnkel);
//    	
//    		// Set Försäkringskassans id
//    		outMeddelande.setForsakringskassansArendeId(inAvsandare.getReferens().getValue());
//    		
//    		// Set meddelandetyp
//    		outMeddelande.setMeddelandetyp(Meddelandetyp.FRAGA_FRAN_FK);
//
//    		// Set ämne
//    		Amne inAmne = inRequest.getFKSKLTaEmotFragaAnrop().getAmne();
//    		outMeddelande.setAmne(transformAmneFromFK(inAmne));
//    		
//    		// Set meddelande rubrik och text
//    		outMeddelande.setMeddelanderubrik(inAmne.getFritext());
//    		outMeddelande.setMeddelandetext(inRequest.getFKSKLTaEmotFragaAnrop().getFraga().getText());
//
//    		// Sista datum för komplettering
//    		outMeddelande.setSistaDatumForKomplettering(inRequest.getFKSKLTaEmotFragaAnrop().getBesvaras());	
//    		
//    		// Meddelande id???
//    		outMeddelande.setMeddelandeId("Referens till meddelande instansen");
    		
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
		
	private Amnetyp transformAmneFromFK(Amne inAmne) {
		if (inAmne.getBeskrivning().equalsIgnoreCase("Arbetstidsförläggning")) {
			return Amnetyp.ARBETSTIDSFORLAGGNING;
		} else if (inAmne.getBeskrivning().equalsIgnoreCase("Avstämningsmöte")) {
			return Amnetyp.AVSTAMNINGSMOTE;
		} else if (inAmne.getBeskrivning().equalsIgnoreCase("Komplettering")) {
			return Amnetyp.KOMPLETTERING_AV_LAKARINTYG;
		} else if (inAmne.getBeskrivning().equalsIgnoreCase("Kontakt")) {
			return Amnetyp.KONTAKT;
// Skall detta vara en egen enumeration på vårdsidan?
		} else if (inAmne.getBeskrivning().equalsIgnoreCase("Påminnelse")) {
			return Amnetyp.OVRIGT;
		} else if (inAmne.getBeskrivning().equalsIgnoreCase("Övrigt")) {
			return Amnetyp.OVRIGT;
		} else {
			return Amnetyp.OVRIGT;
		}
	}	
}