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

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.spring.integration.configuration.SpringIntegrationConfiguration;
import org.jboss.arquillian.spring.integration.utils.TestReflectionHelper;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>Tests {@link SpringIntegrationConfigurationProducer} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringIntegrationConfigurationProducerTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private SpringIntegrationConfigurationProducer instance;

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        instance = new SpringIntegrationConfigurationProducer();
    }

    /**
     * <p>Tests {@link SpringIntegrationConfigurationProducer#initConfiguration(BeforeSuite)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testInitConfigurationDefault() throws Exception {

        BeforeSuite event = new BeforeSuite();

        ArquillianDescriptor descriptor = Descriptors.create(ArquillianDescriptor.class);

        injectDescriptor(descriptor);

        InstanceProducer<SpringIntegrationConfiguration> mockProducer = mock(InstanceProducer.class);
        TestReflectionHelper.setFieldValue(instance, "extensionConfiguration", mockProducer);

        instance.initConfiguration(event);

        ArgumentCaptor<SpringIntegrationConfiguration> argument =
                ArgumentCaptor.forClass(SpringIntegrationConfiguration.class);
        verify(mockProducer).set(argument.capture());

        assertNotNull("The result was null.", argument.getValue());
        assertNull("Invalid customContextClass property.", argument.getValue().getProperty("customContextClass"));
        assertNull("Invalid customAnnotationContextClass property.",
                argument.getValue().getProperty("customAnnotationContextClass"));
    }

    /**
     * <p>Tests {@link SpringIntegrationConfigurationProducer#initConfiguration(BeforeSuite)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testInitConfiguration() throws Exception {

        BeforeSuite event = new BeforeSuite();

        ArquillianDescriptor descriptor = Descriptors.importAs(ArquillianDescriptor.class).from(
                new FileInputStream(new File("src/test/resources", "arquillian.xml")));

        injectDescriptor(descriptor);

        InstanceProducer<SpringIntegrationConfiguration> mockProducer = mock(InstanceProducer.class);
        TestReflectionHelper.setFieldValue(instance, "extensionConfiguration", mockProducer);

        instance.initConfiguration(event);

        ArgumentCaptor<SpringIntegrationConfiguration> argument =
                ArgumentCaptor.forClass(SpringIntegrationConfiguration.class);
        verify(mockProducer).set(argument.capture());

        assertNotNull("The result was null.", argument.getValue());
        assertEquals("Invalid customContextClass property.",
                "org.springframework.context.support.ClassPathXmlApplicationContext",
                argument.getValue().getProperty("customContextClass"));
        assertEquals("Invalid customAnnotationContextClass property.",
                "org.springframework.context.annotation.AnnotationConfigApplicationContext",
                argument.getValue().getProperty("customAnnotationContextClass"));
    }

    /**
     * <p>Initialize the arquillian descriptor.</p>
     *
     * @param descriptor the arquillian descriptor
     *
     * @throws IllegalAccessException if any error occurs
     * @throws NoSuchFieldException   if any error occurs
     */
    private void injectDescriptor(ArquillianDescriptor descriptor) throws IllegalAccessException, NoSuchFieldException {
        Instance<ArquillianDescriptor> mockDescriptorInstance = mock(Instance.class);
        when(mockDescriptorInstance.get()).thenReturn(descriptor);
        TestReflectionHelper.setFieldValue(instance, "descriptor", mockDescriptorInstance);
    }
}
