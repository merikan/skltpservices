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

package se.skl.tp.vp.util;


/**
 * Utility class for the virtualization platform
 * 
 * @author Marcus Krantz [marcus.krantz@callistaenterprise.se]
 */
public final class VPUtil {

	public static final String SESSION_ERROR = "sessionStatus";
	public static final String SESSION_ERROR_DESCRIPTION = "sessionErrorDescription";
	public static final String SESSION_ERROR_TECHNICAL_DESCRIPTION = "sessionErrorTechnicalDescription";

	public static final String RECEIVER_ID = "receiverid";
	public static final String SENDER_ID = "senderid";
	public static final String RIV_VERSION = "rivversion";
	public static final String SERVICE_NAMESPACE = "cxf_service";
	public static final String ENDPOINT_URL = "endpoint_url";

	public static final String TIMER_TOTAL = "total";
	public static final String TIMER_ROUTE = "route";
	public static final String TIMER_ENDPOINT = "endpoint_time";

	public static String nvl(String s) {
		return (s == null) ? "" : s;
	}

}
