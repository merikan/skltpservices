package se.skl.skltpservices.components.analyzer.domain;

import java.util.LinkedList;
import java.util.List;

import se.skl.skltpservices.components.analyzer.services.EntityBuilder;
import se.skl.skltpservices.components.analyzer.services.Event;
import se.skl.skltpservices.components.analyzer.services.State;


public class ServiceProducer implements Comparable<ServiceProducer> {
    // Maps to url
    private String serviceUrl;
    // Maps to name
    private String domainName;
    // Maps to service name
    private String systemName;
    // Maps to description
    private String domainDescription;
    //
    private LinkedList<Event> timeline;
    
    private int timeout;
    
    public static class ServiceProducerBuilder implements EntityBuilder<ServiceProducer> {
    	private ServiceProducer sp = new ServiceProducer();

    	
    	public ServiceProducerBuilder setServiceUrl(String serviceUrl) {
    		sp.serviceUrl = serviceUrl;
    		return this;
    	}
    	
    	public ServiceProducerBuilder setDomainName(String domainName) {
    		sp.domainName = domainName;
    		return this;
    	}
    	
    	public ServiceProducerBuilder setDomainDescription(String domainDescription) {
    		sp.domainDescription = domainDescription;
    		return this;
    	}

    	public ServiceProducerBuilder setSystemName(String systemName) {
    		sp.systemName = systemName;
    		return this;
    	}
  
      	public ServiceProducerBuilder setTimeout(int timeout) {
    		sp.timeout = timeout;
    		return this;
    	}
  
		@Override
		public ServiceProducer build() {
			sp.timeline = new LinkedList<Event>();
			return sp;
		}
    }
    
	@Override
	public int compareTo(ServiceProducer another) {
		int n = this.domainName.compareTo(another.domainName);
		return (n == 0) ? this.systemName.compareTo(another.systemName) : n;
	}

    @Override
    public int hashCode() {
    	return this.serviceUrl.hashCode();
    }
    
    @Override
    public boolean equals(Object another) {
    	if (this == another) {
    		return true;
    	}
    	if (another instanceof ServiceProducer) {
    		return ((ServiceProducer)another).serviceUrl.equals(this.serviceUrl);
    	}
    	return false;
    }
    
    public String getServiceUrl() {
        return serviceUrl;
    }
      
    public String getDomainName() {
        return domainName;
    }
       
    public String getSystemName() {
        return systemName;
    }
    
    public String getDomainDescription() {
        return domainDescription;
    }
        
    public List<Event> getTimeLine() {
    	return timeline;
    }
    
    //
    private void openEvent(Event event) {
		timeline.addFirst(event);
		event.activateTimeout(timeout);
		
		// unlink old events
    	if (timeline.size() > 3) {
    		timeline.removeLast();
    	}    	
    }
    
    //
    private void closeEvent(Event event) {
    	for (Event old : timeline) {
    		if (old.getCorrelationId().equals(event.getCorrelationId())) {
    			if (old.getState() != State.TIMEOUT) {
    				old.deactivateTimeout();
    				old.setOutboundRef(event.getOutboundRef());
    				old.setLatency(event.getTimestamp() - old.getTimestamp());
    				old.setStatus(event.getState());
    			}
    			break;
    		}
    	}    	
    }
    
    // update timeline
    public void update(Event event) {
    	if (event.getState() == State.START) {
    		openEvent(event);
    	} else {
    		closeEvent(event);
    	}
    }
}
