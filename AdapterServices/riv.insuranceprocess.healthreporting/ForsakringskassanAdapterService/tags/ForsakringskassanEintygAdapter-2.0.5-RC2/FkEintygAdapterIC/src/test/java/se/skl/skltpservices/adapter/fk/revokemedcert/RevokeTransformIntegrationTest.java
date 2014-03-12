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
package se.skl.skltpservices.adapter.fk.revokemedcert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.soitoolkit.commons.mule.test.junit4.AbstractTestCase;
import org.soitoolkit.commons.mule.util.RecursiveResourceBundle;

import se.skl.riv.insuranceprocess.healthreporting.qa.v1.Amnetyp;
import se.skl.riv.insuranceprocess.healthreporting.sendmedicalcertificatequestionresponder.v1.SendMedicalCertificateQuestionResponseType;
import se.skl.riv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.skl.skltpservices.adapter.fk.producer.FkAdapterTestProducerLogger;

public class RevokeTransformIntegrationTest extends AbstractTestCase {
	
	private static final RecursiveResourceBundle rb = new RecursiveResourceBundle("FkIntegrationComponent-config");

	@Before
	public void doSetUp() throws Exception {
		super.doSetUp();
	}

	@Override
	protected String getConfigResources() {
		return 	"soitoolkit-mule-jms-connector-activemq-embedded.xml," +
				"FkIntegrationComponent-config.xml," +
			    "teststub-services/RevokeMedicalCertificate-fk-teststub-service.xml";
	}

	@Test
	public void sendMCQMakulering() throws Exception {
		RevokeTransformTestConsumer consumer = new RevokeTransformTestConsumer(
				"https://localhost:12000/tb/eintyg/revoke/SendMedicalCertificateQuestion/1/rivtabp20");

		SendMedicalCertificateQuestionResponseType response = consumer.sendMCQuestion(Amnetyp.MAKULERING_AV_LAKARINTYG, "Kalle");

		Thread.currentThread().sleep(1000);
		
		assertEquals(response.getResult().getResultCode(), ResultCodeEnum.OK);
		
		//Verify http headers are propagated frpm FKAdapter to producer (VP) when revoke is triggered
		assertEquals(rb.getString("FKADAPTER_HSA_ID"), FkAdapterTestProducerLogger.getLatestSenderId());
		assertEquals(rb.getString("VP_INSTANCE_ID"), FkAdapterTestProducerLogger.getLatestVpInstanceId());
	}
	
	@Test
	public void sendMCQOvrigt() throws Exception {
		RevokeTransformTestConsumer consumer = new RevokeTransformTestConsumer(
				"https://localhost:12000/tb/eintyg/revoke/SendMedicalCertificateQuestion/1/rivtabp20");

		SendMedicalCertificateQuestionResponseType response = consumer.sendMCQuestion(Amnetyp.OVRIGT, "Kalle");

		Thread.currentThread().sleep(1000);
		
		assertEquals(response.getResult().getResultCode(), ResultCodeEnum.OK);
	}

}
