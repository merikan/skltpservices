package se.skltp.adapterservices.apse.apsemedicalservicesadapteric.axs.uppdaterasamtycke.v4;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;

import se.riv.inera.se.apotekensservice.argos.v4.ArgosHeaderType;
import se.riv.se.apotekensservice.axs.uppdaterasamtyckeresponder.v4.UppdateraSamtyckeRequestType;
import se.riv.se.apotekensservice.axs.uppdaterasamtyckeresponder.v4.UppdateraSamtyckeResponseType;
import riv.se_apotekensservice.axs.uppdaterasamtycke._4.rivtabp21.ApplicationException;
import riv.se_apotekensservice.axs.uppdaterasamtycke._4.rivtabp21.SystemException;
import riv.se_apotekensservice.axs.uppdaterasamtycke._4.rivtabp21.UppdateraSamtyckeResponderInterface;


public class UppdateraSamtyckeTestproducer implements UppdateraSamtyckeResponderInterface {

	@Override
	@WebResult(name = "UppdateraSamtyckeResponse", targetNamespace = "urn:riv:se.apotekensservice:axs:UppdateraSamtyckeResponder:4", partName = "parameters")
	@WebMethod(operationName = "UppdateraSamtycke", action = "urn:riv:se.apotekensservice:axs:UppdateraSamtyckeResponder:4:UppdateraSamtycke")
	public UppdateraSamtyckeResponseType uppdateraSamtycke(
			@WebParam(partName = "parameters", name = "UppdateraSamtycke", targetNamespace = "urn:riv:se.apotekensservice:axs:UppdateraSamtyckeResponder:4") UppdateraSamtyckeRequestType parameters,
			@WebParam(partName = "LogicalAddress", name = "LogicalAddress", targetNamespace = "urn:riv:itintegration:registry:1", header = true) String logicalAddress,
			@WebParam(partName = "ArgosHeader", name = "ArgosHeader", targetNamespace = "urn:riv:inera.se.apotekensservice:argos:1", header = true) ArgosHeaderType argosHeader)
			throws SystemException, ApplicationException {
		// TODO Auto-generated method stub
		
		UppdateraSamtyckeResponseType response = new UppdateraSamtyckeResponseType();
		return response;
	}
	


}
