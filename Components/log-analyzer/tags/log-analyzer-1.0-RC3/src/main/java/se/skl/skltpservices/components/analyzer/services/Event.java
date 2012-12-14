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

import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Event extends TimerTask {
    private long timestamp;
    private String correlationId;
    private String component;
    private String inboundRef;
    private String outboundRef;
    private long latency;
    private State state;
    private Timer timer = null;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getInboundRef() {
        return inboundRef;
    }

    public void setInboundRef(String inboundRef) {
        this.inboundRef = inboundRef;
    }

    public String getOutboundRef() {
        return outboundRef;
    }

    public void setOutboundRef(String outboundRef) {
        this.outboundRef = outboundRef;
    }

    public long getLatency() {
        return latency;
    }

    public void setLatency(long latency) {
        this.latency = latency;
    }

    public State getState() {
        return state;
    }

    public void setStatus(State state) {
        this.state = state;
    }
    

	@Override
	public synchronized void run() {
		setStatus(State.TIMEOUT);
		setLatency(-1);
		if (this.timer != null) {
			this.timer.cancel();
			this.timer = null;
		}
	}
	
    //
    public synchronized void deactivateTimeout() {
    	if (timer != null) {
    		timer.cancel();
    		timer = null;
    	}
    }
    
    //
    public synchronized void activateTimeout(int seconds) {
    	if (this.timer != null) {
    		this.timer.cancel();
    	}
    	this.timer = new Timer();
    	this.timer.schedule(this, seconds * 1000);    	
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
