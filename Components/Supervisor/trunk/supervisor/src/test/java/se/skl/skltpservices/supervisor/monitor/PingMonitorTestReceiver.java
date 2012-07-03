package se.skl.skltpservices.supervisor.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingMonitorTestReceiver {

	private static final Logger log = LoggerFactory.getLogger(PingMonitorTestReceiver.class);

	public void process(Object message) {
		log.info("PingMonitorTestReceiver received the message: {}", message);
	}
}
