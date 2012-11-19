package se.skl.skltpservices.components.analyzer.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.xml.datatype.XMLGregorianCalendar;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.soitoolkit.commons.logentry.schema.v1.LogEntryType;
import org.soitoolkit.commons.logentry.schema.v1.LogEntryType.ExtraInfo;
import org.soitoolkit.commons.logentry.schema.v1.LogEvent;
import org.soitoolkit.commons.logentry.schema.v1.LogMessageType;
import org.soitoolkit.commons.logentry.schema.v1.LogRuntimeInfoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import se.skl.skltpservices.components.analyzer.TestSupport;
import se.skl.skltpservices.components.analyzer.application.RuntimeStatus;
import se.skl.skltpservices.components.analyzer.application.Service;
import se.skl.skltpservices.components.analyzer.application.ServiceGroup;

public class LogAnalyzerServiceTest extends TestSupport {

    private static Event timedOutEvent;
    private static Event successEvent;
    @Autowired
    private LogAnalyzerService analyzerService;
	// config
    @Value("${analyze.timeout}")
    private int timeout;

    @Before
    public void setUp() {
        timedOutEvent = new Event();
        timedOutEvent.setCorrelationId("Id1");
        timedOutEvent.setStatus(State.TIMEOUT);
        
        successEvent = new Event();
        successEvent.setCorrelationId("Id2");
        successEvent.setStatus(State.SUCCESS);
        
    }
    
    @Test
    public void testBusinessLogicForRuntimeStatus() {
        List<Event> emptyTimeline = Collections.emptyList();
        Assert.assertEquals(RuntimeStatus.DOWN, analyzerService.calcRuntimeStatus(emptyTimeline));
        Assert.assertEquals(RuntimeStatus.DOWN, analyzerService.calcRuntimeStatus(null));
        Assert.assertEquals(RuntimeStatus.UP, checkProducer(successEvent)); 
        Assert.assertEquals(RuntimeStatus.DOWN, checkProducer(timedOutEvent)); 
        Assert.assertEquals(RuntimeStatus.UP, checkProducer(successEvent, timedOutEvent)); 
        Assert.assertEquals(RuntimeStatus.DOWN, checkProducer(timedOutEvent, timedOutEvent)); 
        Assert.assertEquals(RuntimeStatus.DOWN, checkProducer(timedOutEvent, timedOutEvent)); 
        Assert.assertEquals(RuntimeStatus.UP, checkProducer(successEvent, successEvent)); 
        Assert.assertEquals(RuntimeStatus.UP, checkProducer(successEvent, successEvent, successEvent)); 
        Assert.assertEquals(RuntimeStatus.UP, checkProducer(successEvent, successEvent, timedOutEvent)); 
        Assert.assertEquals(RuntimeStatus.UP, checkProducer(successEvent, timedOutEvent, successEvent)); 
        Assert.assertEquals(RuntimeStatus.UP, checkProducer(successEvent, timedOutEvent, timedOutEvent)); 
        Assert.assertEquals(RuntimeStatus.UP, checkProducer(timedOutEvent, successEvent, successEvent)); 
        Assert.assertEquals(RuntimeStatus.DOWN, checkProducer(timedOutEvent, successEvent, timedOutEvent)); 
        Assert.assertEquals(RuntimeStatus.DOWN, checkProducer(timedOutEvent, timedOutEvent, successEvent)); 
        Assert.assertEquals(RuntimeStatus.DOWN, checkProducer(timedOutEvent, timedOutEvent, timedOutEvent)); 
    }

    private RuntimeStatus checkProducer(Event...events) {
        return analyzerService.calcRuntimeStatus(Arrays.asList(events));
    }
    
    private ExtraInfo ci(String name, String value) {
		ExtraInfo extraInfo = new ExtraInfo();
		extraInfo.setName(name);
		extraInfo.setValue(value);
    	return extraInfo;
    }
    
    private LogEvent createLogEvent(String logType) {
		String key = UUID.randomUUID().toString();
		LogEvent event = mock(LogEvent.class);
		when(event.getLogEntry()).thenReturn(mock(LogEntryType.class));
		when(event.getLogEntry().getMessageInfo()).thenReturn(mock(LogMessageType.class));
		when(event.getLogEntry().getRuntimeInfo()).thenReturn(mock(LogRuntimeInfoType.class));
		when(event.getLogEntry().getRuntimeInfo().getBusinessCorrelationId()).thenReturn(key);
		when(event.getLogEntry().getPayload()).thenReturn("no special");
		when(event.getLogEntry().getMessageInfo().getMessage()).thenReturn(logType);
		ExtraInfo extraInfo = new ExtraInfo();
		extraInfo.setName("cxf_service");
		extraInfo.setValue("urn:riv:domain:subdomain:method");
		List<ExtraInfo> list = new LinkedList<ExtraInfo>();
		list.add(ci("endpoint-url", "https://localhost:8080"));
		list.add(ci("domain", "domain"));
		list.add(ci("sub-domain", "sub-domain"));
		list.add(ci("system-name", "system-name"));
		
		when(event.getLogEntry().getExtraInfo()).thenReturn(list);
		
		XMLGregorianCalendar cal = mock(XMLGregorianCalendar.class);
		when(cal.getYear()).thenReturn(2012);
		when(cal.getMonth()).thenReturn(7);
		when(cal.getDay()).thenReturn(12);
		when(cal.getHour()).thenReturn(14);
		when(cal.getMinute()).thenReturn(30);
		when(cal.getSecond()).thenReturn(0);
		when(cal.getMillisecond()).thenReturn(0);
		
		when(event.getLogEntry().getRuntimeInfo().getTimestamp()).thenReturn(cal);
		
		return event;
    }

    @Test
	public void analyzeNormal() {
    	LogEvent event = createLogEvent("req-out");
		
		analyzerService.analyze(event);
		
		when(event.getLogEntry().getMessageInfo().getMessage()).thenReturn("resp-in");
		
		analyzerService.analyze(event);
				
		for (ServiceGroup sg : analyzerService.getCurrentStatusFromAllProducers().getGroups()) {
			Assert.assertEquals("domain", sg.getName());
			Assert.assertEquals("sub-domain", sg.getDescription());
			for (Service s : sg.getServices()) {
				Assert.assertEquals(RuntimeStatus.UP, s.getStatus());
				Assert.assertEquals("https://localhost:8080", s.getEndpointUrl());
				Assert.assertEquals("system-name", s.getSystemName());
			}
		}
	}

    @Test
 	public void analyzeTimeout() {
    	    	
    	Assert.assertTrue(timeout > 0);
     	LogEvent event = createLogEvent("req-out");
 		
 		analyzerService.analyze(event);
 		
 		
 		try {
			Thread.sleep((timeout * 1000) + 100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

 		// too late...
 		when(event.getLogEntry().getMessageInfo().getMessage()).thenReturn("resp-in");	
 		analyzerService.analyze(event);
 				
 		//
 		for (ServiceGroup sg : analyzerService.getCurrentStatusFromAllProducers().getGroups()) {
 			Assert.assertEquals("domain", sg.getName());
 			Assert.assertEquals("sub-domain", sg.getDescription());
 			for (Service s : sg.getServices()) {
 				Assert.assertEquals(RuntimeStatus.DOWN, s.getStatus());
 				Assert.assertEquals("https://localhost:8080", s.getEndpointUrl());
 				Assert.assertEquals("system-name", s.getSystemName());
 			}
 		}
 	}

}
