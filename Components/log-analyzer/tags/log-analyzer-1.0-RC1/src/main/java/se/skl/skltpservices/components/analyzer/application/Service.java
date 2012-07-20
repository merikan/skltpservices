package se.skl.skltpservices.components.analyzer.application;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import se.skl.skltpservices.components.analyzer.services.EntityBuilder;

@XmlRootElement
public class Service implements Serializable {
    private static final long serialVersionUID = 1L;
    @XmlElement private String systemName;
    @XmlElement private String endpointUrl;
    @XmlElement private RuntimeStatus status;

    public static class ServiceBuilder implements EntityBuilder<Service> {
        private Service pri = new Service();
        
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
