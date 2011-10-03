package se.skl.tp.ticket.transformer;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.test.AbstractTestCase;

import se.riv.se.apotekensservice.or.hamtaaktuellaordinationerresponder.v1.HamtaAktuellaOrdinationerResponseType;

public class TicketTransformerIntegrationTest extends AbstractTestCase {

    private static final Logger log = LoggerFactory.getLogger(TicketTransformerIntegrationTest.class);

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
    public void requestSsnWithCompleteArgosHeader() throws Exception {
	String ssn = "196308212817";
	String to = "1234567";

	HamtaAktuellaOrdinationerResponseType response = new TicketTransformerTestConsumer()
		.requestIncludingCompleteArgosInformation(ssn, to);

	Assert.assertNotNull(response);
    }

}
