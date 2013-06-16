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
package org.jboss.arquillian.spring.integration.inject.container;

import org.jboss.arquillian.spring.integration.container.SecurityActions;
import org.jboss.arquillian.spring.integration.context.AbstractApplicationContextProducer;
import org.jboss.arquillian.spring.integration.context.RemoteTestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.test.annotation.SpringContextConfiguration;
import org.jboss.arquillian.test.spi.TestClass;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>The {@link AbstractApplicationContextProducer} that retrieves the {@link ApplicationContext} from an annotated
 * method declared on the test class.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 * @see SpringContextConfiguration
 */
public class CustomRemoteApplicationContextProducer extends AbstractApplicationContextProducer {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(TestClass testClass) {

        return getAnnotatedContextMethods(testClass).length > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RemoteTestScopeApplicationContext createApplicationContext(TestClass testClass) {

        // retrieves the annotated methods
        Method[] customContextMethods = getAnnotatedContextMethods(testClass);

        if (customContextMethods.length > 1) {

            throw new RuntimeException(String.format(
                    "Class %s must declare only one method annotated with SpringContextConfiguration.",
                    testClass.getName()));
        }

        // retrieves the application context by invoking the annotated method
        ApplicationContext applicationContext = getCustomApplicationContext(customContextMethods[0]);

        if (applicationContext == null) {
            throw new RuntimeException(String.format(
                    "The method %s returned null as a application context.", customContextMethods[0].getName()));
        }

        // creates new instance of application context
        return new RemoteTestScopeApplicationContext(applicationContext, testClass, true);
    }

    /**
     * <p>Retrieves the custom application context by invoking the given method.</p>
     *
     * @param customContextMethod the annotated method that creates the instance of {@link ApplicationContext}
     *
     * @return the application context instance
     */
    private ApplicationContext getCustomApplicationContext(Method customContextMethod) {

        try {

            return (ApplicationContext) customContextMethod.invoke(null);
        } catch (IllegalAccessException e) {

            throw new RuntimeException("An error occurred when invoking custom context method.", e);
        } catch (InvocationTargetException e) {

            throw new RuntimeException("An error occurred when invoking custom context method.", e);
        }
    }

    /**
     * <p>Retrieves all methods annotated with {@link SpringContextConfiguration} that are declared by the passed
     * class.</p>
     *
     * @param testClass the test class
     *
     * @return array of methods annotated with {@link SpringContextConfiguration}
     */
    private Method[] getAnnotatedContextMethods(TestClass testClass) {
        return SecurityActions.getStaticMethodsWithAnnotation(testClass.getJavaClass(),
                SpringContextConfiguration.class);
    }
}
