package se.skl.skltpservices.components.analyzer.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.CannotCreateTransactionException;

import se.skl.skltpservices.components.analyzer.application.RuntimeStatus;
import se.skl.skltpservices.components.analyzer.application.Service;
import se.skl.skltpservices.components.analyzer.application.ServiceGroup;
import se.skl.skltpservices.components.analyzer.application.ServiceGroup.ServiceGroupBuilder;
import se.skl.skltpservices.components.analyzer.application.ServiceGroups;
import se.skl.skltpservices.components.analyzer.domain.ServiceProducer;
import se.skl.skltpservices.components.analyzer.domain.ServiceProducerRepository;

@org.springframework.stereotype.Service
public class LogAnalyzerService {
    private ServiceProducerRepository serviceProducerRepository;
    private EvactorService evactorService;

    @Autowired
    public LogAnalyzerService(ServiceProducerRepository serviceProducerRepository, EvactorService evactorService) {
        this.serviceProducerRepository = serviceProducerRepository;
        this.evactorService = evactorService;
    }

    public ServiceGroups getCurrentStatusFromAllProducers() {
    	HashMap<String, ServiceGroup> map = new HashMap<String, ServiceGroup>();

    	for (ServiceProducer serviceProducer : getServicePproducers()) {
    		List<Event> timeline = evactorService.getEventTimelineForProducer(serviceProducer.getId());
    		if (!timeline.isEmpty()) {
    			Service service = new Service.ServiceBuilder()
    			.setEndpointUrl(serviceProducer.getServiceUrl())
    			.setSystemName(serviceProducer.getSystem())
    			.setStatus(calcRuntimeStatus(timeline))
    			.setId(String.valueOf(serviceProducer.getId()))
    			.build();

    			// group services by domain names, don't bother if municipality matches or not
    			ServiceGroup pri = map.get(serviceProducer.getDomain());
    			if (pri == null) {
    				pri = new ServiceGroupBuilder().setName(serviceProducer.getDomain())
    						.setDescription(serviceProducer.getMunicipality())
    						.addService(service)
    						.build();
    				map.put(serviceProducer.getDomain(), pri);
    			} else {
    				pri.getServices().add(service);
    			}
    		}
    	}
    	
    	ServiceGroups groups = new ServiceGroups.ServiceGroupsBuilder().setServiceGroups(map.values()).build();
    	
    	return groups;
    }
    
    // FIXME: ugly workaround to avoid CannotCreateTransactionException after some time of inactivity
    // Root cause probably is something with the hibernate transaction and connect pool settings
    private Iterable<ServiceProducer> getServicePproducers() {
    	try {
    		return serviceProducerRepository.findAll();
    	} catch (CannotCreateTransactionException e) {
    		return serviceProducerRepository.findAll();    		
    	}
    }

    protected RuntimeStatus calcRuntimeStatus(List<Event> timeline) {
        if (timeline != null) {
            for (int i = 0; i < Math.min(3, timeline.size()); i++) {
                Event e = timeline.get(i);
                if (i == 0 && e.getState().equals(State.SUCCESS)) {
                    return RuntimeStatus.UP;
                } else if (i == 1 && !e.getState().equals(State.SUCCESS)) {
                    return RuntimeStatus.DOWN;
                } else if (i == 2 && e.getState().equals(State.SUCCESS)) {
                    return RuntimeStatus.UP;
                }
            }
        }
        return RuntimeStatus.DOWN;
    }
}