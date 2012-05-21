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
package org.jboss.arquillian.spring.container;

import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.spring.configuration.SpringExtensionRemoteConfiguration;
import org.jboss.arquillian.spring.utils.TestReflectionHelper;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * <p>Tests {@link SpringExtensionRemoteConfigurationProducer} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringExtensionRemoteConfigurationProducerTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private SpringExtensionRemoteConfigurationProducer instance;

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        instance = new SpringExtensionRemoteConfigurationProducer();
    }

    /**
     * <p>Tests the {@link SpringExtensionRemoteConfigurationProducer#initRemoteConfiguration(BeforeSuite)}</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testInitRemoteConfiguration() throws Exception {

        BeforeSuite event = new BeforeSuite();

        InstanceProducer<SpringExtensionRemoteConfiguration> mockProducer = mock(InstanceProducer.class);
        TestReflectionHelper.setFieldValue(instance, "remoteConfiguration", mockProducer);

        instance.initRemoteConfiguration(event);

        ArgumentCaptor<SpringExtensionRemoteConfiguration> argument =
                ArgumentCaptor.forClass(SpringExtensionRemoteConfiguration.class);
        verify(mockProducer).set(argument.capture());

        assertNotNull("The result was null.", argument.getValue());
        assertEquals("The custom context class name is incorrect.", "testCustomContextClass",
                argument.getValue().getCustomContextClass());
        assertEquals("The custom annotated context class name is incorrect.", "testCustomAnnotationContextClass",
                argument.getValue().getCustomAnnotationContextClass());
    }
}
