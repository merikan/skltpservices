package se.skl.skltpservices.components.analyzer.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ServiceProducer {
    @Id protected Long id;
    // Maps to url
    private String serviceUrl;
    // Maps to name
    private String domainName;
    // Maps to service name
    private String systemName;
    // Maps to description
    private String domainDescription;
    
    public Long getId() {
        return id;
    }
    public String getServiceUrl() {
        return serviceUrl;
    }
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
    public String getDomainName() {
        return domainName;
    }
    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }
    public String getSystemName() {
        return systemName;
    }
    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
    public String getDomainDescription() {
        return domainDescription;
    }
    public void setDomainDescription(String domainDescription) {
        this.domainDescription = domainDescription;
    }
    
}
