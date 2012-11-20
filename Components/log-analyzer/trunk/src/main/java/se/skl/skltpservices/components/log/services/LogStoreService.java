/**
 * Copyright (c) 2012, Sjukvardsradgivningen. All rights reserved.
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
package se.skl.skltpservices.components.log.services;

import java.io.StringWriter;
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
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.logentry.schema.v1.LogEvent;
import org.soitoolkit.commons.logentry.schema.v1.LogLevelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import se.skl.skltpservices.components.analyzer.domain.LogStoreRepository;
import se.skl.skltpservices.components.analyzer.services.LogAnalyzerService;


@Service
public class LogStoreService {

	private static final String SOITOOLKIT_LOG_STORE_SUFFIX = ":SOITOOLKIT.LOG.STORE";
	private static final String SOITOOLKIT_LOG_ERROR_SUFFIX = ":SOITOOLKIT.LOG.ERROR";
	private static final String SOITOOLKIT_LOG_PING_SUFFIX = ":SOITOOLKIT.LOG.PING";
	private static final Logger log = LoggerFactory.getLogger(LogStoreService.class);
	private static final Logger storeLog = LoggerFactory.getLogger("se.skl.skltpservices.components.log.services.StoreLog");
	
	private static final JAXBContext context = initContext();

	@Value("${log.mq.instances}")
	private String logInstances;
	private List<Consumer> consumers;      
	@Autowired
	private LogStoreRepository repo;
	@Autowired
	private LogAnalyzerService analyzer;

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
		CamelContext camel = new DefaultCamelContext();

		int seqNo = 0;
		this.consumers = new LinkedList<Consumer>();
		for (String instance : logInstances.split(",")) {
			ActiveMQComponent mq = ActiveMQComponent.activeMQComponent(instance.trim());
			String compName = "activemq-" + seqNo++;
			log.info("Listen on { instance: {}, name: {} }", instance, compName);  
			camel.addComponent(compName, mq);
			consumers.add(createConsumer(camel, compName + SOITOOLKIT_LOG_STORE_SUFFIX));
			consumers.add(createConsumer(camel, compName + SOITOOLKIT_LOG_ERROR_SUFFIX));			
			consumers.add(createConsumer(camel, compName + SOITOOLKIT_LOG_PING_SUFFIX));			
		}
	}
	
	//
	protected void logToFile(LogEvent le) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			StringWriter sw = new StringWriter();
			mapper.writeValue(sw, le.getLogEntry());
			if (le.getLogEntry().getMessageInfo().getLevel() == LogLevelType.ERROR) {
				storeLog.error(sw.toString());
			} else {
				storeLog.info(sw.toString());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}

	//
	private Consumer createConsumer(CamelContext camel, String endpointName) throws Exception {
		final boolean monitor = endpointName.endsWith(SOITOOLKIT_LOG_PING_SUFFIX);
		Consumer consumer = camel.getEndpoint(endpointName).createConsumer(new Processor() {		
			@Override
			public void process(Exchange exchange) throws Exception {
				try {
					LogEvent le = unmarshal((String)exchange.getIn().getBody());
					logToFile(le);
					if (monitor) {
						analyzer.analyze(le);
					} else {
						repo.storeInfoEvent(le);
					}
				} catch (Exception e) {
					log.error("Unable to process log event", e);
					throw e;
				}
			}
		});
		log.info("Listener started { endpoint: {} }", endpointName);  
		consumer.start();
		return consumer;
	}
}
