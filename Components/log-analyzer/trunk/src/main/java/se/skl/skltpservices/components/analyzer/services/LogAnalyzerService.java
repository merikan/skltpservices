package se.skl.skltpservices.components.analyzer.services;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import org.soitoolkit.commons.logentry.schema.v1.LogEntryType.ExtraInfo;
import org.soitoolkit.commons.logentry.schema.v1.LogEvent;
import org.springframework.beans.factory.annotation.Value;

import se.skl.skltpservices.components.analyzer.application.RuntimeStatus;
import se.skl.skltpservices.components.analyzer.application.Service;
import se.skl.skltpservices.components.analyzer.application.ServiceGroup;
import se.skl.skltpservices.components.analyzer.application.ServiceGroup.ServiceGroupBuilder;
import se.skl.skltpservices.components.analyzer.application.ServiceGroups;
import se.skl.skltpservices.components.analyzer.domain.ServiceProducer;

@org.springframework.stereotype.Service
public class LogAnalyzerService {
    private static final String SYSTEM_NAME = "system-name";
    private static final String SUB_DOMAIN = "sub-domain";
    private static final String DOMAIN = "domain";
    private static final String ENDPOINT_URL = "endpoint-url";
	
	private Map<String, ServiceProducer> serviceProducerMap = new HashMap<String, ServiceProducer>();

	// config
    @Value("${analyze.timeout}")
    private int timeout;


    public ServiceGroups getCurrentStatusFromAllProducers() {
    	HashMap<String, ServiceGroup> map = new HashMap<String, ServiceGroup>();

    	for (ServiceProducer serviceProducer : getServicePproducers()) {
    		List<Event> timeline = serviceProducer.getTimeLine();
    		if (!timeline.isEmpty()) {
    			Service service = new Service.ServiceBuilder()
    			.setEndpointUrl(serviceProducer.getServiceUrl())
    			.setSystemName(serviceProducer.getSystemName())
    			.setStatus(calcRuntimeStatus(timeline))
    			.build();

    			// group services by domain names, don't bother if sub-domain matches or not
    			ServiceGroup pri = map.get(serviceProducer.getDomainName());
    			if (pri == null) {
    				pri = new ServiceGroupBuilder().setName(serviceProducer.getDomainName())
    						.setDescription(serviceProducer.getDomainDescription())
    						.addService(service)
    						.build();
    				map.put(serviceProducer.getDomainName(), pri);
    			} else {
    				pri.getServices().add(service);
    			}
    		}
    	}
    	
    	ServiceGroups groups = new ServiceGroups.ServiceGroupsBuilder().setServiceGroups(map.values()).build();
    	
    	return groups;
    }
    
    //
    protected ServiceProducer[] getServicePproducers() {
    	Collection<ServiceProducer> c = serviceProducerMap.values();
    	ServiceProducer[] arr = serviceProducerMap.values().toArray(new ServiceProducer[c.size()]);
    	Arrays.sort(arr);    	
    	return arr;
    }
    
    //
    private static String gv(List<ExtraInfo> list, String name) {
    	for (ExtraInfo info : list) {
			if (name.equals(info.getName())) {
				return info.getValue();
			}
		}    
    	return "";
    }
    
    //
    protected ServiceProducer lookupServiceProducer(List<ExtraInfo> list) {
		String endpoint = gv(list, ENDPOINT_URL);
		ServiceProducer sp = serviceProducerMap.get(endpoint);
		if (sp == null) {
			sp = new ServiceProducer.ServiceProducerBuilder().setTimeout(timeout)
				.setDomainDescription(gv(list, SUB_DOMAIN)).setDomainName(gv(list, DOMAIN)).setSystemName(gv(list, SYSTEM_NAME)).setServiceUrl(endpoint).build();
			serviceProducerMap.put(endpoint, sp);
		}
    	return sp;
    }
    
    //
    public void analyze(LogEvent logEvent) {
    	Event event = new Event();
    	String msg = logEvent.getLogEntry().getMessageInfo().getMessage();
    	if ("req-out".equals(msg)) {
    		event.setStatus(State.START);
		} else if ("resp-in".equals(msg)) {
    		event.setStatus(State.SUCCESS);
		} else {
    		event.setStatus(State.FAILURE);
		}
    	event.setTimestamp(toTimestamp(logEvent.getLogEntry().getRuntimeInfo().getTimestamp()));
    	event.setCorrelationId(logEvent.getLogEntry().getRuntimeInfo().getBusinessCorrelationId());
    	ServiceProducer sp = lookupServiceProducer(logEvent.getLogEntry().getExtraInfo());
    	sp.update(event);
    }
    

    protected RuntimeStatus calcRuntimeStatus(List<Event> timeline) {
        if (timeline != null) {
            for (int i = 0; i < Math.min(3, timeline.size()); i++) {
                Event e = timeline.get(i);
                if (i == 0 && e.getState().equals(State.SUCCESS)) {
                    return RuntimeStatus.UP;
                } else if (i == 1 && !e.getState().equals(State.SUCCESS)) {
                    return RuntimeStatus.DOWN;
                } else if (i == 2 && e.getState().equals(State.SUCCESS)) {
                    return RuntimeStatus.UP;
                }
            }
        }
        return RuntimeStatus.DOWN;
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