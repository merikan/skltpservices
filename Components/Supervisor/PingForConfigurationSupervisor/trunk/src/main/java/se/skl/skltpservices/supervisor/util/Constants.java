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

package se.skl.skltpservices.supervisor.util;

import java.util.Map;

import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;


/**
 * Utility class for the virtualization platform
 * 
 * @author Marcus Krantz [marcus.krantz@callistaenterprise.se]
 */
public final class Constants {

	public static final String SESSION_ERROR = "sessionStatus";
	public static final String SESSION_ERROR_DESCRIPTION = "sessionErrorDescription";
	public static final String SESSION_ERROR_TECHNICAL_DESCRIPTION = "sessionErrorTechnicalDescription";

	public static final String RIV_VERSION = "rivversion";
	public static final String SERVICE_NAMESPACE = "cxf_service";
    public static final String SYSTEM_NAME = "system-name";
	public static final String DOMAIN_DESCRIPTION = "domain-description";
	public static final String DOMAIN = "domain";
	public static final String SOURCE = "source";

	public static String nvl(String s) {
		return (s == null) ? "" : s;
	}

	public static void addSessionInfo(MuleMessage message, Map<String, String> map) {
		map.put(SYSTEM_NAME, message.getProperty(SYSTEM_NAME, PropertyScope.SESSION, ""));
		map.put(DOMAIN, message.getProperty(DOMAIN, PropertyScope.SESSION, ""));
		map.put(DOMAIN_DESCRIPTION, message.getProperty(DOMAIN_DESCRIPTION, PropertyScope.SESSION, ""));

		final Boolean error = message.getProperty(SESSION_ERROR, PropertyScope.SESSION, Boolean.FALSE);
		if (Boolean.TRUE.equals(error)) {
			map.put(SESSION_ERROR, error.toString());
			map.put(SESSION_ERROR_DESCRIPTION,
					nvl((String) message.getProperty(Constants.SESSION_ERROR_DESCRIPTION, PropertyScope.SESSION)));
			map.put(SESSION_ERROR_TECHNICAL_DESCRIPTION,
					nvl((String) message.getProperty(Constants.SESSION_ERROR_TECHNICAL_DESCRIPTION, PropertyScope.SESSION)));
		}
	}

}
