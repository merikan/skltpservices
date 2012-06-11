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

import se.skl.riv.insuranceprocess.healthreporting.findallquestions.v1.rivtabp20.FindAllQuestionsResponderInterface;
import se.skl.riv.insuranceprocess.healthreporting.findallquestionsresponder.v1.FindAllQuestionsResponseType;
import se.skl.riv.insuranceprocess.healthreporting.findallquestionsresponder.v1.FindAllQuestionsType;

@WebService(
		serviceName = "FindAllQuestionsResponderService", 
		endpointInterface="se.skl.riv.insuranceprocess.healthreporting.findallquestions.v1.rivtabp20.FindAllQuestionsResponderInterface", 
		portName = "FindAllQuestionsResponderPort", 
		targetNamespace = "urn:riv:insuranceprocess:healthreporting:FindAllQuestions:1:rivtabp20",
		wsdlLocation = "schemas/interactions/FindAllQuestionsInteraction/FindAllQuestionsInteraction_1.0_RIVTABP20.wsdl")
public class ProducerImpl implements FindAllQuestionsResponderInterface {

	public FindAllQuestionsResponseType findAllQuestions(final AttributedURIType logicalAddress, final FindAllQuestionsType parameters) {
		FindAllQuestionsResponseType response = new FindAllQuestionsResponseType();
		return response;
	}
}
