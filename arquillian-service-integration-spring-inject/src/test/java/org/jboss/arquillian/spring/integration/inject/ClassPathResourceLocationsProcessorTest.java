package org.jboss.arquillian.spring.integration.inject;

import org.jboss.arquillian.spring.integration.inject.model.ClientXmlAnnotatedClass;
import org.jboss.arquillian.spring.integration.inject.model.ClientXmlAnnotatedClassWithBothCustomAndDefaultLocations;
import org.jboss.arquillian.spring.integration.inject.model.ClientXmlAnnotatedClassWithNotExistingDefaultLocation;
import org.jboss.arquillian.spring.integration.test.annotation.SpringClientConfiguration;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.jboss.arquillian.spring.integration.inject.ClassPathResourceLocationsProcessor.DEFAULT_LOCATION_SUFFIX;

public class ClassPathResourceLocationsProcessorTest {

    ClassPathResourceLocationsProcessor locationsProcessor = new ClassPathResourceLocationsProcessor();

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
        String [] expectedLocationNames = testClassAnnotation.value();

        // when
        String[] defaultLocations = locationsProcessor.processLocations(testClassAnnotation, wrappedClass);

        // then
        assertThat(defaultLocations).hasSize(expectedLocationNames.length);
        assertThat(defaultLocations).containsOnly(expectedLocationNames);
    }
}
