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
