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
package se.skl.skltpservices.components.analyzer.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
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
import org.soitoolkit.commons.logentry.schema.v1.LogMetadataInfoType;
import org.soitoolkit.commons.logentry.schema.v1.LogRuntimeInfoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import se.skl.skltpservices.components.analyzer.TestSupport;
import se.skl.skltpservices.components.analyzer.application.RuntimeStatus;
import se.skl.skltpservices.components.analyzer.application.Service;
import se.skl.skltpservices.components.analyzer.application.ServiceGroup;
import se.skl.skltpservices.components.analyzer.domain.ServiceProducer;

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
        Assert.assertEquals(RuntimeStatus.DOWN, ServiceProducer.calcRuntimeStatus(emptyTimeline));
        Assert.assertEquals(RuntimeStatus.DOWN, ServiceProducer.calcRuntimeStatus(null));
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
        return ServiceProducer.calcRuntimeStatus(Arrays.asList(events));
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

		when(event.getLogEntry().getMetadataInfo()).thenReturn(mock(LogMetadataInfoType.class));
		when(event.getLogEntry().getMetadataInfo().getEndpoint()).thenReturn("https://localhost:8080");
		List<ExtraInfo> list = new LinkedList<ExtraInfo>();
		list.add(ci("domain", "domain"));
		list.add(ci("domain-description", "domain-description"));
		list.add(ci("system-name", "system-name"));
		
		when(event.getLogEntry().getExtraInfo()).thenReturn(list);
		
		XMLGregorianCalendar cal = mock(XMLGregorianCalendar.class);
		Calendar c = Calendar.getInstance();

		when(cal.getYear()).thenReturn(c.get(Calendar.YEAR));
		when(cal.getMonth()).thenReturn(c.get(Calendar.MONTH));
		when(cal.getDay()).thenReturn(c.get(Calendar.DAY_OF_MONTH));
		when(cal.getHour()).thenReturn(c.get(Calendar.HOUR_OF_DAY));
		when(cal.getMinute()).thenReturn(c.get(Calendar.MINUTE));
		when(cal.getSecond()).thenReturn(c.get(Calendar.SECOND));
		when(cal.getMillisecond()).thenReturn(c.get(Calendar.MILLISECOND));
		
		when(event.getLogEntry().getRuntimeInfo().getTimestamp()).thenReturn(cal);
		
		return event;
    }
    
    private void validateService(RuntimeStatus expectedStatus) {
		for (ServiceGroup sg : analyzerService.getCurrentStatusFromAllProducers().getGroups()) {
			Assert.assertEquals("domain", sg.getName());
			Assert.assertEquals("domain-description", sg.getDescription());
			Collection<Service> services = sg.getServices();
			Assert.assertEquals(1, services .size());
			for (Service s : services) {
				Assert.assertEquals(expectedStatus, s.getStatus());
				Assert.assertEquals("https://localhost:8080", s.getEndpointUrl());
				Assert.assertEquals("system-name", s.getSystemName());
			}
		}    	
    }

    @Test
	public void analyzeNormal() {
    	LogEvent event = createLogEvent("req-out");
		
		analyzerService.analyze(event);
		
		when(event.getLogEntry().getMessageInfo().getMessage()).thenReturn("resp-in");
		
		analyzerService.analyze(event);
		
		validateService(RuntimeStatus.UP);		
	}

    @Test
 	public void analyzeTimeout() {
    	    	
    	Assert.assertTrue(timeout > 0);
     	LogEvent event = createLogEvent("req-out");
 		
 		analyzerService.analyze(event);
 		
 		// wait for timeout
 		try {
			Thread.sleep((timeout * 1000) + 100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

 		// too late...
 		when(event.getLogEntry().getMessageInfo().getMessage()).thenReturn("resp-in");	
 		analyzerService.analyze(event);
 				
		validateService(RuntimeStatus.DOWN);
    }
}
