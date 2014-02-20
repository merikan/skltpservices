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
package se.riv.insuranceprocess.healthreporting.deleteanswers.v1;

import javax.jws.WebService;

import org.w3.wsaddressing10.AttributedURIType;

import se.skl.riv.insuranceprocess.healthreporting.deleteanswers.v1.rivtabp20.DeleteAnswersResponderInterface;
import se.skl.riv.insuranceprocess.healthreporting.deleteanswersresponder.v1.DeleteAnswersResponseType;
import se.skl.riv.insuranceprocess.healthreporting.deleteanswersresponder.v1.DeleteAnswersType;

@WebService(
		serviceName = "DeleteAnswersResponderService", 
		endpointInterface="se.skl.riv.insuranceprocess.healthreporting.deleteanswers.v1.rivtabp20.DeleteAnswersResponderInterface", 
		portName = "DeleteAnswersResponderPort", 
		targetNamespace = "urn:riv:insuranceprocess:healthreporting:DeleteAnswers:1:rivtabp20",
		wsdlLocation = "schemas/interactions/DeleteAnswersInteraction/DeleteAnswersInteraction_1.0_RIVTABP20.wsdl")
public class ProducerImpl implements DeleteAnswersResponderInterface {

	public DeleteAnswersResponseType deleteAnswers(final AttributedURIType logicalAddress, final DeleteAnswersType parameters) {
		DeleteAnswersResponseType response = new DeleteAnswersResponseType();
		return response;
	}
}
