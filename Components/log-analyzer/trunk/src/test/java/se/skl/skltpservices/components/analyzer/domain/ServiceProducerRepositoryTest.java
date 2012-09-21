package se.skl.skltpservices.components.analyzer.domain;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import se.skl.skltpservices.components.analyzer.TestSupport;

public class ServiceProducerRepositoryTest extends TestSupport {

	@Autowired
	private ServiceProducerRepository repo;
	
	@Test
	public void save() {
		ServiceProducer sp = new ServiceProducer();
		sp.setServiceUrl("http://slask");
		sp.setDomainName("TP");
		sp.setSystemName("APIGW");
		sp.setDomainDescription("Nothing special to discuss");
		sp.id = 1L;
		repo.save(sp);
		
		assertTrue(repo.findOne(1L) != null);
	}
	
}
