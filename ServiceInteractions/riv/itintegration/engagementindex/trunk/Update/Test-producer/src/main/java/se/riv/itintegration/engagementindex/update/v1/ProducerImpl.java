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
package se.riv.itintegration.engagementindex.update.v1;

import riv.itintegration.engagementindex._1.EngagementTransactionType;
import riv.itintegration.engagementindex._1.EngagementType;
import riv.itintegration.engagementindex._1.ResultCodeEnum;
import se.riv.itintegration.engagementindex.update.v1.rivtabp21.UpdateResponderInterface;
import se.riv.itintegration.engagementindex.updateresponder.v1.UpdateResponseType;
import se.riv.itintegration.engagementindex.updateresponder.v1.UpdateType;

import javax.jws.WebService;
import java.util.HashSet;
import java.util.Random;

@WebService(serviceName = "UpdateResponderService", endpointInterface = "se.riv.itintegration.engagementindex.update.v1.rivtabp21.UpdateResponderInterface", portName = "UpdateResponderPort", targetNamespace = "urn:riv:itintegration:engagementindex:Update:1:rivtabp21", wsdlLocation = "schemas/interactions/UpdateInteraction/UpdateInteraction_1.0_RIVTABP21.wsdl")
public class ProducerImpl implements UpdateResponderInterface {

	@Override
	public UpdateResponseType update(String arg0, UpdateType updateData) {
        int amountOfUpdates = updateData.getEngagementTransaction().size();
        HashSet<String> receivedSsns = new HashSet<String>(amountOfUpdates * 2);
        for (EngagementTransactionType engagementTransactionType : updateData.getEngagementTransaction()) {
            EngagementType engagementType = engagementTransactionType.getEngagement();
            receivedSsns.add(engagementType.getRegisteredResidentIdentification());
        }
        String allSocialSecurityNumbers = "";
        for (String socialSecurityNumber : receivedSsns) {
            allSocialSecurityNumbers = allSocialSecurityNumbers + ", " + socialSecurityNumber;
        }
        allSocialSecurityNumbers = allSocialSecurityNumbers.replaceFirst(", ", "");

		UpdateResponseType responseType = new UpdateResponseType();
		responseType.setComment(allSocialSecurityNumbers);
        Random random = new Random(System.currentTimeMillis());
        int randomNumber = random.nextInt(100) + 1;
        if (randomNumber < 90) {
		    responseType.setResultCode(ResultCodeEnum.OK);
        } else if (randomNumber < 95) {
            responseType.setResultCode(ResultCodeEnum.INFO);
        } else {
            responseType.setResultCode(ResultCodeEnum.ERROR);
        }
		return responseType;
	}

}
