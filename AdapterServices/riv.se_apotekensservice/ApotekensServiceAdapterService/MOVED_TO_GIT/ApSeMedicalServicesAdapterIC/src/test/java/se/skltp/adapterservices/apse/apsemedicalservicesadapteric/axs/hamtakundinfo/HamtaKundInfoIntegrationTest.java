package se.skltp.adapterservices.apse.apsemedicalservicesadapteric.axs.hamtakundinfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static se.skltp.adapterservices.apse.apsemedicalservicesadapteric.ApSeMedicalServicesAdapterICMuleServer.getAddress;
import static se.skltp.adapterservices.apse.apsemedicalservicesadapteric.ArgosHeaderTestUtil.createOrganizationArgosHeader;
import static se.skltp.adapterservices.apseadapter.axs.hamtafolkinfo.HamtaFolkInfoTestproducer.SSN_OK;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.test.AbstractJmsTestUtil;
import org.soitoolkit.commons.mule.test.ActiveMqJmsTestUtil;
import org.soitoolkit.commons.mule.test.junit4.AbstractTestCase;

import se.riv.se.apotekensservice.axs.hamtakundinforesponder.v1.HamtaKundInfoResponseType;

public class HamtaKundInfoIntegrationTest extends AbstractTestCase {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(HamtaKundInfoIntegrationTest.class);

	private static final String DEFAULT_SERVICE_ADDRESS = getAddress("inbound.endpoint.http.apotekensservice.axs.HamtaKundInfo.v1");
	private static final String ERROR_LOG_QUEUE = "SOITOOLKIT.LOG.ERROR";
	private AbstractJmsTestUtil jmsUtil = null;

	public HamtaKundInfoIntegrationTest() {

		// Only start up Mule once to make the tests run faster...
		// Set to false if tests interfere with each other when Mule is started
		// only once.
		setDisposeContextPerClass(true);
	}

	@Override
	protected void doSetUp() throws Exception {
		super.doSetUp();

		doSetUpJms();
	}

	private void doSetUpJms() {
		// TODO: Fix lazy init of JMS connection et al so that we can create
		// jmsutil in the declaration
		// (The embedded ActiveMQ queue manager is not yet started by Mule when
		// jmsutil is delcared...)
		if (jmsUtil == null)
			jmsUtil = new ActiveMqJmsTestUtil();

		// Clear queues used for error handling
		jmsUtil.clearQueues(ERROR_LOG_QUEUE);
	}

	@Override
	protected String getConfigResources() {
		return "soitoolkit-mule-jms-connector-activemq-embedded.xml," + "ApSeMedicalServicesAdapterIC-common.xml,"
				+ "services/AXS-HamtaKundInfo-v1-apse-service.xml,"
				+ "teststub-services/AXS-HamtaKundInfo-apse-teststub-service.xml";
	}

	@Test
	public void hamtaKundInfo_happydays() throws Exception {

		HamtaKundInfoResponseType response = new HamtaKundInfoTestconsumer(DEFAULT_SERVICE_ADDRESS).callService(SSN_OK,
				createOrganizationArgosHeader());

		assertNotNull(response);
		assertEquals("OK", response.getKundinformation().getStatusKod());
	}
}
