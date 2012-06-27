package se.skl.skltpservices.components.analyzer.services;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class RuntimeStatusDeserializer extends JsonDeserializer<RuntimeStatus> {

    @Override
    public RuntimeStatus deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
            JsonProcessingException {
        while (jp.nextValue() != null){
        }
            System.out.println("Parsing: " + jp.getCurrentName());
        return null;
    }

}
