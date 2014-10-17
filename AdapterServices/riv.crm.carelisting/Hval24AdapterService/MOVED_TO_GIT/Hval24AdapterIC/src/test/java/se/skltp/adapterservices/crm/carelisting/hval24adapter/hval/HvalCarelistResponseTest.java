/**
 * Copyright (c) 2014, Inera AB <http://www.inera.se/>. All rights reserved.
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
package se.skltp.adapterservices.crm.carelisting.hval24adapter.hval;

import junit.framework.Assert;

import org.junit.Test;

public class HvalCarelistResponseTest {
	
	String testdata = "1750    0191212121212TOLVAN                                  TOLVANSSON                              TOLVAR STIGEN                           12345STOCKHOLM                               19  S:t Göran församling                    80  Stockholm                               01  Stockholms län                          0                 5SE2321000016-1HZ6                                               Testvårdcentral A                       2014-03-07SE2321000016-1HZ6                                               Testvårdcentral A                        3                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            00                                                                                                                                                                                                                                                                          _";

	@Test
	public void extract() {
		
		HvalCarelistResponse response = HvalCarelistResponse.extract(testdata);
		
		System.err.println(response.length);
		
		Assert.assertEquals("1750", response.length);
		Assert.assertEquals(Integer.valueOf(0), response.retCode);
		Assert.assertEquals("191212121212", response.personId);
		Assert.assertEquals("TOLVAN", response.fornamn);
		Assert.assertEquals("TOLVANSSON", response.efternamn);
		
		
	}

}
