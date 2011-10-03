package se.skl.tp.ticket.transformer;

import javax.jws.WebService;

import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.inera.se.apotekensservice.or.hamtaaktuellaordinationer.v1.rivtabp20.HamtaAktuellaOrdinationerResponderInterface;
import se.riv.se.apotekensservice.or.hamtaaktuellaordinationerresponder.v1.HamtaAktuellaOrdinationerRequestType;
import se.riv.se.apotekensservice.or.hamtaaktuellaordinationerresponder.v1.HamtaAktuellaOrdinationerResponseType;

@WebService(	serviceName = "HamtaAktuellaOrdinationerResponderService", 
		endpointInterface = "se.riv.inera.se.apotekensservice.or.hamtaaktuellaordinationer.v1.rivtabp20.HamtaAktuellaOrdinationerResponderInterface", 
		portName = "HamtaAktuellaOrdinationerResponderPort", 
		targetNamespace = "urn:riv:inera:se.apotekensservice:or:HamtaAktuellaOrdinationer:1:rivtabp20", 
		wsdlLocation = "schemas/interactions/HamtaAktuellaOrdinationerInteraction/HamtaAktuellaOrdinationerInteraction_1.0_rivtabp20.wsdl")
public class TicketTransformerTestProducer implements HamtaAktuellaOrdinationerResponderInterface {

    @Override
    public HamtaAktuellaOrdinationerResponseType hamtaAktuellaOrdinationer(
	    HamtaAktuellaOrdinationerRequestType parameters, org.w3c.addressing.v1.AttributedURIType logicalAddress,
	    ArgosHeaderType argosHeader)
	    throws se.riv.inera.se.apotekensservice.or.hamtaaktuellaordinationer.v1.rivtabp20.ApplicationException,
	    se.riv.inera.se.apotekensservice.or.hamtaaktuellaordinationer.v1.rivtabp20.SystemException {

	HamtaAktuellaOrdinationerResponseType response = new HamtaAktuellaOrdinationerResponseType();
	return response;
    }

}
