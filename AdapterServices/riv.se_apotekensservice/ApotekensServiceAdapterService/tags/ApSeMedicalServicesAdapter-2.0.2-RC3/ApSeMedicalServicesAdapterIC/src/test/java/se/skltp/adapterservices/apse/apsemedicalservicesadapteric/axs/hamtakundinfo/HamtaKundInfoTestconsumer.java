package se.skltp.adapterservices.apse.apsemedicalservicesadapteric.axs.hamtakundinfo;

import java.net.URL;

import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.addressing.v1.AttributedURIType;

import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.inera.se.apotekensservice.axs.hamtakundinfo.v1.rivtabp20.HamtaKundInfoResponderInterface;
import se.riv.se.apotekensservice.axs.hamtakundinforesponder.v1.HamtaKundInfoRequestType;
import se.riv.se.apotekensservice.axs.hamtakundinforesponder.v1.HamtaKundInfoResponseType;

public class HamtaKundInfoTestconsumer {

	private static final Logger log = LoggerFactory.getLogger(HamtaKundInfoTestconsumer.class);

	private HamtaKundInfoResponderInterface _service = null;

	public HamtaKundInfoTestconsumer(String serviceAddress) {
		JaxWsProxyFactoryBean proxyFactory = new JaxWsProxyFactoryBean();
		proxyFactory.setServiceClass(HamtaKundInfoResponderInterface.class);
		proxyFactory.setAddress(serviceAddress);

		// Used for HTTPS
		SpringBusFactory bf = new SpringBusFactory();
		URL cxfConfig = HamtaKundInfoTestconsumer.class.getClassLoader().getResource("cxf-test-consumer-config.xml");
		if (cxfConfig != null) {
			proxyFactory.setBus(bf.createBus(cxfConfig));
		}

		_service = (HamtaKundInfoResponderInterface) proxyFactory.create();
	}

	public HamtaKundInfoResponseType callService(String id, ArgosHeaderType argosHeader) throws Exception {
		log.debug("Calling sample-soap-service with id = {}", id);
		HamtaKundInfoRequestType request = new HamtaKundInfoRequestType();
		request.setPersonnummer(id);
		return _service.hamtaKundInfo(request, createLogicalAddress("TEST"), argosHeader);
	}

	private AttributedURIType createLogicalAddress(String logicalAddress) {
		AttributedURIType logicalAddressType = new AttributedURIType();
		logicalAddressType.setValue(logicalAddress);
		return logicalAddressType;
	}
}
