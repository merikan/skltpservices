package se.skl.skltpservices.components.analyzer.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.skl.skltpservices.components.analyzer.domain.ServiceProducer;
import se.skl.skltpservices.components.analyzer.domain.ServiceProducerRepository;
import se.skl.skltpservices.components.analyzer.services.ProducerRuntimeInfo.ProducerRuntimeInfoBuilder;

@Service
public class LogAnalyzerService {
    private ServiceProducerRepository serviceProducerRepository;
    private EvactorService evactorService;

    @Autowired
    public LogAnalyzerService(ServiceProducerRepository serviceProducerRepository, EvactorService evactorService) {
        this.serviceProducerRepository = serviceProducerRepository;
        this.evactorService = evactorService;
    }

    public List<ProducerRuntimeInfo> getCurrentStatusFromAllProducers() {
        List<ProducerRuntimeInfo> producerInfo = new ArrayList<ProducerRuntimeInfo>();
        Iterable<ServiceProducer> producers = serviceProducerRepository.findAll();
        System.out.println("Has producers: " +  producers.iterator().hasNext());
        for (ServiceProducer serviceProducer : producers) {
            System.out.println("Fetching latest event for producer " + serviceProducer.getId());
            List<Event> timeline = evactorService.getEventTimelineForProducer(serviceProducer.getId());
            Event event = null;
            if (!timeline.isEmpty()) {
                event = timeline.get(0);
                ProducerRuntimeInfo pri = new ProducerRuntimeInfoBuilder().setDomain(serviceProducer.getDomain())
                        .setSubdomain(serviceProducer.getSubDomain()).setSystemName(serviceProducer.getSystem())
                        .setEndpointUrl(serviceProducer.getServiceUrl()).setType(serviceProducer.getType())
                        .build();
                producerInfo.add(pri);
            }
        }
        return producerInfo;
    }
}
