package se.skl.skltpservices.components.analyzer.domain;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.datatype.XMLGregorianCalendar;

import me.prettyprint.cassandra.model.IndexedSlicesQuery;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HCounterColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.logentry.schema.v1.LogEntryType;
import org.soitoolkit.commons.logentry.schema.v1.LogEntryType.ExtraInfo;
import org.soitoolkit.commons.logentry.schema.v1.LogEvent;
import org.soitoolkit.commons.logentry.schema.v1.LogLevelType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Stores events in an Apache Cassandra database. <p>
 * 
 * This implementation also performs scheduled house-keeping jobs, see {@link #clean()}
 * 
 * @author Peter
 *
 */
@Component
public class CassandraLogStoreRepository implements LogStoreRepository {

	private static final Logger log = LoggerFactory.getLogger(CassandraLogStoreRepository.class);

	private static final String CF_INFO_EVENT = "InfoEvent";
	private static final String CF_WEEKLY_STATS = "WeeklyStats";
	private static final String KEYSPACE = "Log";
	private static final String WAYPOINT_IN = "in.";
	private static final String WAYPOINT_OUT = "out.";
	private static final String WAYPOINT_ERROR = "err.";

	private static Map<String, String> columnNames = new HashMap<String, String>();

	static {
		// info {key, actual column name}
		// once
		columnNames.put("in.cxf_service", "contract");
		columnNames.put("in.senderid", "sender");
		columnNames.put("in.receiverid", "receiver");
		
		// in
		columnNames.put("in.timestamp", "in_timestamp");
		columnNames.put("in.payload", "in_payload");
		columnNames.put("in.rivversion", "in_riv_version");
		
		// out
		columnNames.put("out.timestamp", "out_timestamp");
		columnNames.put("out.payload", "out_payload");
		columnNames.put("out.rivversion", "out_riv_version");
		columnNames.put("out.endpoint.time", "endpoint_time");
		columnNames.put("out.endpoint.url", "endpoint_url");

		// errors
		columnNames.put("err.cxf_service", "contract");
		columnNames.put("err.senderid", "sender");
		columnNames.put("err.receiverid", "receiver");
		columnNames.put("err.timestamp", "err_timestamp");
		columnNames.put("err.payload", "err_payload");
		columnNames.put("err.message", "err_message");
		columnNames.put("err.sessionStatus", "err_flag");
		columnNames.put("err.sessionErrorDescription", "err_description");
		columnNames.put("err.sessionErrorTechnicalDescription", "err_detail");
	}

	// config
    @Value("${log.store.instances}")
    private String logStoreInstances;

    @Value("${log.store.payloadTTL}")
    private int payloadTTL;
    
    @Value("${log.store.timeout}")
    private int timeout;

    @Value("${log.store.metaTTL}")
    private int metaTTL;
    
    @Value("${log.store.weeklyCounterTTL}")
    private int weeklyCounterTTL;
    
    @Value("${log.store.clusterName}")
    private String clusterName;

    @Value("${log.store.counterDomains}")
    private String counterDomains;
    
    private Map<String, List<String>> domainMap = new HashMap<String, List<String>>();
    
	// hector stuff
	private Keyspace keyspace;
	final static StringSerializer SS = StringSerializer.get();
	final static LongSerializer LS = LongSerializer.get();


	//
	public CassandraLogStoreRepository() {
		
	}
	
	// lazy connection.
	private Keyspace getKeySpace() {
		if (this.keyspace == null) {
			synchronized (this) {
				if (this.keyspace == null) {
					CassandraHostConfigurator cfg = new CassandraHostConfigurator(logStoreInstances);
					cfg.setMaxActive(5);
					cfg.setCassandraThriftSocketTimeout(timeout);
					cfg.setMaxWaitTimeWhenExhausted(10000);
					Cluster cluster = HFactory.getOrCreateCluster(clusterName, cfg);
					this.keyspace = HFactory.createKeyspace(KEYSPACE, cluster);
				}
			}
		}
		return this.keyspace;
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
		LogLevelType level = infoEvent.getLogEntry().getMessageInfo().getLevel();
		if (log.isDebugEnabled()) {
			String key = entry.getRuntimeInfo().getBusinessCorrelationId();
			log.info("Store event { key: {}, waypoint: {}, level: {} }", new Object[] { key, waypoint, level });
		}
		boolean error = LogLevelType.ERROR.equals(level);
		
		if ("xreq-in".equals(waypoint)) {
			waypoint = WAYPOINT_IN;
		} else if (error) {
			waypoint = WAYPOINT_ERROR;
		} else {
			waypoint = WAYPOINT_OUT;			
		}

		storeEvent(entry, waypoint, error);
	}

	//
	protected void storeEvent(LogEntryType entry, String waypoint, boolean error) {

		final String columnFamily = CF_INFO_EVENT;

		Mutator<String> m = HFactory.createMutator(getKeySpace(), SS);
		String key = entry.getRuntimeInfo().getBusinessCorrelationId();

		if (key == null || key.length() == 0) {
			key = UUID.randomUUID().toString();
			log.warn("Missing business correlationId for event, new  key { uuid: {} } created.", key);
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setMinimalDaysInFirstWeek(4);

		// add day as YYYY-MM-DD (indexed column)
		if (WAYPOINT_IN.equals(waypoint) || error) {
			m.addInsertion(key, columnFamily, toStringColumn("date", toDate(calendar)));
		}

		m.addInsertion(key, columnFamily,  toLongColumn(columnNames.get(waypoint + "timestamp"), toTimestamp(entry.getRuntimeInfo().getTimestamp())));        

		if (error) {
			add(m, columnFamily, key, toStringColumn(columnNames.get(waypoint + "message"), entry.getMessageInfo().getMessage()));			
		}
		
		for (ExtraInfo info : entry.getExtraInfo()) {
			if (log.isDebugEnabled()) {
				log.debug("ExtraInfo { name: {}, value: {} }", info.getName(), info.getValue());
			}
			String name = columnNames.get(waypoint + info.getName());
			if (name != null) {
				if ("contract".equals(name)) {
					updateStatistics(m, info.getValue(), calendar, error);
				}
				add(m, columnFamily, key, toStringColumn(name, info.getValue()));
			}
		}
		add(m, columnFamily, key, toStringColumn(columnNames.get(waypoint + "payload"), entry.getPayload(), toSeconds(payloadTTL)));
		m.execute();
	}

	//
	private static String toDate(Calendar calendar) {
		return String.format("%tF", calendar.getTime());
	}
	
	//
	private void updateStatistics(Mutator<String> m, String contract, Calendar calendar, boolean error) {
		if (contract == null) {
			contract = "Unknown contract";
		}
		String week = String.format("Week%d%s", calendar.get(Calendar.WEEK_OF_YEAR),  (error) ? "_Error" : "");
		HCounterColumn<String> column = HFactory.createCounterColumn(week, 1L, SS);
		column.setTtl(weeklyCounterTTL);
		m.addCounter("c-" + contract, CF_WEEKLY_STATS, column);
		for (String domain : getDomains(contract)) {
			column = HFactory.createCounterColumn(week, 1L, SS);
			column.setTtl(weeklyCounterTTL);
			m.addCounter("d-" + domain, CF_WEEKLY_STATS, column);
		}
	}
	
	/**
	 * Returns domains for a contract.
	 * 
	 * @param contract the contract.
	 * @return the list of domains, empty if none exists.
	 */
	private List<String> getDomains(String contract) {
		List<String> list = domainMap.get(contract);
		if (list == null) {
			list = new LinkedList<String>();
			domainMap.put(contract, list);
			for (String domain : counterDomains.split(",")) {
				domain = domain.trim();
				if (contract.startsWith(domain)) {
					list.add(domain);
				}
			}
			if (list.isEmpty()) {
				list.add("Other domains");
			}
		}
		return list;
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

	/**
	 * Housekeeping job, cleaning up database.
	 */
	// Every night at 1AM
	//@Scheduled(fixedRate=30000)
	@Scheduled(cron = "0 0 1 * * ?")
	public void clean() {
		log.info("LogStore clean-up started!");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -(metaTTL));
		// check ten days back...
		int sum = 0;
		for (int i = 0; i < 10; i++) {
			for (int n; (n = removeDateBatch(toDate(cal), 1000)) > 0; ) {
				sum += n;
			}
			cal.add(Calendar.DATE, -1);
		}
		log.info("In total has { count: {} } rows been deleted", sum);
		log.info("LogStore clean-up ready!");
	}
	
	/**
	 * Removes all keys of a specific date.
	 * @param date the date (YYYY-MM-DD)
	 * @return number of removed rows.
	 */
	private int removeDateBatch(String date, int batchsz) {
		log.info("Clean-up{ date: {} }", date);
		IndexedSlicesQuery<String, String, String> indexedSlicesQuery = HFactory.createIndexedSlicesQuery(getKeySpace(), SS, SS, SS);
		indexedSlicesQuery.addEqualsExpression("date", date);
		indexedSlicesQuery.setReturnKeysOnly();
		indexedSlicesQuery.setColumnFamily(CF_INFO_EVENT);
		indexedSlicesQuery.setStartKey("");
		indexedSlicesQuery.setRowCount(batchsz);
		Mutator<String> m = HFactory.createMutator(getKeySpace(), SS);

		QueryResult<OrderedRows<String, String, String>> result = indexedSlicesQuery.execute();
		OrderedRows<String, String, String> rows = result.get();
		log.info("Rows { count: {} }", rows.getCount());
		for (Row<String, String, String> row : rows.getList()) {
			m.addDeletion(row.getKey(), CF_INFO_EVENT);
		}
		
		if (rows.getCount() > 0) {
			m.execute();
		}
				
		return rows.getCount();
	}
}
