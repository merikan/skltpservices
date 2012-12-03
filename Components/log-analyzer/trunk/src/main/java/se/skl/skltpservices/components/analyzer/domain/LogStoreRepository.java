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

import java.util.List;
import java.util.Set;

import org.soitoolkit.commons.logentry.schema.v1.LogEvent;

/**
 * Stores (soi-toolkit) log events in a persistent store.
 * 
 * @author Peter
 */
public interface LogStoreRepository {

	/**
	 * Stores information.
	 * 
	 * @param inforEvent the event to store.
	 */
	void storeEvent(LogEvent infoEvent);
	
	/**
	 * Returns domain counters.
	 * 
	 * @param week week of year.
	 * 
	 * @return the counters.
	 */
	List<Counter> getDomainCounters(int week);

	/**
	 * Returns contract counters.
	 * 
	 * @param week week of year.
	 * 
	 * @return the counters.
	 */
	List<Counter> getContractCounters(int week);
	
	/**
	 * Returns senders.
	 * 
	 * @return senders.
	 */
	Set<String> getSenders();

	/**
	 * Returns receivers.
	 * 
	 * @return receivers.
	 */
	Set<String> getReceivers();
	
	/**
	 * Returns contracts.
	 * 
	 * @return contracts.
	 */
	Set<String> getContracts();

}
