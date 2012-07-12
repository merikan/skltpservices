package se.skl.skltpservices.components.log.services;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.util.ByteArrayInputStream;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultCamelContext;
import org.soitoolkit.commons.logentry.schema.v1.LogEvent;
import org.springframework.stereotype.Service;

@Service
public class LogStoreService {

	//static Logger log = Logger.getLogger(StoreService.class);
	private static final JAXBContext context = initContext();
	
	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance(LogEvent.class);
		} catch (JAXBException e) {
			throw new RuntimeException("Unable to create JAXBContext for LogEvent", e);
		}
	}    

    //
	public LogStoreService() {
	}

	//
	private static LogEvent unmarshal(final String msg) {
		try {
			return (LogEvent)context.createUnmarshaller().unmarshal(new ByteArrayInputStream(msg.getBytes()));
		} catch (JAXBException e) {
			throw new IllegalArgumentException(e);
		}
	}	

    
	public void error(Exchange exchange) {
		
		LogEvent le = unmarshal((String)exchange.getIn().getBody());
		System.out.printf("error: \"%s\" (%s) - %s\n", 
				le.getLogEntry().getRuntimeInfo().getBusinessCorrelationId(), 
				le.getLogEntry().getMessageInfo().getMessage(),
				le.getLogEntry().getPayload());
	}

	public void info(Exchange exchange) {
		LogEvent le = unmarshal((String)exchange.getIn().getBody());
	
        
		System.out.printf("stored: \"%s\" (%s) - %s\n", 
				le.getLogEntry().getRuntimeInfo().getBusinessCorrelationId(), 
				le.getLogEntry().getMessageInfo().getMessage(),
				le.getLogEntry().getPayload());

	}

	public static void main(final String[] args) throws Exception {
		String curDir = System.getProperty("user.dir");
		System.out.println(curDir);

		//	    ApplicationContext context = new ClassPathXmlApplicationContext("spring/camel-client.xml");
		//
		//	    // get the camel template for Spring template style sending of messages (= producer)
		//	    ConsumerTemplate camelTemplate = context.getBean("camelTemplate", ConsumerTemplate.class);
		final LogStoreService s = new LogStoreService();

		CamelContext camel = new DefaultCamelContext();
		ActiveMQComponent mq = ActiveMQComponent.activeMQComponent("failover:(tcp://192.168.25.41:61616)");
		camel.addComponent("activemq", mq);
		camel.getEndpoint("activemq:SOITOOLKIT.LOG.STORE").createConsumer(new Processor() {		
			@Override
			public void process(Exchange exchange) throws Exception {
				try {
					s.info(exchange);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		camel.getEndpoint("activemq:SOITOOLKIT.LOG.ERROR").createConsumer(new Processor() {		
			@Override
			public void process(Exchange exchange) throws Exception {
				s.error(exchange);
			}
		}).start();
		System.out.println("Started");  
	}

}
