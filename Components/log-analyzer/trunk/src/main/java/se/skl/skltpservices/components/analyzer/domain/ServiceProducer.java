package se.skl.skltpservices.components.analyzer.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ServiceProducer {
    @Id private Long id;
    private String serviceUrl;
    private String domain;
    private String subDomain;
    private String system;
    private String type;
    
    public Long getId() {
        return id;
    }
    public String getServiceUrl() {
        return serviceUrl;
    }
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
    }
    public String getSubDomain() {
        return subDomain;
    }
    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }
    public String getSystem() {
        return system;
    }
    public void setSystem(String system) {
        this.system = system;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    
}
