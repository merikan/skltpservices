package se.skl.skltpservices.components.analyzer.application;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import se.skl.skltpservices.components.analyzer.services.LogAnalyzerService;

@Controller
@Path("/service-groups")
@Produces({"application/xml; charset=UTF-8", "application/json; charset=UTF-8"})
public class ServiceProducerResource {

    private LogAnalyzerService logAnalyzerService;
    
    @Autowired
    public ServiceProducerResource(LogAnalyzerService logAnalyzerService) {
        this.logAnalyzerService = logAnalyzerService;
    }

    @GET
    public ServiceGroups findAll() {
        return logAnalyzerService.getCurrentStatusFromAllProducers(); 
    }
}