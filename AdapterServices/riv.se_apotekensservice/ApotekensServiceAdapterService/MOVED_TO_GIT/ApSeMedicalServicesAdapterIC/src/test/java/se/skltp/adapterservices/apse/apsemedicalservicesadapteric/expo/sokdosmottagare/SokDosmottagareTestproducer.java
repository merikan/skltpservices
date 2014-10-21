package se.skltp.adapterservices.apse.apsemedicalservicesadapteric.expo.sokdosmottagare;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;

import org.w3c.addressing.v1.AttributedURIType;

import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.inera.se.apotekensservice.expo.sokdosmottagare.v1.rivtabp20.SokDosmottagareResponderInterface;
import se.riv.se.apotekensservice.expo.sokdosmottagareresponder.v1.SokDosmottagareRequestType;
import se.riv.se.apotekensservice.expo.sokdosmottagareresponder.v1.SokDosmottagareResponseType;

public class SokDosmottagareTestproducer implements SokDosmottagareResponderInterface {


	@Override
	@WebResult(name = "SokDosmottagareResponse", targetNamespace = "urn:riv:se.apotekensservice:expo:SokDosmottagareResponder:1", partName = "parameters")
	@WebMethod(operationName = "SokDosmottagare", action = "urn:riv:se.apotekensservice:expo:SokDosmottagareResponder:1:SokDosmottagare")
	public SokDosmottagareResponseType sokDosmottagare(
			@WebParam(partName = "parameters", name = "SokDosmottagare", targetNamespace = "urn:riv:se.apotekensservice:expo:SokDosmottagareResponder:1") SokDosmottagareRequestType parameters,
			@WebParam(partName = "LogicalAddress", name = "To", targetNamespace = "http://www.w3.org/2005/08/addressing", header = true) AttributedURIType logicalAddress,
			@WebParam(partName = "ArgosHeader", name = "ArgosHeader", targetNamespace = "urn:riv:inera.se.apotekensservice:argos:1", header = true) ArgosHeaderType argosHeader)
			throws se.riv.inera.se.apotekensservice.expo.sokdosmottagare.v1.rivtabp20.ApplicationException,
			se.riv.inera.se.apotekensservice.expo.sokdosmottagare.v1.rivtabp20.SystemException {
		// TODO Auto-generated method stub
		

		SokDosmottagareResponseType response = new SokDosmottagareResponseType();
		return response;
		
	}

}
