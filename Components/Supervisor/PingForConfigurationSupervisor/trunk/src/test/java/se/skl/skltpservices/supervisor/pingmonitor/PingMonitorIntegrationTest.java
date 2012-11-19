/**
 * Copyright (c) 2012, Sjukvardsradgivningen. All rights reserved.
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package se.skl.skltpservices.supervisor.pingmonitor;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;

import org.junit.Test;
import org.mule.api.MuleMessage;
import org.soitoolkit.commons.mule.test.AbstractJmsTestUtil;
import org.soitoolkit.commons.mule.test.ActiveMqJmsTestUtil;
import org.soitoolkit.commons.mule.test.junit4.AbstractTestCase;
import org.soitoolkit.commons.mule.util.MiscUtil;
import org.soitoolkit.commons.mule.util.RecursiveResourceBundle;

public class PingMonitorIntegrationTest extends AbstractTestCase {

	private static final RecursiveResourceBundle rb = new RecursiveResourceBundle(
			"PingForConfigurationSupervisor-config");

	private static final String IN_VM_QUEUE = rb.getString("ACTIVELOGTRIGGER_OUT_VM_QUEUE");

	private static final String ERROR_LOG_QUEUE = "SOITOOLKIT.LOG.ERROR";
	private AbstractJmsTestUtil jmsUtil = null;

	public PingMonitorIntegrationTest() {
		setDisposeContextPerClass(true);
	}

	protected String getConfigResources() {
		return "soitoolkit-mule-jms-connector-activemq-embedded.xml,"
				+ "PingForConfigurationSupervisor-common.xml,"
				+ "PingForConfigurationSupervisor-integrationtests-common.xml,"
				+ "PingForConfigurationMonitor-service.xml,"
				+ "teststub-services/LogEventMonitor-teststub-service.xml,"
				+ "teststub-services/PingForConfiguration-testproducer-teststub-service.xml";
	}

	@Override
	protected void doSetUp() throws Exception {
		super.doSetUp();

		doSetUpJms();
	}

	private void doSetUpJms() {
		if (jmsUtil == null)
			jmsUtil = new ActiveMqJmsTestUtil();
		jmsUtil.clearQueues(ERROR_LOG_QUEUE);
		jmsUtil.clearQueues("SOITOOLKIT.LOG.STORE");
	}

	@Test
	public void callingProducerShouldGiveAInfoLogEvent() throws JMSException {

		Map<String, String> props = new HashMap<String, String>();
		props.put("protocol", "https");
		props.put("host", "localhost");
		props.put("port", ":20001");
		props.put("path", "/supervisor/PingForConfiguration/1/rivtabp21");

		final String inputFile = "src/test/resources/testfiles/pingMonitorInput.txt";
		String receivingService = "LogEventInfo-teststub-service";

		final int timeout = 5000;

		String input = MiscUtil.readFileAsString(inputFile);

		// Setup inbound endpoint for vm-transport
		String inboundEndpoint = "vm://" + IN_VM_QUEUE;

		// Invoke the service and wait for the transformed message to arrive at
		// the receiving teststub service
		MuleMessage reply = dispatchAndWaitForServiceComponent(inboundEndpoint, input, props, receivingService, timeout);

		String payload = (String) reply.getPayload();

		assertEquals(
				true,
				payload.contains("<extraInfo><name>source</name><value>se.skl.skltpservices.supervisor.transformer.MonitorLogTransformer</value></extraInfo>"));
			
		assertEquals(true, payload.contains("<payload>org.mule.module.cxf.transport.MuleUniversalConduit"));

		// Verify error-queue
		assertEquals(0, jmsUtil.browseMessagesOnQueue(ERROR_LOG_QUEUE).size());
	}
}
