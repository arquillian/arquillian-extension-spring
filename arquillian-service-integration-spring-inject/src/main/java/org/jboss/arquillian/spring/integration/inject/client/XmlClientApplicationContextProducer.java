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

package org.jboss.arquillian.spring.integration.inject.client;

import org.jboss.arquillian.spring.integration.context.ClientApplicationContextProducer;
import org.jboss.arquillian.spring.integration.context.ClientTestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.inject.utils.ClassPathResourceLocationsProcessor;
import org.jboss.arquillian.spring.integration.test.annotation.SpringClientConfiguration;
import org.jboss.arquillian.test.spi.TestClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>The client application context producer.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class XmlClientApplicationContextProducer implements ClientApplicationContextProducer {

    /**
     * The resource location processor.
     */
    private final ClassPathResourceLocationsProcessor locationsProcessor = new ClassPathResourceLocationsProcessor();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(TestClass testClass) {
        return testClass.isAnnotationPresent(SpringClientConfiguration.class);
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
     *
     * @return the created {@link ApplicationContext}
     */
    private ApplicationContext getApplicationContext(TestClass testClass) {

        SpringClientConfiguration springConfiguration =
                testClass.getAnnotation(SpringClientConfiguration.class);

        String [] locations = locationsProcessor.processLocations(springConfiguration.value(), testClass.getJavaClass());

        return new ClassPathXmlApplicationContext(locations);
    }
}
