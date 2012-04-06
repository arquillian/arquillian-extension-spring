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

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.spring.SpringExtensionConsts;
import org.jboss.arquillian.test.spi.TestEnricher;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * Spring test enricher, injects spring beans.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class SpringInjectionEnricher implements TestEnricher {

    /**
     * Logger used by this class.
     */
    private static final Logger log = Logger.getLogger(SpringInjectionEnricher.class.getName());

    /**
     * Instance of Spring {@link ApplicationContext}.
     */
    @Inject
    private Instance<ApplicationContext> applicationContext;

    /**
     * {@inheritDoc}
     */
    @Override
    public void enrich(Object testCase) {

        if (SecurityActions.isClassPresent(SpringExtensionConsts.APPLICATION_CONTEXT)) {
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
     * Injects the tests case.
     *
     * @param testCase the test case
     */
    private void injectClass(Object testCase) {

        ApplicationContext applicationContext = getApplicationContext();

        if (applicationContext != null) {
            log.fine("Injecting dependencies into bean.");
            injectDependencies(applicationContext, testCase);
        }
    }

    /**
     * Injects dependencies into the test bean.
     *
     * @param applicationContext the {@link ApplicationContext}
     * @param testCase           the test case for which the beans will be injected
     */
    private void injectDependencies(ApplicationContext applicationContext, Object testCase) {

        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        beanFactory.autowireBeanProperties(testCase, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
        beanFactory.initializeBean(testCase, testCase.getClass().getName());
    }

    /**
     * Retrieves the {@link ApplicationContext}.
     *
     * @return the {@link ApplicationContext}
     */
    private ApplicationContext getApplicationContext() {

        return applicationContext.get();
    }
}
