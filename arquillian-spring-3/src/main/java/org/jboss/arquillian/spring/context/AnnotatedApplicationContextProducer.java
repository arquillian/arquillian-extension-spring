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
package org.jboss.arquillian.spring.context;

import org.jboss.arquillian.spring.annotations.SpringAnnotatedConfiguration;
import org.jboss.arquillian.test.spi.TestClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * <p>The {@link AbstractApplicationContextProducer} that creates the {@link AnnotationConfigApplicationContext}
 * instance.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class AnnotatedApplicationContextProducer extends AbstractApplicationContextProducer {

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean supports(TestClass testClass) {
        return testClass.isAnnotationPresent(SpringAnnotatedConfiguration.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected TestScopeApplicationContext createApplicationContext(TestClass testClass) {

        return new TestScopeApplicationContext(getApplicationContext(testClass), true);
    }

    /**
     * <p>Creates the application context.</p>
     *
     * @param testClass the test class
     *
     * @return created {@link ApplicationContext}
     */
    private ApplicationContext getApplicationContext(TestClass testClass) {

        SpringAnnotatedConfiguration springConfiguration = testClass.getAnnotation(SpringAnnotatedConfiguration.class);

        String[] packages = springConfiguration.packages();
        Class<?>[] classes = springConfiguration.classes();

        if (springConfiguration.contextClass() != ApplicationContext.class) {

            // creates custom annotated application context
            return createCustomAnnotatedApplicationContext(testClass, springConfiguration.contextClass(), classes, packages);
        }

        // creates standard spring annotated application context
        return createAnnotatedApplicationContext(testClass, packages, classes);
    }

    /**
     * <p>Creates the instance of {@link AnnotationConfigApplicationContext}.</p>
     *
     * @param testClass the test class
     * @param classes   the annotated classes to register
     * @param packages  the packages containing the annotated classes
     *
     * @return the created instance of {@link AnnotationConfigApplicationContext}
     */
    private ApplicationContext createAnnotatedApplicationContext(TestClass testClass, String[] packages, Class<?>[] classes) {
        if (packages.length > 0 || classes.length > 0) {

            return createAnnotatedApplicationContext(classes, packages);
        }

        throw new RuntimeException("The test: " + testClass.getName()
                + " annotated with SpringAnnotatedConfiguration must specify the configuration classes or packages.");
    }

    /**
     * <p>Creates instance of {@link AnnotationConfigApplicationContext} class.</p>
     *
     * @param classes  the annotated classes to register
     * @param packages the packages containing the annotated classes
     *
     * @return the created instance of {@link AnnotationConfigApplicationContext}
     */
    private ApplicationContext createAnnotatedApplicationContext(Class<?>[] classes, String[] packages) {

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
     * <p>Creates new instance of custom application context.</p>
     *
     * @param applicationContextClass the application context class
     * @param classes                 the annotated classes to register
     * @param packages                the packages containing the annotated classes
     *
     * @return the created instance of {@link ApplicationContext}
     */
    private <T extends ApplicationContext> T createCustomAnnotatedApplicationContext(
            TestClass testClass, Class<T> applicationContextClass, Class<?>[] classes, String[] packages) {

        Constructor<T> ctor;

        if (classes.length > 0 && packages.length > 0) {

            ctor = getConstructor(applicationContextClass, Class[].class, String[].class);

            return createInstance(applicationContextClass, ctor, classes, packages);
        } else if (classes.length > 0) {

            ctor = getConstructor(applicationContextClass, Class[].class);

            return createInstance(applicationContextClass, ctor, (Object[])classes);
        } else if (packages.length > 0) {

            ctor = getConstructor(applicationContextClass, String[].class);

            return createInstance(applicationContextClass, ctor, (Object[])packages);
        }

        throw new RuntimeException("The test: " + testClass.getName()
                + " annotated with SpringAnnotatedConfiguration must specify the configuration classes or packages.");
    }

    /**
     * <p>Creates new instance of the given type.</p>
     *
     * @param applicationContextClass the application context class
     * @param ctor                    the constructor to use
     * @param params                  the constructor parameters
     * @param <T>                     the type of the application context
     *
     * @return the created instance of {@link ApplicationContext}
     */
    private <T extends ApplicationContext> T createInstance(Class<T> applicationContextClass,
                                                            Constructor<T> ctor, Object... params) {

        try {
            return ctor.newInstance(params);
        } catch (InstantiationException e) {

            throw new RuntimeException("Could not create instance of " + applicationContextClass.getName(), e);
        } catch (IllegalAccessException e) {

            throw new RuntimeException("Could not create instance of " + applicationContextClass.getName(), e);
        } catch (InvocationTargetException e) {

            throw new RuntimeException("Could not create instance of " + applicationContextClass.getName(), e);
        }
    }

    /**
     * <p>Retrieves the constructor of the specified type.</p>
     *
     * @param type           the class type for which the constructor will be retrieved
     * @param parameterTypes the types of the parameters
     * @param <T>            the type of the application context
     *
     * @return the retrieved constructor
     */
    private <T extends ApplicationContext> Constructor<T> getConstructor(Class<T> type, Class... parameterTypes) {

        try {
            return type.getConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {

            throw new RuntimeException("Could not find a proper constructor for type: " + type.getName(), e);
        }
    }
}
