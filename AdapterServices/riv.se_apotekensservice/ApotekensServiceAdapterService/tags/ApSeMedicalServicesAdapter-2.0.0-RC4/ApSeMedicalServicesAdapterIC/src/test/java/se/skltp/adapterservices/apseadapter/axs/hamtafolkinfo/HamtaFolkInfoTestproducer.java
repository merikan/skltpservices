package se.skltp.adapterservices.apseadapter.axs.hamtafolkinfo;

import org.w3c.addressing.v1.AttributedURIType;

import riv.se_apotekensservice.axs._1.ApoteksinformationResponse;
import riv.se_apotekensservice.axs._1.KundinformationResponse;
import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.inera.se.apotekensservice.axs.hamtafolkinfo.v1.rivtabp20.ApplicationException;
import se.riv.inera.se.apotekensservice.axs.hamtafolkinfo.v1.rivtabp20.HamtaFolkInfoResponderInterface;
import se.riv.inera.se.apotekensservice.axs.hamtafolkinfo.v1.rivtabp20.SystemException;
import se.riv.se.apotekensservice.axs.hamtafolkinforesponder.v1.HamtaFolkInfoRequestType;
import se.riv.se.apotekensservice.axs.hamtafolkinforesponder.v1.HamtaFolkInfoResponseType;

public class HamtaFolkInfoTestproducer implements HamtaFolkInfoResponderInterface {
	
	public static final String SSN_OK = "121212121212";

	public HamtaFolkInfoResponseType hamtaFolkInfo(
			HamtaFolkInfoRequestType parameters,
			AttributedURIType logicalAddress,
			ArgosHeaderType argosHeader)
			throws SystemException, ApplicationException {
		
		HamtaFolkInfoResponseType respone = new HamtaFolkInfoResponseType();
		respone.setKundinformation(createKundInformation(parameters));
		respone.setApotek(createApoteksInformation(parameters));
		return respone;
	}

	private ApoteksinformationResponse createApoteksInformation(HamtaFolkInfoRequestType parameters) {
		ApoteksinformationResponse response = new ApoteksinformationResponse();
		return response;
	}

	private KundinformationResponse createKundInformation(HamtaFolkInfoRequestType parameters) {
		KundinformationResponse response = new KundinformationResponse();
		response.setStatusKod("OK");
		return response;
	}

}
