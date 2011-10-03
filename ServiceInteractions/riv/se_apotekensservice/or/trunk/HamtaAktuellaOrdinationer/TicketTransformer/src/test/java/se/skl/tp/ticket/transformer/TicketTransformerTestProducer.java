package se.skl.tp.ticket.transformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketTransformerTestProducer {

    private static final Logger log = LoggerFactory.getLogger(TicketTransformerTestProducer.class);

    public Object process(String xmlRequest) {

	log.info("TicketTransformerTestProducer received the request: {}", xmlRequest);

	return null;
    }

}
