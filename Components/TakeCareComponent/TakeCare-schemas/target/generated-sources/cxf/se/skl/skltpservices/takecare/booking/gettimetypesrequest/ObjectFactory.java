
package se.skl.skltpservices.takecare.booking.gettimetypesrequest;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the se.skl.skltpservices.takecare.booking.gettimetypesrequest package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: se.skl.skltpservices.takecare.booking.gettimetypesrequest
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ProfdocHISMessage }
     * 
     */
    public ProfdocHISMessage createProfdocHISMessage() {
        return new ProfdocHISMessage();
    }

    /**
     * Create an instance of {@link ProfdocHISMessage.TimeTypes.TimeType }
     * 
     */
    public ProfdocHISMessage.TimeTypes.TimeType createProfdocHISMessageTimeTypesTimeType() {
        return new ProfdocHISMessage.TimeTypes.TimeType();
    }

    /**
     * Create an instance of {@link ProfdocHISMessage.TimeTypes }
     * 
     */
    public ProfdocHISMessage.TimeTypes createProfdocHISMessageTimeTypes() {
        return new ProfdocHISMessage.TimeTypes();
    }

}
