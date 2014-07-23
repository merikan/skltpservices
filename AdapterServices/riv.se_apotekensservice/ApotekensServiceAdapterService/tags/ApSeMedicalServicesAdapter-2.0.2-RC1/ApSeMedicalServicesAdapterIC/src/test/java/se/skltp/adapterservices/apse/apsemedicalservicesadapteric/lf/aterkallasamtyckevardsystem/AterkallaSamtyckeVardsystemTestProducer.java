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
package se.skltp.adapterservices.apse.apsemedicalservicesadapteric.lf.aterkallasamtyckevardsystem;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;

import org.w3c.addressing.v1.AttributedURIType;

import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.inera.se.apotekensservice.lf.aterkallasamtyckevardsystem.v1.rivtabp20.AterkallaSamtyckeVardsystemResponderInterface;
import se.riv.se.apotekensservice.lf.aterkallasamtyckevardsystemresponder.v1.AterkallaSamtyckeVardsystemRequestType;
import se.riv.se.apotekensservice.lf.aterkallasamtyckevardsystemresponder.v1.AterkallaSamtyckeVardsystemResponseType;


public class AterkallaSamtyckeVardsystemTestProducer implements AterkallaSamtyckeVardsystemResponderInterface {

	@Override
	@WebResult(name = "AterkallaSamtyckeVardsystemResponse", targetNamespace = "urn:riv:se.apotekensservice:lf:AterkallaSamtyckeVardsystemResponder:1", partName = "parameters")
	@WebMethod(operationName = "AterkallaSamtyckeVardsystem", action = "urn:riv:se.apotekensservice:lf:AterkallaSamtyckeVardsystemResponder:1:AterkallaSamtyckeVardsystem")
	public AterkallaSamtyckeVardsystemResponseType aterkallaSamtyckeVardsystem(
			@WebParam(partName = "parameters", name = "AterkallaSamtyckeVardsystem", targetNamespace = "urn:riv:se.apotekensservice:lf:AterkallaSamtyckeVardsystemResponder:1") AterkallaSamtyckeVardsystemRequestType parameters,
			@WebParam(partName = "LogicalAddress", name = "To", targetNamespace = "http://www.w3.org/2005/08/addressing", header = true) AttributedURIType logicalAddress,
			@WebParam(partName = "ArgosHeader", name = "ArgosHeader", targetNamespace = "urn:riv:inera.se.apotekensservice:argos:1", header = true) ArgosHeaderType argosHeader)
			throws se.riv.inera.se.apotekensservice.lf.aterkallasamtyckevardsystem.v1.rivtabp20.SystemException,
			se.riv.inera.se.apotekensservice.lf.aterkallasamtyckevardsystem.v1.rivtabp20.ApplicationException {
		// TODO Auto-generated method stub
		
		AterkallaSamtyckeVardsystemResponseType response = new AterkallaSamtyckeVardsystemResponseType();
		return response;
		
	}
	


}
