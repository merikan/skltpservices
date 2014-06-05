package se.skltp.adapterservices.apse.apsemedicalservicesadapteric.axs.hamtakundinfo.v4;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;

import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import riv.se_apotekensservice.axs.hamtakundinfo._4.rivtabp21.ApplicationException;
import riv.se_apotekensservice.axs.hamtakundinfo._4.rivtabp21.HamtaKundInfoResponderInterface;
import riv.se_apotekensservice.axs.hamtakundinfo._4.rivtabp21.SystemException;
import se.riv.se.apotekensservice.axs.hamtakundinforesponder.v4.HamtaKundInfoRequestType;
import se.riv.se.apotekensservice.axs.hamtakundinforesponder.v4.HamtaKundInfoResponseType;

import riv.se_apotekensservice.axs._4.ApoteksinformationResponse;
import riv.se_apotekensservice.axs._4.KundinformationResponse;

public class HamtaKundInfoTestproducer implements HamtaKundInfoResponderInterface {
	
	public static final String SSN_OK = "121212121212";

	@Override
	@WebResult(name = "HamtaKundInfoResponse", targetNamespace = "urn:riv:se.apotekensservice:axs:HamtaKundInfoResponder:4", partName = "parameters")
	@WebMethod(operationName = "HamtaKundInfo", action = "urn:riv:se.apotekensservice:axs:HamtaKundInfoResponder:4:HamtaKundInfo")
	public HamtaKundInfoResponseType hamtaKundInfo(
			@WebParam(partName = "parameters", name = "HamtaKundInfo", targetNamespace = "urn:riv:se.apotekensservice:axs:HamtaKundInfoResponder:4") HamtaKundInfoRequestType parameters,
			@WebParam(partName = "LogicalAddress", name = "LogicalAddress", targetNamespace = "urn:riv:itintegration:registry:1", header = true) String logicalAddress,
			@WebParam(partName = "ArgosHeader", name = "ArgosHeader", targetNamespace = "urn:riv:inera.se.apotekensservice:argos:1", header = true) ArgosHeaderType argosHeader)
			throws SystemException, ApplicationException {
		// TODO Auto-generated method stub

		HamtaKundInfoResponseType respone = new HamtaKundInfoResponseType();
		respone.setKundinformation(createKundInformation(parameters));
		respone.setApotek(createApoteksInformation(parameters));
		return respone;
		
	}

	private ApoteksinformationResponse createApoteksInformation(HamtaKundInfoRequestType parameters) {
		ApoteksinformationResponse response = new ApoteksinformationResponse();
		return response;
	}

	private KundinformationResponse createKundInformation(HamtaKundInfoRequestType parameters) {
		KundinformationResponse response = new KundinformationResponse();
		response.setStatusKod("OK");
		return response;
	}

}
