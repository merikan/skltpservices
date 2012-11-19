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
package se.skl.skltpservices.supervisor.pingforconfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.util.RecursiveResourceBundle;

import se.riv.itintegration.monitoring.rivtabp21.v1.PingForConfigurationResponderInterface;
import se.riv.itintegration.monitoring.v1.ConfigurationType;
import se.riv.itintegration.monitoring.v1.PingForConfigurationResponseType;
import se.riv.itintegration.monitoring.v1.PingForConfigurationType;

@WebService(portName = "PingForConfigurationResponderPort", name = "PingForConfigurationResponderPort", targetNamespace = "urn:riv:itintegration:monitoring:PingForConfigurationResponder:1")
public class PingForConfigurationTestProducer implements PingForConfigurationResponderInterface {

	public static final String TEST_ID_FAULT_TIMEOUT = "0";
	public static final String TEST_ID_ERROR = "-1";
	public static final String TEST_ID_OK = "SE165565594230-1000";

	private static final RecursiveResourceBundle rb = new RecursiveResourceBundle(
			"PingForConfigurationSupervisor-config");
	private static final long SERVICE_TIMOUT_MS = Long.parseLong(rb.getString("SERVICE_TIMEOUT_MS"));

	private static final Logger logger = LoggerFactory.getLogger(PingForConfigurationTestProducer.class);

	public PingForConfigurationResponseType pingForConfiguration(String logicalAddress,
			PingForConfigurationType parameters) {

		logger.info("pingForConfiguration({},{})", logicalAddress, parameters);
		logger.debug("Ping for configuration, logical adress: {}", parameters.getLogicalAddress());
		logger.debug("Ping for configuration, servicecontract namespace: {}", parameters.getServiceContractNamespace());

		String id = parameters.getLogicalAddress();

		if (TEST_ID_ERROR.equals(id)) {
			throw new RuntimeException("Exception logical adress was found, return RuntimeException");
		}

		// Force a timeout if zero logicalAddress
		if (TEST_ID_FAULT_TIMEOUT.equals(id)) {
			try {
				Thread.sleep(SERVICE_TIMOUT_MS + 1000);
			} catch (InterruptedException e) {
			}
		}

		PingForConfigurationResponseType configurationResponseType = new PingForConfigurationResponseType();
		configurationResponseType.setPingDateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		configurationResponseType.setVersion("1.0");

		configurationResponseType.getConfiguration().add(createConfigurationType("Testproducer", "true"));
		configurationResponseType.getConfiguration().add(createConfigurationType("Anothervalue", "yes another value"));

		return configurationResponseType;
	}

	private ConfigurationType createConfigurationType(String name, String value) {
		ConfigurationType configurationType = new ConfigurationType();
		configurationType.setName(name);
		configurationType.setValue(value);
		return configurationType;
	}

}
