package se.skl.tp.clinicalprocess.requestworkflow.processrequest.mockservice;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;

import org.mule.module.client.MuleClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.ws.addressing.AttributedURIType;
import se.riv.clinicalprocess.requestworkflow.processrequest.v1.ProcessRequestResponseType;
import se.riv.clinicalprocess.requestworkflow.processrequest.v1.ProcessRequestType;

/**
 * A mockup service for clinicalprocess:requestworkflow
 * @author khaleddaham
 */
@Slf4j
public class MockServiceAnswer extends Thread {
	ProcessRequestType request;
	
	public MockServiceAnswer(ProcessRequestType request) {
		super();
		this.request = request;		
	}

	@Override
	public void run() {
		log.debug("Sending autoanswer to question.");
				
		MuleClient client;
		try {
			client = new MuleClient();
			
			AttributedURIType logicalAddressHeader = new AttributedURIType();
			logicalAddressHeader.setValue("2021005521");
            ProcessRequestResponseType response = new ProcessRequestResponseType();

            /*
            ProcessRequestAnswerType answer = new ProcessRequestAnswerType();
			request.setAnswer(getAnswer(question));
			// Create payload to webservice
            */
			Object[] payloadOut = new Object[] {logicalAddressHeader, request};

			client.send("autosvarEndpoint", payloadOut, null);
			log.info("Autoanswer sent for careUnit with id: " + request.getAnswer().getAdressVard().getHosPersonal().getEnhet().getEnhetsId().getExtension());
			
		} catch (Exception e) {
			log.error("Autoanswer exception: " + e.getMessage());
		}		
	}
	
	private static AnswerToFkType getAnswer(QuestionFromFkType question) throws Exception {
		AnswerToFkType meddelande = new AnswerToFkType();

		// Avsändare
		VardAdresseringsType avsandare = new VardAdresseringsType();		
		HosPersonalType hosPersonal = new HosPersonalType();
		EnhetType enhet = new EnhetType();	
		II enhetsId = new II();
		enhetsId.setRoot("1.2.752.129.2.1.4.1");
		enhetsId.setExtension(question.getAdressVard().getHosPersonal().getEnhet().getEnhetsId().getExtension());
		enhet.setEnhetsId(enhetsId);
		enhet.setEnhetsnamn(question.getAdressVard().getHosPersonal().getEnhet().getEnhetsnamn());
		VardgivareType vardgivare = new VardgivareType();
		vardgivare.setVardgivarnamn(question.getAdressVard().getHosPersonal().getEnhet().getVardgivare().getVardgivarnamn());
		II vardgivareId = new II();
		vardgivareId.setRoot("1.2.752.129.2.1.4.1");
		vardgivareId.setExtension(question.getAdressVard().getHosPersonal().getEnhet().getVardgivare().getVardgivareId().getExtension());
		vardgivare.setVardgivareId(vardgivareId);
		enhet.setVardgivare(vardgivare);
		hosPersonal.setEnhet(enhet);
		hosPersonal.setFullstandigtNamn(question.getAdressVard().getHosPersonal().getFullstandigtNamn());
		II personalId = new II();
		personalId.setRoot("1.2.752.129.2.1.4.1");
		personalId.setExtension(question.getAdressVard().getHosPersonal().getPersonalId().getExtension());
		hosPersonal.setPersonalId(personalId);
		avsandare.setHosPersonal(hosPersonal);
		meddelande.setAdressVard(avsandare);
				
		// Avsänt tidpunkt - nu
		meddelande.setAvsantTidpunkt(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));

		// Set läkarutlåtande enkel från vården
		meddelande.setVardReferensId("");
		LakarutlatandeEnkelType lakarutlatandeEnkel = new LakarutlatandeEnkelType();
		PatientType patient = new PatientType();
		II personId = new II();
		personId.setRoot("1.2.752.129.2.1.3.1"); // OID för samordningsnummer är 1.2.752.129.2.1.3.3.
		personId.setExtension(question.getLakarutlatande().getPatient().getPersonId().getExtension());
		patient.setPersonId(personId);
		patient.setFullstandigtNamn(question.getLakarutlatande().getPatient().getFullstandigtNamn()); 
		lakarutlatandeEnkel.setPatient(patient);
		lakarutlatandeEnkel.setLakarutlatandeId(question.getLakarutlatande().getLakarutlatandeId());
		lakarutlatandeEnkel.setSigneringsTidpunkt(question.getLakarutlatande().getSigneringsTidpunkt());
		meddelande.setLakarutlatande(lakarutlatandeEnkel);

		// Set Försäkringskassans id
		meddelande.setFkReferensId(question.getFkReferensId());
		meddelande.setVardReferensId("autosvar");

		// Set ämne
		meddelande.setAmne(question.getAmne());

		// Set meddelande	
		InnehallType fraga = new InnehallType();
		fraga.setMeddelandeText(question.getFraga().getMeddelandeText());
		fraga.setSigneringsTidpunkt(question.getFraga().getSigneringsTidpunkt());
		meddelande.setFraga(fraga);

		InnehallType svar = new InnehallType();
		StringBuffer autoSvar = new StringBuffer();
		autoSvar.append("Detta är en automatisk notifiering från vårdens nationella it-system.");
		autoSvar.append(System.getProperty("line.separator"));
		autoSvar.append("Just detta meddelande gick inte att leverera till avsedd mottagare på grund av ett fel och kommer därför inte att levereras.");
		autoSvar.append(System.getProperty("line.separator"));
		autoSvar.append("För att inte fördröja handläggningen, så ber vi er i detta fall vara vänlig att kommunicera med avsedd mottagare i vården enligt gällande manuella rutiner, dvs genom telefon eller brev.");
		svar.setMeddelandeText(autoSvar.toString());
		svar.setSigneringsTidpunkt(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		meddelande.setSvar(svar);

		return meddelande;
	}	
}




