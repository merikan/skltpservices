package se.skl.skltpservices.components.analyzer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;


/**
 * Zero implementation annotation, have to have zero profile defined. <p>
 * 
 * Currently used to control which implementation to enable.
 * 
 * @see LogServiceConfig
 *
 * @author Peter
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Profile("zero")
public @interface Zero {
}
