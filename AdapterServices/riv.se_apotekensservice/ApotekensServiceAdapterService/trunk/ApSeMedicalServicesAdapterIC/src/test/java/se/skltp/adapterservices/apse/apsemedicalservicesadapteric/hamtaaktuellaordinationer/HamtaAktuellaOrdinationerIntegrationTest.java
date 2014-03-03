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
package se.skltp.adapterservices.apse.apsemedicalservicesadapteric.hamtaaktuellaordinationer;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.soitoolkit.commons.mule.test.junit4.AbstractTestCase;

import se.riv.se.apotekensservice.or.hamtaaktuellaordinationerresponder.v1.HamtaAktuellaOrdinationerResponseType;

public class HamtaAktuellaOrdinationerIntegrationTest extends AbstractTestCase {

//	@BeforeClass
//	public void beforeClass() {
//		setDisposeManagerPerSuite(true);
//		org.soitoolkit.commons.mule.test.AbstractTestCase.setTestTimeoutSecs(240);
//	}

//	@Before
//	public void doSetUp() throws Exception {
//		super.doSetUp();
//		setDisposeManagerPerSuite(true);
//	}

	@Override
	protected String getConfigResources() {
		return "ApSeMedicalServicesAdapterIC-config.xml,services/OR-HamtaAktuellaOrdinationer-apse-service.xml,ApSeIntegrationComponent-teststubs-and-services-config.xml";
	}

	@Ignore
	@Test
	public void testRequestSsnWithCompleteArgosHeader() throws Exception {
		String ssn = "196308212817";
		String to = "1234567";

		HamtaAktuellaOrdinationerResponseType response = new HamtaAllaAktuellaOrdinationerTestConsumer(
				"http://localhost:11000/tjanstebryggan/HamtaAktuellaOrdinationerResponder/V1")
				.requestIncludingCompleteArgosInformation(ssn, to);

		assertNotNull(response);
		assertEquals(ssn, response.getOrdinationslista().getPersonnummer());
	}

	@Ignore
	@Test
	public void testRequestWithEncoding() throws Exception {
		String ssn = "���";
		String to = "1234567";

		HamtaAktuellaOrdinationerResponseType response = new HamtaAllaAktuellaOrdinationerTestConsumer(
				"http://localhost:11000/tjanstebryggan/HamtaAktuellaOrdinationerResponder/V1")
				.requestIncludingCompleteArgosInformation(ssn, to);

		assertNotNull(response);
		assertEquals("���", response.getOrdinationslista().getPersonnummer());
	}

	@Ignore
	@Test
	public void testSoapFaultIsReturnedWhenProducerThrowsRuntimeException() throws Exception {
		String ssn = "";
		String to = "1234567";

		try {
			new HamtaAllaAktuellaOrdinationerTestConsumer(
					"http://localhost:11000/tjanstebryggan/HamtaAktuellaOrdinationerResponder/V1")
					.requestIncludingCompleteArgosInformation(ssn, to);
		} catch (javax.xml.ws.soap.SOAPFaultException e) {
			String faultString = e.getFault().getFaultString();
			Assert.assertThat(faultString, containsString("Personnummer is mandatory"));
			return;
		}
		Assert.fail("An exception was expected");
	}

	@Ignore
	@Test
	public void testApplicationExceptionThrownByProducer() throws Exception {
		String ssn = "APPLICATIONEXCEPTION";
		String to = "1234567";

		try {
			new HamtaAllaAktuellaOrdinationerTestConsumer(
					"http://localhost:11000/tjanstebryggan/HamtaAktuellaOrdinationerResponder/V1")
					.requestIncludingCompleteArgosInformation(ssn, to);
		} catch (javax.xml.ws.soap.SOAPFaultException e) {
			String faultString = e.getFault().getFaultString();
			Assert.assertThat(faultString, org.hamcrest.Matchers.containsString("APPLICATIONEXCEPTION"));
			return;
		}
		Assert.fail("An exception was expected");
	}

	@Ignore
	@Test
	public void testSystemExceptionThrownByProducer() throws Exception {
		String ssn = "SYSTEMEXCEPTION";
		String to = "1234567";

		try {
			new HamtaAllaAktuellaOrdinationerTestConsumer(
					"http://localhost:11000/tjanstebryggan/HamtaAktuellaOrdinationerResponder/V1")
					.requestIncludingCompleteArgosInformation(ssn, to);
		} catch (javax.xml.ws.soap.SOAPFaultException e) {
			String faultString = e.getFault().getFaultString();
			Assert.assertThat(faultString, containsString("SYSTEMEXCEPTION"));
			return;
		}
		Assert.fail("An exception was expected");
	}
}
