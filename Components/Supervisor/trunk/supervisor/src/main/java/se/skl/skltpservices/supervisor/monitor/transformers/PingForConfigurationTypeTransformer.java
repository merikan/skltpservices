package se.skl.skltpservices.supervisor.monitor.transformers;

import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.skl.riv.itintegration.monitoring.pingforconfigurationresponder.v1.PingForConfigurationType;

public class PingForConfigurationTypeTransformer extends AbstractTransformer {

    private static final Logger log = LoggerFactory.getLogger(PingForConfigurationTypeTransformer.class);

    @Override
    protected Object doTransform(Object src, String encoding) throws TransformerException {

        PingForConfigurationType type = new PingForConfigurationType();
        type.setLogicalAddress("dummy-address");
        type.setServiceContractNamespace("dummy-service-contract");

        if (logger.isDebugEnabled()) {
            log.debug("doTransform(" + src.getClass().getSimpleName() + ", " + encoding + ") returns: " + type);
        }

        return new Object[] { "SE165565594230-1000", type };
    }
}