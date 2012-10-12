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
package se.skl.tp.vp.supervisor.pingforconfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.riv.itintegration.monitoring.rivtabp21.v1.PingForConfigurationResponderInterface;
import se.riv.itintegration.monitoring.v1.ConfigurationType;
import se.riv.itintegration.monitoring.v1.PingForConfigurationResponseType;
import se.riv.itintegration.monitoring.v1.PingForConfigurationType;

@WebService(portName = "PingForConfigurationResponderPort", name = "PingForConfigurationResponderPort", targetNamespace = "urn:riv:itintegration:monitoring:PingForConfigurationResponder:1")
public class PingForConfigurationTestProducer implements PingForConfigurationResponderInterface {

	private static final Logger logger = LoggerFactory.getLogger(PingForConfigurationTestProducer.class);

	public PingForConfigurationResponseType pingForConfiguration(String logicalAddress,
			PingForConfigurationType parameters) {

		logger.info("pingForConfiguration({},{})", logicalAddress, parameters);

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
