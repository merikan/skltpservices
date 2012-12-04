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

import java.nio.ByteBuffer;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import com.eaio.uuid.UUIDGen;

/**
 * Manages Cassandras TimeUUIDType.
 * 
 * @author Peter
 */
public class TimeUUID {
	private ByteBuffer uuid;
	private long timestamp;

	public TimeUUID(long timestamp) {
		this.timestamp = timestamp;
		this.uuid = ByteBuffer.wrap(TimeUUIDUtils.asByteArray(new UUID(UUIDGen.createTime(timestamp), UUIDGen.getClockSeqAndNode())));
	}

	public TimeUUID(ByteBuffer uuid) {
		this.uuid = uuid;
		this.timestamp = TimeUUIDUtils.getTimeFromUUID(TimeUUIDUtils.uuid(uuid));
	}

	/**
	 * Returns uuid.
	 * 
	 * @return the uuid.
	 */
	public ByteBuffer asByteBuffer() {
		return uuid;
	}

	/**
	 * Returns timestamp.
	 * 
	 * @return the timestamp.
	 */
	public long getTimestamp() {
		return timestamp;
	}

	public UUID getUUID() {
		return TimeUUIDUtils.uuid(uuid);
	}
}
