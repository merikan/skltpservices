package se.skl.skltpservices.components.log.services;

import org.apache.camel.Exchange;

/**
 * Stores camel events in a persistent store.
 * 
 * @author Peter
 */
public interface LogStore {

	/**
	 * Stores information.
	 * 
	 * @param exchange the exchange.
	 */
	void info(Exchange exchange);
	
	/** 
	 * Stores errors.
	 * 
	 * @param exchange the exchange.
	 */
	void error(Exchange exchange);
}
