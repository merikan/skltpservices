package se.skl.skltpservices.supervisor.trigger;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.jms.JMSException;
import javax.sql.DataSource;

import org.junit.Test;
import org.mule.api.MuleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jdbc.JdbcScriptEngine;
import org.soitoolkit.commons.mule.jdbc.JdbcUtil;
import org.soitoolkit.commons.mule.test.AbstractJmsTestUtil;
import org.soitoolkit.commons.mule.test.ActiveMqJmsTestUtil;
import org.soitoolkit.commons.mule.test.junit4.AbstractTestCase;
import org.soitoolkit.commons.mule.util.MiscUtil;
import org.soitoolkit.commons.mule.util.RecursiveResourceBundle;

public class ActiveLogTriggerIntegrationTest extends AbstractTestCase {

    private static final Logger log = LoggerFactory.getLogger(ActiveLogTriggerIntegrationTest.class);
    private static final RecursiveResourceBundle rb = new RecursiveResourceBundle("skltpSupervisor-config");

    private static final String OUT_VM_QUEUE = rb.getString("ACTIVELOGTRIGGER_OUT_VM_QUEUE");

    private static final String ERROR_LOG_QUEUE = "SOITOOLKIT.LOG.ERROR";
    private AbstractJmsTestUtil jmsUtil = null;

    /**
     * 
     * DLQ tests expects the following setup in activemq.xml (in the <policyEntry> - element): <deadLetterStrategy> <!--
     * Use the prefix 'DLQ.' for the destination name, and make the DLQ a queue rather than a topic -->
     * <individualDeadLetterStrategy queuePrefix="DLQ." useQueueForQueueMessages="true" /> </deadLetterStrategy>
     * 
     */
    public ActiveLogTriggerIntegrationTest() {

        // Only start up Mule once to make the tests run faster...
        // Set to false if tests interfere with each other when Mule is started only once.
        setDisposeContextPerClass(true);
    }

    protected String getConfigResources() {
        return "soitoolkit-mule-jms-connector-activemq-embedded.xml," +
        "soitoolkit-mule-jdbc-datasource-hsql-embedded.xml," + "skltpSupervisor-jdbc-connector.xml," +
        "skltpSupervisor-common.xml," + "skltpSupervisor-integrationtests-common.xml," +
        // FIXME. MULE STUDIO.
        // "services/activeLogTrigger-service.xml," +
                "activeLogTrigger-service.xml," + "teststub-services/activeLogTrigger-teststub-service.xml";
    }

    @Override
    protected void doSetUp() throws Exception {
        super.doSetUp();
        doSetUpJms();
        doSetUpDb();
    }

    private void doSetUpJms() {
        // TODO: Fix lazy init of JMS connection et al so that we can create jmsutil in the declaration
        // (The embedded ActiveMQ queue manager is not yet started by Mule when jmsutil is delcared...)
        if (jmsUtil == null)
            jmsUtil = new ActiveMqJmsTestUtil();

        // Clear queues used for error handling
        jmsUtil.clearQueues(ERROR_LOG_QUEUE);
    }

    private void doSetUpDb() throws FileNotFoundException {
        DataSource ds = JdbcUtil.lookupDataSource(muleContext, "soitoolkit-jdbc-datasource");
        JdbcScriptEngine se = new JdbcScriptEngine(ds);

        try {
            se.execute("src/environment/setup/skltpSupervisor-db-drop-tables.sql");
        } catch (Throwable ex) {
            log.warn("Drop db script failed, maybe no db exists? " + ex.getMessage());
        }
        se.execute("src/environment/setup/skltpSupervisor-db-create-tables.sql");
        se.execute("src/environment/setup/skltpSupervisor-db-insert-testdata.sql");
    }

    @Test
    public void testActiveLogTrigger_ok() throws JMSException {

        Map<String, String> props = new HashMap<String, String>();
        final String inputFile = "src/test/resources/testfiles/activeLogTrigger/input.txt";
        String expectedResultFile = "src/test/resources/testfiles/activeLogTrigger/expected-result.txt";
        String receivingService = "activeLogTrigger-teststub-service";

        final int timeout = 5000;

        String input = MiscUtil.readFileAsString(inputFile);
        String expectedResult = MiscUtil.readFileAsString(expectedResultFile);

        StringTokenizer st = new StringTokenizer(input, ",");
        String inId = st.nextToken().split("=")[1];
        String inEndpoint = st.nextToken().split("=")[1];
        String inDomain = st.nextToken().split("=")[1];
        String inSubdomain = st.nextToken().split("=")[1];
        String inSystem = st.nextToken().split("=")[1];
        String inMunicipality = st.nextToken().split("=")[1];

        String inboundEndpoint = "jdbc://INSERT INTO ServiceProducer(ID, ENDPOINT, DOMAIN, SUBDOMAIN, SYSTEM, MUNICIPALITY) VALUES ('"
                + inId + "', '" + inEndpoint + "', '" + inDomain + "', '" + inSubdomain + "', '" + inSystem + "', '" + inMunicipality + "')";

        // Invoke the service and wait for the transformed message to arrive at the receiving teststub service
        MuleMessage reply = dispatchAndWaitForServiceComponent(inboundEndpoint, input, props, receivingService, timeout);

        String transformedMessage = reply.getPayload().toString();

        // Remove any new lines added by various transports and transformers, e.g. the email-related ones...
        transformedMessage = MiscUtil.removeTrailingNewLines(transformedMessage);

        // Verify the result, i.e. the transformed message
        // assertEquals(expectedResult, transformedMessage);

        // Verify error-queue
        assertEquals(0, jmsUtil.browseMessagesOnQueue(ERROR_LOG_QUEUE).size());
    }
}
