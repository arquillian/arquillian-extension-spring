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
package org.jboss.arquillian.spring.configuration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>Utility class for {@link SpringExtensionRemoteConfiguration}.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class SpringExtensionRemoteConfigurationUtils {

    /**
     * <p>Represents the name of the properties files used for passing the settings to the remote container.</p>
     */
    public static final String SPRING_REMOTE_PROPERTIES = "arquillian-spring-remote-configuration.properties";

    /**
     * <p>Represents the name of property that stores the custom context class name.</p>
     */
    public static final String CUSTOM_CONTEXT_CLASS_PROPERTY_NAME = "customContextClass";

    /**
     * <p>Represents the name of property that stores the custom annotation context class name.</p>
     */
    public static final String CUSTOM_ANNOTATION_CONTEXT_CLASS_PROPERTY_NAME = "customAnnotationContextClass";

    /**
     * <p>Returns the text representation of the {@link SpringExtensionRemoteConfiguration}.</p>
     *
     * @param config the configuration
     *
     * @return text representation of the passed configuration
     */
    public static String toString(SpringExtensionRemoteConfiguration config) {

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Properties properties = new Properties();
            setPropertyValue(properties, CUSTOM_CONTEXT_CLASS_PROPERTY_NAME, config.getCustomContextClass());
            setPropertyValue(properties, CUSTOM_ANNOTATION_CONTEXT_CLASS_PROPERTY_NAME,
                    config.getCustomAnnotationContextClass());

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
    public static SpringExtensionRemoteConfiguration loadResource(InputStream inputStream) {

        try {
            SpringExtensionRemoteConfiguration springExtensionRemoteConfiguration =
                    new SpringExtensionRemoteConfiguration();

            if (inputStream != null) {
                Properties properties = new Properties();
                properties.load(inputStream);

                springExtensionRemoteConfiguration.setCustomContextClass(
                        getPropertyValue(properties, CUSTOM_CONTEXT_CLASS_PROPERTY_NAME));
                springExtensionRemoteConfiguration.setCustomAnnotationContextClass(
                        getPropertyValue(properties, CUSTOM_ANNOTATION_CONTEXT_CLASS_PROPERTY_NAME));
            }

            return springExtensionRemoteConfiguration;

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