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
package se.skltp.adapterservices.apse.apsemedicalservicesadapteric.lfutskrift;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static se.skltp.adapterservices.apse.apsemedicalservicesadapteric.ApSeMedicalServicesAdapterICMuleServer.getAddress;
import static se.skltp.adapterservices.apse.apsemedicalservicesadapteric.lfutskrift.LFUtskriftTestProducer.CITIZENREQUEST;
import static se.skltp.adapterservices.apse.apsemedicalservicesadapteric.lfutskrift.LFUtskriftTestProducer.ORGANIZATIONREQUEST;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.test.AbstractJmsTestUtil;
import org.soitoolkit.commons.mule.test.ActiveMqJmsTestUtil;
import org.soitoolkit.commons.mule.test.junit4.AbstractTestCase;

import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.se.apotekensservice.lf.lfutskriftresponder.v1.LFUtskriftRequestType;
import se.riv.se.apotekensservice.lf.lfutskriftresponder.v1.LFUtskriftResponseType;

public class LFUtskriftIntegrationTest extends AbstractTestCase {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(LFUtskriftIntegrationTest.class);

	private static final String DEFAULT_SERVICE_ADDRESS = getAddress("inbound.endpoint.apotekensservice.lf.LFUtskrift");
 
 
	private static final String ERROR_LOG_QUEUE = "SOITOOLKIT.LOG.ERROR";
	private AbstractJmsTestUtil jmsUtil = null;
 

    public LFUtskriftIntegrationTest() {
    
 
        // Only start up Mule once to make the tests run faster...
        // Set to false if tests interfere with each other when Mule is started only once.
        setDisposeContextPerClass(true);
    }

    @Override
	protected void doSetUp() throws Exception {
		super.doSetUp();
	
		doSetUpJms();  
     }

	private void doSetUpJms() {
		// TODO: Fix lazy init of JMS connection et al so that we can create jmsutil in the declaration
		// (The embedded ActiveMQ queue manager is not yet started by Mule when jmsutil is delcared...)
		if (jmsUtil == null) jmsUtil = new ActiveMqJmsTestUtil();
		
 
		// Clear queues used for error handling
		jmsUtil.clearQueues(ERROR_LOG_QUEUE);
    }
	
	@Override
	protected String getConfigResources() {
		return 	"ApSeMedicalServicesAdapterIC-config.xml," +
				"ApSeIntegrationComponent-teststubs-and-services-config.xml";
	}

	@Test
	public void testCitizenServiceRequest_happydays() throws Exception {
		String to = "logicaladdress";

		ArgosHeaderType argosHeader = createCompleteCitizenArgosHeader();
		
		LFUtskriftRequestType request = new LFUtskriftRequestType();
		request.setPersonnummer(CITIZENREQUEST);
		request.setAnvandarNamn(argosHeader.getFornamn());

		LFUtskriftResponseType response = new LFUtskriftTestConsumer(
				DEFAULT_SERVICE_ADDRESS)
				.citicenRequest(request, to, argosHeader);

		assertNotNull(response);
		assertEquals(CITIZENREQUEST, response.getPatient().getPersonnummer());
	}

	@Ignore
	@Test
	public void testOrganizationRequestRequest_happydays() throws Exception {
		String to = "logicaladdress";

		ArgosHeaderType argosHeader = createCompleteOrganizationArgosHeader();
		
		LFUtskriftRequestType request = new LFUtskriftRequestType();
		request.setPersonnummer(ORGANIZATIONREQUEST);
		request.setAnvandarNamn(argosHeader.getFornamn());

		LFUtskriftResponseType response = new LFUtskriftTestConsumer(
				DEFAULT_SERVICE_ADDRESS)
				.organizationRequest(request, to, argosHeader);

		assertNotNull(response);
		assertEquals(ORGANIZATIONREQUEST, response.getPatient().getPersonnummer());
	}

	private ArgosHeaderType createCompleteCitizenArgosHeader() {
		ArgosHeaderType argosHeader = new ArgosHeaderType();
		argosHeader.setFornamn("Agda");
		argosHeader.setEfternamn("Andersson");
		argosHeader.setHsaID("188803099368");
		argosHeader.setRollnamn("PRIVATPERSON");
		argosHeader.setOrganisationsnummer("1234567890");
		argosHeader.setRequestId("123456");
		argosHeader.setSystemIp("192.0.0.1");
		argosHeader.setSystemnamn("Melior");
		argosHeader.setSystemversion("1.0");
		return argosHeader;
	}

	private ArgosHeaderType createCompleteOrganizationArgosHeader() {
		ArgosHeaderType argosHeader = new ArgosHeaderType();
		argosHeader.setArbetsplatskod("1234567890");
		argosHeader.setArbetsplatsnamn("Sjukhuset");
		argosHeader.setBefattningskod("123456");
		argosHeader.setEfternamn("Jansson");
		argosHeader.setFornamn("Ake");
		argosHeader.setForskrivarkod("1111129");
		argosHeader.setHsaID("TSE6565656565-1003");
		argosHeader.setKatalog("HSA");
		argosHeader.setLegitimationskod("1");
		argosHeader.setOrganisationsnummer("1234567890");
		argosHeader.setPostadress("Vagen 1");
		argosHeader.setPostnummer("11111");
		argosHeader.setPostort("Staden");
		argosHeader.setRequestId("123456");
		argosHeader.setRollnamn("FORSKRIVARE");
		argosHeader.setSystemIp("192.0.0.1");
		argosHeader.setSystemnamn("Melior");
		argosHeader.setSystemversion("1.0");
		argosHeader.setTelefonnummer("08-1234567");
		argosHeader.setYrkesgrupp("Lakare");
		return argosHeader;
	}
}
