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

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.riv.itintegration.monitoring.v1.PingForConfigurationType;

/**
 * Creates a PingforConfiguration request payload..
 * 
 * @since VP-2.0
 * @author Anders
 */
public class PingForConfigurationTypeTransformer extends AbstractMessageTransformer {

	private static final Logger log = LoggerFactory.getLogger(PingForConfigurationTypeTransformer.class);

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		PingForConfigurationType type = new PingForConfigurationType();

		if (logger.isDebugEnabled()) {
			log.debug("doTransform(" + message.getClass().getSimpleName() + ", " + encoding + ") returns: " + type);
		}
		
		return new Object[] { "SE165565594230-1000", type };
	}
}
