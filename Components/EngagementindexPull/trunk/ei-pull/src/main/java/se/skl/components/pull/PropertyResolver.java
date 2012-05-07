package se.skl.components.pull;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

/**
 * Author: Henrik Rostam
 **/

public class PropertyResolver extends PropertyPlaceholderConfigurer {

    private static Map<String, String> propertyStore = new HashMap<String, String>();

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties properties) throws BeansException {
        // Instantiate and populate the properties
        super.processProperties(beanFactory, properties);
        for (Object key : properties.keySet()) {
            String stringKey = key.toString();
            String stringValue = parseStringValue(properties.getProperty(stringKey), properties, new HashSet());
            propertyStore.put(stringKey, stringValue);
        }
    }

    public static String get(String name) {
        return propertyStore.get(name);
    }
}

