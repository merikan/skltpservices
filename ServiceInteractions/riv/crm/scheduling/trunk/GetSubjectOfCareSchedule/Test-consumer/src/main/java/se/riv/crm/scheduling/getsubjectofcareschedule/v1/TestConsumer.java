/**
 * Copyright 2009 Sjukvardsradgivningen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public

 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the

 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,

 *   Boston, MA 02111-1307  USA
 */
package se.riv.crm.scheduling.getsubjectofcareschedule.v1;

import java.net.URL;

import javax.xml.ws.BindingProvider;

import se.riv.crm.scheduling.getsubjectofcareschedule.v1.rivtabp21.GetSubjectOfCareScheduleResponderInterface;
import se.riv.crm.scheduling.getsubjectofcareschedule.v1.rivtabp21.GetSubjectOfCareScheduleResponderService;
import se.riv.crm.scheduling.getsubjectofcarescheduleresponder.v1.GetSubjectOfCareScheduleResponseType;
import se.riv.crm.scheduling.getsubjectofcarescheduleresponder.v1.GetSubjectOfCareScheduleType;

public final class TestConsumer {

	private static final String LOGISK_ADDRESS = "/GetSubjectOfCareSchedule/1/rivtabp21";

	private static String host = "192.168.25.40:20000/vp";

	// private static String host = "test1.esb.ntjp.se/vp";

	public static void main(String[] args) {

		if (args.length > 0) {
			host = args[0];
		}

		// Setup ssl info for the initial ?wsdl lookup...
		System.setProperty("javax.net.ssl.keyStore", "../../certs/consumer.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "password");

		// pkcs12, jks
		System.setProperty("javax.net.ssl.keyStoreType", "jks");
		System.setProperty("javax.net.ssl.trustStore", "../../certs/truststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "password");
		System.setProperty("javax.net.ssl.trustStoreType", "jks");

		String endpointAddress = "https://" + host + LOGISK_ADDRESS;
		System.out.println("Consumer connecting to " + endpointAddress);
		String p = callService(endpointAddress, "Ping");
		System.out.println("Returned: " + p);
	}

	public static String callService(String endpointAddress, String logicalAddresss) {

		GetSubjectOfCareScheduleType request = new GetSubjectOfCareScheduleType();
		request.setHealthcareFacility("HSA-VKK123");
		request.setSubjectOfCare("19751026-6849");

		try {
			GetSubjectOfCareScheduleResponseType response = getService(endpointAddress).getSubjectOfCareSchedule(
					"Test", null, request);
			return ("GetSubjectOfCareScheduleResponseType response=" + response.getTimeslotDetail());
		} catch (Exception ex) {
			System.out.println("Exception=" + ex.getMessage());
			return null;
		}
	}

	// public static URL createEndpointUrlFromServiceAddress(String
	// serviceAddress) {
	// try {
	// return new URL(serviceAddress + "?wsdl");
	// } catch (MalformedURLException e) {
	// throw new RuntimeException("Malformed URL Exception: " + e.getMessage());
	// }
	// }

	private static GetSubjectOfCareScheduleResponderInterface getService(String endpointAddress) {

		try {
			final ClassLoader loader = Thread.currentThread().getContextClassLoader();
			final URL wsdl = loader
					.getResource("schemas/interactions/GetSubjectOfCareScheduleInteraction/GetSubjectOfCareScheduleInteraction_1.1_RIVTABP21.wsdl");

			GetSubjectOfCareScheduleResponderService service = new GetSubjectOfCareScheduleResponderService(wsdl);
			GetSubjectOfCareScheduleResponderInterface serviceInterface = service
					.getGetSubjectOfCareScheduleResponderPort();

			((BindingProvider) serviceInterface).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
					endpointAddress);

			return serviceInterface;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
}
