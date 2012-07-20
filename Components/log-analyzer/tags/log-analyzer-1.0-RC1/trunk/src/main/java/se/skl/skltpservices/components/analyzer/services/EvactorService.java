package se.skl.skltpservices.components.analyzer.services;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

@Service
public class EvactorService {
    private WebResource webResource;
    
    @Autowired
    public EvactorService(WebResource webResource) {
        this.webResource = webResource;
    }


    public List<Event> getEventTimelineForProducer(long producerId) {
        List<Event> timeline = webResource.path("timeline").path("Producer:" + producerId).accept(MediaType.APPLICATION_JSON).
        get(new GenericType<List<Event>>() {});
        return timeline;
    }
    
}
