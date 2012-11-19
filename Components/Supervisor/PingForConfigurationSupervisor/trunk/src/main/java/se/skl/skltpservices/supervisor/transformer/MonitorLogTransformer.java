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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.JAXBContext;

import org.mule.api.ExceptionPayload;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.context.MuleContextAware;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.transformer.AbstractMessageTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.api.log.EventLogMessage;
import org.soitoolkit.commons.mule.api.log.EventLogger;
import org.soitoolkit.commons.mule.log.DefaultEventLogger;
import org.soitoolkit.commons.mule.log.EventLoggerFactory;

import se.skl.skltpservices.supervisor.util.Constants;

/**
 * Transforms for active monitoring (PingForConfiguration).
 * 
 * @author Peter
 * 
 */
public class MonitorLogTransformer extends AbstractMessageTransformer implements MuleContextAware {

	private static final String RIVTABP21 = "RIVTABP21";

	private static final String NAMESPACE_PING_FOR_CONFIG = "urn:riv:itintegration:monitoring:PingForConfiguration:1:rivtabp21";

	private static final String XSD = "xsd";

	private static final String WSDL = "wsdl";

	private static final String HTTP_QUERY_STRING = "http.query.string";

	private static final Logger log = LoggerFactory.getLogger(MonitorLogTransformer.class);

	private EventLogger eventLogger;

	private JAXBContext jaxbContext = null;

	@Override
	public void setMuleContext(MuleContext muleContext) {
		super.setMuleContext(muleContext);

		log.debug("MuleContext injected");

		// Also inject the muleContext in the event-logger (since we create the
		// event-logger for now)
		if (eventLogger == null) {
			eventLogger = EventLoggerFactory.getEventLogger(muleContext);
		}

		// TODO: this is an ugly workaround for injecting the jaxbObjToXml
		// dependency ...
		if (eventLogger instanceof DefaultEventLogger) {
			((DefaultEventLogger) eventLogger).setJaxbContext(jaxbContext);
		}
	}

	
	/*
	 * Property logType
	 */
	private String logType = "";

	public void setLogType(String logType) {
		this.logType = logType;
	}

	/*
	 * Property integrationScenario
	 */
	private String integrationScenario = "";

	public void setIntegrationScenario(String integrationScenario) {
		this.integrationScenario = integrationScenario;
	}

	/*
	 * Property contractId
	 */
	private String contractId = "";

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	private Map<String, String> businessContextId;

	public void setBusinessContextId(Map<String, String> businessContextId) {
		this.businessContextId = businessContextId;
	}

	private Map<String, String> extraInfo;

	public void setExtraInfo(Map<String, String> extraInfo) {
		this.extraInfo = extraInfo;
	}

	/**
	 * Setter for the jaxbContext
	 * 
	 * @param jaxbContext
	 */
	public void setJaxbContext(JAXBContext jaxbContext) {
		this.jaxbContext = jaxbContext;

		if (eventLogger instanceof DefaultEventLogger) {
			((DefaultEventLogger) eventLogger).setJaxbContext(jaxbContext);
		}
	}

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {

		Map<String, String> evaluatedExtraInfo = null;
		Map<String, String> evaluatedBusinessContextId = null;
		try {
			// Skip logging if an error has occurred, then the error is logged
			// by an error handler
			ExceptionPayload exp = message.getExceptionPayload();
			if (exp != null) {
				log.debug("Skip logging message, exception detected! " + exp.getException().getMessage());
				return message;
			}

			String httpQS = message.getInboundProperty(HTTP_QUERY_STRING);
			if (WSDL.equalsIgnoreCase(httpQS) || XSD.equalsIgnoreCase(httpQS)) {
				log.debug("Skip logging message, wsdl or xsd call detected!");
				return message;
			}

			evaluatedExtraInfo = evaluateMapInfo(extraInfo, message);
			evaluatedBusinessContextId = evaluateMapInfo(businessContextId, message);

			if (evaluatedExtraInfo == null) {
				evaluatedExtraInfo = new HashMap<String, String>();
			}

			evaluatedExtraInfo.put(Constants.SOURCE, getClass().getName());
			Constants.addSessionInfo(message, evaluatedExtraInfo);

			if (message.getProperty(Constants.SERVICE_NAMESPACE, PropertyScope.SESSION) == null) {
				message.setProperty(Constants.SERVICE_NAMESPACE,
						NAMESPACE_PING_FOR_CONFIG, PropertyScope.SESSION);
				message.setProperty(Constants.RIV_VERSION, RIVTABP21, PropertyScope.SESSION);
			}

			if (log.isDebugEnabled()) {
				log.debug(toDebugLogString(evaluatedExtraInfo, evaluatedBusinessContextId));
			}
			
			EventLogMessage infoMsg = new EventLogMessage();
			infoMsg.setLogMessage(logType);
			infoMsg.setMuleMessage(message);
			infoMsg.setIntegrationScenario(integrationScenario);
			infoMsg.setContractId(contractId);
			infoMsg.setBusinessContextId(evaluatedBusinessContextId);
			infoMsg.setExtraInfo(evaluatedExtraInfo);

			eventLogger.logInfoEvent(infoMsg);
			
		} catch (Exception e) {
			log.error(toDebugLogString(evaluatedExtraInfo, evaluatedBusinessContextId), e);
		}

		return message;

	}


	// returns a context log mesage
	private String toDebugLogString(Map<String, String> evaluatedExtraInfo,
			Map<String, String> evaluatedBusinessContextId) {
		return String.format("LogEvent [logType: %s, intergrationScenario: %s, contractId: %s, %s, %s]", logType,
				integrationScenario, contractId, toString("businessContextId", evaluatedBusinessContextId),
				toString("extraInfo", evaluatedExtraInfo));
	}

	// returns a log string
	private static String toString(String name, Map<String, String> map) {
		StringBuilder b = new StringBuilder();
		b.append(name);
		b.append(": [");
		if (map != null) {
			boolean d = false;
			for (Map.Entry<String, String> e : map.entrySet()) {
				if (d) {
					b.append(", ");
				} else {
					d = true;
				}
				b.append(String.format("[key: %s, value: %s]", e.getKey(), e.getValue()));
			}
		}
		b.append("]");
		return b.toString();
	}

	//
	private Map<String, String> evaluateMapInfo(Map<String, String> map, MuleMessage message) {

		if (map == null)
			return null;

		Set<Entry<String, String>> ei = map.entrySet();
		Map<String, String> evaluatedMap = new HashMap<String, String>();
		for (Entry<String, String> entry : ei) {
			String key = entry.getKey();
			String value = entry.getValue();
			value = evaluateValue(key, value, message);
			evaluatedMap.put(key, value);
		}
		return evaluatedMap;
	}

	private String evaluateValue(String key, String value, MuleMessage message) {
		try {
			if (isValidExpression(value)) {
				String before = value;
				Object eval = muleContext.getExpressionManager().evaluate(value.toString(), message);

				if (eval == null) {
					value = "UNKNOWN";

				} else if (eval instanceof List) {
					@SuppressWarnings("rawtypes")
					List l = (List) eval;
					value = l.get(0).toString();

				} else {
					value = eval.toString();
				}
				if (log.isDebugEnabled()) {
					log.debug("Evaluated extra-info for key: " + key + ", " + before + " ==> " + value);
				}
			}
		} catch (Throwable ex) {
			String errMsg = "Faild to evaluate expression: " + key + " = " + value;
			log.warn(errMsg, ex);
			value = errMsg + ", " + ex;
		}
		return value;
	}

	//
	private boolean isValidExpression(String expression) {
		try {
			return muleContext.getExpressionManager().isValidExpression(expression);
		} catch (Throwable ex) {
			return false;
		}
	}
}
