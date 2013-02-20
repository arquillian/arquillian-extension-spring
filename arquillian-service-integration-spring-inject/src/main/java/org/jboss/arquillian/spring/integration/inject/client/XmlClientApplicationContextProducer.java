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
import org.jboss.arquillian.spring.integration.test.annotation.SpringClientConfiguration;
import org.jboss.arquillian.test.spi.TestClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * <p>The client application context producer.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class XmlClientApplicationContextProducer implements ClientApplicationContextProducer {

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    static final String DEFAULT_LOCATION_SUFFIX = "-context.xml";
    private final String CLASSPATH_PREFIX = "classpath:";
    private final String SLASH = "/";

    /**
     * Array returned when no custom and default locations were specified for test class.
     */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * Suffix required for default location matching.
     */
    static final String DEFAULT_LOCATION_SUFFIX = "-context.xml";
    private final String CLASSPATH_PREFIX = "classpath:";
    private final String SLASH = "/";

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

        return new ClientTestScopeApplicationContext(getApplicationContext(testClass), true);
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

        String [] locations = processLocations(springConfiguration, testClass.getJavaClass());

        return new ClassPathXmlApplicationContext(locations);
    }

    /**
     * <p> Searches for XML resources for {@param wrappedClass} test case.</p>
     *
     * @param springConfiguration - context configuration for TestClass instance.
     * @param wrappedClass - test class wrapped by TestClass instance.
     * @return array which contains set of test resource locations.
     */
    String[] processLocations(SpringClientConfiguration springConfiguration, Class wrappedClass) {
        if (customLocationsWereSpecified(springConfiguration)) {
            return springConfiguration.value();
        }
        return defaultLocationForGivenTestClass(wrappedClass);
    }

    /**
     * Checks whether custom XML resource location was provided in {@param springConfiguration}
     *
     * @param springConfiguration context configuration for TestClass instance.
     * @return true if custom location was specified, false otherwise.
     */
    private boolean customLocationsWereSpecified(SpringClientConfiguration springConfiguration) {
        return springConfiguration.value().length > 0;
    }

    /**
     * Builds array with default locations for {@param testClass} test instance.
     * @param testClass - test class wrapped by TestClass instance.
     * @return array with default location (in form testClassName-context.xml) or empty array otherwise.
     */
    String[] defaultLocationForGivenTestClass(Class testClass) {
        String fullyQualifiedTestClassName = testClass.getName();
        String defaultConfigResourcePath = SLASH + fullyQualifiedTestClassName.replace('.', '/') + DEFAULT_LOCATION_SUFFIX;
        String prefixedResourcePath = CLASSPATH_PREFIX + defaultConfigResourcePath;
        ClassPathResource classPathResource = new ClassPathResource(defaultConfigResourcePath, testClass);

        if (classPathResource.exists()) {
            return new String[] {prefixedResourcePath};
        }

        return EMPTY_STRING_ARRAY;
    }
}
