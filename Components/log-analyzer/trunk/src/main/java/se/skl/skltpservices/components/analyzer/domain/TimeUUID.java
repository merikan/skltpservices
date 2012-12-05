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
	
	/**
	 * Creates an UUID of type 1 to be used in time series.
	 * 
	 * @param timestamp the timestamp in millis.
	 * @param query true if this shall be input to a query, otherwise it's guaranteed to be unique.
	 */
	public TimeUUID(long timestamp, boolean query) {
		this.timestamp = timestamp;
		this.uuid = new UUID(query ? createQueryTime(timestamp) : UUIDGen.createTime(timestamp), UUIDGen.getClockSeqAndNode());
	}

	public TimeUUID(UUID uuid) {
		this.uuid = uuid;
		this.timestamp = TimeUUIDUtils.getTimeFromUUID(uuid);
	}

	private static long createQueryTime(long currentTimeMillis) {
		long time;

		// UTC time

		final long timeMillis = (currentTimeMillis * 10000) + 0x01B21DD213814000L;

		// time low

		time = timeMillis << 32;

		// time mid

		time |= (timeMillis & 0xFFFF00000000L) >> 16;

		// time hi and version

		time |= 0x1000 | ((timeMillis >> 48) & 0x0FFF); // version 1

		return time;

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
		return uuid;
	}
}
