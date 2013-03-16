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

package org.jboss.arquillian.spring.integration.inject.container;

import org.jboss.arquillian.spring.integration.SpringInjectConstants;
import org.jboss.arquillian.spring.integration.container.SecurityActions;
import org.jboss.arquillian.spring.integration.context.AbstractApplicationContextProducer;
import org.jboss.arquillian.spring.integration.context.RemoteTestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.inject.utils.ClassPathResourceLocationsProcessor;
import org.jboss.arquillian.spring.integration.test.annotation.SpringConfiguration;
import org.jboss.arquillian.test.spi.TestClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * <p>The {@link AbstractApplicationContextProducer} that creates the {@link org.springframework.context.support.ClassPathXmlApplicationContext}
 * with configuration loaded from locations specified by the test..</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class XmlRemoteApplicationContextProducer extends AbstractApplicationContextProducer {

    /**
     * The resource location processor.
     */
    private ClassPathResourceLocationsProcessor locationsProcessor = new ClassPathResourceLocationsProcessor();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(TestClass testClass) {
        return testClass.isAnnotationPresent(SpringConfiguration.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RemoteTestScopeApplicationContext createApplicationContext(TestClass testClass) {

        return new RemoteTestScopeApplicationContext(getApplicationContext(testClass), testClass, true);
    }

    /**
     * <p>Creates the application context.</p>
     *
     * @param testClass the test class
     *
     * @return created {@link ApplicationContext}
     */
    private ApplicationContext getApplicationContext(TestClass testClass) {

        SpringConfiguration springConfiguration = testClass.getAnnotation(SpringConfiguration.class);

        String[] locations = locationsProcessor.processLocations(springConfiguration.value(), testClass.getJavaClass());

        Class<? extends ApplicationContext> applicationContextClass = getCustomContextClass();
        if (springConfiguration.contextClass() != ApplicationContext.class) {

            applicationContextClass = springConfiguration.contextClass();
        }

        // if the application context class was not specified, then use the default
        if (applicationContextClass == null) {
            applicationContextClass = ClassPathXmlApplicationContext.class;
        }

        return createInstance(applicationContextClass, locations);
    }

    /**
     * <p>Retrieves the custom context class.</p>
     *
     * @return the custom context class
     */
    private Class<? extends ApplicationContext> getCustomContextClass() {

        String customContextClass =
                getRemoteConfiguration().getProperty(SpringInjectConstants.CONFIGURATION_CUSTOM_CONTEXT_CLASS);

        if (customContextClass != null
                && customContextClass.trim().length() > 0) {

            return (Class<? extends ApplicationContext>)
                    SecurityActions.classForName(customContextClass);
        }

        return null;
    }

    /**
     * <p>Creates new instance of {@link org.springframework.context.ApplicationContext}.</p>
     *
     * @param applicationContextClass the class of application context
     * @param locations               the locations from which load the configuration files
     *
     * @return the created {@link org.springframework.context.ApplicationContext} instance
     */
    private <T extends ApplicationContext> ApplicationContext createInstance(Class<T> applicationContextClass,
                                                                             String[] locations) {

        try {
            Constructor<T> ctor = applicationContextClass.getConstructor(String[].class);

            return ctor.newInstance((Object) locations);
        } catch (NoSuchMethodException e) {

            throw new RuntimeException("Could not create instance of " + applicationContextClass.getName()
                    + ", no appropriate constructor found.", e);
        } catch (InvocationTargetException e) {

            throw new RuntimeException("Could not create instance of " + applicationContextClass.getName(), e);
        } catch (InstantiationException e) {

            throw new RuntimeException("Could not create instance of " + applicationContextClass.getName(), e);
        } catch (IllegalAccessException e) {

            throw new RuntimeException("Could not create instance of " + applicationContextClass.getName(), e);
        }
    }
}
