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

import org.soitoolkit.commons.logentry.schema.v1.LogEvent;

import se.skl.skltpservices.components.analyzer.application.ServiceGroups;
import se.skl.skltpservices.components.analyzer.domain.ServiceProducer;

public interface LogAnalyzerService {

    /**
     * Analyzes log event.
     * 
     * @param logEvent the event.
     */
    void analyze(LogEvent logEvent);
    
    /**
     * Returns all service groups and services.
     * 
     * @return service groups.
     */
    ServiceGroups getCurrentStatusFromAllProducers();
    
    /**
     * Returns service producers.
     * 
     * @return service producers.
     */
    public ServiceProducer[] getServicePproducers();
}
