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

package org.jboss.arquillian.spring.integration.client;

import java.util.Collections;
import java.util.Map;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.config.descriptor.api.ExtensionDef;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.spring.integration.SpringIntegrationConstants;
import org.jboss.arquillian.spring.integration.configuration.SpringIntegrationConfiguration;

/**
 * <p>Producer that creates the configuration loaded from the arquillian descriptor.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class SpringIntegrationConfigurationProducer {

    /**
     * <p>Represents the configuration for this extension.</p>
     */
    @Inject
    @ApplicationScoped
    private InstanceProducer<SpringIntegrationConfiguration> extensionConfiguration;

    /**
     * <p>Creates the Spring extension configuration before the test suite is being run.</p>
     *
     * @param beforeSuiteEvent the event fired before execution of the test suite
     */
    public void initConfiguration(@Observes ArquillianDescriptor descriptor) {

        SpringIntegrationConfiguration config = getConfiguration(descriptor);

        extensionConfiguration.set(config);
    }

    /**
     * <p>Creates instance of {@link SpringIntegrationConfiguration} from descriptor.</p>
     *
     * @param descriptor the descriptor
     *
     * @return the created instance of {@link SpringIntegrationConfiguration}
     */
    private SpringIntegrationConfiguration getConfiguration(ArquillianDescriptor descriptor) {

        Map<String, String> properties = getExtensionProperties(descriptor);

        return new SpringIntegrationConfiguration(properties);
    }

    /**
     * <p>Retrieves the extension properties from configuration file.</p>
     *
     * @param desc the descriptor
     *
     * @return the extension properties map
     */
    private Map<String, String> getExtensionProperties(ArquillianDescriptor desc) {
        for (ExtensionDef extensionDef : desc.getExtensions()) {

            if (SpringIntegrationConstants.EXTENSION_CONFIGURATION_PREFIX.equals(extensionDef.getExtensionName())) {

                return extensionDef.getExtensionProperties();
            }
        }

        return Collections.emptyMap();
    }

    /**
     * <p>Retrieves the property value as string.</p>
     *
     * @param properties   the properties map
     * @param propertyName the property name
     * @param defaultValue the default value used when property is not present
     *
     * @return the property value
     */
    private String getStringProperty(Map<String, String> properties, String propertyName, String defaultValue) {

        if (properties.containsKey(propertyName)) {
            return properties.get(propertyName);
        }

        return defaultValue;
    }
}

