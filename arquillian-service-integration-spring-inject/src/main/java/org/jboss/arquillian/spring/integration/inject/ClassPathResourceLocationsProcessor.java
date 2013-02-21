package org.jboss.arquillian.spring.integration.inject;

import org.jboss.arquillian.spring.integration.test.annotation.SpringClientConfiguration;
import org.jboss.arquillian.spring.integration.test.annotation.SpringConfiguration;
import org.springframework.core.io.ClassPathResource;

public class ClassPathResourceLocationsProcessor {
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
