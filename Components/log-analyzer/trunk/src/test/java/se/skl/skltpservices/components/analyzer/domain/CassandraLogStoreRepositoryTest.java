package se.skl.skltpservices.components.analyzer.domain;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.xml.datatype.XMLGregorianCalendar;

import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.factory.HFactory;

import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.After;
import org.junit.Before;
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
	
	
	@Before
	public void init() throws Exception {
		EmbeddedCassandraServerHelper.startEmbeddedCassandra("embedded-cassandra.yaml");
		Cluster cluster = HFactory.getOrCreateCluster("SKLTP", new CassandraHostConfigurator("localhost:9161"));
		List<ColumnFamilyDefinition> list = new LinkedList<ColumnFamilyDefinition>();
		list.add(HFactory.createColumnFamilyDefinition("Log", "InfoEvent"));
		ColumnFamilyDefinition cdef = HFactory.createColumnFamilyDefinition("Log", "WeeklyStats");
		cdef.setDefaultValidationClass(ComparatorType.COUNTERTYPE.getClassName());
		list.add(cdef);
		cluster.addKeyspace(
				new ThriftKsDef("Log", "org.apache.cassandra.locator.SimpleStrategy", 1, list));
	}
	
	@After
	public void clean() {
		EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
	}
	
	@Test
	public void infoEvent() {
		LogEvent event = mock(LogEvent.class);
		when(event.getLogEntry()).thenReturn(mock(LogEntryType.class));
		when(event.getLogEntry().getMessageInfo()).thenReturn(mock(LogMessageType.class));
		when(event.getLogEntry().getRuntimeInfo()).thenReturn(mock(LogRuntimeInfoType.class));
		when(event.getLogEntry().getRuntimeInfo().getBusinessCorrelationId()).thenReturn(UUID.randomUUID().toString());
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
	}
}
