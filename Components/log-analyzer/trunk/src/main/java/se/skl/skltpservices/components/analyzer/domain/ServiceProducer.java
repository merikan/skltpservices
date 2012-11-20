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

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.skl.skltpservices.components.analyzer.services.EntityBuilder;
import se.skl.skltpservices.components.analyzer.services.Event;
import se.skl.skltpservices.components.analyzer.services.State;


public class ServiceProducer implements Comparable<ServiceProducer> {
	private static final Logger log = LoggerFactory.getLogger(ServiceProducer.class);

	// Maps to url
    private String serviceUrl;
    // Maps to name
    private String domainName;
    // Maps to service name
    private String systemName;
    // Maps to description
    private String domainDescription;
    //
    private long lastUpdated;
    //
    private LinkedList<Event> timeline;
    //
    private int timeout;
    // If not updated in 1 hour it's assumed to be expired and removed from the list
    private static long EXPIRED_AFTER_MS = (3600 * 1000);
    
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
			sp.touch();
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

    public void setDomainName(String domainName) {
        this.domainName = domainName ;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
    	this.systemName = systemName;
    }

    public String getDomainDescription() {
        return domainDescription;
    }

    public void setDomainDescription(String domainDescription) {
        this.domainDescription = domainDescription;
    }
    
    public boolean isExpired() {
    	return (System.currentTimeMillis() - lastUpdated) > EXPIRED_AFTER_MS;
    }
    
    //
    public long getMaxLatency() {
    	long max = 0;
    	for (Event e : getTimeLine()) {
    		max = Math.max(max, e.getLatency());
    	}
    	return max;
    }
    
    /**
     * Returns the last time this provider was updated.
     * 
     * @return the timestamp in millis.
     */
    public long getLastUpdated() {
    	return lastUpdated;
    }
    
    //
    private void touch() {
       	this.lastUpdated = System.currentTimeMillis();   	
    }
    
    //
    public List<Event> getTimeLine() {
    	return timeline;
    }
    
    //
    private void openEvent(Event event) {
		timeline.addFirst(event);
		event.activateTimeout(timeout);
		
		// unlink old events
    	if (timeline.size() > 3) {
    		Event e = timeline.removeLast();
    		log.debug("remove: " + e);
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
    	touch();
    	if (event.getState() == State.START) {
    		openEvent(event);
    	} else {
    		closeEvent(event);
    	}
    }
}
