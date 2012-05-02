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

import org.jboss.arquillian.spring.SpringExtensionConsts;
import org.jboss.arquillian.spring.annotations.SpringConfiguration;
import org.jboss.arquillian.test.spi.TestClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * <p>The {@link AbstractApplicationContextProducer} that creates the {@link org.springframework.context.support.ClassPathXmlApplicationContext}
 * instance using xml files.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class XmlApplicationContextProducer extends AbstractApplicationContextProducer {

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean supports(TestClass testClass) {
        return testClass.isAnnotationPresent(SpringConfiguration.class);
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
     * @return created {@link org.springframework.context.ApplicationContext}
     */
    private ApplicationContext getApplicationContext(TestClass testClass) {

        SpringConfiguration springConfiguration = testClass.getAnnotation(SpringConfiguration.class);

        String[] locations = new String[]{SpringExtensionConsts.DEFAULT_LOCATION};
        if (springConfiguration.value().length > 0) {

            locations = springConfiguration.value();
        }

        Class<? extends ApplicationContext> applicationContextClass = ClassPathXmlApplicationContext.class;
        if (springConfiguration.contextClass() != ApplicationContext.class) {

            applicationContextClass = springConfiguration.contextClass();
        }

        return createInstance(applicationContextClass, locations);
    }

    /**
     * <p>Creates new instance of {@link ApplicationContext}.</p>
     *
     * @param applicationContextClass the class of application context
     * @param locations               the locations from which load the configuration files
     *
     * @return the created {@link ApplicationContext} instance
     */
    private <T extends ApplicationContext> ApplicationContext createInstance(Class<T> applicationContextClass,
                                                                             String[] locations) {

        try {
            Constructor<T> ctor = applicationContextClass.getConstructor(String[].class);

            return (T) ctor.newInstance(new Object[] {locations});
        } catch (NoSuchMethodException e) {

            throw new RuntimeException("Could not create instance of " + applicationContextClass.getName()
                    + ", no appropriate constructor found.", e);
        } catch (InvocationTargetException e) {

            throw new RuntimeException("Could not create instance of " + applicationContextClass.getName(), e);
        } catch (InstantiationException e) {

            throw new RuntimeException("Could not create instance of " + applicationContextClass.getName(), e);
        } catch (IllegalAccessException e) {

            throw new RuntimeException("Could not create instance of " + applicationContextClass.getName(), e);
        }
    }
}
