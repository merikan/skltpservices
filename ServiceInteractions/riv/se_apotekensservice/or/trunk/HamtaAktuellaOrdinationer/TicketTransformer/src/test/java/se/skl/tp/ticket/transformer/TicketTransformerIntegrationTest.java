package se.skl.tp.ticket.transformer;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.soitoolkit.commons.mule.test.AbstractTestCase;

import se.riv.se.apotekensservice.or.hamtaaktuellaordinationerresponder.v1.HamtaAktuellaOrdinationerResponseType;
import se.skl.tp.ticket.transformer.testconsumer.HamtaAllaAktuellaOrdinationerTestConsumer;

public class TicketTransformerIntegrationTest extends AbstractTestCase {

    public TicketTransformerIntegrationTest() {
	super();
	setDisposeManagerPerSuite(true);
	setTestTimeoutSecs(120);
    }

    protected String getConfigResources() {
	return "services/ticketTransformerService-service.xml,"
		+ "teststub-services/ticketTransformerService-teststub-service.xml";
    }

    @Before
    protected void doSetUp() throws Exception {
	super.doSetUp();
    }

    @Test
    public void testRequestSsnWithCompleteArgosHeader() throws Exception {
	String ssn = "196308212817";
	String to = "1234567";

	HamtaAktuellaOrdinationerResponseType response = new HamtaAllaAktuellaOrdinationerTestConsumer()
		.requestIncludingCompleteArgosInformation(ssn, to);

	Assert.assertNotNull(response);
    }

}
