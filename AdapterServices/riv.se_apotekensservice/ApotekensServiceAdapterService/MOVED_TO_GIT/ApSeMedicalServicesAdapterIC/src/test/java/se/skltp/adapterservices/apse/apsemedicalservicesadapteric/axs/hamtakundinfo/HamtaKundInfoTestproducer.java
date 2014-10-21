package se.skltp.adapterservices.apse.apsemedicalservicesadapteric.axs.hamtakundinfo;

import org.w3c.addressing.v1.AttributedURIType;

import riv.se_apotekensservice.axs._1.ApoteksinformationResponse;
import riv.se_apotekensservice.axs._1.KundinformationResponse;
import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.inera.se.apotekensservice.axs.hamtakundinfo.v1.rivtabp20.ApplicationException;
import se.riv.inera.se.apotekensservice.axs.hamtakundinfo.v1.rivtabp20.HamtaKundInfoResponderInterface;
import se.riv.inera.se.apotekensservice.axs.hamtakundinfo.v1.rivtabp20.SystemException;
import se.riv.se.apotekensservice.axs.hamtakundinforesponder.v1.HamtaKundInfoRequestType;
import se.riv.se.apotekensservice.axs.hamtakundinforesponder.v1.HamtaKundInfoResponseType;

public class HamtaKundInfoTestproducer implements HamtaKundInfoResponderInterface {
	
	public static final String SSN_OK = "121212121212";

	public HamtaKundInfoResponseType hamtaKundInfo(
			HamtaKundInfoRequestType parameters,
			AttributedURIType logicalAddress,
			ArgosHeaderType argosHeader)
			throws SystemException, ApplicationException {
		
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
