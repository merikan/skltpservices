package se.skl.skltpservices.components.analyzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import se.skl.skltpservices.components.analyzer.domain.LogStoreRepository;
import se.skl.skltpservices.components.analyzer.services.LogAnalyzerService;

/**
 * Keeps spring profile based plug-able APIs.
 * 
 * @author Peter
 *
 */
@Configuration
public class LogServiceConfig {

    @Autowired
    private LogStoreRepository logStoreRepository;
    
    @Autowired
    private LogAnalyzerService logAnalyzerService;
    
    public LogStoreRepository getLogStoreRepository() {
        return logStoreRepository;
    }
    
    public LogAnalyzerService getLogAnalyzerService() {
        return logAnalyzerService;
    }
}
