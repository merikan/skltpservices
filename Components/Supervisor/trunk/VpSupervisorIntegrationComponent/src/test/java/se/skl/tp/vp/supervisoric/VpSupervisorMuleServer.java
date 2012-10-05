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
package se.skl.tp.vp.supervisoric;

import org.soitoolkit.commons.mule.test.StandaloneMuleServer;

import org.soitoolkit.commons.mule.util.RecursiveResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VpSupervisorMuleServer {

	public static final String MULE_SERVER_ID = "EIPullIC";

	private static final Logger logger = LoggerFactory.getLogger(VpSupervisorMuleServer.class);
	private static final RecursiveResourceBundle rb = new RecursiveResourceBundle(
			"VpSupervisorIntegrationComponent-config");

	public static void main(String[] args) throws Exception {

		StandaloneMuleServer muleServer = new StandaloneMuleServer(MULE_SERVER_ID, "VpSupervisorIntegrationComponent-teststubs-and-services-config.xml", true);

		muleServer.run();
	}

	/**
	 * Address based on usage of the servlet-transport and a config-property for
	 * the URI-part
	 * 
	 * @param serviceUrlPropertyName
	 * @return
	 */
	public static String getAddress(String serviceUrlPropertyName) {

		String url = rb.getString(serviceUrlPropertyName);

		logger.info("URL: {}", url);
		return url;

	}
}