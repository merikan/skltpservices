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
