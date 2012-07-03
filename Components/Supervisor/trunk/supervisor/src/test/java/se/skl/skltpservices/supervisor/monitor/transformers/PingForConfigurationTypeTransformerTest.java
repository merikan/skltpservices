package se.skl.skltpservices.supervisor.monitor.transformers;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.mule.api.transformer.TransformerException;
import org.mule.util.StringUtils;
import org.soitoolkit.commons.mule.util.MiscUtil;

import se.skl.riv.itintegration.monitoring.pingforconfigurationresponder.v1.PingForConfigurationType;
import se.skl.skltpservices.supervisor.monitor.transformers.PingForConfigurationTypeTransformer;

public class PingForConfigurationTypeTransformerTest {

    @Test
    public void testTransformer() throws IOException, TransformerException {

        // Read input and expected result from testfiles
        String input = MiscUtil.readFileAsString("src/test/resources/testfiles/pingForConfiguration/input.txt");

        // Create the transformer under test and let it perform the transformation
        PingForConfigurationTypeTransformer transformer = new PingForConfigurationTypeTransformer();
        Object[] result = (Object[]) transformer.doTransform(input, "UTF-8");

        // Compare the result to the expected value
        assertEquals("SE165565594230-1000", result[0]);
        PingForConfigurationType type = (PingForConfigurationType) result[1];
        Assert.assertFalse(StringUtils.isBlank(type.getLogicalAddress()));
        Assert.assertFalse(StringUtils.isBlank(type.getServiceContractNamespace()));
    }

}