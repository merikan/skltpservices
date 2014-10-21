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
package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getdiagnosis.v2;

import java.net.URL;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBusFactory;

public class Producer implements Runnable{
	
	public static final String AGDA_ANDERSSON = "188803099368";
	public static final String TOLVAN_TOLVANSSON = "121212121212";
	public static final String LABAN_MEIJER = "194804032094";
	public static final String GUNBRITT_BODEN = "192011189228";
	public static final String SVEN_STURESSON = "194911172296";
	public static final String ULLA_ALM = "198611062384";
	
	private static String servicePath = 'GetDiagnosis/2/rivtabp21'
	private static String ENDPOINT_PRODUCER_1 = "http://0.0.0.0:20007/producer_1/teststub/$servicePath";
	private static String ENDPOINT_PRODUCER_2 = "http://0.0.0.0:20007/producer_2/teststub/$servicePath";
	private static String ENDPOINT_PRODUCER_3 = "http://0.0.0.0:20007/producer_3/teststub/$servicePath";
	private static String ENDPOINT_PRODUCER_4 = "http://0.0.0.0:20007/producer_4/teststub/$servicePath";
	private static String ENDPOINT_PRODUCER_5 = "http://0.0.0.0:20007/producer_5/teststub/$servicePath";
	private static String ENDPOINT_PRODUCER_6 = "http://0.0.0.0:20007/producer_6/teststub/$servicePath";
	
	protected Producer(String address, final Object producer) throws Exception {
		println "Starting testproducer with endpoint: ${address}"

		// Loads a cxf configuration file to use
		final SpringBusFactory bf = new SpringBusFactory();
		final URL busFile = this.getClass().getClassLoader().getResource("cxf-producer.xml");
		final Bus bus = bf.createBus(busFile.toString());

		SpringBusFactory.setDefaultBus(bus);
		
		Endpoint.publish(address, producer);
	}
	
	@Override
	public void run() {
		println "Testproducer ready..."
	}

	public static void main(String[] args) throws Exception {
		
		if (args.length > 0) {
			ENDPOINT_PRODUCER_1 = args[0];
			ENDPOINT_PRODUCER_2 = args[1];
			ENDPOINT_PRODUCER_3 = args[2];
			ENDPOINT_PRODUCER_4 = args[3];
			ENDPOINT_PRODUCER_5 = args[4];
			ENDPOINT_PRODUCER_6 = args[5];
		}

		new Thread(new Producer(ENDPOINT_PRODUCER_1, new Producer1())).start();
		new Thread(new Producer(ENDPOINT_PRODUCER_2, new Producer2())).start();
		new Thread(new Producer(ENDPOINT_PRODUCER_3, new Producer3())).start();
		new Thread(new Producer(ENDPOINT_PRODUCER_4, new Producer4())).start();
		new Thread(new Producer(ENDPOINT_PRODUCER_5, new Producer5())).start();
		new Thread(new Producer(ENDPOINT_PRODUCER_6, new Producer6())).start();
	}	
}
