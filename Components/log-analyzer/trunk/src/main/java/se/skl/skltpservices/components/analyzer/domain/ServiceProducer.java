package se.skl.skltpservices.components.analyzer.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ServiceProducer {
    @Id private Long id;
    // Maps to url
    private String serviceUrl;
    // Maps to name
    private String domain;
    // Maps to service name
    private String system;
    // Maps to description
    private String municipality;
    
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
    public String getSystem() {
        return system;
    }
    public void setSystem(String system) {
        this.system = system;
    }
    public String getMunicipality() {
        return municipality;
    }
    public void setMunicipality(String municiplaity) {
        this.municipality = municiplaity;
    }
    
}
