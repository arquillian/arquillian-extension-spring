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
package org.jboss.arquillian.spring.container;

import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.spring.SpringExtensionConsts;
import org.jboss.arquillian.spring.annotations.SpringConfiguration;
import org.jboss.arquillian.spring.annotations.SpringWebConfiguration;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * A producer that creates the instance of {@link ApplicationContext}.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class ApplicationContextProducer {

    /**
     * The logger used by this class.
     */
    private static final Logger log = Logger.getLogger(ApplicationContextProducer.class.getName());

    /**
     * Producer proxy for {@link ApplicationContext}.
     */
    @Inject
    @ApplicationScoped
    private InstanceProducer<ApplicationContext> applicationContextProducer;

    /**
     * Builds the application context before the test suite is being executed.
     *
     * @param beforeClass the event fired before execution of concrete class
     */
    public void initApplicationContext(@Observes BeforeClass beforeClass) {

        ApplicationContext applicationContext;

        if (isSpringTest(beforeClass.getTestClass())) {

            if (isWebTest(beforeClass.getTestClass())) {
                applicationContext = ContextLoader.getCurrentWebApplicationContext();
            } else {
                applicationContext = createApplicationContext(beforeClass.getTestClass());
            }

            // sets the application context to be shared among all tests
            if (applicationContext != null) {
                applicationContextProducer.set(applicationContext);

                log.fine("Successfully created application context for test class: "
                        + beforeClass.getTestClass().getName());
            } else {

                log.warning("The application context could not be created");
            }
        }
    }

    /**
     * Returns whether the given test runs in web application.
     *
     * @param testClass the test class
     *
     * @return true if the given test runs in web application, false otherwise
     */
    private boolean isWebTest(TestClass testClass) {
        return testClass.isAnnotationPresent(SpringWebConfiguration.class);
    }

    /**
     * Returns whether the given test is spring test and requires bean injection.
     *
     * @param testClass the test class
     *
     * @return true if the test class is spring test, false otherwise
     */
    private boolean isSpringTest(TestClass testClass) {
        return testClass.isAnnotationPresent(SpringConfiguration.class)
                || testClass.isAnnotationPresent(SpringWebConfiguration.class);
    }

    /**
     * Creates the application context.
     *
     * @param testClass the test class
     *
     * @return created {@link ApplicationContext}
     */
    private ApplicationContext createApplicationContext(TestClass testClass) {

        if (testClass.isAnnotationPresent(SpringConfiguration.class)) {
            SpringConfiguration springConfiguration = testClass.getAnnotation(SpringConfiguration.class);

            String[] packages = springConfiguration.packages();
            Class<?>[] classes = springConfiguration.classes();
            if (packages.length > 0 || classes.length > 0) {

                return createConfigApplicationContext(classes, packages);
            }

            String[] configLocations = getConfigLocations(springConfiguration);
            if (configLocations.length > 0) {

                return new ClassPathXmlApplicationContext(configLocations);
            }
        }

        return new ClassPathXmlApplicationContext(SpringExtensionConsts.DEFAULT_LOCATION);
    }

    /**
     * Creates instance of {@link AnnotationConfigApplicationContext} class.
     *
     * @param classes  the annotated classes to register
     * @param packages the packages containing the annotated classes
     *
     * @return the created instance of {@link AnnotationConfigApplicationContext}
     */
    private ApplicationContext createConfigApplicationContext(Class<?>[] classes, String[] packages) {

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

    /**
     * Retrieves the config locations from annotation.
     *
     * @param springConfiguration {@link SpringConfiguration} annotation
     *
     * @return config locations
     */
    private String[] getConfigLocations(SpringConfiguration springConfiguration) {
        ArrayList<String> mergedLocations = new ArrayList();
        if (springConfiguration.locations() != null) {
            mergedLocations.addAll(Arrays.asList(springConfiguration.locations()));
        }

        if (springConfiguration.value() != null) {
            mergedLocations.addAll(Arrays.asList(springConfiguration.value()));
        }

        return mergedLocations.toArray(new String[mergedLocations.size()]);
    }
}
