package se.skl.skltpservices.components.analyzer.application;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import se.skl.skltpservices.components.analyzer.services.EntityBuilder;

@XmlRootElement
public class ServiceGroup {
   @XmlElement private String domain;
   @XmlElement private String subdomain;
   @XmlElement private String municipality;
   @XmlElement(name="service") private Collection<Service> services;

    public static class ServiceGroupBuilder implements EntityBuilder<ServiceGroup> {
        private ServiceGroup serviceGroup = new ServiceGroup();


        public ServiceGroupBuilder() {
            serviceGroup.services = new ArrayList<Service>();
        }
       
        public ServiceGroupBuilder setDomain(String domain) {
            serviceGroup.domain = domain;
            return this;
        }

        public ServiceGroupBuilder setSubdomain(String subdomain) {
            serviceGroup.subdomain = subdomain;
            return this;
        }

        public ServiceGroupBuilder setMunicipality(String municipality) {
            serviceGroup.municipality = municipality;
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

    public String getDomain() {
        return domain;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public String getMunicipality() {
        return municipality;
    }

    public Collection<Service> getServices() {
        return services;
    }

}
