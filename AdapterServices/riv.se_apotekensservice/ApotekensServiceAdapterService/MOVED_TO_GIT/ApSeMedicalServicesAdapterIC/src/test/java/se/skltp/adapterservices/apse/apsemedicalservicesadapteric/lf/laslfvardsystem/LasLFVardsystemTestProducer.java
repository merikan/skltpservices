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
package se.skltp.adapterservices.apse.apsemedicalservicesadapteric.lf.laslfvardsystem;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;

import org.w3c.addressing.v1.AttributedURIType;

import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.inera.se.apotekensservice.lf.laslfvardsystem.v1.rivtabp20.LasLFVardsystemResponderInterface;
import se.riv.se.apotekensservice.lf.laslfvardsystemresponder.v1.LasLFVardsystemRequestType;
import se.riv.se.apotekensservice.lf.laslfvardsystemresponder.v1.LasLFVardsystemResponseType;


public class LasLFVardsystemTestProducer implements LasLFVardsystemResponderInterface {

	@Override
	@WebResult(name = "LasLFVardsystemResponse", targetNamespace = "urn:riv:se.apotekensservice:lf:LasLFVardsystemResponder:1", partName = "parameters")
	@WebMethod(operationName = "LasLFVardsystem", action = "urn:riv:se.apotekensservice:lf:LasLFVardsystemResponder:1:LasLFVardsystem")
	public LasLFVardsystemResponseType lasLFVardsystem(
			@WebParam(partName = "parameters", name = "LasLFVardsystem", targetNamespace = "urn:riv:se.apotekensservice:lf:LasLFVardsystemResponder:1") LasLFVardsystemRequestType parameters,
			@WebParam(partName = "LogicalAddress", name = "To", targetNamespace = "http://www.w3.org/2005/08/addressing", header = true) AttributedURIType logicalAddress,
			@WebParam(partName = "ArgosHeader", name = "ArgosHeader", targetNamespace = "urn:riv:inera.se.apotekensservice:argos:1", header = true) ArgosHeaderType argosHeader)
			throws se.riv.inera.se.apotekensservice.lf.laslfvardsystem.v1.rivtabp20.SystemException,
			se.riv.inera.se.apotekensservice.lf.laslfvardsystem.v1.rivtabp20.ApplicationException {
		// TODO Auto-generated method stub

		LasLFVardsystemResponseType response = new LasLFVardsystemResponseType();
		return response;
		
	}


}
