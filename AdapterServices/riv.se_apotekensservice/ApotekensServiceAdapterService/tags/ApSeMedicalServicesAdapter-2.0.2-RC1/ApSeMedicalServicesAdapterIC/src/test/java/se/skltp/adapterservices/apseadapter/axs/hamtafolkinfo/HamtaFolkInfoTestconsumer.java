package se.skltp.adapterservices.apseadapter.axs.hamtafolkinfo;

import java.net.URL;

import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.util.RecursiveResourceBundle;
import org.w3c.addressing.v1.AttributedURIType;

import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.inera.se.apotekensservice.axs.hamtafolkinfo.v1.rivtabp20.HamtaFolkInfoResponderInterface;
import se.riv.se.apotekensservice.axs.hamtafolkinforesponder.v1.HamtaFolkInfoRequestType;
import se.riv.se.apotekensservice.axs.hamtafolkinforesponder.v1.HamtaFolkInfoResponseType;

public class HamtaFolkInfoTestconsumer {

	private static final Logger log = LoggerFactory.getLogger(HamtaFolkInfoTestconsumer.class);

	private HamtaFolkInfoResponderInterface _service = null;

	public HamtaFolkInfoTestconsumer(String serviceAddress) {
		JaxWsProxyFactoryBean proxyFactory = new JaxWsProxyFactoryBean();
		proxyFactory.setServiceClass(HamtaFolkInfoResponderInterface.class);
		proxyFactory.setAddress(serviceAddress);

		// Used for HTTPS
		SpringBusFactory bf = new SpringBusFactory();
		URL cxfConfig = HamtaFolkInfoTestconsumer.class.getClassLoader().getResource("cxf-test-consumer-config.xml");
		if (cxfConfig != null) {
			proxyFactory.setBus(bf.createBus(cxfConfig));
		}

		_service = (HamtaFolkInfoResponderInterface) proxyFactory.create();
	}

	public HamtaFolkInfoResponseType callService(String id, ArgosHeaderType argosHeader) throws Exception {
		log.debug("Calling sample-soap-service with id = {}", id);
		HamtaFolkInfoRequestType request = new HamtaFolkInfoRequestType();
		request.setPersonnummer(id);
		return _service.hamtaFolkInfo(request, createLogicalAddress("TEST"), argosHeader);
	}

	private AttributedURIType createLogicalAddress(String logicalAddress) {
		AttributedURIType logicalAddressType = new AttributedURIType();
		logicalAddressType.setValue(logicalAddress);
		return logicalAddressType;
	}
}
