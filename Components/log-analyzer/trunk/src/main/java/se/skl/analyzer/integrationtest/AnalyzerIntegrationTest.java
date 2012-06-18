package se.skl.analyzer.integrationtest;

import java.util.UUID;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultProducerTemplate;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

public class AnalyzerIntegrationTest {

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode rootNode = mapper.createObjectNode(); // will be of type ObjectNode
        ObjectNode objectNode = (ObjectNode) rootNode;
        objectNode.put("id", UUID.randomUUID().toString());
        objectNode.put("timestamp", System.currentTimeMillis());
        objectNode.put("correlationId", "corrId");
        objectNode.put("component", "Component");
        objectNode.put("client", "Client");
        objectNode.put("server", "Server");
        objectNode.put("state", "START");
        objectNode.put("message", "Message - Payload");
        System.out.println(rootNode.toString());

        DefaultCamelContext camelContext = new DefaultCamelContext();
        camelContext.addComponent("activemq", ActiveMQComponent.activeMQComponent("tcp://localhost:61616"));
        ProducerTemplate template = new DefaultProducerTemplate(camelContext);
        template.start();
        template.sendBody("activemq:queue:sendToAnalyser", rootNode.toString());
        
        rootNode = mapper.createObjectNode();
        objectNode = (ObjectNode) rootNode;
        objectNode.put("id", UUID.randomUUID().toString());
        objectNode.put("timestamp", System.currentTimeMillis());
        objectNode.put("correlationId", "corrId");
        objectNode.put("component", "Component");
        objectNode.put("client", "Client");
        objectNode.put("server", "Server");
        objectNode.put("state", "SUCCESS");
        objectNode.put("message", "Message - Payload");

        template.sendBody("activemq:queue:sendToAnalyser", rootNode.toString());
        template.stop();

    }
}
