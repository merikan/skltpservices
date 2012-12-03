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
	private UUID uuid;
	private long timestamp;

	public TimeUUID(long timestamp) {
		this.timestamp = timestamp;
		this.uuid = new UUID(UUIDGen.createTime(timestamp), UUIDGen.getClockSeqAndNode());;
	}

	public TimeUUID(ByteBuffer uuid) {
		this.uuid = TimeUUIDUtils.uuid(uuid);
		this.timestamp = TimeUUIDUtils.getTimeFromUUID(this.uuid);
	}

	/**
	 * Returns byte array.
	 *
	 * @param uuid the uuid
	 *
	 * @return the byte[]
	 */
	public ByteBuffer toByteBuffer() {
		return TimeUUIDUtils.asByteBuffer(uuid);
	}


	/**
	 * Returns uuid.
	 * 
	 * @return the uuid.
	 */
	public UUID getUUID() {
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

}
