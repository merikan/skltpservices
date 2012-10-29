package se.skl.skltpservices.adapter.apse.hamtapatientinfo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.soitoolkit.commons.mule.test.AbstractTestCase;

import se.riv.se.apotekensservice.axs.hamtapatientinforesponder.v1.HamtaPatientInfoResponseType;

public class HamtaPatientInfoIntegrationTest extends AbstractTestCase {

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
		return "ApSeIntegrationComponent-common.xml,services/AXS-HamtaPatintInfo-apse-service.xml,ApSeIntegrationComponent-teststubs-and-services-config.xml";
    }

    @Test
    public void testRequestSsnWithCompleteArgosHeader() throws Exception {
	String ssn = "196308212817";
	String to = "1234567";

	HamtaPatientInfoResponseType response = new HamtaPatientInfoTestConsumer("http://localhost:11000/tb/HamtaPatientInfoResponder/V1")
		.requestIncludingCompleteArgosInformation(ssn, to);

	assertNotNull(response);
	assertTrue(response.isFinnsOrdination());
    }

    @Test
    public void testEncodingIsProperThroughIntegration() throws Exception {
	String ssn = "ÅÄÖ";
	String to = "1234567";

	HamtaPatientInfoResponseType response = new HamtaPatientInfoTestConsumer("http://localhost:11000/tb/HamtaPatientInfoResponder/V1")
		.requestIncludingCompleteArgosInformation(ssn, to);

	assertNotNull(response);
	assertEquals("ÅÄÖ", response.getDosproducent());
    }

}
