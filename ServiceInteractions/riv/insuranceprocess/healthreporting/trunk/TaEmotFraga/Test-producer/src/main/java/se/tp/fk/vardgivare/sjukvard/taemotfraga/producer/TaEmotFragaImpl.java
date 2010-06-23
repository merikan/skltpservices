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
package se.tp.fk.vardgivare.sjukvard.taemotfraga.producer;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.w3.wsaddressing10.AttributedURIType;

import se.fk.vardgivare.sjukvard.taemotfraga.v1.rivtabp20.TaEmotFragaResponderInterface;
import se.fk.vardgivare.sjukvard.taemotfragaresponder.v1.TaEmotFragaResponseType;
import se.fk.vardgivare.sjukvard.taemotfragaresponder.v1.TaEmotFragaType;

@WebService(
		serviceName = "TaEmotFragaResponderService", 
		endpointInterface="se.fk.vardgivare.sjukvard.taemotfraga.v1.rivtabp20.TaEmotFragaResponderInterface", 
		portName = "TaEmotFragaResponderPort", 
		targetNamespace = "urn:riv:fk:vardgivare:sjukvard:TaEmotFraga:1:rivtabp20",
		wsdlLocation = "schemas/TaEmotFragaInteraction_0.9_rivtabp20.wsdl")
public class TaEmotFragaImpl implements TaEmotFragaResponderInterface {

	static Map<String, List<String>> questionMap = new HashMap<String, List<String>>(); 
	
	public TaEmotFragaResponseType taEmotFraga(
			AttributedURIType logicalAddress, TaEmotFragaType parameters) {
		try {
			TaEmotFragaResponseType response = new TaEmotFragaResponseType();
			
			// Transform payload to xml string
            StringWriter writer = new StringWriter();
        	Marshaller marshaller = JAXBContext.newInstance(TaEmotFragaType.class).createMarshaller();
        	marshaller.marshal(new JAXBElement(new QName("urn:riv:fk:vardgivare:sjukvard:TaEmotFragaResponder:1", "TaEmotFraga"), TaEmotFragaType.class, parameters), writer);
			String payload = (String)writer.toString();
			System.out.println(payload);
//			if (payload.startsWith("<?")) {
//				int pos = payload.indexOf("?>");
//				payload = payload.substring(pos + 2);
//			}

			// Store question in an map with an array with vårdenhet HSA-id as the key
			String vardenhetHsaId = parameters.getFKSKLTaEmotFragaAnrop().getAdressering().getMottagare().getOrganisation().getEnhet().getId().getValue();
			
			// Create an entry for this hsaid
			if (!questionMap.containsKey(vardenhetHsaId)) {
				List<String> questions = new ArrayList<String>();
				questionMap.put(vardenhetHsaId, questions);
			}
				
			// Add question for this key
			List<String> questions = questionMap.get(vardenhetHsaId);
			questions.add(payload);
			
			// Print out all questions for this id
			for(int i = 0; i < questions.size(); i++) {
				System.out.println("Index: " +  i + ". Value: " + questions.get(i));
			}
			
			System.out.println("response sent!");

			return response;
		} catch (Exception e) {
			System.out.println("Error occured: " + e);
			return null;
		}
	}
}