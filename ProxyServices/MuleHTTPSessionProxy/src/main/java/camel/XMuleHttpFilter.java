package camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;


/**
 * Created with IntelliJ IDEA.
 * User: khaled.daham@callistaenterprise.se
 */
public class XMuleHttpFilter {
    public static void main(String args[]) throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            public void configure() {
                from("jetty:http://0.0.0.0:8082/?matchOnUriPrefix=true")
                .removeHeaders("X-MULE*")
                .to("jetty:http://localhost:8081/?bridgeEndpoint=true&throwExceptionOnFailure=false");
            }
        });
        context.start();
    }
}