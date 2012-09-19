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
