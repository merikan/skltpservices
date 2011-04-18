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
package se.riv.insuranceprocess.healthreporting.v1;

import javax.jws.WebService;

import org.w3.wsaddressing10.AttributedURIType;

import se.skl.riv.insuranceprocess.healthreporting.findallanswers.v1.rivtabp20.FindAllAnswersResponderInterface;
import se.skl.riv.insuranceprocess.healthreporting.findallanswersresponder.v1.FindAllAnswersResponseType;
import se.skl.riv.insuranceprocess.healthreporting.findallanswersresponder.v1.FindAllAnswersType;

@WebService(
		serviceName = "FindAllAnswersResponderService", 
		endpointInterface="se.skl.riv.insuranceprocess.healthreporting.findallanswers.v1.rivtabp20.FindAllAnswersResponderInterface", 
		portName = "FindAllAnswersResponderPort", 
		targetNamespace = "urn:riv:insuranceprocess:healthreporting:FindAllAnswers:1:rivtabp20",
		wsdlLocation = "schemas/interactions/FindAllAnswersInteraction/FindAllAnswersInteraction_1.0_RIVTABP20.wsdl")
public class ProducerImpl implements FindAllAnswersResponderInterface {

	public FindAllAnswersResponseType findAllAnswers(final AttributedURIType logicalAddress, final FindAllAnswersType parameters) {
		FindAllAnswersResponseType response = new FindAllAnswersResponseType();
		return response;
	}
}
