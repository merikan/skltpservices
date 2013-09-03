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

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import se.skl.skltpservices.components.analyzer.services.EntityBuilder;

@XmlRootElement(name="service-group")
public class ServiceGroup {
   @XmlElement(name="name") private String name;
   @XmlElement(name="description") private String description;
   @XmlElement(name="service") private Collection<Service> services;

    public static class ServiceGroupBuilder implements EntityBuilder<ServiceGroup> {
        private ServiceGroup serviceGroup = new ServiceGroup();


        public ServiceGroupBuilder() {
            serviceGroup.services = new ArrayList<Service>();
        }
       
        public ServiceGroupBuilder setName(String name) {
            serviceGroup.name = name;
            return this;
        }

  
        public ServiceGroupBuilder setDescription(String description) {
            serviceGroup.description = description;
            return this;
        }
        
        public ServiceGroupBuilder addService(Service service) {
            serviceGroup.services.add(service);
            return this;
        }

        @Override
        public ServiceGroup build() {
            return serviceGroup;
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Collection<Service> getServices() {
        return services;
    }

}
