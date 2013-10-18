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
package se.skl.skltpservices.adapter.apse.hamtapatientinfo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.soitoolkit.commons.mule.test.AbstractTestCase;

import se.riv.se.apotekensservice.axs.hamtapatientinforesponder.v1.HamtaPatientInfoResponseType;

public class HamtaPatientInfoIntegrationTest extends AbstractTestCase {

    @BeforeClass
    public void beforeClass() {
	setDisposeManagerPerSuite(true);
	setTestTimeoutSecs(120);
    }

    @Before
    public void doSetUp() throws Exception {
	super.doSetUp();
	setDisposeManagerPerSuite(true);
    }

    @Override
    protected String getConfigResources() {
		return "ApSeIntegrationComponent-common.xml,services/AXS-HamtaPatintInfo-apse-service.xml,ApSeIntegrationComponent-teststubs-and-services-config.xml";
    }

    @Test
    public void testRequestSsnWithCompleteArgosHeader() throws Exception {
	String ssn = "196308212817";
	String to = "1234567";

	HamtaPatientInfoResponseType response = new HamtaPatientInfoTestConsumer("http://localhost:11000/tjanstebryggan/HamtaPatientInfoResponder/V1")
		.requestIncludingCompleteArgosInformation(ssn, to);

	assertNotNull(response);
	assertTrue(response.isFinnsOrdination());
    }

    @Test
    public void testEncodingIsProperThroughIntegration() throws Exception {
	String ssn = "ÅÄÖ";
	String to = "1234567";

	HamtaPatientInfoResponseType response = new HamtaPatientInfoTestConsumer("http://localhost:11000/tjanstebryggan/HamtaPatientInfoResponder/V1")
		.requestIncludingCompleteArgosInformation(ssn, to);

	assertNotNull(response);
	assertEquals("ÅÄÖ", response.getDosproducent());
    }

}
