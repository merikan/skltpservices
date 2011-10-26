package se.skl.tp.ticket.saml;

import static org.hamcrest.Matchers.containsString;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.soitoolkit.commons.mule.test.AbstractTestCase;

import se.riv.se.apotekensservice.or.hamtaaktuellaordinationerresponder.v1.HamtaAktuellaOrdinationerResponseType;
import se.skl.tp.ticket.testconsumer.HamtaAllaAktuellaOrdinationerTestConsumer;

public class HamtaAktuellaOrdinationerIntegrationTest extends AbstractTestCase {

    @BeforeClass
    public void beforeClass() {
	setDisposeManagerPerSuite(true);
	setTestTimeoutSecs(240);
    }

    @Before
    public void doSetUp() throws Exception {
	super.doSetUp();
	setDisposeManagerPerSuite(true);
    }

    @Override
    protected String getConfigResources() {
	return "TicketTransformer-teststubs-and-services-config.xml";
    }

    @Test
    public void testRequestSsnWithCompleteArgosHeader() throws Exception {
	String ssn = "196308212817";
	String to = "1234567";

	HamtaAktuellaOrdinationerResponseType response = new HamtaAllaAktuellaOrdinationerTestConsumer()
		.requestIncludingCompleteArgosInformation(ssn, to);

	assertNotNull(response);
	assertEquals(ssn, response.getOrdinationslista().getPersonnummer());
    }

    @Test
    public void testRequestWithEncoding() throws Exception {
	String ssn = "ÅÄÖ";
	String to = "1234567";

	HamtaAktuellaOrdinationerResponseType response = new HamtaAllaAktuellaOrdinationerTestConsumer()
		.requestIncludingCompleteArgosInformation(ssn, to);

	assertNotNull(response);
	assertEquals("ÅÄÖ", response.getOrdinationslista().getPersonnummer());
    }

    @Test
    public void testSoapFaultIsReturnedWhenProducerThrowsRuntimeException() throws Exception {
	String ssn = "";
	String to = "1234567";

	try {
	    new HamtaAllaAktuellaOrdinationerTestConsumer().requestIncludingCompleteArgosInformation(ssn, to);
	} catch (javax.xml.ws.soap.SOAPFaultException e) {
	    String faultString = e.getFault().getFaultString();
	    Assert.assertThat(faultString, containsString("Personnummer is mandatory"));
	    return;
	}
	Assert.fail("An exception was expected");
    }

    @Test
    public void testApplicationExceptionThrownByProducer() throws Exception {
	String ssn = "APPLICATIONEXCEPTION";
	String to = "1234567";

	try {
	    new HamtaAllaAktuellaOrdinationerTestConsumer().requestIncludingCompleteArgosInformation(ssn, to);
	} catch (javax.xml.ws.soap.SOAPFaultException e) {
	    String faultString = e.getFault().getFaultString();
	    Assert.assertThat(faultString, containsString("APPLICATIONEXCEPTION"));
	    return;
	}
	Assert.fail("An exception was expected");
    }

    @Test
    public void testSystemExceptionThrownByProducer() throws Exception {
	String ssn = "SYSTEMEXCEPTION";
	String to = "1234567";

	try {
	    new HamtaAllaAktuellaOrdinationerTestConsumer().requestIncludingCompleteArgosInformation(ssn, to);
	} catch (javax.xml.ws.soap.SOAPFaultException e) {
	    String faultString = e.getFault().getFaultString();
	    Assert.assertThat(faultString, containsString("SYSTEMEXCEPTION"));
	    return;
	}
	Assert.fail("An exception was expected");
    }
}
