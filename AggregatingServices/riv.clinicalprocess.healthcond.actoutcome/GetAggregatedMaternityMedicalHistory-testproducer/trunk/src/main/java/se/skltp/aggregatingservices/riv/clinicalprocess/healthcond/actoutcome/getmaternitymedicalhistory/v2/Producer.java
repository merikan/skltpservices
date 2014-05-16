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
package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistory.v2;

import java.net.URL;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBusFactory;

public class Producer implements Runnable{
	
	private static String ENDPOINT_PRODUCER_1 = "http://0.0.0.0:20202/producer_1/teststub/GetMaternityMedicalHistory/2/rivtabp21";
	private static String ENDPOINT_PRODUCER_2 = "http://0.0.0.0:20202/producer_2/teststub/GetMaternityMedicalHistory/2/rivtabp21";
	private static String ENDPOINT_PRODUCER_3 = "http://0.0.0.0:20202/producer_3/teststub/GetMaternityMedicalHistory/2/rivtabp21";
	private static String ENDPOINT_PRODUCER_4 = "http://0.0.0.0:20202/producer_4/teststub/GetMaternityMedicalHistory/2/rivtabp21";
	private static String ENDPOINT_PRODUCER_5 = "http://0.0.0.0:20202/producer_5/teststub/GetMaternityMedicalHistory/2/rivtabp21";
	
	protected Producer(String address, final Object producer) throws Exception {
		System.out.println("Starting GetMaternityMedicalHistory testproducer with endpoint: " + address);

		// Loads a cxf configuration file to use
		final SpringBusFactory bf = new SpringBusFactory();
		final URL busFile = this.getClass().getClassLoader().getResource("cxf-producer.xml");
		final Bus bus = bf.createBus(busFile.toString());

		SpringBusFactory.setDefaultBus(bus);
		
		Endpoint.publish(address, producer);
	}
	
	@Override
	public void run() {
		System.out.println("GetMaternityMedicalHistory testproducer ready...");
	}

	public static void main(String[] args) throws Exception {
		
		if (args.length > 0) {
			ENDPOINT_PRODUCER_1 = args[0];
			ENDPOINT_PRODUCER_2 = args[1];
			ENDPOINT_PRODUCER_3 = args[2];
			ENDPOINT_PRODUCER_4 = args[3];
			ENDPOINT_PRODUCER_5 = args[4];
		}

		new Thread(new Producer(ENDPOINT_PRODUCER_1, new GetMaternityMedicalHistoryProducer1())).start();
		new Thread(new Producer(ENDPOINT_PRODUCER_2, new GetMaternityMedicalHistoryProducer2())).start();
		new Thread(new Producer(ENDPOINT_PRODUCER_3, new GetMaternityMedicalHistoryProducer3())).start();
		new Thread(new Producer(ENDPOINT_PRODUCER_4, new GetMaternityMedicalHistoryProducer4())).start();
		new Thread(new Producer(ENDPOINT_PRODUCER_5, new GetMaternityMedicalHistoryProducer5())).start();
	}	
}
