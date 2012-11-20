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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.xml.datatype.XMLGregorianCalendar;

import me.prettyprint.cassandra.model.BasicColumnDefinition;
import me.prettyprint.cassandra.model.BasicColumnFamilyDefinition;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.ThriftCfDef;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ColumnIndexType;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;

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

import se.skl.skltpservices.components.analyzer.TestSupport;

public class CassandraLogStoreRepositoryTest extends TestSupport {

	@Autowired
	private CassandraLogStoreRepository repo;

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
		eventDf.setName("InfoEvent");
		
		// Date index
		BasicColumnDefinition dateDf = new BasicColumnDefinition();
		dateDf.setName(StringSerializer.get().toByteBuffer("date"));
		dateDf.setIndexName("date_index");
		dateDf.setIndexType(ColumnIndexType.KEYS);
		dateDf.setValidationClass(ComparatorType.ASCIITYPE.getClassName());
		eventDf.addColumnDefinition(dateDf);
		
		list.add(new ThriftCfDef(eventDf));
		
		ColumnFamilyDefinition counterDef = HFactory.createColumnFamilyDefinition("Log", "WeeklyStats");
		counterDef.setDefaultValidationClass(ComparatorType.COUNTERTYPE.getClassName());
		list.add(counterDef);
		
		cluster.addKeyspace(
				new ThriftKsDef("Log", "org.apache.cassandra.locator.SimpleStrategy", 1, list), true);
		
		this.key = UUID.randomUUID().toString();
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
		ExtraInfo extraInfo = new ExtraInfo();
		extraInfo.setName("cxf_service");
		extraInfo.setValue("urn:riv:domain:subdomain:method");
		when(event.getLogEntry().getExtraInfo()).thenReturn(Collections.singletonList(extraInfo));
		
		XMLGregorianCalendar cal = mock(XMLGregorianCalendar.class);
		when(cal.getYear()).thenReturn(2012);
		when(cal.getMonth()).thenReturn(7);
		when(cal.getDay()).thenReturn(12);
		when(cal.getHour()).thenReturn(14);
		when(cal.getMinute()).thenReturn(30);
		when(cal.getSecond()).thenReturn(0);
		when(cal.getMillisecond()).thenReturn(0);
		
		when(event.getLogEntry().getRuntimeInfo().getTimestamp()).thenReturn(cal);
		repo.storeInfoEvent(event);
		assertNotNull(queryEvent());
	}
	
	@Test
	public void clean() {
		create();
		((CassandraLogStoreRepository)repo).clean();
		assertNull(queryEvent());
	}
	
	//
	private HColumn<String, String> queryEvent() {
		ColumnQuery<String, String, String> columnQuery = HFactory.createStringColumnQuery(HFactory.createKeyspace("Log", cluster));
		columnQuery.setColumnFamily("InfoEvent");
		columnQuery.setName("date");
		columnQuery.setKey(key);
		QueryResult<HColumn<String, String>> result = columnQuery.execute();
		return result.get();

	}
}
