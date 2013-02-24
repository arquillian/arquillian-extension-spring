package org.jboss.arquillian.spring.integration.javaconfig.client;

import org.jboss.arquillian.spring.integration.test.annotation.SpringClientAnnotationConfiguration;
import org.jboss.arquillian.test.spi.TestClass;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class DefaultConfigurationClassesProcessor {

    static final String VALIDATION_MESSAGE_SUFFIX_INNER_CLASS_DECLARED_NOT_STATIC = " should be declared static.";
    static final String VALIDATION_MESSAGE_SUFFIX_INNER_CONFIGURATION_CLASS_DECLARED_FINAL = " must not be declared final.";

    /**
     * Finds classes declared as configuration source.
     * First, springConfiguration.classes property is being checked.
     * If this check returns empty array, default configuration is being searched (inner static class marked
     * as @Configuration)
     *
     * @param springConfiguration - spring configuration
     * @param testClass - class annotated with {springConfiguration}
     * @return
     */
    Class<?>[] findConfigurationClasses(SpringClientAnnotationConfiguration springConfiguration, TestClass testClass) {
        if (customClassesWereSpecified(springConfiguration)) {
            return springConfiguration.classes();
        }
        return defaultConfigurationClasses(testClass.getJavaClass());
    }

    /**
     * Checks whether any custom configuration classes were declared in SpringClientAnnotationConfiguration annotation.
     * @param springConfiguration - annotation to check
     * @return true if classes() array is not empty, false otherwise.
     */
    private boolean customClassesWereSpecified(SpringClientAnnotationConfiguration springConfiguration) {
        return springConfiguration.classes().length > 0;
    }

    /**
     * Tries to find all default configuration classes for given testClass
     * @param testClass - test class to check
     * @return - list of all inner static classes marked with @Configuration annotation
     */
    private Class<?>[] defaultConfigurationClasses(Class testClass) {
        Class<?>[] configurationCandidates = testClass.getClasses();
        Set<Class<?>> configurationClasses = new HashSet<Class<?>>();

        for (Class<?> configurationCandidate : configurationCandidates) {
            if (configurationCandidate.isAnnotationPresent(Configuration.class)) {
                validateConfigurationCandidate(configurationCandidate);
                configurationClasses.add(configurationCandidate);
            }
        }

        return configurationClasses.toArray(new Class<?>[0]);
    }

    /**
     * Validates configuration class candidate correctness (is must be static non final class)
     * @param configurationCandidate - configuration class to verify
     */
    private void validateConfigurationCandidate(Class<?> configurationCandidate) {
        throwExceptionIfConfiguratoinClassNotDeclaredStatic(configurationCandidate);
        throwExceptionIfConfigurationClassDeclaredFinal(configurationCandidate);
    }

    private void throwExceptionIfConfigurationClassDeclaredFinal(Class<?> configurationCandidate) {
        if (Modifier.isFinal(configurationCandidate.getModifiers())) {
            throw new RuntimeException(buildValidationMessage(configurationCandidate, VALIDATION_MESSAGE_SUFFIX_INNER_CONFIGURATION_CLASS_DECLARED_FINAL));
        }
    }

    private void throwExceptionIfConfiguratoinClassNotDeclaredStatic(Class<?> configurationCandidate) {
        if (!Modifier.isStatic(configurationCandidate.getModifiers())) {
            throw new RuntimeException(buildValidationMessage(configurationCandidate, VALIDATION_MESSAGE_SUFFIX_INNER_CLASS_DECLARED_NOT_STATIC));
        }
    }

    private String buildValidationMessage(Class<?> configurationCandidate, String validationMessageSuffix) {
        return "Default Java configuration class: " + configurationCandidate + validationMessageSuffix;
    }
}
