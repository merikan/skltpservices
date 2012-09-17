package se.skl.skltpservices.components.analyzer.services;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import se.skl.skltpservices.components.analyzer.application.RuntimeStatus;

public class LogAnalyzerServiceTest {

    private static Event timedOutEvent;
    private static Event successEvent;
    private LogAnalyzerService analyzerService = new LogAnalyzerService(null, null);
    
    @Before
    public void setUp() {
        timedOutEvent = new Event();
        timedOutEvent.setId("Id1");
        timedOutEvent.setStatus(State.TIMEOUT);
        
        successEvent = new Event();
        successEvent.setId("Id2");
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
}
