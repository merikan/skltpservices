package se.skl.skltpservices.supervisor.monitor;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.jms.JMSException;
import javax.sql.DataSource;

import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jdbc.JdbcScriptEngine;
import org.soitoolkit.commons.mule.jdbc.JdbcUtil;
import org.soitoolkit.commons.mule.test.AbstractJmsTestUtil;
import org.soitoolkit.commons.mule.test.ActiveMqJmsTestUtil;
import org.soitoolkit.commons.mule.test.junit4.AbstractTestCase;
import org.soitoolkit.commons.mule.util.MiscUtil;

public class PingMonitorIntegrationTest extends AbstractTestCase {

    private static final Logger log = LoggerFactory.getLogger(PingMonitorIntegrationTest.class);

    private static final String INFO_LOG_QUEUE = "SOITOOLKIT.LOG.INFO";
    private static final String ERROR_LOG_QUEUE = "SOITOOLKIT.LOG.ERROR";
    private AbstractJmsTestUtil jmsUtil = null;

    /**
     * 
     * DLQ tests expects the following setup in activemq.xml (in the <policyEntry> - element): <deadLetterStrategy> <!--
     * Use the prefix 'DLQ.' for the destination name, and make the DLQ a queue rather than a topic -->
     * <individualDeadLetterStrategy queuePrefix="DLQ." useQueueForQueueMessages="true" /> </deadLetterStrategy>
     * 
     */
    public PingMonitorIntegrationTest() {

        // Only start up Mule once to make the tests run faster...
        // Set to false if tests interfere with each other when Mule is started only once.
        setDisposeContextPerClass(true);
    }

    protected String getConfigResources() {
        return "soitoolkit-mule-jms-connector-activemq-embedded.xml," +

        "soitoolkit-mule-jdbc-datasource-hsql-embedded.xml," + "skltpMonitor-jdbc-connector.xml," +

        "skltpMonitor-common.xml," + "skltpMonitor-integrationtests-common.xml," +
        // FIXME. MULE STUDIO.
        // "services/pingMonitor-service.xml," +
                "pingMonitor-service.xml," + "teststub-services/pingMonitor-teststub-service.xml";
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

        // Clear queues used for the outbound endpoint
        jmsUtil.clearQueues(INFO_LOG_QUEUE);

        // Clear queues used for error handling
        jmsUtil.clearQueues(ERROR_LOG_QUEUE);
    }

    private void doSetUpDb() throws FileNotFoundException {
        DataSource ds = JdbcUtil.lookupDataSource(muleContext, "soitoolkit-jdbc-datasource");
        JdbcScriptEngine se = new JdbcScriptEngine(ds);

        try {
            se.execute("src/environment/setup/skltpMonitor-db-drop-tables.sql");
        } catch (Throwable ex) {
            log.warn("Drop db script failed, maybe no db exists? " + ex.getMessage());
        }
        se.execute("src/environment/setup/skltpMonitor-db-create-tables.sql");
        se.execute("src/environment/setup/skltpMonitor-db-insert-testdata.sql");
    }

//    @Test
    public void testPingMonitor_ok() throws JMSException, MalformedURLException {

        Map<String, String> props = new HashMap<String, String>();
        final String inputFile = "src/test/resources/testfiles/endpoint/input.txt";
        String receivingService = "pingMonitor-teststub-service";

        final int timeout = 5000;

        String input = MiscUtil.readFileAsString(inputFile);

        StringTokenizer st = new StringTokenizer(input, ",");
        String inId = st.nextToken().split("=")[1];
        String inEndpoint = st.nextToken().split("=")[1];
        String inDomain = st.nextToken().split("=")[1];
        String inboundEndpoint = "jdbc://INSERT INTO PINGMONITOR_EXPORT_TB(ID, ENDPOINT, DOMAIN) VALUES ('" + inId
                + "', '" + inEndpoint + "', '" + inDomain + "')";

        URL url = new URL(inboundEndpoint);
        String expectedHost = url.getHost();
        String expectedPort = Integer.toString(url.getPort());
        String expectedPath = url.getPath();

        // Invoke the service and wait for the transformed message to arrive at the receiving teststub service
        MuleMessage reply = dispatchAndWaitForServiceComponent(inboundEndpoint, input, props, receivingService, timeout);

        String host = reply.getProperty("host", PropertyScope.INBOUND);
        String port = reply.getProperty("port", PropertyScope.INBOUND);
        String path = reply.getProperty("path", PropertyScope.INBOUND);

        // Verify the result, i.e. the transformed message
        assertEquals(expectedHost, host);
        assertEquals(expectedPort, port);
        assertEquals(expectedPath, path);

        // Verify outbound jms-queues
        assertEquals(0, jmsUtil.browseMessagesOnQueue(INFO_LOG_QUEUE).size());

        // Verify error-queue
        assertEquals(0, jmsUtil.browseMessagesOnQueue(ERROR_LOG_QUEUE).size());
    }

}
