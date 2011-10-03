package se.skl.tp.ticket.transformer;

 
import org.soitoolkit.commons.mule.test.StandaloneMuleServer;
 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TicketTransformerMuleServer {

	private static final Logger logger = LoggerFactory.getLogger(TicketTransformerMuleServer.class);

	public static final String MULE_SERVER_ID   = "tickettransformer";
 
	public static final String MULE_CONFIG      = "TicketTransformer-teststubs-and-services-config.xml"; // both teststubs and services
//	public static final String MULE_CONFIG      = "TicketTransformer-teststubs-only-config.xml"; // only teststubs
//	public static final String MULE_CONFIG      = "vp-config.xml"; // only services

	public static void main(String[] args) throws Exception {
		
	    	logger.info("Starting mule with args",args);
	    
		StandaloneMuleServer muleServer = new StandaloneMuleServer(MULE_SERVER_ID, MULE_CONFIG);
 
		muleServer.run();
	}
	
 
}