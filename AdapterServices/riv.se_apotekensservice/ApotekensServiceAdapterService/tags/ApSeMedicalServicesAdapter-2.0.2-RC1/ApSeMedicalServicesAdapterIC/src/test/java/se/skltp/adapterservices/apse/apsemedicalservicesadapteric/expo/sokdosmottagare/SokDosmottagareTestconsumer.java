package se.skltp.adapterservices.apse.apsemedicalservicesadapteric.expo.sokdosmottagare;

import java.net.URL;

import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.addressing.v1.AttributedURIType;

import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.inera.se.apotekensservice.expo.sokdosmottagare.v1.rivtabp20.SokDosmottagareResponderInterface;
import se.riv.se.apotekensservice.expo.sokdosmottagareresponder.v1.SokDosmottagareRequestType;
import se.riv.se.apotekensservice.expo.sokdosmottagareresponder.v1.SokDosmottagareResponseType;

public class SokDosmottagareTestconsumer {

	private static final Logger log = LoggerFactory.getLogger(SokDosmottagareTestconsumer.class);

	private SokDosmottagareResponderInterface _service = null;

	public SokDosmottagareTestconsumer(String serviceAddress) {
		JaxWsProxyFactoryBean proxyFactory = new JaxWsProxyFactoryBean();
		proxyFactory.setServiceClass(SokDosmottagareResponderInterface.class);
		proxyFactory.setAddress(serviceAddress);

		// Used for HTTPS
		SpringBusFactory bf = new SpringBusFactory();
		URL cxfConfig = SokDosmottagareTestconsumer.class.getClassLoader().getResource("cxf-test-consumer-config.xml");
		if (cxfConfig != null) {
			proxyFactory.setBus(bf.createBus(cxfConfig));
		}

		_service = (SokDosmottagareResponderInterface) proxyFactory.create();
	}

	public SokDosmottagareResponseType callService(String id, ArgosHeaderType argosHeader) throws Exception {
		log.debug("Calling sample-soap-service with id = {}", id);
		SokDosmottagareRequestType request = new SokDosmottagareRequestType();
		return _service.sokDosmottagare(request, createLogicalAddress("TEST"), argosHeader);
	}

	private AttributedURIType createLogicalAddress(String logicalAddress) {
		AttributedURIType logicalAddressType = new AttributedURIType();
		logicalAddressType.setValue(logicalAddress);
		return logicalAddressType;
	}
}
