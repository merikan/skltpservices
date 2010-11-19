package se.skl.tp.taemotsvar.transformers;

import iso.v21090.dt.v1.II;

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

import se.fk.vardgivare.sjukvard.taemotsvarresponder.v1.TaEmotSvarType;
import se.fk.vardgivare.sjukvard.v1.Adressering.Avsandare;
import se.fk.vardgivare.sjukvard.v1.Adressering.Mottagare;
import se.fk.vardgivare.sjukvard.v1.Amne;
import se.fk.vardgivare.sjukvard.v1.Enhet;
import se.fk.vardgivare.sjukvard.v1.Lakarintygsreferens;
import se.fk.vardgivare.sjukvard.v1.Organisation;
import se.fk.vardgivare.sjukvard.v1.Patient;
import se.fk.vardgivare.sjukvard.v1.Person;
import se.skl.riv.insuranceprocess.healthreporting.qa.v1.Amnetyp;
import se.skl.riv.insuranceprocess.healthreporting.qa.v1.CaseType;
import se.skl.riv.insuranceprocess.healthreporting.qa.v1.FkAdresseringsType;
import se.skl.riv.insuranceprocess.healthreporting.qa.v1.LakarutlatandeEnkelType;
import se.skl.riv.insuranceprocess.healthreporting.qa.v1.MeddelandeType;
import se.skl.riv.insuranceprocess.healthreporting.qa.v1.Meddelandetyp;
import se.skl.riv.insuranceprocess.healthreporting.qa.v1.VardAdresseringsType;
import se.skl.riv.insuranceprocess.healthreporting.receivemedicalcertificateanswerresponder.v1.ReceiveMedicalCertificateAnswerType;
import se.skl.riv.insuranceprocess.healthreporting.v1.EnhetType;
import se.skl.riv.insuranceprocess.healthreporting.v1.HosPersonalType;
import se.skl.riv.insuranceprocess.healthreporting.v1.OrganisationType;
import se.skl.riv.insuranceprocess.healthreporting.v1.PatientType;
import se.skl.riv.insuranceprocess.healthreporting.v1.VardgivareType;

public class FkRequest2VardTransformer extends AbstractMessageAwareTransformer
{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public FkRequest2VardTransformer()
    {
        super();
        registerSourceType(Object.class);
        setReturnClass(Object.class);
    }
    
	public Object transform(MuleMessage message, String outputEncoding) throws TransformerException {
		try {			
			// Transform the XML payload into a JAXB object
            Unmarshaller unmarshaller = JAXBContext.newInstance(TaEmotSvarType.class).createUnmarshaller();
            XMLStreamReader streamPayload = (XMLStreamReader)((Object[])message.getPayload())[1];
            TaEmotSvarType inRequest = (TaEmotSvarType)((JAXBElement)unmarshaller.unmarshal(streamPayload)).getValue();
		
			// Get receiver to adress from Mule property
			String receiverId = (String)message.getProperty("receiverid");			

			// Create new JAXB object for the outgoing data
			ReceiveMedicalCertificateAnswerType outRequest = new ReceiveMedicalCertificateAnswerType();
    		MeddelandeType outMeddelande = new MeddelandeType();
    		outRequest.setAnswer(outMeddelande);

			// Transform between incoming and outgoing objects
    		// Avsändare - FK
    		Avsandare inAvsandare = inRequest.getFKSKLTaEmotSvarAnrop().getAdressering().getAvsandare();
    		Organisation inOrganisationAvsandare = inAvsandare.getOrganisation();
    		    		
    		FkAdresseringsType outAvsandare = new FkAdresseringsType();
    		outMeddelande.setAdressFK(outAvsandare);    		
    		OrganisationType outOrganisationAvsandare = new OrganisationType();
    		outAvsandare.setOrganisation(outOrganisationAvsandare);

    		outOrganisationAvsandare.setOrganisationsId(inOrganisationAvsandare.getOrganisationsnummer().getValue());
    		outOrganisationAvsandare.setOrganisationsnamn(inOrganisationAvsandare.getNamn().getValue());
         	
    		// Mottagare - Vården
    		Mottagare inMottagare = inRequest.getFKSKLTaEmotSvarAnrop().getAdressering().getMottagare();
    		Organisation inOrganisationMottagare = inMottagare.getOrganisation();
    		Enhet inEnhetMottagare = inOrganisationMottagare.getEnhet();
    		Person inPersonMottagare = inEnhetMottagare.getPerson();
    		    		
    		VardAdresseringsType outMottagare = new VardAdresseringsType();
    		outMeddelande.setAdressVard(outMottagare);    		
    		HosPersonalType outHosPersonalMottagare = new HosPersonalType();
    		outMottagare.setHosPersonal(outHosPersonalMottagare);

    		if (inPersonMottagare.getNamn() != null && inPersonMottagare.getNamn().length() > 0) {
        		outHosPersonalMottagare.setFullstandigtNamn(inPersonMottagare.getNamn());    			
    		} else {
        		outHosPersonalMottagare.setFullstandigtNamn(inPersonMottagare.getFornamn() + " " + inPersonMottagare.getEfternamn() );
    		}
    		II outPersonalIdMottagare = new II();
    		outPersonalIdMottagare.setRoot("1.2.752.129.2.1.4.1");
    		outPersonalIdMottagare.setExtension(inPersonMottagare.getId().getValue());
    		outHosPersonalMottagare.setPersonalId(outPersonalIdMottagare);

    		EnhetType outEnhetMottagare = new EnhetType();	
    		II outEnhetsIdMottagare = new II();
    		outEnhetsIdMottagare.setRoot("1.2.752.129.2.1.4.1");
    		outEnhetsIdMottagare.setExtension(inEnhetMottagare.getId().getValue());
    		outEnhetMottagare.setEnhetsId(outEnhetsIdMottagare);
//    		outEnhetMottagare.setTelefonnummer(inEnhetMottagare.getKontaktuppgifter().getTelefon().getValue());
//    		outEnhetMottagare.setPostadress(inEnhetMottagare.getKontaktuppgifter().getAdress().getPostadress().getValue());
//    		outEnhetMottagare.setPostnummer(inEnhetMottagare.getKontaktuppgifter().getAdress().getPostnummer().getValue());
//    		outEnhetMottagare.setPostort(inEnhetMottagare.getKontaktuppgifter().getAdress().getPostort().getValue());
    		outEnhetMottagare.setEnhetsnamn(inEnhetMottagare.getNamn().getValue());
    		outHosPersonalMottagare.setEnhet(outEnhetMottagare);
    		
    		VardgivareType outVardgivareMottagare = new VardgivareType();
    		outVardgivareMottagare.setVardgivarnamn(inOrganisationMottagare.getNamn().getValue());
    		II outVardgivareIdMottagare = new II();
    		outVardgivareIdMottagare.setRoot("1.2.752.129.2.1.4.1");
    		outVardgivareIdMottagare.setExtension(inOrganisationMottagare.getId().getValue());
    		outVardgivareMottagare.setVardgivareId(outVardgivareIdMottagare);
    		outEnhetMottagare.setVardgivare(outVardgivareMottagare);
    		 		
    		// Avsänt tidpunkt
    		XMLGregorianCalendar inSkickades = inRequest.getFKSKLTaEmotSvarAnrop().getAdressering().getSkickades();
    		outMeddelande.setAvsantTidpunkt(inSkickades);
    		
    		// Set läkarutlåtande enkel från vården
    		Lakarintygsreferens inLakarutlatande = inRequest.getFKSKLTaEmotSvarAnrop().getLakarintyg();
    		Patient inPatient = inRequest.getFKSKLTaEmotSvarAnrop().getPatient();
    		
    		LakarutlatandeEnkelType outLakarutlatandeEnkel = new LakarutlatandeEnkelType();
    		PatientType outPatient = new PatientType();
    		II outPersonId = new II();
    		outPersonId.setRoot("1.2.752.129.2.1.3.1"); // OID för samordningsnummer är 1.2.752.129.2.1.3.3.
    		outPersonId.setExtension(inPatient.getIdentifierare());
    		outPatient.setPersonId(outPersonId);
    		// Hur göra med fullständigt namn?
    		outPatient.setFornamn(inPatient.getFornamn()); 
    		outPatient.setEfternamn(inPatient.getEfternamn());
    		outLakarutlatandeEnkel.setPatient(outPatient);
    		outLakarutlatandeEnkel.setLakarutlatandeId(inLakarutlatande.getReferens());
    		outLakarutlatandeEnkel.setSigneringsTidpunkt(inLakarutlatande.getSignerades());
    		outMeddelande.setLakarutlatande(outLakarutlatandeEnkel);
    	
    		// Set Försäkringskassans id
    		outMeddelande.setForsakringskassansArendeId(inAvsandare.getReferens().getValue());
    		
    		// Set meddelandetyp
    		outMeddelande.setMeddelandetyp(Meddelandetyp.FRAGA_FRAN_FK);

    		// Set ämne
    		Amne inAmne = inRequest.getFKSKLTaEmotSvarAnrop().getAmne();
    		outMeddelande.setAmne(transformAmneFromFK(inAmne));
    		
    		// Set meddelande rubrik och text
    		if (inAmne.getFritext() != null && inAmne.getFritext().length() > 0) {
    			outMeddelande.setMeddelanderubrik(inAmne.getFritext());
    		}
    		CaseType fraga = new CaseType();
    		fraga.setMeddelandeText(inRequest.getFKSKLTaEmotSvarAnrop().getFraga().getText());
    		fraga.setSigneringsTidpunkt(inRequest.getFKSKLTaEmotSvarAnrop().getFraga().getSignerades());	
    		outMeddelande.setFraga(fraga);
    		CaseType svar = new CaseType();
    		svar.setMeddelandeText(inRequest.getFKSKLTaEmotSvarAnrop().getSvar().getText());
    		svar.setSigneringsTidpunkt(inRequest.getFKSKLTaEmotSvarAnrop().getSvar().getSignerades());	
    		outMeddelande.setSvar(svar);
    		
    		// Sista datum för komplettering
//    		outMeddelande.setSistaDatumForKomplettering(inRequest.getFKSKLTaEmotSvarAnrop().getBesvaras());	
    		
    		// Meddelande id???
    		outMeddelande.setMeddelandeId(inMottagare.getReferens().getValue());
    		
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