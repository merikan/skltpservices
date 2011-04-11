package se.skl.tp.sendmedcertquestion.transformers;

import java.util.ResourceBundle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamReader;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.wsaddressing10.AttributedURIType;

import se.fk.vardgivare.sjukvard.taemotfragaresponder.v1.TaEmotFragaType;
import se.fk.vardgivare.sjukvard.taemotsvarresponder.v1.TaEmotSvarType;
import se.fk.vardgivare.sjukvard.v1.Adress;
import se.fk.vardgivare.sjukvard.v1.Adressering;
import se.fk.vardgivare.sjukvard.v1.Amne;
import se.fk.vardgivare.sjukvard.v1.Enhet;
import se.fk.vardgivare.sjukvard.v1.InternIdentitetsbeteckning;
import se.fk.vardgivare.sjukvard.v1.Kontaktuppgifter;
import se.fk.vardgivare.sjukvard.v1.Lakarintygsreferens;
import se.fk.vardgivare.sjukvard.v1.Meddelande;
import se.fk.vardgivare.sjukvard.v1.Namn;
import se.fk.vardgivare.sjukvard.v1.Organisation;
import se.fk.vardgivare.sjukvard.v1.Patient;
import se.fk.vardgivare.sjukvard.v1.Person;
import se.fk.vardgivare.sjukvard.v1.Postadress;
import se.fk.vardgivare.sjukvard.v1.Postnummer;
import se.fk.vardgivare.sjukvard.v1.Postort;
import se.fk.vardgivare.sjukvard.v1.ReferensAdressering;
import se.fk.vardgivare.sjukvard.v1.TaEmotFraga;
import se.fk.vardgivare.sjukvard.v1.TaEmotSvar;
import se.fk.vardgivare.sjukvard.v1.Telefon;
import se.fk.vardgivare.sjukvard.v1.Adressering.Avsandare;
import se.fk.vardgivare.sjukvard.v1.Adressering.Mottagare;
import se.skl.riv.insuranceprocess.healthreporting.qa.v1.Amnetyp;
import se.skl.riv.insuranceprocess.healthreporting.qa.v1.LakarutlatandeEnkelType;
import se.skl.riv.insuranceprocess.healthreporting.qa.v1.VardAdresseringsType;
import se.skl.riv.insuranceprocess.healthreporting.sendmedicalcertificatequestionsponder.v1.SendMedicalCertificateQuestionType;
import se.skl.riv.insuranceprocess.healthreporting.v2.EnhetType;
import se.skl.riv.insuranceprocess.healthreporting.v2.HosPersonalType;
import se.skl.riv.insuranceprocess.healthreporting.v2.PatientType;
import se.skl.riv.insuranceprocess.healthreporting.v2.VardgivareType;

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
		ResourceBundle rb = ResourceBundle.getBundle("fkdataSendMCQuestion");	    

		try {			
			// Transform the XML payload into a JAXB object
            Unmarshaller unmarshaller = JAXBContext.newInstance(SendMedicalCertificateQuestionType.class).createUnmarshaller();
            XMLStreamReader streamPayload = (XMLStreamReader)((Object[])message.getPayload())[1];
            SendMedicalCertificateQuestionType inRequest = (SendMedicalCertificateQuestionType)((JAXBElement)unmarshaller.unmarshal(streamPayload)).getValue();
    		
			// Get receiver to adress from Mule property
			String receiverId = (String)message.getProperty("receiverid");			

			// Create new JAXB object for the outgoing data
			TaEmotFragaType outRequest = new TaEmotFragaType();
    		TaEmotFraga outTaEmotFraga = new TaEmotFraga();
    		outRequest.setFKSKLTaEmotFragaAnrop(outTaEmotFraga);

			// Transform between incoming and outgoing objects
    		// Avsändare - Vården
    		VardAdresseringsType inAvsandare = inRequest.getQuestion().getAdressVard();
    		HosPersonalType inHoSPersonalAvsandare = inAvsandare.getHosPersonal();
    		EnhetType inEnhetAvsandare = inHoSPersonalAvsandare.getEnhet();
    		VardgivareType inVardgivareAvsandare = inEnhetAvsandare.getVardgivare();
    		
    		Avsandare outAvsandare = new Avsandare();
    		Adressering outAdressering = new Adressering();
    		outAdressering.setAvsandare(outAvsandare);
    		outTaEmotFraga.setAdressering(outAdressering);
   		
    		ReferensAdressering referensId = new ReferensAdressering();
    		referensId.setValue(inRequest.getQuestion().getVardReferensId());
			outAvsandare.setReferens(referensId);
    		
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
    		outPersonAvsandare.setNamn(inHoSPersonalAvsandare.getFullstandigtNamn());
    		outEnhetAvsandare.setPerson(outPersonAvsandare);
    		
    		// Mottagare - FK    		
    		Mottagare outMottagare = new Mottagare();
    		outAdressering.setMottagare(outMottagare);
    		Organisation outOrganisationMottagare = new Organisation();
    		outMottagare.setOrganisation(outOrganisationMottagare);
    		
    		Namn outOrganisationNamnMottagare = new Namn();
    		outOrganisationNamnMottagare.setValue(rb.getString("FK"));
    		outOrganisationMottagare.setNamn(outOrganisationNamnMottagare);
    		InternIdentitetsbeteckning outOrganisationIdMottagare = new InternIdentitetsbeteckning();
    		outOrganisationIdMottagare.setValue("202100-5521");
    		outOrganisationMottagare.setId(outOrganisationIdMottagare);    		

    		// Patient
    		LakarutlatandeEnkelType inLakarutlatande = inRequest.getQuestion().getLakarutlatande();
    		PatientType inPatient = inLakarutlatande.getPatient();
    		Patient outPatient = new Patient();
    		outPatient.setIdentifierare(inPatient.getPersonId().getExtension());
    		outPatient.setNamn(inPatient.getFullstandigtNamn());
    		outTaEmotFraga.setPatient(outPatient );
    		
    		// Ämne
    		Amnetyp inAmne = inRequest.getQuestion().getAmne();
    		Amne outAmne = new Amne();
    		outAmne.setBeskrivning(transformAmneFromVarden(inAmne, rb));
    		outTaEmotFraga.setAmne(outAmne);
    		    		
    		// Läkarintyg referens
    		Lakarintygsreferens outLakarintyg = new Lakarintygsreferens();
    		outLakarintyg.setReferens(inLakarutlatande.getLakarutlatandeId());
    		outLakarintyg.setSignerades(inLakarutlatande.getSigneringsTidpunkt());
    		outTaEmotFraga.setLakarintyg(outLakarintyg );
    		
			// Fraga
			Meddelande fraga = new Meddelande();
			fraga.setText(inRequest.getQuestion().getFraga().getMeddelandeText());
			fraga.setSignerades(inRequest.getQuestion().getFraga().getSigneringsTidpunkt()); 
			outTaEmotFraga.setFraga(fraga );
									    		
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
		
	private String transformAmneFromVarden(Amnetyp inAmne, ResourceBundle rb) {
		if (inAmne.compareTo(Amnetyp.ARBETSTIDSFORLAGGNING) == 0) {
			return rb.getString("ARBTIDFORL");
		} else if (inAmne.compareTo(Amnetyp.AVSTAMNINGSMOTE) == 0) {
			return  rb.getString("AVSTAMMOTE");
		} else if (inAmne.compareTo(Amnetyp.KOMPLETTERING_AV_LAKARINTYG) == 0) {
			return  "Komplettering";
		} else if (inAmne.compareTo(Amnetyp.KONTAKT) == 0) {
			return  "Kontakt";
		} else if (inAmne.compareTo(Amnetyp.OVRIGT) == 0) {
			return rb.getString("OVRIGT");
		} else if (inAmne.compareTo(Amnetyp.MAKULERING_AV_LAKARINTYG) == 0) {
			return  "Makulering";
		} else {
			return rb.getString("OVRIGT");
		}
	}	
}