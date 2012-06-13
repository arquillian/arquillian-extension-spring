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

package org.jboss.arquillian.spring.integration.container;

import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.spring.integration.configuration.SpringIntegrationConfiguration;
import org.jboss.arquillian.spring.integration.configuration.SpringIntegrationConfigurationExporter;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;

/**
 * <p>The producer that creates the {@link SpringIntegrationConfiguration} in the container loaded from properties
 * file.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class SpringRemoteIntegrationConfigurationProducer {

    /**
     * <p>Producer proxy for {@link SpringIntegrationConfiguration}.</p>
     */
    @Inject
    @ApplicationScoped
    private InstanceProducer<SpringIntegrationConfiguration> remoteConfiguration;

    /**
     * <p>Builds the application context before the test suite is being executed.</p>
     *
     * @param beforeSuite the event fired before execution of test suite
     */
    public void initRemoteConfiguration(@Observes BeforeSuite beforeSuite) {

        SpringIntegrationConfiguration config = loadConfigurationFromProperties();

        if (config != null) {

            remoteConfiguration.set(config);
        }
    }

    /**
     * <p>Loads the remote configuration from properties file.</p>
     *
     * @return the loaded configuration
     */
    private SpringIntegrationConfiguration loadConfigurationFromProperties() {

        return SpringIntegrationConfigurationExporter.loadResource(
                SecurityActions.getResource(SpringIntegrationConfigurationExporter.SPRING_REMOTE_PROPERTIES));
    }
}