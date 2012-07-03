package se.skl.skltpservices.supervisor.trigger.transformers;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;
import org.mule.tck.junit4.AbstractMuleContextTestCase;

import se.skl.skltpservices.supervisor.trigger.transformers.EndpointExtractionTransformer;

public class EndpointExtractionTransformerTest extends AbstractMuleContextTestCase {

    @Test
    public void testTransformer() throws Exception {
        // Read input and expected result from testfiles
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("id", 1L);
        message.put("serviceUrl", "http://example.com:8080/path/to/service");

        // Create the transformer under test and let it perform the transformation
        EndpointExtractionTransformer transformer = new EndpointExtractionTransformer();
        MuleMessage result = (MuleMessage) transformer.transformMessage(new DefaultMuleMessage(message, muleContext),
                "UTF-8");

        // Compare the result to the expected value
        Set<String> propertyNames = result.getPropertyNames(PropertyScope.OUTBOUND);
        assertTrue(propertyNames.contains("host"));
        assertTrue(propertyNames.contains("port"));
        assertTrue(propertyNames.contains("path"));
        assertTrue(propertyNames.contains("producerId"));
        assertEquals("example.com", (String)result.getProperty("host", PropertyScope.OUTBOUND));
        assertEquals(8080, result.getProperty("port", PropertyScope.OUTBOUND));
        assertEquals("/path/to/service", (String)result.getProperty("path", PropertyScope.OUTBOUND));
        assertEquals("1", result.getProperty("producerId", PropertyScope.OUTBOUND));

    }
}