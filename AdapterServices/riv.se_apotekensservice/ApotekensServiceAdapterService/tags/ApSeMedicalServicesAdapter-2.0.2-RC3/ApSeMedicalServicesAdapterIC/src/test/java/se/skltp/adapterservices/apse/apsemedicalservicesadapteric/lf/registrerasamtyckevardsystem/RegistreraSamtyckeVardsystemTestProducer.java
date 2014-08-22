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
package se.skltp.adapterservices.apse.apsemedicalservicesadapteric.lf.registrerasamtyckevardsystem;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;

import org.w3c.addressing.v1.AttributedURIType;

import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.inera.se.apotekensservice.lf.registrerasamtyckevardsystem.v1.rivtabp20.RegistreraSamtyckeVardsystemResponderInterface;
import se.riv.se.apotekensservice.lf.registrerasamtyckevardsystemresponder.v1.RegistreraSamtyckeVardsystemRequestType;
import se.riv.se.apotekensservice.lf.registrerasamtyckevardsystemresponder.v1.RegistreraSamtyckeVardsystemResponseType;


public class RegistreraSamtyckeVardsystemTestProducer implements RegistreraSamtyckeVardsystemResponderInterface {

	@Override
	@WebResult(name = "RegistreraSamtyckeVardsystemResponse", targetNamespace = "urn:riv:se.apotekensservice:lf:RegistreraSamtyckeVardsystemResponder:1", partName = "parameters")
	@WebMethod(operationName = "RegistreraSamtyckeVardsystem", action = "urn:riv:se.apotekensservice:lf:RegistreraSamtyckeVardsystemResponder:1:RegistreraSamtyckeVardsystem")
	public RegistreraSamtyckeVardsystemResponseType registreraSamtyckeVardsystem(
			@WebParam(partName = "parameters", name = "RegistreraSamtyckeVardsystem", targetNamespace = "urn:riv:se.apotekensservice:lf:RegistreraSamtyckeVardsystemResponder:1") RegistreraSamtyckeVardsystemRequestType parameters,
			@WebParam(partName = "LogicalAddress", name = "To", targetNamespace = "http://www.w3.org/2005/08/addressing", header = true) AttributedURIType logicalAddress,
			@WebParam(partName = "ArgosHeader", name = "ArgosHeader", targetNamespace = "urn:riv:inera.se.apotekensservice:argos:1", header = true) ArgosHeaderType argosHeader)
			throws se.riv.inera.se.apotekensservice.lf.registrerasamtyckevardsystem.v1.rivtabp20.SystemException,
			se.riv.inera.se.apotekensservice.lf.registrerasamtyckevardsystem.v1.rivtabp20.ApplicationException {
		// TODO Auto-generated method stub

		RegistreraSamtyckeVardsystemResponseType response = new RegistreraSamtyckeVardsystemResponseType();
		return response;
	}



}
