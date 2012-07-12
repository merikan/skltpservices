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

import org.soitoolkit.commons.logentry.schema.v1.LogEntryType;
import org.soitoolkit.commons.logentry.schema.v1.LogEntryType.ExtraInfo;
import org.soitoolkit.commons.logentry.schema.v1.LogEvent;

/**
 * Stores events in an apache cassandra database.
 * 
 * @author Peter
 *
 */
public class CassandraLogStoreRepository implements LogStoreRepository {

	private static final String CF_INFO_EVENT = "InfoEvent";
	private static final String CF_ERROR_EVENT = "ErrorEvent";
	private static final String CF_WEEKLY_STATS = "WeeklyStats";
	private static final String CLUSTER = "Test Cluster";
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
		columnNames.put("in.timestamp", "in.timestamp");
		columnNames.put("in.payload", "in.payload");
		columnNames.put("in.rivversion", "in.riv_version");
		// out
		columnNames.put("out.timestamp", "out.timestamp");
		columnNames.put("out.rivversion", "out.riv_version");
		columnNames.put("out.payload", "out.payload");

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

	// hector stuff
	private Keyspace keyspace;
	final static StringSerializer SS = StringSerializer.get();
	final static LongSerializer LS = LongSerializer.get();


	//
	public CassandraLogStoreRepository() {
		CassandraHostConfigurator cfg = new CassandraHostConfigurator("localhost:9160");
		cfg.setMaxActive(5);
		cfg.setCassandraThriftSocketTimeout(3000);
		cfg.setMaxWaitTimeWhenExhausted(4000);
		Cluster cluster = HFactory.getOrCreateCluster(CLUSTER, cfg);
		this.keyspace = HFactory.createKeyspace(KEYSPACE, cluster);
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

		long ttl = 300;

		// add day as YYYY-MM-DD (indexed column)
		if (WAYPOINT_IN.equals(waypoint)) {
			m.addInsertion(key, columnFamily, toStringColumn("day", String.format("%tF", calendar.getTime()), ttl));
		}

		m.addInsertion(key, columnFamily,  toLongColumn(columnNames.get(waypoint + "timestamp"), toTimestamp(entry.getRuntimeInfo().getTimestamp()), ttl));        

		for (ExtraInfo info : entry.getExtraInfo()) {
			String name = columnNames.get(waypoint + info.getName());
			System.out.printf("add (%s=%s)\n", name, info.getValue());
			if (name != null) {
				if ("contract".equals(name)) {
					updateStatistics(m, info.getValue(), calendar, error);
				}
				add(m, columnFamily, key, toStringColumn(name, info.getValue(), ttl));
			}
		}

		add(m, columnFamily, key, toStringColumn(columnNames.get(waypoint + "payload"), entry.getPayload(), ttl));
		
		m.execute();
	}

	//
	private static void updateStatistics(Mutator<String> m, String contract, Calendar calendar, boolean error) {
		if (contract == null) {
			contract = "Unknown";
		}
		String week = String.format("Week%d_%s", (error) ? "Error" : "", calendar.get(Calendar.WEEK_OF_YEAR));
		HCounterColumn<String> column = HFactory.createCounterColumn(week, 1L, SS);
		m.addCounter(contract, CF_WEEKLY_STATS, column);
	}

	//
	private static HColumn<String, String> toStringColumn(String name, String value, long ttl) {
		System.out.printf("setting %s=%s\n", name, value);
		if (value == null || value.length() == 0) {
			return null;
		}
		HColumn<String, String> column = HFactory.createColumn(name, value, ttl, SS, SS);
		return column;
	}

	//
	private static HColumn<String, Long> toLongColumn(String name, long value, long ttl) {
		System.out.printf("setting %s=%d\n", name, value);
		HColumn<String, Long> column = HFactory.createColumn(name, value, ttl, SS, LS);
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
