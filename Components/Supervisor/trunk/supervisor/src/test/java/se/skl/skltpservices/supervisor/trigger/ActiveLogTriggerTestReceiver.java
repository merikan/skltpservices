package se.skl.skltpservices.supervisor.trigger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveLogTriggerTestReceiver {

	private static final Logger log = LoggerFactory.getLogger(ActiveLogTriggerTestReceiver.class);

	public void process(Object message) {
		log.info("ActiveLogTriggerTestReceiver received the message: {}", message);
	}
}
