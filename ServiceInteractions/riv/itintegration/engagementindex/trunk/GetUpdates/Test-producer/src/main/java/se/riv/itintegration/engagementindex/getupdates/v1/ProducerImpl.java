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
package se.riv.itintegration.engagementindex.getupdates.v1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.jws.WebService;

import org.apache.commons.lang.StringUtils;
import riv.itintegration.engagementindex._1.EngagementType;
import se.riv.itintegration.engagementindex.getupdates.v1.rivtabp21.GetUpdatesResponderInterface;
import se.riv.itintegration.engagementindex.getupdatesresponder.v1.GetUpdatesResponseType;
import se.riv.itintegration.engagementindex.getupdatesresponder.v1.GetUpdatesType;
import se.riv.itintegration.engagementindex.getupdatesresponder.v1.RegisteredResidentEngagementType;

@WebService(serviceName = "GetUpdatesResponderService", endpointInterface = "se.riv.itintegration.engagementindex.getupdates.v1.rivtabp21.GetUpdatesResponderInterface", portName = "GetUpdatesResponderPort", targetNamespace = "urn:riv:itintegration:engagementindex:GetUpdates:1:rivtabp21", wsdlLocation = "schemas/interactions/GetUpdatesInteraction/GetUpdatesInteraction_1.0_RIVTABP21.wsdl")
public class ProducerImpl implements GetUpdatesResponderInterface {

    @Override
    public GetUpdatesResponseType getUpdates(String arg0, GetUpdatesType request) {
        GetUpdatesResponseType response = new GetUpdatesResponseType();
        response.setResponseIsComplete(true);

        String serviceDomain = request.getServiceDomain();
        if (StringUtils.equals(serviceDomain, "riv:crm:scheduling")) {
            // Simulate a partial request - if the previous result set is never sent from the consumer, then this would probably become an infinite loop
            if (request.getRegisteredResidentLastFetched().isEmpty()) {
                response.getRegisteredResidentEngagement().add(createRegisteredResidentEngagementType(serviceDomain, "197303160555"));
                response.setResponseIsComplete(false);
            } else {
                response.getRegisteredResidentEngagement().add(createRegisteredResidentEngagementType(serviceDomain, "197707070707"));
                response.setResponseIsComplete(true);
            }
        } else if (StringUtils.equals(serviceDomain, "riv:itintegration:engagementindex")) {
            long firstSocialSecurityNumber = new Long("190102030405");
            long amountOfPersons = 100;
            for (long i = 0; i < amountOfPersons; i++) {
                String tmpSsn = String.valueOf(firstSocialSecurityNumber + i);
                response.getRegisteredResidentEngagement().add(createRegisteredResidentEngagementType(serviceDomain, tmpSsn));
            }
        } else if (StringUtils.equals(serviceDomain, "riv:careprocess:request")) {
            Random random = new Random(System.currentTimeMillis());
            for (int amountOfRandomPersons = 300; amountOfRandomPersons > 0; amountOfRandomPersons--) {
                String registeredResidentIdentification = generateRandomSsn(random);
                response.getRegisteredResidentEngagement().add(createRegisteredResidentEngagementType(serviceDomain, registeredResidentIdentification));
            }
        } else {
            // Simulate returning empty list
        }
        return response;
    }

    private String generateRandomSsn(Random random) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date startDate;
        try {
            startDate = simpleDateFormat.parse("19000101");
        } catch (ParseException e) {
            startDate =  new Date(0L);
        }
        Calendar calendar = Calendar.getInstance();
        Date currentDate = new Date();
        Date randomBirthDate;
        do {
            calendar.setTime(startDate);
            int secondsToAdd = Math.abs(random.nextInt());
            calendar.add(Calendar.SECOND, secondsToAdd);
            int additionalYearsToAdd = Math.abs(random.nextInt(60));
            calendar.add(Calendar.YEAR, additionalYearsToAdd);
            randomBirthDate = calendar.getTime();
        } while (!randomBirthDate.before(currentDate)); // A date of birth which is in the future is not reasonable!
        String lastFour = generateLastFourInSsn(random);
        return simpleDateFormat.format(randomBirthDate) + lastFour;
    }

    private String generateLastFourInSsn(Random random) {
        int lastFourNumber = random.nextInt(10000);
        String lastFourAsString = String.valueOf(lastFourNumber);
        int amountOfZeroes = 4 - lastFourAsString.length();
        return StringUtils.repeat("0", amountOfZeroes) + lastFourAsString;
    }

    private RegisteredResidentEngagementType createRegisteredResidentEngagementType(String serviceDomain, String registeredResidentIdentification) {
        RegisteredResidentEngagementType engagementType = new RegisteredResidentEngagementType();
        engagementType.getEngagement().add(createEngagementType(registeredResidentIdentification, serviceDomain));
        engagementType.setRegisteredResidentIdentification(registeredResidentIdentification);
        return engagementType;
    }

    private EngagementType createEngagementType(String registeredResidentIdentification, String serviceDomain) {
        EngagementType engagementType = new EngagementType();
        engagementType.setBusinessObjectInstanceIdentifier("");
        engagementType.setCategorization("");
        engagementType.setCreationTime("");
        engagementType.setLogicalAddress("");
        engagementType.setMostRecentContent("");
        engagementType.setOwner("");
        engagementType.setRegisteredResidentIdentification(registeredResidentIdentification);
        engagementType.setServiceDomain(serviceDomain);
        engagementType.setSourceSystem("");
        engagementType.setUpdateTime(new Date().toString());
        return engagementType;
    }

}
