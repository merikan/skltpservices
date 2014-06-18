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
package se.skltp.aggregatingservices.riv.crm.requeststatus.getrequestactivities.v1;

import java.net.URL;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBusFactory;

public class Producer implements Runnable{
	
	public static final String AGDA_ANDERSSON = "188803099368";
	public static final String FRIDA_KRANSTEGE = "197705232382";
	public static final String LABAN_MEIJER = "194804032094";
	public static final String NINA_GREGER = "197309069289";
	public static final String OSKAR_JOHANSSON = "199008252398";
	public static final String ULLA_ALM = "198611062384";
	
	private static String ENDPOINT_PRODUCER_1 = "http://0.0.0.0:20203/producer_1/teststub/GetRequestActivities/1/rivtabp21";
	private static String ENDPOINT_PRODUCER_2 = "http://0.0.0.0:20203/producer_2/teststub/GetRequestActivities/1/rivtabp21";
	private static String ENDPOINT_PRODUCER_3 = "http://0.0.0.0:20203/producer_3/teststub/GetRequestActivities/1/rivtabp21";
	private static String ENDPOINT_PRODUCER_4 = "http://0.0.0.0:20203/producer_4/teststub/GetRequestActivities/1/rivtabp21";
	private static String ENDPOINT_PRODUCER_5 = "http://0.0.0.0:20203/producer_5/teststub/GetRequestActivities/1/rivtabp21";
	
	protected Producer(String address, final Object producer) throws Exception {
		println "Starting GetRequestActivities testproducer with endpoint: ${address}"

		// Loads a cxf configuration file to use
		final SpringBusFactory bf = new SpringBusFactory();
		final URL busFile = this.getClass().getClassLoader().getResource("cxf-producer.xml");
		final Bus bus = bf.createBus(busFile.toString());

		SpringBusFactory.setDefaultBus(bus);
		
		Endpoint.publish(address, producer);
	}
	
	@Override
	public void run() {
		println "GetRequestActivities testproducer ready..."
	}

	public static void main(String[] args) throws Exception {
		
		if (args.length > 0) {
			ENDPOINT_PRODUCER_1 = args[0];
			ENDPOINT_PRODUCER_2 = args[1];
			ENDPOINT_PRODUCER_3 = args[2];
			ENDPOINT_PRODUCER_4 = args[3];
			ENDPOINT_PRODUCER_5 = args[4];
		}

		new Thread(new Producer(ENDPOINT_PRODUCER_1, new GetRequestActivitiesProducer1())).start();
		new Thread(new Producer(ENDPOINT_PRODUCER_2, new GetRequestActivitiesProducer2())).start();
		new Thread(new Producer(ENDPOINT_PRODUCER_3, new GetRequestActivitiesProducer3())).start();
		new Thread(new Producer(ENDPOINT_PRODUCER_4, new GetRequestActivitiesProducer4())).start();
		new Thread(new Producer(ENDPOINT_PRODUCER_5, new GetRequestActivitiesProducer5())).start();
	}	
}
