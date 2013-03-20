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
package org.jboss.arquillian.spring.integration.javaconfig.client;

import org.jboss.arquillian.spring.integration.context.ClientApplicationContextProducer;
import org.jboss.arquillian.spring.integration.context.ClientTestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.javaconfig.utils.AnnotationApplicationContextProducer;
import org.jboss.arquillian.spring.integration.javaconfig.utils.DefaultConfigurationClassesProcessor;
import org.jboss.arquillian.spring.integration.test.annotation.SpringClientAnnotationConfiguration;
import org.jboss.arquillian.test.spi.TestClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * <p>The {@link ClientApplicationContextProducer} implementation that creates the
 * {@link AnnotationConfigApplicationContext} instance on the client side.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class AnnotationClientApplicationContextProducer implements ClientApplicationContextProducer {

    /**
     * The default configuration processor.
     */
    private final DefaultConfigurationClassesProcessor configurationClassesProcessor = new DefaultConfigurationClassesProcessor();

    /**
     * The annotation configuration producer.
     */
    private final AnnotationApplicationContextProducer annotationApplicationContextProducer = new AnnotationApplicationContextProducer();

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

        return new ClientTestScopeApplicationContext(getApplicationContext(testClass), testClass, true);
    }

    /**
     * <p>Creates the application context.</p>
     *
     * @param testClass the test class
     * @return created {@link ApplicationContext}
     */
    private ApplicationContext getApplicationContext(TestClass testClass) {

        SpringClientAnnotationConfiguration springConfiguration =
                testClass.getAnnotation(SpringClientAnnotationConfiguration.class);

        Class<?> wrappedTestClass = testClass.getJavaClass();

        Class<?>[] classes = configurationClassesProcessor.findConfigurationClasses(springConfiguration, wrappedTestClass);
        String[] packages = configurationClassesProcessor.findPackages(springConfiguration, wrappedTestClass);

        // creates standard spring annotated application context
        return annotationApplicationContextProducer.createAnnotatedApplicationContext(testClass, packages, classes);
    }
}
