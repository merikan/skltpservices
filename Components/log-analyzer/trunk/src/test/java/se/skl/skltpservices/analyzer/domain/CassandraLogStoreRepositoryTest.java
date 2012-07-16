package se.skl.skltpservices.analyzer.domain;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.UUID;

import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.soitoolkit.commons.logentry.schema.v1.LogEntryType;
import org.soitoolkit.commons.logentry.schema.v1.LogEntryType.ExtraInfo;
import org.soitoolkit.commons.logentry.schema.v1.LogEvent;
import org.soitoolkit.commons.logentry.schema.v1.LogMessageType;
import org.soitoolkit.commons.logentry.schema.v1.LogRuntimeInfoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.skl.skltpservices.components.analyzer.domain.CassandraLogStoreRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:applicationContext.xml")
public class CassandraLogStoreRepositoryTest {

	@Autowired
	private CassandraLogStoreRepository repo;
	
	@Before
	public void init() {
		this.repo = new CassandraLogStoreRepository();
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
