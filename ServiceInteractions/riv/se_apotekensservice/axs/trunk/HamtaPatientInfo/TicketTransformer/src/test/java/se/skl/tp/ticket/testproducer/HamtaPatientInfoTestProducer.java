package se.skl.tp.ticket.testproducer;

import javax.jws.WebService;

import org.w3c.addressing.v1.AttributedURIType;

import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.inera.se.apotekensservice.axs.hamtapatientinfo.v1.rivtabp20.ApplicationException;
import se.riv.inera.se.apotekensservice.axs.hamtapatientinfo.v1.rivtabp20.HamtaPatientInfoResponderInterface;
import se.riv.inera.se.apotekensservice.axs.hamtapatientinfo.v1.rivtabp20.SystemException;
import se.riv.se.apotekensservice.axs.hamtapatientinforesponder.v1.HamtaPatientInfoRequestType;
import se.riv.se.apotekensservice.axs.hamtapatientinforesponder.v1.HamtaPatientInfoResponseType;

@WebService(serviceName = "HamtaPatientInfoResponderService", endpointInterface = "se.riv.inera.se.apotekensservice.axs.hamtapatientinfo.v1.rivtabp20.HamtaPatientInfoResponderInterface", portName = "HamtaPatientInfoResponderPort", targetNamespace = "urn:riv:inera:se.apotekensservice:axs:HamtaPatientInfo:1:rivtabp20", wsdlLocation = "schemas/interactions/HamtaPatientInfoInteraction/HamtaPatientInfoInteraction_1.0_rivtabp20.wsdl")
public class HamtaPatientInfoTestProducer implements HamtaPatientInfoResponderInterface {

    @Override
    public HamtaPatientInfoResponseType hamtaPatientInfo(HamtaPatientInfoRequestType arg0, AttributedURIType arg1,
	    ArgosHeaderType arg2) throws ApplicationException, SystemException {
	HamtaPatientInfoResponseType response = new HamtaPatientInfoResponseType();
	response.setFinnsOrdination(true);
	return response;
    }

}
