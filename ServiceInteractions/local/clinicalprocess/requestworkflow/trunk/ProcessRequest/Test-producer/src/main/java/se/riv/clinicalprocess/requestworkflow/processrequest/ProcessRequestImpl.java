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
package se.riv.clinicalprocess.requestworkflow.processrequest;

import javax.jws.WebService;

import se.riv.clinicalprocess.requestworkflow.processrequest.v1.ProcessRequestResponderInterface;
import se.riv.clinicalprocess.requestworkflow.processrequest.v1.ProcessRequestResponseType;
import se.riv.clinicalprocess.requestworkflow.processrequest.v1.ProcessRequestType;
import se.riv.clinicalprocess.requestworkflow.v1.ResultCodeEnum;

@WebService(
		serviceName = "ProcessRequestResponderService", 
		endpointInterface="se.riv.clinicalprocess.requestworkflow.processrequest.v1.ProcessRequestResponderInterface", 
		portName = "ProcessRequestResponderPort", 
		targetNamespace = "urn:riv:clinicalprocess:requestworkflow:ProcessRequest:1:rivtabp21",
		wsdlLocation = "schemas/interactions/ProcessRequestInteraction/ProcessRequestInteraction_1.0_RIVTABP21.wsdl")
public class ProcessRequestImpl implements ProcessRequestResponderInterface {

	public ProcessRequestResponseType processRequest(String logicalAddress, ProcessRequestType parameters) {
		ProcessRequestResponseType response = new ProcessRequestResponseType();
		response.setResultCode(ResultCodeEnum.OK);
		return response;
	}
}