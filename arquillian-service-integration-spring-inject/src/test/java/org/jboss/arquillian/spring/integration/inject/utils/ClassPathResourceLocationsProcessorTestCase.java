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
package org.jboss.arquillian.spring.integration.inject.utils;

import org.jboss.arquillian.spring.integration.inject.model.ClientXmlAnnotatedClass;
import org.jboss.arquillian.spring.integration.inject.model.ClientXmlAnnotatedClassWithBothCustomAndDefaultLocations;
import org.jboss.arquillian.spring.integration.inject.model.ClientXmlAnnotatedClassWithNotExistingDefaultLocation;
import org.jboss.arquillian.spring.integration.test.annotation.SpringClientConfiguration;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.jboss.arquillian.spring.integration.inject.utils.ClassPathResourceLocationsProcessor.DEFAULT_LOCATION_SUFFIX;

/**
 * Tests the {@link org.jboss.arquillian.spring.integration.inject.utils.ClassPathResourceLocationsProcessor} class.
 */
public class ClassPathResourceLocationsProcessorTestCase {

    /**
     * Represents the instance of the tested class.
     */
    private ClassPathResourceLocationsProcessor locationsProcessor = new ClassPathResourceLocationsProcessor();

    @Test
    public void emptyArrayShouldBeReturnedWhenDefaultTestResourceDoesNotExist() {
        // given
        Class testClass = ClientXmlAnnotatedClassWithNotExistingDefaultLocation.class;

        // when
        String[] defaultLocations = locationsProcessor.defaultLocationForGivenTestClass(testClass);

        // then
        assertThat(defaultLocations).isEmpty();
    }

    @Test
    public void arrayWithDefaultLocationShouldBeReturnedWhenDefaultResourceExists() {
        // given
        Class testClass = ClientXmlAnnotatedClass.class;
        String expectedLocationName = testClass.getClass().getSimpleName() + DEFAULT_LOCATION_SUFFIX;

        // when
        String[] defaultLocations = locationsProcessor.defaultLocationForGivenTestClass(testClass);

        // then
        assertThat(defaultLocations).hasSize(1);
        assertThat(defaultLocations[0]).contains(expectedLocationName);
    }

    @Test
    public void arrayWithCustomLocationShouldBeReturnedWhenBothDefaultAndCustomResourcesExist() {
        // given
        Class<ClientXmlAnnotatedClassWithBothCustomAndDefaultLocations> wrappedClass = ClientXmlAnnotatedClassWithBothCustomAndDefaultLocations.class;

        SpringClientConfiguration testClassAnnotation = wrappedClass.getAnnotation(SpringClientConfiguration.class);
        String[] expectedLocationNames = testClassAnnotation.value();

        // when
        String[] defaultLocations = locationsProcessor.processLocations(testClassAnnotation.value(), wrappedClass);

        // then
        assertThat(defaultLocations).hasSize(expectedLocationNames.length);
        assertThat(defaultLocations).containsOnly((Object[]) expectedLocationNames);
    }
}
