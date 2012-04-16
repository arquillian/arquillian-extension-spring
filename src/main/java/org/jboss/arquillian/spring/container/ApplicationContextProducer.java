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
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.text.MessageFormat;
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
                applicationContext = getWebApplicationContext(beforeClass.getTestClass());
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
     * Retrieves the web application context for the given test.
     *
     * @param testClass the test class
     *
     * @return {@link ApplicationContext} retrieves from web application
     */
    private ApplicationContext getWebApplicationContext(TestClass testClass) {

        SpringWebConfiguration springWebConfiguration;
        WebApplicationContext rootContext;
        ApplicationContext applicationContext;

        rootContext = ContextLoader.getCurrentWebApplicationContext();

        if (rootContext == null) {

            throw new RuntimeException("The Spring Root Web Application Context could not be found.");
        }

        springWebConfiguration = testClass.getAnnotation(SpringWebConfiguration.class);

        if (!isEmpty(springWebConfiguration.servletName())) {

            applicationContext = getServletApplicationContext(rootContext, springWebConfiguration.servletName());

            if (applicationContext == null) {

                throw new RuntimeException("Could not find the application context for servlet: " +
                        springWebConfiguration.servletName());
            }
        } else {
            // uses the root context as the main application context
            applicationContext = rootContext;
        }

        return applicationContext;
    }

    /**
     * Creates the application context.
     *
     * @param testClass the test class
     *
     * @return created {@link ApplicationContext}
     */
    private ApplicationContext createApplicationContext(TestClass testClass) {

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

        return new ClassPathXmlApplicationContext(SpringExtensionConsts.DEFAULT_LOCATION);
    }

    private ApplicationContext getServletApplicationContext(WebApplicationContext rootContext, String servletName) {

        // retrieves the servlet context from web application context
        ServletContext servletContext = rootContext.getServletContext();

        // retrieves the application context for the given servlet using it's name
        return (ApplicationContext) servletContext.getAttribute(MessageFormat.format(
                "org.springframework.web.servlet.FrameworkServlet.CONTEXT.{0}", servletName));
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

    /**
     * Whether the passed string is null or empty string (after trimming).
     *
     * @param value the string to validate
     *
     * @return true if the passed is null or empty, false otherwise
     */
    private static boolean isEmpty(String value) {

        return value == null || value.trim().length() == 0;
    }
}
