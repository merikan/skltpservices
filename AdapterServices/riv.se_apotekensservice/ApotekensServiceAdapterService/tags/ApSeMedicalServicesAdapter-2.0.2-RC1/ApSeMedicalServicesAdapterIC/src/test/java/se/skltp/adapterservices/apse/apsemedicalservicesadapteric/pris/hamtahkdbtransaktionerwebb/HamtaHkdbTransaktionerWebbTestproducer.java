package se.skltp.adapterservices.apse.apsemedicalservicesadapteric.pris.hamtahkdbtransaktionerwebb;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;

import org.w3c.addressing.v1.AttributedURIType;

import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.inera.se.apotekensservice.pris.hamtahkdbtransaktionerwebb.v1.rivtabp20.HamtaHkdbTransaktionerWebbResponderInterface;
import se.riv.se.apotekensservice.pris.hamtahkdbtransaktionerwebbresponder.v1.HamtaHkdbTransaktionerWebbRequestType;
import se.riv.se.apotekensservice.pris.hamtahkdbtransaktionerwebbresponder.v1.HamtaHkdbTransaktionerWebbResponseType;


public class HamtaHkdbTransaktionerWebbTestproducer implements HamtaHkdbTransaktionerWebbResponderInterface {

	@Override
	@WebResult(name = "HamtaHkdbTransaktionerWebbResponse", targetNamespace = "urn:riv:se.apotekensservice:pris:HamtaHkdbTransaktionerWebbResponder:1", partName = "parameters")
	@WebMethod(operationName = "HamtaHkdbTransaktionerWebb", action = "urn:riv:se.apotekensservice:pris:HamtaHkdbTransaktionerWebbResponder:1:HamtaHkdbTransaktionerWebb")
	public HamtaHkdbTransaktionerWebbResponseType hamtaHkdbTransaktionerWebb(
			@WebParam(partName = "parameters", name = "HamtaHkdbTransaktionerWebb", targetNamespace = "urn:riv:se.apotekensservice:pris:HamtaHkdbTransaktionerWebbResponder:1") HamtaHkdbTransaktionerWebbRequestType parameters,
			@WebParam(partName = "LogicalAddress", name = "To", targetNamespace = "http://www.w3.org/2005/08/addressing", header = true) AttributedURIType logicalAddress,
			@WebParam(partName = "ArgosHeader", name = "ArgosHeader", targetNamespace = "urn:riv:inera.se.apotekensservice:argos:1", header = true) ArgosHeaderType argosHeader)
			throws se.riv.inera.se.apotekensservice.pris.hamtahkdbtransaktionerwebb.v1.rivtabp20.SystemException,
			se.riv.inera.se.apotekensservice.pris.hamtahkdbtransaktionerwebb.v1.rivtabp20.ApplicationException {
		// TODO Auto-generated method stub
		
		HamtaHkdbTransaktionerWebbResponseType response = new HamtaHkdbTransaktionerWebbResponseType();
		return response;
	}


}
