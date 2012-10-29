package se.skl.skltpservices.adapter.apse;

import org.soitoolkit.commons.mule.test.StandaloneMuleServer;


public class ApseIntegrationComponentMuleServer {


	public static final String MULE_SERVER_ID   = "ApSeIntegrationComponent";
 
	public static final String MULE_CONFIG      = "ApSeIntegrationComponent-config.xml,ApSeIntegrationComponent-teststubs-and-services-config.xml";

	public static void main(String[] args) throws Exception {
		
		StandaloneMuleServer muleServer = new StandaloneMuleServer(MULE_SERVER_ID, MULE_CONFIG);
 
		muleServer.run();
	}

}