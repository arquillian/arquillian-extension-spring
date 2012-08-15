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

package org.jboss.arquillian.spring.integration.javaconfig.client;

import org.jboss.arquillian.spring.integration.SpringJavaConfigConstants;
import org.jboss.arquillian.spring.integration.container.SecurityActions;
import org.jboss.arquillian.spring.integration.context.ClientApplicationContextProducer;
import org.jboss.arquillian.spring.integration.context.ClientTestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.context.TestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.test.annotation.SpringAnnotationConfiguration;
import org.jboss.arquillian.spring.integration.test.annotation.SpringClientAnnotationConfiguration;
import org.jboss.arquillian.test.spi.TestClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * <p>The {@link ClientApplicationContextProducer} implementation that creates the
 * {@link AnnotationConfigApplicationContext} instance on the client side.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class AnnotationClientApplicationContextProducer implements ClientApplicationContextProducer {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(TestClass testClass) {
        return testClass.isAnnotationPresent(SpringClientAnnotationConfiguration.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClientTestScopeApplicationContext createApplicationContext(TestClass testClass) {

        return new ClientTestScopeApplicationContext(getApplicationContext(testClass), true);
    }

    /**
     * <p>Creates the application context.</p>
     *
     * @param testClass the test class
     * @return created {@link org.springframework.context.ApplicationContext}
     */
    private ApplicationContext getApplicationContext(TestClass testClass) {

        SpringClientAnnotationConfiguration springConfiguration =
                testClass.getAnnotation(SpringClientAnnotationConfiguration.class);

        String[] packages = springConfiguration.packages();
        Class<?>[] classes = springConfiguration.classes();

        // creates standard spring annotated application context
        return createAnnotatedApplicationContext(testClass, packages, classes);
    }

    /**
     * <p>Creates the instance of {@link AnnotationConfigApplicationContext}.</p>
     *
     * @param testClass the test class
     * @param classes   the annotated classes to register
     * @param packages  the packages containing the annotated classes
     * @return the created instance of {@link AnnotationConfigApplicationContext}
     */
    private ApplicationContext createAnnotatedApplicationContext(TestClass testClass, String[] packages,
                                                                 Class<?>[] classes) {

        if (packages.length > 0 || classes.length > 0) {

            return createAnnotatedApplicationContext(classes, packages);
        }

        throw new RuntimeException("The test: " + testClass.getName()
                + " annotated with SpringClientAnnotationConfiguration must specify the configuration"
                + " classes or packages.");
    }

    /**
     * <p>Creates instance of {@link AnnotationConfigApplicationContext} class.</p>
     *
     * @param classes  the annotated classes to register
     * @param packages the packages containing the annotated classes
     * @return the created instance of {@link AnnotationConfigApplicationContext}
     */
    private ApplicationContext createAnnotatedApplicationContext(Class<?>[] classes, String[] packages) {

        if (classes.length > 0) {
            AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(classes);

            if (packages.length > 0) {
                applicationContext.scan(packages);
                applicationContext.refresh();
            }

            return applicationContext;

        } else {

            return new AnnotationConfigApplicationContext(packages);
        }
    }
}
