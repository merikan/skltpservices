package se.skl.tp.ticket.saml;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.soitoolkit.commons.mule.test.AbstractTestCase;

import se.riv.se.apotekensservice.axs.hamtapatientinforesponder.v1.HamtaPatientInfoResponseType;
import se.skl.tp.ticket.testconsumer.HamtaPatientInfoTestConsumer;

public class HamtaPatientInfoSamlTicketTransformerIntegrationTest extends AbstractTestCase {

    @BeforeClass
    public void beforeClass() {
	setDisposeManagerPerSuite(true);
	setTestTimeoutSecs(120);
    }

    @Before
    public void doSetUp() throws Exception {
	super.doSetUp();
	setDisposeManagerPerSuite(true);
    }

    @Override
    protected String getConfigResources() {
	return "HamtaPatientInfo-teststubs-and-services-config.xml";
    }

    @Test
    public void testRequestSsnWithCompleteArgosHeader() throws Exception {
	String ssn = "196308212817";
	String to = "1234567";

	HamtaPatientInfoResponseType response = new HamtaPatientInfoTestConsumer()
		.requestIncludingCompleteArgosInformation(ssn, to);

	assertNotNull(response);
	assertTrue(response.isFinnsOrdination());
    }

}
