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

import me.prettyprint.hector.api.beans.Composite;
import static se.skl.skltpservices.components.analyzer.domain.CassandraLogStoreRepository.*;

public class ReverseEvent {
	private TimeUUID timeUUID;
	private String contract;
	private boolean error;
	private String receiver;
	private String sender;
	private String payload;
	private String key;
	private long timestamp;

	public ReverseEvent(String key, String payload, boolean error, long timestamp) {
		this.key = key;
		this.payload = nvl(payload);
		this.error = error;
		this.timestamp = timestamp;
		this.timeUUID = new TimeUUID(timestamp, false);
		this.receiver = "";
		this.sender = "";
	}

	//
	String nvl(String s) {
		return (s == null || s.length() == 0) ? "" : s;
	}

	//
	public void add(String name, String value) {
		if (CONTRACT.equals(name)) {
			this.contract = nvl(value);
		} else if (RECEIVER.equals(name)) {
			this.receiver = nvl(value);
		} else if (SENDER.equals(name)) {
			this.sender = nvl(value);
		}
	}

	public Composite key() {
		Composite c = new Composite();
		c.addComponent(contract, SS);
		c.addComponent(((error) ? "y" : "n"), SS);
		c.addComponent(sender, SS);
		c.addComponent(receiver, SS);
		return c;
	}

	public Composite value() {
		Composite c = new Composite();
		c.addComponent(key, SS);
		c.addComponent(payload, SS);
		return c;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getReceiver() {
		return receiver;
	}

	public String getSender() {
		return sender;
	}

	public String getContract() {
		return contract;
	}

	public TimeUUID getTimeUUID() {
		return timeUUID;
	}
}
