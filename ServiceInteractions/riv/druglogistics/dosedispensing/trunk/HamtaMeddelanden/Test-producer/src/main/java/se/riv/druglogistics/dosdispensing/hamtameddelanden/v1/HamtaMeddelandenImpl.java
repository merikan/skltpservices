/**
 * Copyright 2009 Sjukvardsradgivningen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public

 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the

 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,

 *   Boston, MA 02111-1307  USA
 */
package se.riv.druglogistics.dosdispensing.hamtameddelanden.v1;

import java.util.GregorianCalendar;
import java.util.UUID;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.w3c.addressing_1_0.AttributedURIType;

import se.riv.druglogistics.dosedispensing_1.HamtaMeddelandenResponderInterface;
import se.riv.druglogistics.dosedispensing_1.HamtaMeddelandenResponseType;
import se.riv.druglogistics.dosedispensing_1.HamtaMeddelandenType;
import se.riv.druglogistics.dosedispensing_1.HamtaMeddelandeninfoResponse;
import se.riv.druglogistics.dosedispensing_1.IdentitetstypEnum;
import se.riv.druglogistics.dosedispensing_1.MeddelandePrioritetEnum;
import se.riv.druglogistics.dosedispensing_1.MeddelandeStatusEnum;
import se.riv.druglogistics.dosedispensing_1.PatientinfoResponse;
import se.riv.druglogistics.dosedispensing_1.YrkesKodEnum;

@WebService(serviceName = "HamtaMeddelandenResponderService", endpointInterface = "se.riv.druglogistics.dosedispensing_1.HamtaMeddelandenResponderInterface", portName = "HamtaMeddelandenResponderPort", targetNamespace = "urn:riv:druglogistics:dosedispensing:HamtaMeddelanden:1:rivtabp20", wsdlLocation = "interactions/HamtaMeddelandenInteraction/HamtaMeddelandenInteraction_1.0_RIVTABP20.wsdl")
public class HamtaMeddelandenImpl implements HamtaMeddelandenResponderInterface {

	private static final String ONE_RESPONSE = "1111111111";
	private static final String LARGE_RESPONSE = "9999999999";
	private static final String ERROR_RESPONSE = "2222222222";
	private static final String TIMEOUT_RESPONSE = "3333333333";
	private static final String TIMEOUT_RESPONSE_ONCE = "4444444444";
	private static int TIMEOUT_MILLIS = 40000;
	private static int TIMEOUT_MILLIS_ONCE = 3000;
	
	private static boolean sleep = true;

	@Override
	@WebResult(name = "HamtaMeddelandenResponse", targetNamespace = "urn:riv:druglogistics:dosedispensing:HamtaMeddelandenResponder:1", partName = "parameters")
	@WebMethod(operationName = "HamtaMeddelanden", action = "urn:riv:druglogistics:dosedispensing:HamtaMeddelandenResponder:1:HamtaMeddelanden")
	public HamtaMeddelandenResponseType hamtaMeddelanden(
			@WebParam(partName = "LogicalAddress", name = "To", targetNamespace = "http://www.w3.org/2005/08/addressing", header = true) AttributedURIType arg0,
			@WebParam(partName = "parameters", name = "HamtaMeddelanden", targetNamespace = "urn:riv:druglogistics:dosedispensing:HamtaMeddelandenResponder:1") HamtaMeddelandenType request) {

		HamtaMeddelandenResponseType responseType = new HamtaMeddelandenResponseType();
		responseType.setMeddelandeid((int) Math.random());
		responseType.setMeddelandetext("Meddelande text");
		responseType.setResultatkod("Resultatkod");

		String glnKod = request.getGlnkod();

		if (LARGE_RESPONSE.equals(glnKod)) {
			createLargeResponse(responseType);
		} else if (ONE_RESPONSE.equals(glnKod)) {
			responseType.getMeddelanden().add(createMeddelandeResponse());
		} else if (ERROR_RESPONSE.equals(glnKod)) {
			throw new RuntimeException("Error occured when trying to retrive information from using glnkod: " + glnKod);
		} else if (TIMEOUT_RESPONSE.equals(glnKod)) {
			timeOutResponse(TIMEOUT_MILLIS, responseType);
		} else if (TIMEOUT_RESPONSE_ONCE.equals(glnKod)) {
		
			System.err.println("Sleep: " + sleep);
			
			if (sleep) {
				System.err.println("ZZZZZZZZZZZZZZZ");
				timeOutResponse(TIMEOUT_MILLIS_ONCE, responseType);
			} else {
				responseType.getMeddelanden().add(createMeddelandeResponse());
			}
			sleep = !sleep;
			
		}

		return responseType;
	}

	private void timeOutResponse(Integer timeout, HamtaMeddelandenResponseType responseType) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
		}
		responseType.getMeddelanden().add(createMeddelandeResponse());
	}

	private void createLargeResponse(HamtaMeddelandenResponseType responseType) {
		for (int i = 0; i < 5000; i++) {
			responseType.getMeddelanden().add(createMeddelandeResponse());
		}
	}

	private HamtaMeddelandeninfoResponse createMeddelandeResponse() {
		HamtaMeddelandeninfoResponse response = new HamtaMeddelandeninfoResponse();
		response.setDosapoteknamn("DOS-APOTEKETES NAMN");
		response.setGlnkod("GLN-KOD");
		response.setKommunikationsriktning(0);
		response.setMeddelande("Meddelande text");
		response.setMeddelandeid(UUID.randomUUID().toString());
		response.setMeddelandestatus(MeddelandeStatusEnum.O);
		response.setMeddelandestatusarbetsplats("Meddelandestatus arbetsplats");
		response.setMeddelandestatusefternamn("Meddelandestatus efternamn");
		response.setMeddelandestatusfornamn("Meddelandestatus fornamn");
		response.setOrdinationsid("Ordinationsid");
		response.setPatientinformation(createPatientResponse());
		response.setPrioritet(MeddelandePrioritetEnum.H);
		response.setRubrik("Rubrik");
		response.setSandarearbetsplats("Sandare arbetsplats");
		response.setSandareefternamn("Sandare efternamn");
		response.setSandarefornamn("Sandare fornamn");
		response.setSandarehsaid("Sandare hsaid");
		response.setSandareyrkeskod(YrkesKodEnum.LK);
		response.setSandningstidpunkt(createNow());
		response.setStatustidpunkt(createNow());
		return response;
	}

	private XMLGregorianCalendar createNow() {
		try {
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
			XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
			return now;
		} catch (Exception e) {
			return null;
		}
	}

	private PatientinfoResponse createPatientResponse() {
		PatientinfoResponse response = new PatientinfoResponse();
		response.setEfternamn("Tolvansson");
		response.setFornamn("Tolvan");
		response.setIdentitetstyp(IdentitetstypEnum.P);
		response.setKommunkod("Kommunkod");
		response.setLanskod("Landskod");
		response.setMellannamn("Tolv");
		response.setPersonid("191212121212");
		return response;
	}

}
