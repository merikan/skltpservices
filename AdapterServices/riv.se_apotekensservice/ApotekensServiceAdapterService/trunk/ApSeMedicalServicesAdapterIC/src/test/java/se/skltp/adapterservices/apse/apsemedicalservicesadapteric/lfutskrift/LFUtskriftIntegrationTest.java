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

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.soitoolkit.commons.mule.test.junit4.AbstractTestCase;

import static org.junit.Assert.*;
import static se.skltp.adapterservices.apse.apsemedicalservicesadapteric.lfutskrift.LFUtskriftTestProducer.*;

import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.se.apotekensservice.lf.lfutskriftresponder.v1.LFUtskriftRequestType;
import se.riv.se.apotekensservice.lf.lfutskriftresponder.v1.LFUtskriftResponseType;

public class LFUtskriftIntegrationTest extends AbstractTestCase {

//	@BeforeClass
//	public void beforeClass() {
//		setDisposeManagerPerSuite(true);
//		setTestTimeoutSecs(120);
//	}

//	@Before
//	public void doSetUp() throws Exception {
//		super.doSetUp();
//		setDisposeManagerPerSuite(true);
//	}

	@Override
	protected String getConfigResources() {
		return "ApSeMedicalServicesAdapterIC-config.xml,services/LF-LFUtskrift-apse-service.xml,ApSeIntegrationComponent-teststubs-and-services-config.xml";
	}

	@Ignore
	@Test
	public void testCitizenServiceRequest_happydays() throws Exception {
		String to = "logicaladdress";

		ArgosHeaderType argosHeader = createCompleteCitizenArgosHeader();
		
		LFUtskriftRequestType request = new LFUtskriftRequestType();
		request.setPersonnummer(CITIZENREQUEST);
		request.setAnvandarNamn(argosHeader.getFornamn());

		LFUtskriftResponseType response = new LFUtskriftTestConsumer(
				"http://localhost:11000/tjanstebryggan/LFUtskriftResponder/V1")
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
				"http://localhost:11000/tjanstebryggan/LFUtskriftResponder/V1")
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
