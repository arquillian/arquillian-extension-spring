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
package org.jboss.arquillian.spring.integration.lifecycle;

import org.jboss.arquillian.core.api.Event;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.spring.integration.context.ApplicationContextDestroyer;
import org.jboss.arquillian.spring.integration.context.ApplicationContextProducer;
import org.jboss.arquillian.spring.integration.context.TestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.event.ApplicationContextCreatedEvent;
import org.jboss.arquillian.spring.integration.event.ApplicationContextDestroyedEvent;
import org.jboss.arquillian.spring.integration.event.ApplicationContextEvent;
import org.jboss.arquillian.spring.integration.test.annotation.ContextLifeCycle;
import org.jboss.arquillian.spring.integration.test.annotation.ContextLifeCycleMode;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.spi.event.suite.After;
import org.jboss.arquillian.test.spi.event.suite.AfterSuite;
import org.jboss.arquillian.test.spi.event.suite.Before;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;

import java.util.Collection;

/**
 * <p>The application context lifecycle handler, which is responsible for instantiating the application context prior
 * the test and destroying it as soon as it's not needed any longer.</p>
 *
 * <p>Currently the application context can be created once for whole test case or separately for each individual test.
 * The strategy on how the application context is being instantiated is being determined from the {@link
 * ContextLifeCycle} annotation defined on the test class. By default all the tests in the same test case share the same
 * instance of application context.</p>
 *
 * @param <TProd> the type of {@link ApplicationContextProducer} that can provide specific type
 * @param <T>     the type of application context instance
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @see ApplicationContextProducer
 * @see ContextLifeCycle
 * @see ContextLifeCycleMode
 */
public abstract class AbstractApplicationContextLifecycleHandler<TProd extends ApplicationContextProducer<T>,
        T extends TestScopeApplicationContext> {

    /**
     * <p>Represents the default ApplicationContext life cycle mode.</p>
     */
    private static final ContextLifeCycleMode DEFAULT_LIFE_CYCLE_MODE = ContextLifeCycleMode.TEST_CASE;

    /**
     * <p>Represents the instance of {@link ServiceLoader}.</p>
     */
    @Inject
    private Instance<ServiceLoader> serviceLoaderInstance;

    /**
     * <p>Represents the application context created event.</p>
     */
    @Inject
    private Event<ApplicationContextEvent> applicationContextEvent;

    /**
     * <p>Retrieves the application context.</p>
     *
     * @return the application context
     */
    protected abstract T getApplicationContext();

    /**
     * <p>Sets the application context instance.</p>
     *
     * @param applicationContext the application context instance
     */
    protected abstract void setApplicationContext(T applicationContext);

    /**
     * <p>Retrieves the {@link ApplicationContextProducer} instance.</p>
     *
     * @return the {@link ApplicationContextProducer} instance
     */
    protected abstract Class<TProd> getProducerClass();

    /**
     * <p>The before class event handler.</p>
     *
     * <p>Delegates to the registered {@link ApplicationContextProducer} instances in order to create the application
     * context.</p>
     *
     * @param event the before class event
     */
    public void beforeClass(@Observes BeforeClass event) {

        createTestApplicationContext(event.getTestClass());
    }

    /**
     * <p>The before test event handler.</p>
     *
     * <p>Delegates to the registered {@link ApplicationContextProducer} instances in order to create the application
     * context.</p>
     *
     * @param event the before test event
     */
    public void beforeTest(@Observes Before event) {

        createTestApplicationContext(event.getTestClass());
    }

    /**
     * <p>The after test event handler.</p>
     *
     * <p>Delegates to the registered {@link ApplicationContextProducer} instances in order to create the application
     * context.</p>
     *
     * @param event the after test event
     */
    public void afterTest(@Observes After event) {

        ContextLifeCycleMode mode = getContextLifeCycleMode(event.getTestClass());
        if (mode == ContextLifeCycleMode.TEST) {

            destroyTestApplicationContext();
        }
    }

    /**
     * <p>The before test event handler.</p>
     *
     * <p>Delegates to the registered {@link ApplicationContextProducer} instances in order to create the application
     * context.</p>
     *
     * @param event the after suite event
     */
    public void afterSuite(@Observes AfterSuite event) {

        destroyTestApplicationContext();
    }

    /**
     * <p>Instantiates the application context if needed base on the meta data provided on the test class.</p>
     *
     * <p>The actual instances creation is being delegated to the registered </p>
     *
     * @param testClass the instance of the test class
     */
    private void createTestApplicationContext(TestClass testClass) {

        if (getApplicationContext() != null &&
                getApplicationContext().getTestClass().getJavaClass().equals(testClass.getJavaClass())) {

            return;
        }

        // creates the application context instance
        T applicationContext = createApplicationContext(testClass);

        if (applicationContext != null) {

            // triggers the application context created event
            applicationContextEvent.fire(new ApplicationContextCreatedEvent(applicationContext));

            setApplicationContext(applicationContext);
        }
    }

    /**
     * <p>Creates the application context. The application context creation is being delegates to the registered
     * instances of {@link ApplicationContextProducer}.</p>
     *
     * @param testClass the test class
     *
     * @return the application context
     */
    private T createApplicationContext(TestClass testClass) {

        ServiceLoader serviceLoader = serviceLoaderInstance.get();

        // retrieves the list of all registered application context producers
        Collection<TProd> applicationContextProducers = serviceLoader.all(getProducerClass());

        for (TProd applicationContextProducer : applicationContextProducers) {

            if (applicationContextProducer.supports(testClass)) {

                return applicationContextProducer.createApplicationContext(testClass);
            }
        }

        // the given test is not supported
        return null;
    }

    /**
     * <p>Destroys the application context if it exists.</p>
     */
    private void destroyTestApplicationContext() {
        T applicationContext = getApplicationContext();

        if (applicationContext != null) {
            destroyApplicationContext(applicationContext);

            // triggers the application context destroyed event
            applicationContextEvent.fire(new ApplicationContextDestroyedEvent(applicationContext));
        }
    }

    /**
     * <p>Destroys the application context.</p>
     *
     * <p>The implementation by default delegates to the registered {@link ApplicationContextDestroyer} to perform the
     * application context clean up.</p>
     *
     * @param applicationContext the application context to be destroyed
     */
    private void destroyApplicationContext(TestScopeApplicationContext applicationContext) {

        // single service is expected
        getService(ApplicationContextDestroyer.class).destroyApplicationContext(applicationContext);
    }

    /**
     * <p>Retrieves the context life cycle mode.</p>
     *
     * @param testClass the test class
     *
     * @return the {@link ContextLifeCycleMode} defined for the test
     */
    private ContextLifeCycleMode getContextLifeCycleMode(TestClass testClass) {

        ContextLifeCycle contextLifeCycle = testClass.getAnnotation(ContextLifeCycle.class);

        if (contextLifeCycle != null) {

            return contextLifeCycle.value();
        }

        return DEFAULT_LIFE_CYCLE_MODE;
    }

    /**
     * <p>Retrieves the service by it's type. <p/> The implementation uses {@link ServiceLoader} in order to retrieve
     * first instance of specified kind. Any other registered instance in given scope is being discarded.</p>
     *
     * @param clazz the class of the service
     * @param <T>   the service type
     *
     * @return the service instance
     */
    private <T> T getService(Class<T> clazz) {

        Collection<T> collection = serviceLoaderInstance.get().all(clazz);
        return collection.iterator().next();
    }
}
