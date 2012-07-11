package se.skl.skltpservices.components.log.services;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HCounterColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.MutationResult;
import me.prettyprint.hector.api.mutation.Mutator;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.util.ByteArrayInputStream;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultCamelContext;
import org.soitoolkit.commons.logentry.schema.v1.LogEntryType;
import org.soitoolkit.commons.logentry.schema.v1.LogEntryType.ExtraInfo;
import org.soitoolkit.commons.logentry.schema.v1.LogEvent;
import org.springframework.stereotype.Service;

@Service
public class LogStoreService implements LogStore {

	//static Logger log = Logger.getLogger(StoreService.class);
	private static final JAXBContext context = initContext();

	private static final String CF_EVENT = "Event";
	private static final String CF_STATS = "Stats";
	private static final String CLUSTER = "Test Cluster";
	private static final String KEYSPACE = "Log";
	
	private static Map<String, String> validNames = new HashMap<String, String>();
	
	static {
		// once
		validNames.put("in.cxf_service", "contract");
		validNames.put("in.senderid", "sender");
		validNames.put("in.receiverid", "receiver");
		validNames.put("out.sessionStatus", "error");
		validNames.put("out.sessionErrorDescription", "error_message");
		validNames.put("out.sessionErrorTechnicalDescription", "error_detail");
		//
		validNames.put("in.timestamp", "in.timestamp");
		validNames.put("in.payload", "in.payload");
		validNames.put("in.rivversion", "in.riv_version");
		//
		validNames.put("out.timestamp", "out.timestamp");
		validNames.put("out.rivversion", "out.riv_version");
		validNames.put("out.payload", "out.payload");
	}
	
	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance(LogEvent.class);
		} catch (JAXBException e) {
			throw new RuntimeException("Unable to create JAXBContext for LogEvent", e);
		}
	}
	
	// hector stuff
    private Keyspace keyspace;
    final static StringSerializer ss = StringSerializer.get();
    final static LongSerializer ls = LongSerializer.get();
    

    //
	public LogStoreService() {
		CassandraHostConfigurator cfg = new CassandraHostConfigurator("localhost:9160");
		cfg.setMaxActive(5);
		cfg.setCassandraThriftSocketTimeout(3000);
		cfg.setMaxWaitTimeWhenExhausted(4000);
		Cluster cluster = HFactory.getOrCreateCluster(CLUSTER, cfg);
		this.keyspace = HFactory.createKeyspace(KEYSPACE, cluster);
	}

	//
	private static LogEvent unmarshal(final String msg) {
		try {
			return (LogEvent)context.createUnmarshaller().unmarshal(new ByteArrayInputStream(msg.getBytes()));
		} catch (JAXBException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	//
	private void updateStatistics(Mutator<String> m, String contract, Calendar calendar) {
		if (contract == null) {
			contract = "Unidentified";
		}
		String week = String.format("Week%d", calendar.get(Calendar.WEEK_OF_YEAR));
		HCounterColumn<String> column = HFactory.createCounterColumn(week, 1L, ss);
		m.addCounter(contract, CF_STATS, column);
	}

	//
    private HColumn<String, String> toStringColumn(String name, String value, int ttl) {
    	if (value == null || value.length() == 0) {
    		return null;
    	}
        HColumn<String, String> column = HFactory.createColumn(name, value, ttl, ss, ss);
        return column;
    }

    //
    private HColumn<String, Long> toLongColumn(String name, long value, int ttl) {
    	HColumn<String, Long> column = HFactory.createColumn(name, value, ttl, ss, ls);
        return column;
    }
    
    //
    private void add(Mutator<String> mutator, String key, HColumn<String, String> column) {
    	if (column != null) {
    		mutator.addInsertion(key, CF_EVENT, column);
    	}
    }

    
	@Override
	public void error(Exchange exchange) {
		
		LogEvent le = unmarshal((String)exchange.getIn().getBody());
		System.out.printf("error: \"%s\" (%s) - %s\n", 
				le.getLogEntry().getRuntimeInfo().getBusinessCorrelationId(), 
				le.getLogEntry().getMessageInfo().getMessage(),
				le.getLogEntry().getPayload());
	}

	@Override
	public void info(Exchange exchange) {
		LogEvent le = unmarshal((String)exchange.getIn().getBody());
	
		LogEntryType entry = le.getLogEntry();
		

		Mutator<String> m = HFactory.createMutator(keyspace, ss);
		String key = entry.getRuntimeInfo().getBusinessCorrelationId();
       	
		String waypoint = entry.getMessageInfo().getMessage();
		if ("xreq-in".equals(waypoint)) {
			waypoint = "in";
		} else if ("xresp-out".equals(waypoint)) {
			waypoint = "out";
		} else {
			return;
		}
		
		int ttl = 86400;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setMinimalDaysInFirstWeek(4);
		
		m.addInsertion(key, CF_EVENT, toStringColumn("day", String.format("%tF", calendar.getTime()), ttl));
        m.addInsertion(key, CF_EVENT,  toLongColumn(waypoint + ".timestamp", toTimestamp(entry.getRuntimeInfo().getTimestamp()), ttl));        
        
        for (ExtraInfo info : entry.getExtraInfo()) {
        	String name = validNames.get(waypoint + "." + info.getName());
        	if (name != null) {
        		if ("contract".equals(name)) {
        			updateStatistics(m, info.getValue(), calendar);
        		}
        		add(m, key, toStringColumn(name, info.getValue(), ttl));
        	}
        }

        add(m, key, toStringColumn(waypoint + ".payload", entry.getPayload(), ttl));
        
        MutationResult rc = m.execute();
        
		System.out.printf("stored in (%s, %d): \"%s\" (%s) - %s\n", 
				rc.getHostUsed().getName(),
				rc.getExecutionTimeMicro(),
				le.getLogEntry().getRuntimeInfo().getBusinessCorrelationId(), 
				le.getLogEntry().getMessageInfo().getMessage(),
				le.getLogEntry().getPayload());

	}

	public static void main(final String[] args) throws Exception {
		String curDir = System.getProperty("user.dir");
		System.out.println(curDir);

		Calendar calendar = Calendar.getInstance();

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
	
	//
	private static long toTimestamp(final XMLGregorianCalendar timestamp) {	
		final Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, timestamp.getHour());
		c.set(Calendar.MINUTE, timestamp.getMinute());
		c.set(Calendar.SECOND, timestamp.getSecond());
		c.set(Calendar.MILLISECOND, timestamp.getMillisecond());
		c.set(Calendar.YEAR, timestamp.getYear());
		c.set(Calendar.MONTH, timestamp.getMonth() - 1);
		c.set(Calendar.DAY_OF_MONTH, timestamp.getDay());
		return c.getTime().getTime();
	}
}
