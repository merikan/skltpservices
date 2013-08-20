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

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.logentry.schema.v1.LogEntryType;
import org.soitoolkit.commons.logentry.schema.v1.LogEntryType.ExtraInfo;
import org.soitoolkit.commons.logentry.schema.v1.LogEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import se.skl.skltpservices.components.analyzer.application.Service;
import se.skl.skltpservices.components.analyzer.application.ServiceGroup;
import se.skl.skltpservices.components.analyzer.application.ServiceGroup.ServiceGroupBuilder;
import se.skl.skltpservices.components.analyzer.application.ServiceGroups;
import se.skl.skltpservices.components.analyzer.domain.ServiceProducer;

@Component
public class RestfulLogAnalyzerService implements LogAnalyzerService {
    private static final Logger log = LoggerFactory.getLogger(RestfulLogAnalyzerService.class);

    private static final String SYSTEM_NAME = "system-name";
    private static final String DOMAIN_DESCRIPTION = "domain-description";
    private static final String DOMAIN = "domain";

    private Map<String, ServiceProducer> serviceProducerMap = Collections.synchronizedMap(new HashMap<String, ServiceProducer>());

    // config
    @Value("${analyze.timeout}")
    private int timeout;


    @Override
    public ServiceGroups getCurrentStatusFromAllProducers() {
        HashMap<String, ServiceGroup> map = new HashMap<String, ServiceGroup>();

        for (ServiceProducer serviceProducer : getServicePproducers()) {
            List<Event> timeline = serviceProducer.getTimeLine();
            if (!timeline.isEmpty()) {
                Service service = new Service.ServiceBuilder()
                .setEndpointUrl(serviceProducer.getServiceUrl())
                .setSystemName(serviceProducer.getSystemName())
                .setStatus(serviceProducer.getStatus())
                .build();

                // group services by domain names, don't bother if sub-domain matches or not
                ServiceGroup pri = map.get(serviceProducer.getDomainName());
                if (pri == null) {
                    pri = new ServiceGroupBuilder().setName(serviceProducer.getDomainName())
                            .setDescription(serviceProducer.getDomainDescription())
                            .addService(service)
                            .build();
                    map.put(serviceProducer.getDomainName(), pri);
                } else {
                    pri.getServices().add(service);
                }
            }
            // remove old producers (inactive)
            if (serviceProducer.isExpired()) {
                log.debug("expired: {}", serviceProducer.getServiceUrl());
                serviceProducerMap.remove(serviceProducer.getServiceUrl());
            }
        }

        ServiceGroups groups = new ServiceGroups.ServiceGroupsBuilder().setServiceGroups(map.values()).build();

        return groups;
    }

    @Override
    public ServiceProducer[] getServicePproducers() {
        Collection<ServiceProducer> c = serviceProducerMap.values();
        ServiceProducer[] arr = serviceProducerMap.values().toArray(new ServiceProducer[c.size()]);
        Arrays.sort(arr);    	
        return arr;
    }

    //
    private static String gv(List<ExtraInfo> list, String name) {
        for (ExtraInfo info : list) {
            if (name.equals(info.getName())) {
                return info.getValue();
            }
        }    
        return "";
    }

    //
    protected ServiceProducer lookupServiceProducer(LogEntryType logEntry) {
        String endpoint = logEntry.getMetadataInfo().getEndpoint();
        log.debug("Ping monitor message for: {}", endpoint);
        ServiceProducer sp = serviceProducerMap.get(endpoint);
        List<ExtraInfo> list = logEntry.getExtraInfo();
        if (sp == null) {
            // Always update meta-data
            sp = new ServiceProducer.ServiceProducerBuilder().setTimeout(timeout)
                    .setDomainDescription(gv(list, DOMAIN_DESCRIPTION)).setDomainName(gv(list, DOMAIN)).setSystemName(gv(list, SYSTEM_NAME)).setServiceUrl(endpoint).build();
            serviceProducerMap.put(endpoint, sp);
        } else {
            sp.setSystemName(gv(list, SYSTEM_NAME));
            sp.setDomainDescription(gv(list, DOMAIN_DESCRIPTION));
            sp.setDomainName(gv(list, DOMAIN));
        }
        log.debug("#producers in  map: {}", serviceProducerMap.size());
        return sp;
    }

    @Override
    public void analyze(LogEvent logEvent) {
        Event event = new Event();
        String msg = logEvent.getLogEntry().getMessageInfo().getMessage();
        if ("req-out".equals(msg)) {
            event.setStatus(State.START);
        } else if ("resp-in".equals(msg)) {
            event.setStatus(State.SUCCESS);
        } else {
            event.setStatus(State.FAILURE);
        }
        event.setComponent(logEvent.getLogEntry().getRuntimeInfo().getComponentId() + "@" + logEvent.getLogEntry().getRuntimeInfo().getHostName());
        event.setTimestamp(toTimestamp(logEvent.getLogEntry().getRuntimeInfo().getTimestamp()));
        event.setCorrelationId(logEvent.getLogEntry().getRuntimeInfo().getBusinessCorrelationId());
        ServiceProducer sp = lookupServiceProducer(logEvent.getLogEntry());
        log.debug("event: {}", event);
        sp.update(event);
    }


    //
    private static long toTimestamp(final XMLGregorianCalendar timestamp) {	
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, timestamp.getHour());
        c.set(Calendar.MINUTE, timestamp.getMinute());
        c.set(Calendar.SECOND, timestamp.getSecond());
        c.set(Calendar.MILLISECOND, timestamp.getMillisecond());
        c.set(Calendar.YEAR, timestamp.getYear());
        c.set(Calendar.MONTH, timestamp.getMonth() - 1);
        c.set(Calendar.DAY_OF_MONTH, timestamp.getDay());
        return c.getTime().getTime();
    }
}