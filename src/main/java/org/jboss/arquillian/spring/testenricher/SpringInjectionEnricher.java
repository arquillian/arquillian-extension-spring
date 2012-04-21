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
package org.jboss.arquillian.spring.testenricher;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.spring.SpringExtensionConsts;
import org.jboss.arquillian.spring.context.TestScopeApplicationContext;
import org.jboss.arquillian.test.spi.TestEnricher;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * <p>Spring test enricher, injects spring beans.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class SpringInjectionEnricher implements TestEnricher {

    /**
     * <p>Logger used by this class.</p>
     */
    private static final Logger log = Logger.getLogger(SpringInjectionEnricher.class.getName());

    /**
     * <p>Instance of Spring {@link ApplicationContext}.</p>
     */
    @Inject
    private Instance<TestScopeApplicationContext> testApplicationContext;

    /**
     * {@inheritDoc}
     */
    @Override
    public void enrich(Object testCase) {

        if (SecurityActions.isClassPresent(SpringExtensionConsts.APPLICATION_CONTEXT)
                && applicationContextExists()) {
            injectClass(testCase);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] resolve(Method method) {

        return new Object[method.getParameterTypes().length];
    }

    /**
     * <p>Injects beans into the tests case.</p>
     *
     * @param testCase the test case
     */
    private void injectClass(Object testCase) {

        ApplicationContext applicationContext = getApplicationContext();

        if (applicationContext != null) {
            log.fine("Injecting dependencies into test case: " + testCase.getClass().getName());
            injectDependencies(applicationContext, testCase);
        }
    }

    /**
     * <p>Injects dependencies into the test case.</p>
     *
     * @param applicationContext the {@link ApplicationContext}
     * @param testCase           the test case for which the beans will be injected
     */
    private void injectDependencies(ApplicationContext applicationContext, Object testCase) {

        // retrieves the bean factory
        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        // injects all the members
        beanFactory.autowireBeanProperties(testCase, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
        // initialize the bean
        beanFactory.initializeBean(testCase, testCase.getClass().getName());
    }

    /**
     * <p>Retrieves the {@link ApplicationContext}.</p>
     *
     * @return the {@link ApplicationContext}
     */
    private ApplicationContext getApplicationContext() {

        if (testApplicationContext.get() != null) {

            return testApplicationContext.get().getApplicationContext();
        }

        return null;
    }

    /**
     * <p>Returns whether the application context for the test has been created.</p>
     *
     * @return true if the application context exists, false otherwise
     */
    private boolean applicationContextExists() {

        return testApplicationContext.get() != null;
    }
}
