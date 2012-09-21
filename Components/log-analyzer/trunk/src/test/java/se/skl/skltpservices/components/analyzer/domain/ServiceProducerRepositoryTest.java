package se.skl.skltpservices.components.analyzer.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import se.skl.skltpservices.components.analyzer.TestSupport;

public class ServiceProducerRepositoryTest extends TestSupport {

	@Autowired
	private ServiceProducerRepository repo;
	
	@Before
	public void before() {
		ServiceProducer sp = new ServiceProducer();
		sp.setServiceUrl("http://slask");
		sp.setDomainName("TP");
		sp.setSystemName("APIGW");
		sp.setDomainDescription("Nothing special to discuss ŒŠš");
		sp.id = 1L;
		repo.save(sp);
	}
	
	@Test
	public void find() {
		assertTrue(repo.findOne(1L) != null);
	}
	
	@Test
	public void validate() {
		ServiceProducer sp = repo.findOne(1L);
		assertEquals(sp.getServiceUrl(), "http://slask");
		assertEquals(sp.getDomainName(), "TP");
		assertEquals(sp.getSystemName(), "APIGW");
		assertEquals(sp.getDomainDescription(), "Nothing special to discuss ŒŠš");
	}

}
