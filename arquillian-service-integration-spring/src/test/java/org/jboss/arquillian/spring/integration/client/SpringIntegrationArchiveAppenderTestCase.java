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

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.spring.integration.SpringIntegrationConstants;
import org.jboss.arquillian.spring.integration.configuration.SpringIntegrationConfiguration;
import org.jboss.arquillian.spring.integration.configuration.SpringIntegrationConfigurationExporter;
import org.jboss.arquillian.spring.integration.container.ContainerApplicationContextLifecycleHandler;
import org.jboss.arquillian.spring.integration.container.SecurityActions;
import org.jboss.arquillian.spring.integration.container.SpringIntegrationRemoteExtension;
import org.jboss.arquillian.spring.integration.container.SpringRemoteIntegrationConfigurationProducer;
import org.jboss.arquillian.spring.integration.context.AbstractApplicationContextProducer;
import org.jboss.arquillian.spring.integration.context.ApplicationContextDestroyer;
import org.jboss.arquillian.spring.integration.context.ApplicationContextProducer;
import org.jboss.arquillian.spring.integration.context.DefaultApplicationContextDestroyer;
import org.jboss.arquillian.spring.integration.context.RemoteApplicationContextProducer;
import org.jboss.arquillian.spring.integration.context.RemoteTestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.context.TestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.enricher.AbstractSpringInjectionEnricher;
import org.jboss.arquillian.spring.integration.event.ApplicationContextCreatedEvent;
import org.jboss.arquillian.spring.integration.event.ApplicationContextDestroyedEvent;
import org.jboss.arquillian.spring.integration.event.ApplicationContextEvent;
import org.jboss.arquillian.spring.integration.event.ApplicationContextInvalidatedEvent;
import org.jboss.arquillian.spring.integration.lifecycle.AbstractApplicationContextLifecycleHandler;
import org.jboss.arquillian.spring.integration.utils.TestReflectionHelper;
import org.jboss.arquillian.spring.integration.utils.TestResourceHelper;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <p>Tests {@link SpringIntegrationArchiveAppender} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringIntegrationArchiveAppenderTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private SpringIntegrationArchiveAppender instance;

    /**
     * <p>Represents the list of required classes.</p>
     */
    private final static List<Class<?>> REQUIRED_CLASSES = Arrays.asList(
            SpringIntegrationConfiguration.class, SpringIntegrationConfigurationExporter.class,
            SecurityActions.class, AbstractSpringInjectionEnricher.class,
            SpringIntegrationRemoteExtension.class, SpringRemoteIntegrationConfigurationProducer.class,
            AbstractApplicationContextProducer.class, ApplicationContextDestroyer.class,
            ApplicationContextProducer.class, TestScopeApplicationContext.class, RemoteTestScopeApplicationContext.class,
            RemoteApplicationContextProducer.class, RemoteTestScopeApplicationContext.class,
            AbstractSpringInjectionEnricher.class, SpringIntegrationConstants.class,
            AbstractApplicationContextLifecycleHandler.class, ContainerApplicationContextLifecycleHandler.class,
            ApplicationContextDestroyer.class, DefaultApplicationContextDestroyer.class,
            ApplicationContextEvent.class, ApplicationContextCreatedEvent.class,
            ApplicationContextDestroyedEvent.class, ApplicationContextInvalidatedEvent.class);

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        instance = new SpringIntegrationArchiveAppender();
    }

    /**
     * <p>Tests the {@link SpringIntegrationArchiveAppender#createAuxiliaryArchive()} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testCreateAuxiliaryArchive() throws Exception {

        SpringIntegrationConfiguration configuration = new SpringIntegrationConfiguration(
                Collections.<String, String>emptyMap());

        Instance<SpringIntegrationConfiguration> mockConfigurationInstance = mock(Instance.class);
        when(mockConfigurationInstance.get()).thenReturn(configuration);
        TestReflectionHelper.setFieldValue(instance, "configuration", mockConfigurationInstance);

        Archive archive = instance.createAuxiliaryArchive();

        assertNotNull("Method returned null.", archive);
        assertTrue("The returned archive has incorrect type.", archive instanceof JavaArchive);

        for (Class c : REQUIRED_CLASSES) {

            assertTrue("The required type is missing: " + c.getName(),
                    archive.contains(TestResourceHelper.getClassResourcePath(c)));
        }
    }
}