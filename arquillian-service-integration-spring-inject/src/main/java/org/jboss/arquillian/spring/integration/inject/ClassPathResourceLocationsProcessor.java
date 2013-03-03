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

package org.jboss.arquillian.spring.integration.inject;

import org.jboss.arquillian.spring.integration.test.annotation.SpringClientConfiguration;
import org.jboss.arquillian.spring.integration.test.annotation.SpringConfiguration;
import org.springframework.core.io.ClassPathResource;

/**
 * This class is responsible for finding all custom and default XML Spring context configuration files.
 *
 * Default context file name format is: TestClassName-context.xml
 *
 * @author <a href="mailto:kurlenda@gmail.com">Jakub Kurlenda</a>
 *
 */
public class ClassPathResourceLocationsProcessor {
    /**
     * Array returned when no custom and default locations were specified for test class.
     */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * Suffix required for default location matching.
     */
    static final String DEFAULT_LOCATION_SUFFIX = "-context.xml";
    private static final String CLASSPATH_PREFIX = "classpath:";
    private static final String SLASH = "/";

    /**
     * <p> Searches for XML resources for {@param wrappedClass} test case.</p>
     *
     * @param springConfiguration - context configuration for TestClass instance.
     * @param wrappedClass - test class wrapped by TestClass instance.
     * @return array which contains set of test resource locations.
     */
    public String[] processLocations(SpringClientConfiguration springConfiguration, Class wrappedClass) {
        return processLocationsInternal(springConfiguration.value(), wrappedClass);
    }

    public String[] processLocations(SpringConfiguration springConfiguration, Class wrappedClass) {
        return processLocationsInternal(springConfiguration.value(), wrappedClass);
    }

    private String[] processLocationsInternal(String[] configurationLocations, Class wrappedClass) {
        if (customLocationsWereSpecified(configurationLocations)) {
            return configurationLocations;
        }
        return defaultLocationForGivenTestClass(wrappedClass);
    }

    /**
     * Checks whether custom XML resource location was provided in {@param springConfiguration}
     *
     * @param locations context configuration for TestClass instance.
     * @return true if custom location was specified, false otherwise.
     */
    private boolean customLocationsWereSpecified(String [] locations) {
        return locations.length > 0;
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
