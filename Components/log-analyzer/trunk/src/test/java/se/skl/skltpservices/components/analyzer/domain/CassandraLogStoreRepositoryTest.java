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
package se.skl.skltpservices.components.analyzer.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.datatype.XMLGregorianCalendar;

import me.prettyprint.cassandra.model.BasicColumnFamilyDefinition;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.ThriftCfDef;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;

import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.soitoolkit.commons.logentry.schema.v1.LogEntryType;
import org.soitoolkit.commons.logentry.schema.v1.LogEntryType.ExtraInfo;
import org.soitoolkit.commons.logentry.schema.v1.LogEvent;
import org.soitoolkit.commons.logentry.schema.v1.LogMessageType;
import org.soitoolkit.commons.logentry.schema.v1.LogRuntimeInfoType;
import org.springframework.beans.factory.annotation.Autowired;

import se.skl.skltpservices.components.analyzer.LogServiceConfig;
import se.skl.skltpservices.components.analyzer.TestSupport;

public class CassandraLogStoreRepositoryTest extends TestSupport {

	@Autowired
	private LogServiceConfig logServiceConfig;

	private static Cluster cluster;
	private String key;
	
	@BeforeClass
	public static void start() throws Exception {
		EmbeddedCassandraServerHelper.startEmbeddedCassandra("embedded-cassandra.yaml");		
		cluster = HFactory.getOrCreateCluster("TestCluster", new CassandraHostConfigurator("localhost:9162"));
	}
	
	@AfterClass
	public static void stop() {
		EmbeddedCassandraServerHelper.stopEmbeddedCassandra();
	}

	
	@Before
	public void init() throws Exception {
		EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();

		List<ColumnFamilyDefinition> list = new LinkedList<ColumnFamilyDefinition>();
		
		ColumnFamilyDefinition eventDf = new BasicColumnFamilyDefinition();
		eventDf.setKeyspaceName("Log");
		eventDf.setName("Event");		
		list.add(new ThriftCfDef(eventDf));
		
		ColumnFamilyDefinition timelineDef = HFactory.createColumnFamilyDefinition("Log", "EventTimeLine");
		timelineDef.setKeyValidationClass("CompositeType(AsciiType, AsciiType, AsciiType, AsciiType)");
		timelineDef.setDefaultValidationClass("CompositeType(AsciiType, UTF8Type)");
		timelineDef.setComparatorType(ComparatorType.TIMEUUIDTYPE);
		list.add(timelineDef);

		ColumnFamilyDefinition metaDf = new BasicColumnFamilyDefinition();
		metaDf.setKeyspaceName("Log");
		metaDf.setName("MetaData");		
		metaDf.setKeyValidationClass("AsciiType");
		metaDf.setDefaultValidationClass("UTF8Type");
		metaDf.setComparatorType(ComparatorType.UTF8TYPE);
		list.add(new ThriftCfDef(metaDf));

		
		ColumnFamilyDefinition counterDef = HFactory.createColumnFamilyDefinition("Log", "WeeklyStats");
		counterDef.setDefaultValidationClass(ComparatorType.COUNTERTYPE.getClassName());
		list.add(counterDef);
		
		cluster.addKeyspace(
				new ThriftKsDef("Log", "org.apache.cassandra.locator.SimpleStrategy", 1, list), true);
		
		this.key = java.util.UUID.randomUUID().toString();
	}
	
	ExtraInfo createExtraInfo(String name, String value) {
		ExtraInfo info = new ExtraInfo();
		info.setName(name);
		info.setValue(value);
		return info;
	}
	
	@Test
	public void create() {
		LogEvent event = mock(LogEvent.class);
		when(event.getLogEntry()).thenReturn(mock(LogEntryType.class));
		when(event.getLogEntry().getMessageInfo()).thenReturn(mock(LogMessageType.class));
		when(event.getLogEntry().getRuntimeInfo()).thenReturn(mock(LogRuntimeInfoType.class));
		when(event.getLogEntry().getRuntimeInfo().getBusinessCorrelationId()).thenReturn(key);
		when(event.getLogEntry().getPayload()).thenReturn("no special");
		when(event.getLogEntry().getMessageInfo().getMessage()).thenReturn("xreq-in");
		
		List<ExtraInfo> extraInfo = new LinkedList<ExtraInfo>();
		
		extraInfo.add(createExtraInfo("cxf_service", "urn:riv:insuranceprocess:healthreporting"));
		extraInfo.add(createExtraInfo("senderid", "sender-id"));
		extraInfo.add(createExtraInfo("receiverid", "receiver-id"));	
		
		when(event.getLogEntry().getExtraInfo()).thenReturn(extraInfo);
		
		XMLGregorianCalendar cal = mock(XMLGregorianCalendar.class);
		when(cal.getYear()).thenReturn(2012);
		when(cal.getMonth()).thenReturn(7);
		when(cal.getDay()).thenReturn(12);
		when(cal.getHour()).thenReturn(14);
		when(cal.getMinute()).thenReturn(30);
		when(cal.getSecond()).thenReturn(0);
		when(cal.getMillisecond()).thenReturn(0);
		
		when(event.getLogEntry().getRuntimeInfo().getTimestamp()).thenReturn(cal);
		final LogStoreRepository repo = logServiceConfig.getLogStoreRepository();
		repo.storeEvent(event);
		assertNotNull(queryEvent());
		assertEquals(1, repo.getContracts().size());
		assertEquals(1, repo.getSenders().size());
		assertEquals(1, repo.getReceivers().size());		
	}
	
		
	@Test
	public void counters() {
		create();
                final CassandraLogStoreRepository repo = (CassandraLogStoreRepository)logServiceConfig.getLogStoreRepository();
		List<Counter> list = repo.getDomainCounters(repo.getCalendar().get(Calendar.WEEK_OF_YEAR));
		assertEquals(1, list.size());
		list = repo.getContractCounters(repo.getCalendar().get(Calendar.WEEK_OF_YEAR));
		assertEquals(1, list.size());		
	}
	
	@Test
	public void reverseIndex() {	
		final long t0 = System.currentTimeMillis();
		Map<String, ReverseEvent> map = new HashMap<String, ReverseEvent>();
		
		Set<UUID> keySet = new HashSet<UUID>();
		
		String payload = "payload";
		int n0 = 0;
		for (int i = 0; i < 100; i++) {
			String key = java.util.UUID.randomUUID().toString() + "." + i;
			long timestamp = System.currentTimeMillis();
			boolean error = false;
			ReverseEvent r = new ReverseEvent(key, payload, error, timestamp);
			r.add("contract", "urn:riv:insuranceprocess:healthreporting");
			r.add("sender", "sender-id");
			r.add("receiver", "receiver-id-" + (i % 10));
			if (r.getReceiver().equals("receiver-id-0")) {
				keySet.add(r.getTimeUUID().getUUID());
				n0++;
			}			
			map.put(key, r);
		}

		// make sure there are no duplicate column names
		assertEquals(n0, keySet.size());
		
		// scramble order
		for (ReverseEvent r : map.values()) {
	                final CassandraLogStoreRepository repo = (CassandraLogStoreRepository)logServiceConfig.getLogStoreRepository();
			repo.updateReverseIndex(r, 3600);
		}
		
		final long t1 = System.currentTimeMillis();

		ReverseEvent r = new ReverseEvent(key, payload, false, System.currentTimeMillis());
		r.add("contract", "urn:riv:insuranceprocess:healthreporting");
		r.add("sender", "sender-id");
		r.add("receiver", "receiver-id-0");

                final CassandraLogStoreRepository repo = (CassandraLogStoreRepository)logServiceConfig.getLogStoreRepository();
		SliceQuery<Composite, UUID, Composite> query = HFactory.createSliceQuery(repo.getKeySpace(), CassandraLogStoreRepository.CS, CassandraLogStoreRepository.TS, CassandraLogStoreRepository.CS);
		query.setColumnFamily(CassandraLogStoreRepository.CF_EVENT_TIMELINE).setKey(r.key());
		ColumnIterator<UUID, Composite> iter = new ColumnIterator<UUID, Composite>(query);
		
		long lastTimestamp = -1;
		while (iter.hasNext()) {
			HColumn<UUID, Composite> col = iter.next();
			Composite value = col.getValue();
			long timestamp = new TimeUUID(col.getName()).getTimestamp();
			
			System.out.println(">>>>>>>>>>>>> " + new Date(timestamp));
			// time order
			assertTrue(timestamp >= lastTimestamp);
			// no strange times
			assertTrue(t0 <= timestamp);
			assertTrue(timestamp <= t1);
			lastTimestamp = timestamp;
			
			assertTrue(keySet.remove(col.getName()));

			@SuppressWarnings("unchecked")
			Object v = value.getComponent(1).getValue(CassandraLogStoreRepository.SS);
			assertTrue(v.equals(payload));
		}

		// verify that all columns has been retrieved
		assertEquals(0, keySet.size());

	}
	
	//
	private HColumn<String, String> queryEvent() {
		ColumnQuery<String, String, String> columnQuery = HFactory.createStringColumnQuery(HFactory.createKeyspace("Log", cluster));
		columnQuery.setColumnFamily("Event");
		columnQuery.setName("date");
		columnQuery.setKey(key);
		QueryResult<HColumn<String, String>> result = columnQuery.execute();
		return result.get();

	}
}
