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
package org.jboss.arquillian.spring.integration.javaconfig.utils;

import org.jboss.arquillian.spring.integration.test.annotation.SpringAnnotationConfiguration;
import org.jboss.arquillian.spring.integration.test.annotation.SpringClientAnnotationConfiguration;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is responsible for finding default Spring configuration classes. Default configuration class must be
 * public inner static class marked with @Configuration annotation.
 *
 * @author <a href="mailto:kurlenda@gmail.com">Jakub Kurlenda</a>
 */
public class DefaultConfigurationClassesProcessor {

    public static final String VALIDATION_MESSAGE_SUFFIX_INNER_CLASS_DECLARED_NOT_STATIC = " should be declared static.";

    public static final String VALIDATION_MESSAGE_SUFFIX_INNER_CONFIGURATION_CLASS_DECLARED_FINAL = " must not be declared final.";

    private static final String[] EMPTY_ARRAY = new String[0];

    /**
     * Finds classes declared as configuration source. First, springConfiguration.classes property is being checked. If
     * this check returns empty array, default configuration is being searched (inner static class marked as
     * @Configuration)
     *
     * @param springConfiguration - spring configuration
     * @param testClass           - class annotated with {springConfiguration}
     *
     * @return the classes that matches
     */
    public Class<?>[] findConfigurationClasses(SpringClientAnnotationConfiguration springConfiguration, Class testClass) {
        if (customClassesOrPackagesWereSpecified(springConfiguration)) {
            return springConfiguration.classes();
        }
        return defaultConfigurationClasses(testClass);
    }

    /**
     * Finds classes declared as configuration source. First, springConfiguration.classes property is being checked. If
     * this check returns empty array, default configuration is being searched (inner static class marked as
     * @Configuration)
     *
     * @param springConfiguration - spring configuration
     * @param testClass           - class annotated with {springConfiguration}
     *
     * @return list of configuration classes defined for test.
     */
    public Class<?>[] findConfigurationClasses(SpringAnnotationConfiguration springConfiguration, Class testClass) {
        if (customClassesOrPackagesWereSpecified(springConfiguration)) {
            return springConfiguration.classes();
        }
        return defaultConfigurationClasses(testClass);
    }

    /**
     * Finds package names defined on either SpringClientAnnotationConfiguration annotation level or testClass level.
     * Currently packages from these two sources are not merged.
     *
     * @param springConfiguration - spring configuration annotation
     * @param testClass           - class annotated with {springConfiguration}
     *
     * @return list of packages defined for test.
     */
    public String[] findPackages(SpringClientAnnotationConfiguration springConfiguration, Class testClass) {
        if (packagesWereDefinedOnAnnotationLevel(springConfiguration)) {
            return springConfiguration.packages();
        }
        return EMPTY_ARRAY;
    }

    /**
     * Finds package names defined on either SpringClientAnnotationConfiguration annotation level or testClass level.
     * Currently packages from these two sources are not merged.
     *
     * @param springConfiguration - spring configuration annotation
     * @param testClass           - class annotated with {springConfiguration}
     *
     * @return list of packages defined for test.
     */
    public String[] findPackages(SpringAnnotationConfiguration springConfiguration, Class testClass) {
        if (packagesWereDefinedOnAnnotationLevel(springConfiguration)) {
            return springConfiguration.packages();
        }
        return EMPTY_ARRAY;
    }

    private boolean packagesWereDefinedOnAnnotationLevel(SpringClientAnnotationConfiguration springConfiguration) {
        return springConfiguration.packages().length > 0;
    }

    private boolean packagesWereDefinedOnAnnotationLevel(SpringAnnotationConfiguration springConfiguration) {
        return springConfiguration.packages().length > 0;
    }

    /**
     * Checks whether any custom configuration classes were declared in SpringClientAnnotationConfiguration annotation.
     *
     * @param springConfiguration - annotation to check
     *
     * @return true if classes() array is not empty, false otherwise.
     */
    private boolean customClassesOrPackagesWereSpecified(SpringClientAnnotationConfiguration springConfiguration) {
        return springConfiguration.classes().length > 0 || springConfiguration.packages().length > 0;
    }

    /**
     * Checks whether any custom configuration classes were declared in SpringClientAnnotationConfiguration annotation.
     *
     * @param springConfiguration - annotation to check
     *
     * @return true if classes() array is not empty, false otherwise.
     */
    private boolean customClassesOrPackagesWereSpecified(SpringAnnotationConfiguration springConfiguration) {
        return springConfiguration.classes().length > 0 || springConfiguration.packages().length > 0;
    }

    /**
     * Tries to find all default configuration classes for given testClass
     *
     * @param testClass - test class to check
     *
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
     *
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
