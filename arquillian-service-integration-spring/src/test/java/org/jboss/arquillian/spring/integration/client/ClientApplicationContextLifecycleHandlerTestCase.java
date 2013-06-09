/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat Middleware LLC, and individual contributors
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

import org.jboss.arquillian.core.api.Event;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.spring.integration.context.ApplicationContextDestroyer;
import org.jboss.arquillian.spring.integration.context.ClientApplicationContextProducer;
import org.jboss.arquillian.spring.integration.context.ClientTestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.context.TestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.event.ApplicationContextCreatedEvent;
import org.jboss.arquillian.spring.integration.event.ApplicationContextDestroyedEvent;
import org.jboss.arquillian.spring.integration.model.TestCaseTest;
import org.jboss.arquillian.spring.integration.model.TestMethodTest;
import org.jboss.arquillian.spring.integration.utils.TestReflectionHelper;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.spi.event.suite.After;
import org.jboss.arquillian.test.spi.event.suite.AfterSuite;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * <p>Tests {@link ClientApplicationContextLifecycleHandler} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class ClientApplicationContextLifecycleHandlerTestCase {

    /**
     * Represents the test class used for testing.
     */
    private static final Object TEST_OBJECT = new TestCaseTest();

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private ClientApplicationContextLifecycleHandler instance;

    /**
     * <p>Represents an instance of {@link ClientApplicationContextProducer} that will always support the test
     * class.</p>
     */
    private ClientApplicationContextProducer supportedApplicationContextProducer;

    /**
     * <p>Represents an instance of {@link ClientApplicationContextProducer} that will never support the test
     * class.</p>
     */
    private ClientApplicationContextProducer notSupportedApplicationContextProducer;

    /**
     * <p>Represents an instance of {@link ApplicationContextDestroyer} used for testing.</p>
     */
    private ApplicationContextDestroyer applicationContextDestroyer;

    /**
     * <p>Represents the instance of {@link org.jboss.arquillian.test.spi.TestClass}.</p>
     */
    private TestClass testClass;

    /**
     * <p>Represents the event that is being triggered when the application context is being created.</p>
     */
    private Event<ApplicationContextCreatedEvent> mockApplicationContextCreatedEvent;

    /**
     * <p>Represents the event that is being triggered when the application context is being destroyed.</p>
     */
    private Event<ApplicationContextDestroyedEvent> mockApplicationContextDestroyedEvent;

    /**
     * Represents the test method used for testing.
     */
    private Method testMethod;

    /**
     * <p>Sets up the test environment.</p>
     *
     * @throws Exception if any error occurs
     */
    @Before
    public void setUp() throws Exception {

        testClass = new TestClass(TEST_OBJECT.getClass());
        testMethod = TEST_OBJECT.getClass().getMethod("testMethod");

        instance = new ClientApplicationContextLifecycleHandler();

        supportedApplicationContextProducer = mock(ClientApplicationContextProducer.class);
        when(supportedApplicationContextProducer.supports(any(TestClass.class))).thenReturn(true);
        when(supportedApplicationContextProducer.createApplicationContext(any(TestClass.class)))
                .thenReturn(new ClientTestScopeApplicationContext(new GenericApplicationContext(), testClass, true));

        notSupportedApplicationContextProducer = mock(ClientApplicationContextProducer.class);
        when(notSupportedApplicationContextProducer.supports(any(TestClass.class))).thenReturn(false);

        applicationContextDestroyer = mock(ApplicationContextDestroyer.class);

        mockApplicationContextCreatedEvent = mock(Event.class);
        TestReflectionHelper.setFieldValue(instance, "applicationContextEvent", mockApplicationContextCreatedEvent);
    }

    /**
     * <p>Tests {@link  ClientApplicationContextLifecycleHandler#beforeClass(org.jboss.arquillian.test.spi.event.suite.BeforeClass)}
     * method, when the test class is supported.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testInitApplicationContextSupportedBeforeClass() throws Exception {

        List<ClientApplicationContextProducer> producers = new ArrayList<ClientApplicationContextProducer>();
        producers.add(notSupportedApplicationContextProducer);
        producers.add(supportedApplicationContextProducer);

        ServiceLoader serviceLoader = mock(ServiceLoader.class);
        when(serviceLoader.all(ClientApplicationContextProducer.class)).thenReturn(producers);

        Instance<ServiceLoader> mockServiceLoader = mock(Instance.class);
        when(mockServiceLoader.get()).thenReturn(serviceLoader);
        TestReflectionHelper.setFieldValue(instance, "serviceLoaderInstance", mockServiceLoader);

        InstanceProducer<ClientTestScopeApplicationContext> mockApplicationContext = mock(InstanceProducer.class);
        TestReflectionHelper.setFieldValue(instance, "applicationContextInstance", mockApplicationContext);

        instance.beforeClass(new BeforeClass(TEST_OBJECT.getClass()));

        verify(mockApplicationContextCreatedEvent).fire(any(ApplicationContextCreatedEvent.class));
        verify(mockApplicationContext).set((ClientTestScopeApplicationContext) notNull());
    }

    /**
     * <p>Tests {@link  ClientApplicationContextLifecycleHandler#beforeTest(org.jboss.arquillian.test.spi.event.suite.Before)}
     * method, when the test class is supported.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testInitApplicationContextSupportedTestCase() throws Exception {

        List<ClientApplicationContextProducer> producers = new ArrayList<ClientApplicationContextProducer>();
        producers.add(notSupportedApplicationContextProducer);
        producers.add(supportedApplicationContextProducer);

        ServiceLoader serviceLoader = mock(ServiceLoader.class);
        when(serviceLoader.all(ClientApplicationContextProducer.class)).thenReturn(producers);

        Instance<ServiceLoader> mockServiceLoader = mock(Instance.class);
        when(mockServiceLoader.get()).thenReturn(serviceLoader);
        TestReflectionHelper.setFieldValue(instance, "serviceLoaderInstance", mockServiceLoader);

        InstanceProducer<ClientTestScopeApplicationContext> mockApplicationContext = mock(InstanceProducer.class);
        TestReflectionHelper.setFieldValue(instance, "applicationContextInstance", mockApplicationContext);

        instance.beforeTest(new org.jboss.arquillian.test.spi.event.suite.Before(TEST_OBJECT, testMethod));

        verify(mockApplicationContextCreatedEvent).fire(any(ApplicationContextCreatedEvent.class));
        verify(mockApplicationContext).set((ClientTestScopeApplicationContext) notNull());
    }

    /**
     * <p>Tests {@link  ClientApplicationContextLifecycleHandler#beforeTest(org.jboss.arquillian.test.spi.event.suite.Before)}
     * method, when the test class is supported.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testInitApplicationContextSupportedTestMethod() throws Exception {

        List<ClientApplicationContextProducer> producers = new ArrayList<ClientApplicationContextProducer>();
        producers.add(notSupportedApplicationContextProducer);
        producers.add(supportedApplicationContextProducer);

        ServiceLoader serviceLoader = mock(ServiceLoader.class);
        when(serviceLoader.all(ClientApplicationContextProducer.class)).thenReturn(producers);

        Instance<ServiceLoader> mockServiceLoader = mock(Instance.class);
        when(mockServiceLoader.get()).thenReturn(serviceLoader);
        TestReflectionHelper.setFieldValue(instance, "serviceLoaderInstance", mockServiceLoader);

        InstanceProducer<ClientTestScopeApplicationContext> mockApplicationContext = mock(InstanceProducer.class);
        TestReflectionHelper.setFieldValue(instance, "applicationContextInstance", mockApplicationContext);

        instance.beforeTest(new org.jboss.arquillian.test.spi.event.suite.Before(new TestMethodTest(),
                TestMethodTest.class.getMethod("testMethod")));

        verify(mockApplicationContextCreatedEvent).fire(any(ApplicationContextCreatedEvent.class));
        verify(mockApplicationContext).set((ClientTestScopeApplicationContext) notNull());
    }

    /**
     * <p>Tests {@link  ClientApplicationContextLifecycleHandler#beforeClass(BeforeClass)} method, when the test class
     * is not supported.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testInitApplicationContextNotSupported() throws Exception {

        List<ClientApplicationContextProducer> producers = new ArrayList<ClientApplicationContextProducer>();
        producers.add(notSupportedApplicationContextProducer);

        ServiceLoader serviceLoader = mock(ServiceLoader.class);
        when(serviceLoader.all(ClientApplicationContextProducer.class)).thenReturn(producers);

        Instance<ServiceLoader> mockServiceLoader = mock(Instance.class);
        when(mockServiceLoader.get()).thenReturn(serviceLoader);
        TestReflectionHelper.setFieldValue(instance, "serviceLoaderInstance", mockServiceLoader);

        InstanceProducer<ClientTestScopeApplicationContext> mockApplicationContext = mock(InstanceProducer.class);
        when(mockApplicationContext.get()).thenReturn(null);
        TestReflectionHelper.setFieldValue(instance, "applicationContextInstance", mockApplicationContext);

        instance.beforeClass(new BeforeClass(TEST_OBJECT.getClass()));

        verifyNoMoreInteractions(mockApplicationContextCreatedEvent);
    }

    /**
     * <p>Tests {@link  ClientApplicationContextLifecycleHandler#afterTest(After)} method when there is no application
     * context created.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testDestroyApplicationContextNoApplicationContext() throws Exception {

        List<ApplicationContextDestroyer> destroyers = new ArrayList<ApplicationContextDestroyer>();
        destroyers.add(applicationContextDestroyer);

        ServiceLoader serviceLoader = mock(ServiceLoader.class);
        when(serviceLoader.all(ApplicationContextDestroyer.class)).thenReturn(destroyers);

        Instance<ServiceLoader> mockServiceLoader = mock(Instance.class);
        when(mockServiceLoader.get()).thenReturn(serviceLoader);
        TestReflectionHelper.setFieldValue(instance, "serviceLoaderInstance", mockServiceLoader);

        InstanceProducer<ClientTestScopeApplicationContext> mockApplicationContext = mock(InstanceProducer.class);
        when(mockApplicationContext.get()).thenReturn(null);
        TestReflectionHelper.setFieldValue(instance, "applicationContextInstance", mockApplicationContext);

        instance.afterTest(new After(TEST_OBJECT, testMethod));

        verifyZeroInteractions(applicationContextDestroyer);
    }

    /**
     * <p>Tests {@link  ClientApplicationContextLifecycleHandler#afterTest(After)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testDestroyApplicationContextForTestCase() throws Exception {

        List<ApplicationContextDestroyer> destroyers = new ArrayList<ApplicationContextDestroyer>();
        destroyers.add(applicationContextDestroyer);

        ServiceLoader serviceLoader = mock(ServiceLoader.class);
        when(serviceLoader.all(ApplicationContextDestroyer.class)).thenReturn(destroyers);

        Instance<ServiceLoader> mockServiceLoader = mock(Instance.class);
        when(mockServiceLoader.get()).thenReturn(serviceLoader);
        TestReflectionHelper.setFieldValue(instance, "serviceLoaderInstance", mockServiceLoader);

        ApplicationContext applicationContext = mock(ApplicationContext.class);
        ClientTestScopeApplicationContext ClientTestScopeApplicationContext =
                new ClientTestScopeApplicationContext(applicationContext, new TestClass(Object.class), true);

        InstanceProducer<ClientTestScopeApplicationContext> mockApplicationContext = mock(InstanceProducer.class);
        when(mockApplicationContext.get()).thenReturn(ClientTestScopeApplicationContext);
        TestReflectionHelper.setFieldValue(instance, "applicationContextInstance", mockApplicationContext);

        instance.afterTest(new After(TEST_OBJECT, testMethod));

        verifyZeroInteractions(applicationContextDestroyer);
    }

    /**
     * <p>Tests {@link ClientApplicationContextLifecycleHandler#afterTest(After)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testDestroyApplicationContextForTestMethod() throws Exception {

        List<ApplicationContextDestroyer> destroyers = new ArrayList<ApplicationContextDestroyer>();
        destroyers.add(applicationContextDestroyer);

        ServiceLoader serviceLoader = mock(ServiceLoader.class);
        when(serviceLoader.all(ApplicationContextDestroyer.class)).thenReturn(destroyers);

        Instance<ServiceLoader> mockServiceLoader = mock(Instance.class);
        when(mockServiceLoader.get()).thenReturn(serviceLoader);
        TestReflectionHelper.setFieldValue(instance, "serviceLoaderInstance", mockServiceLoader);

        ApplicationContext applicationContext = mock(ApplicationContext.class);
        ClientTestScopeApplicationContext ClientTestScopeApplicationContext =
                new ClientTestScopeApplicationContext(applicationContext, new TestClass(Object.class), true);

        InstanceProducer<ClientTestScopeApplicationContext> mockApplicationContext = mock(InstanceProducer.class);
        when(mockApplicationContext.get()).thenReturn(ClientTestScopeApplicationContext);
        TestReflectionHelper.setFieldValue(instance, "applicationContextInstance", mockApplicationContext);

        instance.afterTest(new After(new TestMethodTest(), TestMethodTest.class.getMethod("testMethod")));

        verify(applicationContextDestroyer).destroyApplicationContext(any(TestScopeApplicationContext.class));
    }

    /**
     * <p>Tests {@link ClientApplicationContextLifecycleHandler#afterSuite(AfterSuite)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testDestroyApplicationContextAfterSuite() throws Exception {

        List<ApplicationContextDestroyer> destroyers = new ArrayList<ApplicationContextDestroyer>();
        destroyers.add(applicationContextDestroyer);

        ServiceLoader serviceLoader = mock(ServiceLoader.class);
        when(serviceLoader.all(ApplicationContextDestroyer.class)).thenReturn(destroyers);

        Instance<ServiceLoader> mockServiceLoader = mock(Instance.class);
        when(mockServiceLoader.get()).thenReturn(serviceLoader);
        TestReflectionHelper.setFieldValue(instance, "serviceLoaderInstance", mockServiceLoader);

        ApplicationContext applicationContext = mock(ApplicationContext.class);
        ClientTestScopeApplicationContext ClientTestScopeApplicationContext =
                new ClientTestScopeApplicationContext(applicationContext, new TestClass(Object.class), true);

        InstanceProducer<ClientTestScopeApplicationContext> mockApplicationContext = mock(InstanceProducer.class);
        when(mockApplicationContext.get()).thenReturn(ClientTestScopeApplicationContext);
        TestReflectionHelper.setFieldValue(instance, "applicationContextInstance", mockApplicationContext);

        instance.afterSuite(new AfterSuite());

        verify(applicationContextDestroyer).destroyApplicationContext(any(TestScopeApplicationContext.class));
    }
}
