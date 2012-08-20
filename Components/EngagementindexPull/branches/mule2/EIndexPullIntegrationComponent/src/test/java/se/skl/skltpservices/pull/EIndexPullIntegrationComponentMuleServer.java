package se.skl.skltpservices.pull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.test.StandaloneMuleServer;

public class EIndexPullIntegrationComponentMuleServer {

	private static final Logger logger = LoggerFactory.getLogger(EIndexPullIntegrationComponentMuleServer.class);
	public static final String MULE_SERVER_ID = "EngagementIndexPull-Service";
	public static final String MULE_CONFIG = "teststubs-and-services-config.xml";

	public static void main(String[] args) throws Exception {
		logger.info("Starting mule with args", args);
		StandaloneMuleServer muleServer = new StandaloneMuleServer(MULE_SERVER_ID, MULE_CONFIG);
		muleServer.run();
	}

}