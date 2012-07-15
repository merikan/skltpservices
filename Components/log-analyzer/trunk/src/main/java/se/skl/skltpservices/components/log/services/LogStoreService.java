package se.skl.skltpservices.components.log.services;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.util.ByteArrayInputStream;
import org.apache.camel.CamelContext;
import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.logentry.schema.v1.LogEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import se.skl.skltpservices.components.analyzer.domain.LogStoreRepository;


@Service
public class LogStoreService {

	private static final Logger log = LoggerFactory.getLogger(LogStoreService.class);
	private static final JAXBContext context = initContext();
	
    @Value("${log.mq.instances}")
    private String logInstances;
    private List<Consumer> consumers;      
	@Autowired
	private LogStoreRepository repo;

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

	/**
	 * Stops queue listeners.
	 */
	public void stop() {
		if (consumers == null) {
			return;
		}
		for (Consumer consumer : consumers) {
			try {
				consumer.stop();
			} catch (Exception e) {
				log.error("Error while trying to stop conusmer", e);
			}
		}
		consumers = null;
	}
	
	/**
	 * Starts queue listeners.
	 * 
	 * @throws Exception on any configuration error.
	 */
	public void start() throws Exception {
		this.repo.connect();
		
		CamelContext camel = new DefaultCamelContext();
		
		int i = 0;
		this.consumers= new LinkedList<Consumer>();

		for (String instance : logInstances.split(",")) {
			ActiveMQComponent mq = ActiveMQComponent.activeMQComponent(instance.trim());
			String compName = "activemq-" + i++;
			camel.addComponent(compName, mq);

			
			Consumer consumer = camel.getEndpoint(compName + ":SOITOOLKIT.LOG.STORE").createConsumer(new Processor() {		
				@Override
				public void process(Exchange exchange) throws Exception {
					try {
						LogEvent le = unmarshal((String)exchange.getIn().getBody());
						repo.storeInfoEvent(le);
					} catch (Exception e) {
						log.error("Unable to store log info event", e);
						throw e;
					}
				}
			});
			consumers.add(consumer);
			consumer.start();
			
			log.info(String.format("Started %s:SOITOOLKIT.LOG.STORE listener on %s", compName, instance));  

			consumer = camel.getEndpoint(compName + ":SOITOOLKIT.LOG.ERROR").createConsumer(new Processor() {		
				@Override
				public void process(Exchange exchange) throws Exception {
					try {
						LogEvent le = unmarshal((String)exchange.getIn().getBody());
						repo.storeErrorEvent(le);
					} catch (Exception e) {
						log.error("Unable to store log error event", e);
						throw e;
					}
				}
			});
			consumers.add(consumer);
			consumer.start();
			log.info(String.format("Started %s:SOITOOLKIT.LOG.ERROR listener on %s", compName, instance));  		
		}		
	}

	public static void main(final String[] args) throws Exception {
		String curDir = System.getProperty("user.dir");
		System.out.println(curDir);

		//	    ApplicationContext context = new ClassPathXmlApplicationContext("spring/camel-client.xml");
		//
		//	    // get the camel template for Spring template style sending of messages (= producer)
		//	    ConsumerTemplate camelTemplate = context.getBean("camelTemplate", ConsumerTemplate.class);
		final LogStoreService s = new LogStoreService();
		s.start();

	}

}
