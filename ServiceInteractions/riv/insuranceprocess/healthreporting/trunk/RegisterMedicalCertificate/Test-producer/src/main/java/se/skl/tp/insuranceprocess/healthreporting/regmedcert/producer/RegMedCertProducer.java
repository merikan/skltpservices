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

package se.skl.tp.insuranceprocess.healthreporting.regmedcert.producer;

import javax.xml.ws.Endpoint;


public class RegMedCertProducer {

    protected RegMedCertProducer() throws Exception {
        System.out.println("Starting Producer");

        Object implementor = new RegMedCertImpl();
        String address = "https://localhost:21000/vp/RegisterMedicalCertificate/1/rivtabp20";
        Endpoint.publish(address, implementor);
    }

	public static void main(String[] args) throws Exception {
		
        new RegMedCertProducer();
        System.out.println("Producer ready...");
        
        Thread.sleep(5 * 60 * 1000);
        System.out.println("Producer exiting");
        System.exit(0);
    }
	
}
