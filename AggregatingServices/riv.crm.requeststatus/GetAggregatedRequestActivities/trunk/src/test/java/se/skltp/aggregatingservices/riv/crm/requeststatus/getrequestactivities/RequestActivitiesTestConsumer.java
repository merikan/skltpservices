package se.skltp.aggregatingservices.riv.crm.requeststatus.getrequestactivities;

import static se.skltp.agp.test.producer.TestProducerDb.TEST_RR_ID_ONE_HIT;

import java.net.URL;

import javax.xml.ws.Holder;

import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.riv.crm.requeststatus.getrequestactivities.v1.rivtabp21.GetRequestActivitiesResponderInterface;
import se.riv.crm.requeststatus.getrequestactivitiesresponder.v1.GetRequestActivitiesResponseType;
import se.riv.crm.requeststatus.getrequestactivitiesresponder.v1.GetRequestActivitiesType;
import se.skltp.agp.test.consumer.SoapHeaderCxfInterceptor;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;

public class RequestActivitiesTestConsumer {

	private static final Logger log = LoggerFactory.getLogger(RequestActivitiesTestConsumer.class);

	private GetRequestActivitiesResponderInterface _service = null;
	
	public static void main(String[] args) {
		String serviceAddress = RequestActivitiesMuleServer.getAddress("SERVICE_INBOUND_URL");
		String personnummer = TEST_RR_ID_ONE_HIT;

		RequestActivitiesTestConsumer consumer = new RequestActivitiesTestConsumer(serviceAddress);
		Holder<GetRequestActivitiesResponseType> responseHolder = new Holder<GetRequestActivitiesResponseType>();
		Holder<ProcessingStatusType> processingStatusHolder = new Holder<ProcessingStatusType>();
		long now = System.currentTimeMillis();
		consumer.callService("logical-adress", personnummer, processingStatusHolder, responseHolder);
		log.info("Returned #timeslots = " + responseHolder.value.getRequestActivity().size() + " in " + (System.currentTimeMillis() - now) + " ms.");

		now = System.currentTimeMillis();
		consumer.callService("logical-adress", personnummer, processingStatusHolder, responseHolder);
		log.info("Returned #timeslots = " + responseHolder.value.getRequestActivity().size() + " in " + (System.currentTimeMillis() - now) + " ms.");
	
		now = System.currentTimeMillis();
		consumer.callService("logical-adress", personnummer, processingStatusHolder, responseHolder);
		log.info("Returned #timeslots = " + responseHolder.value.getRequestActivity().size() + " in " + (System.currentTimeMillis() - now) + " ms.");
	
		now = System.currentTimeMillis();
		consumer.callService("logical-adress", personnummer, processingStatusHolder, responseHolder);
		log.info("Returned #timeslots = " + responseHolder.value.getRequestActivity().size() + " in " + (System.currentTimeMillis() - now) + " ms.");
	
	}
	
	public RequestActivitiesTestConsumer(String serviceAddress) {
		JaxWsProxyFactoryBean proxyFactory = new JaxWsProxyFactoryBean();
		proxyFactory.setServiceClass(GetRequestActivitiesResponderInterface.class);
		proxyFactory.setAddress(serviceAddress);
		
		// Used for HTTPS
		SpringBusFactory bf = new SpringBusFactory();
		URL cxfConfig = RequestActivitiesTestConsumer.class.getClassLoader().getResource("cxf-test-consumer-config.xml");
		if (cxfConfig != null) {
			proxyFactory.setBus(bf.createBus(cxfConfig));
		}

		_service  = (GetRequestActivitiesResponderInterface) proxyFactory.create(); 

//		try {
//            URL url =  new URL(serviceAddress + "?wsdl");
//            _service = new GetAggregatedRequestActivitiesResponderService(url).getGetAggregatedRequestActivitiesResponderPort();
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("Malformed URL Exception: " + e.getMessage());
//        }
	}

	public void callService(String logicalAddress, String id, Holder<ProcessingStatusType> processingStatusHolder, Holder<GetRequestActivitiesResponseType> responseHolder) {

		log.debug("Calling GetRequestActivities-soap-service with id = {}", id);
		
		GetRequestActivitiesType request = new GetRequestActivitiesType();
		request.setSubjectOfCareId(id);

		GetRequestActivitiesResponseType response = _service.getRequestActivities(logicalAddress, request);
		responseHolder.value = response;
		
		processingStatusHolder.value = SoapHeaderCxfInterceptor.getLastFoundProcessingStatus();
	}
}