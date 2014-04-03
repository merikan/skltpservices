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
