package se.skl.skltpservices.components.analyzer.domain;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HCounterColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.logentry.schema.v1.LogEntryType;
import org.soitoolkit.commons.logentry.schema.v1.LogEntryType.ExtraInfo;
import org.soitoolkit.commons.logentry.schema.v1.LogEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Stores events in an Apache Cassandra database.
 * 
 * @author Peter
 *
 */
@Component
public class CassandraLogStoreRepository implements LogStoreRepository {

	private static final Logger log = LoggerFactory.getLogger(CassandraLogStoreRepository.class);

	private static final String CF_INFO_EVENT = "InfoEvent";
	private static final String CF_ERROR_EVENT = "ErrorEvent";
	private static final String CF_WEEKLY_STATS = "WeeklyStats";
	private static final String KEYSPACE = "Log";
	private static final String WAYPOINT_IN = "in.";
	private static final String WAYPOINT_OUT = "out.";
	private static final String WAYPOINT_ERR = "err.";

	private static Map<String, String> columnNames = new HashMap<String, String>();

	static {
		// info {key, actual column name}
		// once
		columnNames.put("in.cxf_service", "contract");
		columnNames.put("in.senderid", "sender");
		columnNames.put("in.receiverid", "receiver");
		columnNames.put("out.sessionStatus", "error");
		columnNames.put("out.sessionErrorDescription", "error_message");
		columnNames.put("out.sessionErrorTechnicalDescription", "error_detail");
		// in
		columnNames.put("in.timestamp", "in_timestamp");
		columnNames.put("in.payload", "in_payload");
		columnNames.put("in.rivversion", "in_riv_version");
		// out
		columnNames.put("out.timestamp", "out_timestamp");
		columnNames.put("out.rivversion", "out_riv_version");
		columnNames.put("out.payload", "out_payload");

		// error  {key, actual column name}
		columnNames.put("err.cxf_service", "contract");
		columnNames.put("err.senderid", "sender");
		columnNames.put("err.receiverid", "receiver");
		columnNames.put("err.timestamp", "timestamp");
		columnNames.put("err.sessionErrorDescription", "message");
		columnNames.put("err.sessionErrorDescription", "descriptioon");
		columnNames.put("err.sessionErrorTechnicalDescription", "detail");
		columnNames.put("err.payload", "payload");
		columnNames.put("err.rivversion", "riv_version");
	}

	// config
    @Value("${log.store.instances}")
    private String logStoreInstances;

    @Value("${log.store.payloadTTL}")
    private int payloadTTL;
    
    @Value("${log.store.infoTTL}")
    private int infoTTL;
    
    @Value("${log.store.errorTTL}")
    private int errorTTL;
    
    @Value("${log.store.weeklyCounterTTL}")
    private int weeklyCounterTTL;
    
    @Value("${log.store.clusterName}")
    private String clusterName;
    
    

	// hector stuff
	private Keyspace keyspace;
	final static StringSerializer SS = StringSerializer.get();
	final static LongSerializer LS = LongSerializer.get();


	//
	public CassandraLogStoreRepository() {
		
	}
	
	// connects.
	@Override
	public void connect() {
		CassandraHostConfigurator cfg = new CassandraHostConfigurator(logStoreInstances);
		cfg.setMaxActive(5);
		cfg.setCassandraThriftSocketTimeout(3000);
		cfg.setMaxWaitTimeWhenExhausted(4000);
		Cluster cluster = HFactory.getOrCreateCluster(clusterName, cfg);
		this.keyspace = HFactory.createKeyspace(KEYSPACE, cluster);		
	}

	/**
	 * Returns TTL i seconds.
	 * 
	 * @param days TTL in days.
	 * @return TTL in seconds.
	 */
	private static int toSeconds(int days) {
		return (days * 24 * 3600);
	}

	@Override
	public void storeInfoEvent(LogEvent infoEvent) {
		LogEntryType entry = infoEvent.getLogEntry();

		String waypoint = entry.getMessageInfo().getMessage();		
		if ("xreq-in".equals(waypoint)) {
			waypoint = WAYPOINT_IN;
		} else if ("xresp-out".equals(waypoint)) {
			waypoint = WAYPOINT_OUT;
		} else {
			log.warn("Unexpected waypoint {} (event ignored)", waypoint);
			return;
		}

		storeEvent(entry, waypoint);
	}

	@Override
	public void storeErrorEvent(LogEvent errorEvent) {
		storeEvent(errorEvent.getLogEntry(), WAYPOINT_ERR);
	}

	//
	protected void storeEvent(LogEntryType entry, String waypoint) {

		boolean error = WAYPOINT_ERR.equals(waypoint);

		final String columnFamily = (error) ? CF_ERROR_EVENT : CF_INFO_EVENT;

		Mutator<String> m = HFactory.createMutator(this.keyspace, SS);
		String key = entry.getRuntimeInfo().getBusinessCorrelationId();

		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setMinimalDaysInFirstWeek(4);

		// add day as YYYY-MM-DD (indexed column)
		if (WAYPOINT_IN.equals(waypoint)) {
			m.addInsertion(key, columnFamily, toStringColumn("date", String.format("%tF", calendar.getTime())));
		}

		m.addInsertion(key, columnFamily,  toLongColumn(columnNames.get(waypoint + "timestamp"), toTimestamp(entry.getRuntimeInfo().getTimestamp())));        

		for (ExtraInfo info : entry.getExtraInfo()) {
			String name = columnNames.get(waypoint + info.getName());
			if (name != null) {
				if ("contract".equals(name)) {
					updateStatistics(m, info.getValue(), calendar, error);
				}
				add(m, columnFamily, key, toStringColumn(name, info.getValue(), toSeconds((error) ? errorTTL : infoTTL)));
			}
		}

		add(m, columnFamily, key, toStringColumn(columnNames.get(waypoint + "payload"), entry.getPayload(), toSeconds(payloadTTL)));
		
		m.execute();
	}

	//
	private void updateStatistics(Mutator<String> m, String contract, Calendar calendar, boolean error) {
		if (contract == null) {
			contract = "Unknown";
		}
		String week = String.format("Week%d%s", calendar.get(Calendar.WEEK_OF_YEAR),  (error) ? "_Error" : "");
		HCounterColumn<String> column = HFactory.createCounterColumn(week, 1L, SS);
		column.setTtl(weeklyCounterTTL);
		m.addCounter(contract, CF_WEEKLY_STATS, column);
	}

	//
	private static HColumn<String, String> toStringColumn(String name, String value) {
		if (value == null || value.length() == 0) {
			return null;
		}
		HColumn<String, String> column = HFactory.createColumn(name, value, SS, SS);
		return column;
	}

	//
	private static HColumn<String, String> toStringColumn(String name, String value, int ttl) {
		HColumn<String, String> column = toStringColumn(name, value);
		if (column != null) {
			column.setTtl(ttl);
		}
		return column;
	}

	//
	private static HColumn<String, Long> toLongColumn(String name, long value) {
		HColumn<String, Long> column = HFactory.createColumn(name, value, SS, LS);
		return column;
	}

	//
	private static void add(Mutator<String> mutator, String columnFamily, String key, HColumn<String, String> column) {
		if (column != null) {
			mutator.addInsertion(key, columnFamily, column);
		}
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
