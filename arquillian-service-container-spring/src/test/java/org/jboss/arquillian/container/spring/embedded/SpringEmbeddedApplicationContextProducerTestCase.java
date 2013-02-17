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
package org.jboss.arquillian.container.spring.embedded;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.spring.integration.context.RemoteApplicationContextProducer;
import org.jboss.arquillian.spring.integration.context.RemoteTestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.context.TestScopeApplicationContext;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.GenericApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * <p>Tests {@link SpringEmbeddedApplicationContextProducer} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringEmbeddedApplicationContextProducerTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private SpringEmbeddedApplicationContextProducer instance;

    /**
     * <p>Represents an instance of {@link RemoteApplicationContextProducer} that will always support the test
     * class.</p>
     */
    private RemoteApplicationContextProducer supportedApplicationContextProducer;

    /**
     * <p>Represents an instance of {@link RemoteApplicationContextProducer} that will never support the test
     * class.</p>
     */
    private RemoteApplicationContextProducer notSupportedApplicationContextProducer;

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        instance = new SpringEmbeddedApplicationContextProducer();

        supportedApplicationContextProducer = mock(RemoteApplicationContextProducer.class);
        when(supportedApplicationContextProducer.supports(any(TestClass.class))).thenReturn(true);
        when(supportedApplicationContextProducer.createApplicationContext(any(TestClass.class)))
                .thenReturn(new RemoteTestScopeApplicationContext(new GenericApplicationContext(), true));

        notSupportedApplicationContextProducer = mock(RemoteApplicationContextProducer.class);
        when(notSupportedApplicationContextProducer.supports(any(TestClass.class))).thenReturn(false);
    }

    /**
     * <p>Tests {@link SpringEmbeddedApplicationContextProducer#initApplicationContext(BeforeClass)} method, when the
     * test class is supported.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testInitApplicationContextSupported() throws Exception {

        List<RemoteApplicationContextProducer> producers = new ArrayList<RemoteApplicationContextProducer>();
        producers.add(notSupportedApplicationContextProducer);
        producers.add(supportedApplicationContextProducer);

        ServiceLoader serviceLoader = mock(ServiceLoader.class);
        when(serviceLoader.all(RemoteApplicationContextProducer.class)).thenReturn(producers);

        Instance<ServiceLoader> mockServiceLoader = mock(Instance.class);
        when(mockServiceLoader.get()).thenReturn(serviceLoader);
        TestReflectionHelper.setFieldValue(instance, "serviceLoader", mockServiceLoader);

        InstanceProducer<TestScopeApplicationContext> mockApplicationContext = mock(InstanceProducer.class);
        TestReflectionHelper.setFieldValue(instance, "testApplicationContext", mockApplicationContext);

        instance.initApplicationContext(new BeforeClass(Object.class));

        verify(mockApplicationContext).set((TestScopeApplicationContext) notNull());
    }

    /**
     * <p>Tests {@link SpringEmbeddedApplicationContextProducer#initApplicationContext(BeforeClass)} method, when the
     * test class is not supported.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testInitApplicationContextNotSupported() throws Exception {

        List<RemoteApplicationContextProducer> producers = new ArrayList<RemoteApplicationContextProducer>();
        producers.add(notSupportedApplicationContextProducer);

        ServiceLoader serviceLoader = mock(ServiceLoader.class);
        when(serviceLoader.all(RemoteApplicationContextProducer.class)).thenReturn(producers);

        Instance<ServiceLoader> mockServiceLoader = mock(Instance.class);
        when(mockServiceLoader.get()).thenReturn(serviceLoader);
        TestReflectionHelper.setFieldValue(instance, "serviceLoader", mockServiceLoader);

        InstanceProducer<TestScopeApplicationContext> mockApplicationContext = mock(InstanceProducer.class);
        TestReflectionHelper.setFieldValue(instance, "testApplicationContext", mockApplicationContext);

        instance.initApplicationContext(new BeforeClass(Object.class));

        verifyZeroInteractions(mockApplicationContext);
    }
}
