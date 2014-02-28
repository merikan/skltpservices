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
package se.skl.skltpservices.components.analyzer.application;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import se.skl.skltpservices.components.analyzer.services.EntityBuilder;

@XmlRootElement(name="service")
public class Service implements Serializable {
    private static final long serialVersionUID = 1L;
    @XmlElement(name="id") private String id;
    @XmlElement(name="name") private String systemName;
    @XmlElement(name="url") private String endpointUrl;    
    @XmlElement(name="status") private RuntimeStatus status;

    public static class ServiceBuilder implements EntityBuilder<Service> {
        private Service pri = new Service();
        
        public ServiceBuilder setId(String id) {
            pri.id = id;
            return this;
        }
        
        public ServiceBuilder setStatus(RuntimeStatus status) {
            pri.status = status;
            return this;
        }

        public ServiceBuilder setSystemName(String systemName) {
            pri.systemName = systemName;
            return this;
        }

        public ServiceBuilder setEndpointUrl(String endpointUrl) {
            pri.endpointUrl = endpointUrl;
            return this;
        }

        @Override
        public Service build() {
            return pri;
        }
    }

    private Service() {
    }

    
    public RuntimeStatus getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public String getSystemName() {
        return systemName;
    }

    public String getEndpointUrl() {
        return endpointUrl;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
