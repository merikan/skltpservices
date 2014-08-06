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
package se.skltp.adapterservices.crm.carelisting.hval24adapter.integrationtests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.transport.http.HttpConnector;

import se.skl.riv.crm.carelistingresponder.v1.getlisting.GetListingResponseType;

public class CarelistingIntegrationTest extends FunctionalTestCase
{
    private String urlCarelist = null;
    private String urlHval = null;

	public CarelistingIntegrationTest() {
		ResourceBundle rb = ResourceBundle.getBundle("Hval24Adapter-config");
		urlCarelist = rb.getString("inbound.endpoint.hval24.getlisting");
		urlHval = rb.getString("inbound.http.endpoint.hval24.getlisting.teststub");
	}
	
	@Override
	protected String getConfigResources()
	{
		return 
			"GetListing-hval-service.xml," + 
			"Hval24Adapter-common.xml," + 
			"teststub-services/carelisting-teststub-service.xml";
	}
	
	@Test
    public void testNationelVirtualService() throws Exception
    {
    	String personId = "19621008-3611";
    	String logicalAddress = "01";
		Consumer consumer = new Consumer(urlCarelist);
		GetListingResponseType reply = consumer.callService(logicalAddress, personId);
		assertEquals(personId, reply.getSubjectOfCare().getPersonId());
	}
	
	@Test
    public void testBackendService() throws Exception
    {
    	String personId = "196210083611";
    	MuleClient client = new MuleClient(muleContext);
    	Map<String, Object> props = new HashMap<String, Object>();
        props.put(HttpConnector.HTTP_METHOD_PROPERTY, "GET");
        MuleMessage result = client.send(urlHval + "/" + personId, "", props);
        String res = convertStreamToString((InputStream)result.getPayload());
        assertTrue(res.indexOf(personId) != -1);
        assertTrue(res.indexOf("Kalle") != -1);
        assertTrue(res.indexOf("LAN X") != -1);
    }
	
	public void testReceiverIdNullProvided() throws Exception
    {
    	String personId = "196210083611";
    	MuleClient client = new MuleClient(muleContext);
    	Map<String, Object> props = new HashMap<String, Object>();
        props.put(HttpConnector.HTTP_METHOD_PROPERTY, "GET");
        props.put("receiverid", null);
        MuleMessage result = client.send(urlHval + "/" + personId, "", props);
        String res = convertStreamToString((InputStream)result.getPayload());
        assertTrue(res.indexOf(personId) != -1);
        assertTrue(res.indexOf("Kalle") != -1);
        assertTrue(res.indexOf("LAN X") != -1);
    }
   
	private String convertStreamToString(InputStream is) {
        try {
			    StringBuilder sb = new StringBuilder();
			    String line;
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		        while ((line = reader.readLine()) != null) {
		            sb.append(line).append("\n");
		        }
			    return sb.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
}