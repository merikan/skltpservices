package se.skl.skltpservices.components.analyzer.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import se.skl.skltpservices.components.analyzer.application.RuntimeStatus;
import se.skl.skltpservices.components.analyzer.application.Service;
import se.skl.skltpservices.components.analyzer.application.ServiceGroup;
import se.skl.skltpservices.components.analyzer.application.ServiceGroup.ServiceGroupBuilder;
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

    public List<ServiceGroup> getCurrentStatusFromAllProducers() {
        List<ServiceGroup> producerInfo = new ArrayList<ServiceGroup>();
        Iterable<ServiceProducer> producers = serviceProducerRepository.findAll();
        for (ServiceProducer serviceProducer : producers) {
            List<Event> timeline = evactorService.getEventTimelineForProducer(serviceProducer.getId());
            if (!timeline.isEmpty()) {
                ServiceGroup pri = new ServiceGroupBuilder().setDomain(serviceProducer.getDomain())
                        .setSubdomain(serviceProducer.getSubDomain())
                        .setMunicipality(serviceProducer.getMunicipality())
                        .addService(new Service.ServiceBuilder()
                                .setEndpointUrl(serviceProducer.getServiceUrl())
                                .setSystemName(serviceProducer.getSystem())
                                .setStatus(calcRuntimeStatus(timeline))
                                .build()
                        )
                        .build();
                producerInfo.add(pri);
            }
        }
        return producerInfo;
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