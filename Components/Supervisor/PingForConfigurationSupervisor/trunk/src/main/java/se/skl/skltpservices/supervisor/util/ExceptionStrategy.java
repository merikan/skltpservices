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
package se.skl.skltpservices.supervisor.util;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;

import org.mule.api.MessagingException;
import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;
import org.mule.config.ExceptionHelper;
import org.mule.exception.DefaultMessagingExceptionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs error events on any kind of exception, and should be used for all VP
 * services.
 * 
 * @author Peter
 * @since VP-2.0
 */
public class ExceptionStrategy extends DefaultMessagingExceptionStrategy {
	private static final Logger log = LoggerFactory.getLogger(ExceptionStrategy.class);

	private final EventLogger eventLogger;

	public ExceptionStrategy(MuleContext muleContext) {
		super(muleContext);
		this.eventLogger = new EventLogger(muleContext);
	}

	/**
	 * Setter for the jaxbContext
	 * 
	 * @param jaxbContext
	 */
	public void setJaxbContext(JAXBContext jaxbContext) {
		this.eventLogger.setJaxbContext(jaxbContext);
	}

	//
	static String nvl(String s) {
		return (s == null) ? "" : s;
	}

	@Override
	protected void logException(Throwable t) {

		log.debug("Entering VPExceptionStrategy...");

		Map<String, String> extraInfo = new HashMap<String, String>();
		extraInfo.put("source", getClass().getName());

		MuleException muleException = ExceptionHelper.getRootMuleException(t);
		if (muleException != null) {

			if (muleException instanceof MessagingException) {
				MessagingException me = (MessagingException) muleException;

				MuleMessage msg = me.getEvent().getMessage();

				Throwable ex = (me.getCause() == null ? me : me.getCause());

				msg.setProperty(Constants.SESSION_ERROR, Boolean.TRUE, PropertyScope.SESSION);
				msg.setProperty(Constants.SESSION_ERROR_DESCRIPTION, nvl(ex.getMessage()), PropertyScope.SESSION);
				msg.setProperty(Constants.SESSION_ERROR_TECHNICAL_DESCRIPTION, nvl(ex.toString()), PropertyScope.SESSION);
				msg.setProperty(Constants.SESSION_ERROR, Boolean.TRUE, PropertyScope.SESSION);

				eventLogger.addSessionInfo(msg, extraInfo);
				eventLogger.logErrorEvent(ex, msg, null, extraInfo);

			} else {
				Map<?, ?> info = ExceptionHelper.getExceptionInfo(muleException);
				eventLogger.logErrorEvent(muleException, info.get("Payload"), null, extraInfo);
			}

		} else {
			eventLogger.logErrorEvent(t, "", null, extraInfo);
		}

	}
}
