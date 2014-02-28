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

import se.skl.skltpservices.components.analyzer.services.EntityBuilder;

@XmlRootElement(name="service-groups")
public class ServiceGroups {
	   @XmlElement(name="service-group") private Collection<ServiceGroup> groups;
	   
	   public static class ServiceGroupsBuilder implements EntityBuilder<ServiceGroups> {
	        private ServiceGroups serviceGroups = new ServiceGroups();


	        public ServiceGroupsBuilder() {
	        }
	        
	        public ServiceGroupsBuilder setServiceGroups(Collection<ServiceGroup> groups) {
	            serviceGroups.groups = groups;
	            return this;
	        }
	        
	        @Override
	        public ServiceGroups build() {
	            return serviceGroups;
	        }
	   }
	   
	   public Collection<ServiceGroup> getGroups() {
		   return groups;
	   }

}
