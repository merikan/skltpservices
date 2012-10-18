package se.skl.skltpservices.adapter.fk.vardgivare.sjukvard.taemotsvar;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.soitoolkit.commons.mule.test.junit4.AbstractTestCase;

import se.fk.vardgivare.sjukvard.taemotsvarresponder.v1.TaEmotSvarResponseType;

public class TaEmotSvarIntegrationTest extends AbstractTestCase {

	public TaEmotSvarIntegrationTest() {
		// Only start up Mule once to make the tests run faster...
		// Set to false if tests interfere with each other when Mule is started
		// only once.
		setDisposeContextPerClass(true);
	}

	@Before
	public void doSetUp() throws Exception {
		super.doSetUp();
	}

	@Override
	protected String getConfigResources() {
		return "soitoolkit-mule-jms-connector-activemq-embedded.xml,FkIntegrationComponent-common.xml,ReceiveMedicalertificateAnswer-fk-service.xml,teststub-services/ReceiveMedicalertificateAnswer-fk-teststub-service.xml";
	}

	@Test
	public void testTaEmotSvar() throws Exception {

		TaEmotSvarTestConsumer consumer = new TaEmotSvarTestConsumer(
				"https://localhost:12000/tb/fk/ifv/TaEmotSvar/1/rivtabp20");

		TaEmotSvarResponseType response = consumer.taEmotSvar();

		assertNotNull(response);

	}

}
