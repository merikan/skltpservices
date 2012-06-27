package se.skl.skltpservices.components.analyzer.services;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ProducerRuntimeInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private RuntimeStatus status;
    private String domain;
    private String subdomain;
    private String type;
    private String systemName;
    private String endpointUrl;

    public static class ProducerRuntimeInfoBuilder implements EntityBuilder<ProducerRuntimeInfo> {
        private ProducerRuntimeInfo pri = new ProducerRuntimeInfo();

        public ProducerRuntimeInfoBuilder setStatus(RuntimeStatus status) {
            pri.status = status;
            return this;
        }

        public ProducerRuntimeInfoBuilder setDomain(String domain) {
            pri.domain = domain;
            return this;
        }

        public ProducerRuntimeInfoBuilder setSubdomain(String subdomain) {
            pri.subdomain = subdomain;
            return this;
        }

        public ProducerRuntimeInfoBuilder setType(String type) {
            pri.type = type;
            return this;
        }

        public ProducerRuntimeInfoBuilder setSystemName(String systemName) {
            pri.systemName = systemName;
            return this;
        }

        public ProducerRuntimeInfoBuilder setEndpointUrl(String endpointUrl) {
            pri.endpointUrl = endpointUrl;
            return this;
        }

        @Override
        public ProducerRuntimeInfo build() {
            return pri;
        }
    }

    private ProducerRuntimeInfo() {
    }

    public RuntimeStatus getStatus() {
        return status;
    }

    public String getDomain() {
        return domain;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public String getType() {
        return type;
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
