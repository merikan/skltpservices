package se.skl.skltpservices.components.analyzer.domain;

import org.soitoolkit.commons.logentry.schema.v1.LogEvent;

/**
 * Stores camel events in a persistent store.
 * 
 * @author Peter
 */
public interface LogStoreRepository {

	/**
	 * Stores information.
	 * 
	 * @param inforEvent the event to store.
	 */
	void storeInfoEvent(LogEvent infoEvent);
	
	/** 
	 * Stores errors.
	 * 
	 * @param errorEvent the event to store.
	 */
	void storeErrorEvent(LogEvent errorEvent);
}
