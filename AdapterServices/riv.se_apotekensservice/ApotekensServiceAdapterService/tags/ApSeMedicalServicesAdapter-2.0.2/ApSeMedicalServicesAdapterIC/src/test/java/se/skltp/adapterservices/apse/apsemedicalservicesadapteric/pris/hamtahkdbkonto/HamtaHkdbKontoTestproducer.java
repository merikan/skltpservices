package se.skltp.adapterservices.apse.apsemedicalservicesadapteric.pris.hamtahkdbkonto;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;

import org.w3c.addressing.v1.AttributedURIType;

import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.inera.se.apotekensservice.pris.hamtahkdbkonto.v1.rivtabp20.ApplicationException;
import se.riv.inera.se.apotekensservice.pris.hamtahkdbkonto.v1.rivtabp20.HamtaHkdbKontoResponderInterface;
import se.riv.inera.se.apotekensservice.pris.hamtahkdbkonto.v1.rivtabp20.SystemException;
import se.riv.se.apotekensservice.pris.hamtahkdbkontoresponder.v1.HamtaHkdbKontoRequestType;
import se.riv.se.apotekensservice.pris.hamtahkdbkontoresponder.v1.HamtaHkdbKontoResponseType;

public class HamtaHkdbKontoTestproducer implements HamtaHkdbKontoResponderInterface {

	@Override
	@WebResult(name = "HamtaHkdbKontoResponse", targetNamespace = "urn:riv:se.apotekensservice:pris:HamtaHkdbKontoResponder:1", partName = "parameters")
	@WebMethod(operationName = "HamtaHkdbKonto", action = "urn:riv:se.apotekensservice:pris:HamtaHkdbKontoResponder:1:HamtaHkdbKonto")
	public HamtaHkdbKontoResponseType hamtaHkdbKonto(
			@WebParam(partName = "parameters", name = "HamtaHkdbKonto", targetNamespace = "urn:riv:se.apotekensservice:pris:HamtaHkdbKontoResponder:1") HamtaHkdbKontoRequestType parameters,
			@WebParam(partName = "LogicalAddress", name = "To", targetNamespace = "http://www.w3.org/2005/08/addressing", header = true) AttributedURIType logicalAddress,
			@WebParam(partName = "ArgosHeader", name = "ArgosHeader", targetNamespace = "urn:riv:inera.se.apotekensservice:argos:1", header = true) ArgosHeaderType argosHeader)
			throws SystemException, ApplicationException {
		// TODO Auto-generated method stub
		
		HamtaHkdbKontoResponseType response = new HamtaHkdbKontoResponseType();
		return response;
	}

}
