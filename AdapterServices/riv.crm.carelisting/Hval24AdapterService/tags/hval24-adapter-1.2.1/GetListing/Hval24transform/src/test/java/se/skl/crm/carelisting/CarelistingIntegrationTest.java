package se.skl.crm.carelisting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;
import org.mule.transport.http.HttpConnector;

import se.skl.riv.crm.carelistingresponder.v1.getlisting.GetListingResponseType;

public class CarelistingIntegrationTest extends FunctionalTestCase
{
    private String urlCarelist = null;
    private String urlHval = null;

	public CarelistingIntegrationTest() {
		ResourceBundle rb = ResourceBundle.getBundle("hval");
		urlCarelist = rb.getString("carelisting.url");
		urlHval = rb.getString("hvalriks.url");
	}
	
	@Override
	protected String getConfigResources()
	{
		return "carelisting-unittest-config.xml";
	}
	
    public void testNationelVirtualService() throws Exception
    {
    	String personId = "19621008-3611";
    	String logicalAddress = "01";
		Consumer consumer = new Consumer(urlCarelist);
		GetListingResponseType reply = consumer.callService(logicalAddress, personId);
		assertEquals(personId, reply.getSubjectOfCare().getPersonId());
	}
	
    public void testBackendService() throws Exception
    {
    	String personId = "196210083611";
    	MuleClient client = new MuleClient();
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
    	MuleClient client = new MuleClient();
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