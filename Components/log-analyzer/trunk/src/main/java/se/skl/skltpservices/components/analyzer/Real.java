package se.skl.skltpservices.components.analyzer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;


/**
 * Real implementation annotation, have to have production or test profile defined.
 * 
 * @author Peter
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Profile({"production", "test" })
public @interface Real {
}