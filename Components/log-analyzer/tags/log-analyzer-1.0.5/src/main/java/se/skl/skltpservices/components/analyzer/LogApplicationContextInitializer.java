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
package se.skl.skltpservices.components.analyzer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;


/**
 * Initializes profile to use from local settings file.
 * 
 * @author Peter
 */
public class LogApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static Logger LOG = LoggerFactory.getLogger(LogApplicationContextInitializer.class);

    private static String CFG_FILE = "file:" + System.getProperty("user.home") + "/.log-analyzer/config.properties";
    
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment env = applicationContext.getEnvironment();
        try {
            env.getPropertySources().addFirst(new ResourcePropertySource(CFG_FILE));
            boolean analysisDisabled = Boolean.valueOf(env.getProperty("log.storeDisabled", "false"));
            if (analysisDisabled) {
                env.setActiveProfiles("zero");
            } else {
                env.setActiveProfiles("production");
            }
            LOG.info("Initializing with Spring Profiles: \"{}\"",  env.getActiveProfiles());
            LOG.info("local properties loaded", CFG_FILE);
        } catch (IOException e) {
            LOG.error("Unable to load local settings: " + CFG_FILE);
        }
    }
}