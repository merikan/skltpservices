package se.skl.tp.clinicalprocess.requestworkflow.processrequest.mockservice;

import org.mule.module.client.MuleClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.ws.addressing.AttributedURIType;
import riv.clinicalprocess.requestworkflow.request.ERemissTyp;
import se.riv.clinicalprocess.requestworkflow.processrequest.v1.ProcessRequestResponseType;
import se.riv.clinicalprocess.requestworkflow.processrequest.v1.ProcessRequestType;

/**
 * A mockup service for clinicalprocess:requestworkflow
 *
 * @author khaleddaham
 */
@Slf4j
public class MockServiceAnswer extends Thread {
    private static class ProcessRequestAnswerType {
        public ProcessRequestAnswerType() {
        }
    }
    ERemissTyp request;

    public MockServiceAnswer(ERemissTyp request) {
        super();
        this.request = request;
    }

    @Override
    public void run() {
        log.debug("Sending autoanswer to request.");

        MuleClient client;
        try {
            client = new MuleClient();

            AttributedURIType logicalAddressHeader = new AttributedURIType();
            logicalAddressHeader.setValue("2021005521");
            ProcessRequestResponseType response = new ProcessRequestResponseType();

            /*
             * ProcessRequestAnswerType answer = new ProcessRequestAnswerType(); request.setAnswer(getAnswer(request)); // Create payload to webservice
             */
            Object[] payloadOut = new Object[]{logicalAddressHeader, request};

            client.send("autosvarEndpoint", payloadOut, null);
            log.info("Autoanswer sent for careUnit with id: " + request);

        } catch (Exception e) {
            log.error("Autoanswer exception: " + e.getMessage());
        }
    }

    private static ProcessRequestAnswerType getAnswer(ProcessRequestType request) throws Exception {
        /**
         * AnswerToFkType meddelande = new AnswerToFkType(); VardAdresseringsType avsandare = new VardAdresseringsType(); HosPersonalType hosPersonal = new HosPersonalType(); EnhetType enhet = new
         * EnhetType(); II enhetsId = new II(); enhetsId.setRoot("1.2.752.129.2.1.4.1"); enhetsId.setExtension(request.getAdressVard().getHosPersonal().getEnhet().getEnhetsId().getExtension());
         * enhet.setEnhetsId(enhetsId); enhet.setEnhetsnamn(request.getAdressVard().getHosPersonal().getEnhet().getEnhetsnamn()); VardgivareType vardgivare = new VardgivareType();
         * vardgivare.setVardgivarnamn(request.getAdressVard().getHosPersonal().getEnhet().getVardgivare().getVardgivarnamn()); II vardgivareId = new II(); vardgivareId.setRoot("1.2.752.129.2.1.4.1");
         * vardgivareId.setExtension(request.getAdressVard().getHosPersonal().getEnhet().getVardgivare().getVardgivareId().getExtension()); vardgivare.setVardgivareId(vardgivareId);
         * enhet.setVardgivare(vardgivare); hosPersonal.setEnhet(enhet); hosPersonal.setFullstandigtNamn(request.getAdressVard().getHosPersonal().getFullstandigtNamn()); II personalId = new II();
         * personalId.setRoot("1.2.752.129.2.1.4.1"); personalId.setExtension(request.getAdressVard().getHosPersonal().getPersonalId().getExtension()); hosPersonal.setPersonalId(personalId);
         * avsandare.setHosPersonal(hosPersonal); meddelande.setAdressVard(avsandare);
         *
         * meddelande.setAvsantTidpunkt(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
         *
         * meddelande.setVardReferensId(""); LakarutlatandeEnkelType lakarutlatandeEnkel = new LakarutlatandeEnkelType(); PatientType patient = new PatientType(); II personId = new II();
         * personId.setRoot("1.2.752.129.2.1.3.1"); // OID för samordningsnummer är 1.2.752.129.2.1.3.3. personId.setExtension(request.getLakarutlatande().getPatient().getPersonId().getExtension());
         * patient.setPersonId(personId); patient.setFullstandigtNamn(request.getLakarutlatande().getPatient().getFullstandigtNamn()); lakarutlatandeEnkel.setPatient(patient);
         * lakarutlatandeEnkel.setLakarutlatandeId(request.getLakarutlatande().getLakarutlatandeId()); lakarutlatandeEnkel.setSigneringsTidpunkt(request.getLakarutlatande().getSigneringsTidpunkt());
         * meddelande.setLakarutlatande(lakarutlatandeEnkel);
         *
         * // Set Försäkringskassans id meddelande.setFkReferensId(request.getFkReferensId()); meddelande.setVardReferensId("autosvar");
         *
         * // Set ämne meddelande.setAmne(request.getAmne());
         *
         * // Set meddelande InnehallType fraga = new InnehallType(); fraga.setMeddelandeText(request.getFraga().getMeddelandeText());
         * fraga.setSigneringsTidpunkt(request.getFraga().getSigneringsTidpunkt()); meddelande.setFraga(fraga);
         *
         * InnehallType svar = new InnehallType(); StringBuffer autoSvar = new StringBuffer(); autoSvar.append("Detta är en automatisk notifiering från vårdens nationella it-system.");
         * autoSvar.append(System.getProperty("line.separator")); autoSvar.append("Just detta meddelande gick inte att leverera till avsedd mottagare på grund av ett fel och kommer därför inte att
         * levereras."); autoSvar.append(System.getProperty("line.separator")); autoSvar.append("För att inte fördröja handläggningen, så ber vi er i detta fall vara vänlig att kommunicera med avsedd
         * mottagare i vården enligt gällande manuella rutiner, dvs genom telefon eller brev."); svar.setMeddelandeText(autoSvar.toString());
         * svar.setSigneringsTidpunkt(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar())); meddelande.setSvar(svar);
         *
         * return meddelande;
         */
        return new ProcessRequestAnswerType();
    }
}
