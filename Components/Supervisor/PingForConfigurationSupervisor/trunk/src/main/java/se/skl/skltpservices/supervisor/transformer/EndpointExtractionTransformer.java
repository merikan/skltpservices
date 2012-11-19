/**
 * Copyright (c) 2012, Sjukvardsradgivningen. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package se.skl.skltpservices.supervisor.transformer;

import java.net.MalformedURLException;
import java.net.URL;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.transformer.AbstractMessageTransformer;
import org.mule.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.skl.skltpservices.supervisor.util.Constants;

/**
 * Extracts producer Endpoint from payload.
 * 
 * @since VP-2.0
 * @author Anders
 *
 */
public class EndpointExtractionTransformer extends AbstractMessageTransformer {


	private static final Logger log = LoggerFactory.getLogger(EndpointExtractionTransformer.class);

    @Override
    public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
        String record = message.getPayload(String.class);
        
        String[] fields = record.split(",");
        String endpoint = fields[0].trim();
        String system = fields[1].trim();
        String domain = fields[2].trim();
        String subDomain = fields[3].trim();

        if (StringUtils.isBlank(endpoint)) {
            String msg = "Ignoring endpoint since it's blank";
            log.warn(msg);
            throw new RuntimeException(msg);
        }
        
        URL endpointUrl;
        try {
            endpointUrl = new URL(endpoint);
        } catch (MalformedURLException e) {
            String msg = "Ignoring endpoint since it's illegal";
            log.warn(msg);
            throw new RuntimeException(msg);
        }

        if (logger.isDebugEnabled()) {
            log.debug("doTransform(" + this.getClass().getSimpleName() + ", " + encoding + ") returns: " + message);
        }

        message.setProperty("protocol", endpointUrl.getProtocol(), PropertyScope.OUTBOUND);
        message.setProperty("host", endpointUrl.getHost(), PropertyScope.OUTBOUND);
        if (endpointUrl.getPort() > 0) {
        	message.setProperty("port", ":" + endpointUrl.getPort(), PropertyScope.OUTBOUND);
        } else {
        	message.setProperty("port", "", PropertyScope.OUTBOUND);
        }
        message.setProperty("path", endpointUrl.getPath(), PropertyScope.OUTBOUND);
        
        message.setProperty(Constants.ENDPOINT_URL, endpoint, PropertyScope.SESSION);
        message.setProperty(Constants.DOMAIN, domain, PropertyScope.SESSION);
        message.setProperty(Constants.SUB_DOMAIN, subDomain, PropertyScope.SESSION);
        message.setProperty(Constants.SYSTEM_NAME, system, PropertyScope.SESSION);

        return message;
    }
    
}
