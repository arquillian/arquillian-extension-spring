/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.arquillian.spring.integration.configuration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <p>Utility class for exporting and importing {@link SpringIntegrationConfiguration}.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class SpringIntegrationConfigurationExporter {

    /**
     * <p>Represents the name of the properties files used for passing the settings to the remote container.</p>
     */
    public static final String SPRING_REMOTE_PROPERTIES = "arquillian-spring-remote-configuration.properties";

    /**
     * <p>Returns the text representation of the {@link SpringIntegrationConfiguration}.</p>
     *
     * @param config the configuration
     *
     * @return text representation of the passed configuration
     */
    public static String toString(SpringIntegrationConfiguration config) {

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Properties properties = new Properties();

            for (Map.Entry<String, String> property : config.getProperties().entrySet()) {

                setPropertyValue(properties, property.getKey(), property.getValue());
            }

            properties.store(outputStream, "arquillian-spring-remote-configuration");

            return outputStream.toString();
        } catch (IOException e) {

            throw new RuntimeException("Could not write the properties file.", e);
        }
    }

    /**
     * <p>Loads the configuration from the input stream.</p>
     *
     * @param inputStream the input stream with the properties
     *
     * @return the loaded configuration
     */
    public static SpringIntegrationConfiguration loadResource(InputStream inputStream) {

        try {
            Properties props = new Properties();
            props.load(inputStream);

            Map<String, String> properties = new HashMap<String, String>();

            String propertyName;
            Enumeration<?> propertyNames = props.propertyNames();

            while (propertyNames.hasMoreElements()) {
                propertyName = (String) propertyNames.nextElement();
                properties.put(propertyName, getPropertyValue(props, propertyName));
            }

            return new SpringIntegrationConfiguration(properties);

        } catch (IOException e) {

            throw new RuntimeException("Could not load the properties files.", e);
        }
    }

    /**
     * <p>Retrieves the properties value.</p>
     *
     * @param properties   the properties to use
     * @param propertyName the property name
     *
     * @return the property value
     */
    private static String getPropertyValue(Properties properties, String propertyName) {

        String value = properties.getProperty(propertyName);

        if ("".equals(value)) {
            return null;
        }

        return value;
    }

    /**
     * <p>Sets the property value.</p>
     *
     * @param properties   the properties to use
     * @param propertyName the property name
     * @param value        the property value
     */
    private static void setPropertyValue(Properties properties, String propertyName, String value) {

        properties.setProperty(propertyName, getPropertyValueOrDefault(value));
    }

    /**
     * <p>Retrieves the property value or empty string if the value is null.</p>
     *
     * @param value the property value
     *
     * @return the property value
     */
    private static String getPropertyValueOrDefault(String value) {
        return value != null ? value : "";
    }
}
