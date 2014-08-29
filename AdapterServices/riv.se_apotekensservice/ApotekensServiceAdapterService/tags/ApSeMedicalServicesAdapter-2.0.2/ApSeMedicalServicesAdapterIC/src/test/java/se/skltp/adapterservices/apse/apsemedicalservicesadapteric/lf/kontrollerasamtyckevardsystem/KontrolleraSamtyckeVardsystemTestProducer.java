/**
 * Copyright (c) 2013 Center for eHalsa i samverkan (CeHis).
 * 							<http://cehis.se/>
 *
 * This file is part of SKLTP.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package se.skltp.adapterservices.apse.apsemedicalservicesadapteric.lf.kontrollerasamtyckevardsystem;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;

import org.w3c.addressing.v1.AttributedURIType;

import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.inera.se.apotekensservice.lf.kontrollerasamtyckevardsystem.v1.rivtabp20.KontrolleraSamtyckeVardsystemResponderInterface;
import se.riv.se.apotekensservice.lf.kontrollerasamtyckevardsystemresponder.v1.KontrolleraSamtyckeVardsystemRequestType;
import se.riv.se.apotekensservice.lf.kontrollerasamtyckevardsystemresponder.v1.KontrolleraSamtyckeVardsystemResponseType;


public class KontrolleraSamtyckeVardsystemTestProducer implements KontrolleraSamtyckeVardsystemResponderInterface {

	@Override
	@WebResult(name = "KontrolleraSamtyckeVardsystemResponse", targetNamespace = "urn:riv:se.apotekensservice:lf:KontrolleraSamtyckeVardsystemResponder:1", partName = "parameters")
	@WebMethod(operationName = "KontrolleraSamtyckeVardsystem", action = "urn:riv:se.apotekensservice:lf:KontrolleraSamtyckeVardsystemResponder:1:KontrolleraSamtyckeVardsystem")
	public KontrolleraSamtyckeVardsystemResponseType kontrolleraSamtyckeVardsystem(
			@WebParam(partName = "parameters", name = "KontrolleraSamtyckeVardsystem", targetNamespace = "urn:riv:se.apotekensservice:lf:KontrolleraSamtyckeVardsystemResponder:1") KontrolleraSamtyckeVardsystemRequestType parameters,
			@WebParam(partName = "LogicalAddress", name = "To", targetNamespace = "http://www.w3.org/2005/08/addressing", header = true) AttributedURIType logicalAddress,
			@WebParam(partName = "ArgosHeader", name = "ArgosHeader", targetNamespace = "urn:riv:inera.se.apotekensservice:argos:1", header = true) ArgosHeaderType argosHeader)
			throws se.riv.inera.se.apotekensservice.lf.kontrollerasamtyckevardsystem.v1.rivtabp20.ApplicationException,
			se.riv.inera.se.apotekensservice.lf.kontrollerasamtyckevardsystem.v1.rivtabp20.SystemException {
		// TODO Auto-generated method stub

		KontrolleraSamtyckeVardsystemResponseType response = new KontrolleraSamtyckeVardsystemResponseType();
		return response;
		
	}

	

}
