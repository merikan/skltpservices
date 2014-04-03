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
import static se.skltp.adapterservices.apse.apsemedicalservicesadapteric.ArgosHeaderTestUtil.createCompleteCitizenArgosHeader;
import static se.skltp.adapterservices.apse.apsemedicalservicesadapteric.ArgosHeaderTestUtil.createOrganizationArgosHeader;
import static se.skltp.adapterservices.apse.apsemedicalservicesadapteric.lfutskrift.LFUtskriftTestProducer.CITIZENREQUEST;
import static se.skltp.adapterservices.apse.apsemedicalservicesadapteric.lfutskrift.LFUtskriftTestProducer.ORGANIZATIONREQUEST;

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

	private static final String DEFAULT_SERVICE_ADDRESS = getAddress("inbound.endpoint.http.apotekensservice.lf.LFUtskrift");
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
		return 	"soitoolkit-mule-jms-connector-activemq-embedded.xml," + 
				"ApSeMedicalServicesAdapterIC-common.xml," +
				"services/LF-LFUtskrift-apse-service.xml," +
				"teststub-services/LF-LFUtskrift-apse-teststub-service.xml";
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

	@Test
	public void testOrganizationRequestRequest_happydays() throws Exception {
		String to = "logicaladdress";

		ArgosHeaderType argosHeader = createOrganizationArgosHeader();
		
		LFUtskriftRequestType request = new LFUtskriftRequestType();
		request.setPersonnummer(ORGANIZATIONREQUEST);
		request.setAnvandarNamn(argosHeader.getFornamn());

		LFUtskriftResponseType response = new LFUtskriftTestConsumer(
				DEFAULT_SERVICE_ADDRESS)
				.organizationRequest(request, to, argosHeader);

		assertNotNull(response);
		assertEquals(ORGANIZATIONREQUEST, response.getPatient().getPersonnummer());
	}
}
