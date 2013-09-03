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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.soitoolkit.commons.logentry.schema.v1.LogEvent;
import org.springframework.stereotype.Component;

import se.skl.skltpservices.components.analyzer.Zero;

/**
 * Zero/noop repository implementation.
 * 
 * @author Peter
 *
 */
@Zero @Component
public class ZeroLogStoreRepository implements LogStoreRepository {

    @Override
    public void storeEvent(LogEvent infoEvent) {
    }

    @Override
    public List<Counter> getDomainCounters(int week) {
        return Collections.emptyList();
    }

    @Override
    public List<Counter> getContractCounters(int week) {
        return Collections.emptyList();
    }

    @Override
    public Set<String> getSenders() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getReceivers() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getContracts() {
        return Collections.emptySet();
    }

    @Override
    public List<EventSummary> getTimeLine(String contract, String error,
            String sender, String receiver, long time) {
        return Collections.emptyList();
    }

    @Override
    public Map<String, String> getEventProperties(String id) {
        return Collections.emptyMap();
    }

}
