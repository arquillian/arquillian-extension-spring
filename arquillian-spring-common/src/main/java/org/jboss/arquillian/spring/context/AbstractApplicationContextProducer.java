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

import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;

import java.util.logging.Logger;

/**
 * <p>Abstract application context producer, the concrete implementation will be responsible for actual creating the
 * application context for the given test case.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public abstract class AbstractApplicationContextProducer {

    /**
     * <p>The logger used by this class.</p>
     */
    private static final Logger log = Logger.getLogger(AbstractApplicationContextProducer.class.getName());

    /**
     * <p>Producer proxy for {@link org.springframework.context.ApplicationContext}.</p>
     */
    @Inject
    @ApplicationScoped
    private InstanceProducer<TestScopeApplicationContext> testApplicationContext;

    /**
     * <p>Builds the application context before the test suite is being executed.</p>
     *
     * @param beforeClass the event fired before execution of test case
     */
    public void initApplicationContext(@Observes BeforeClass beforeClass) {

        if (supports(beforeClass.getTestClass())) {

            TestScopeApplicationContext applicationContext = createApplicationContext(beforeClass.getTestClass());

            if (applicationContext != null) {

                testApplicationContext.set(applicationContext);

                log.fine("Successfully created application context for test class: "
                        + beforeClass.getTestClass().getName());
            }
        }
    }

    /**
     * <p>Returns whether the given extension support the given test class and can be used for creating the application
     * context.</p>
     *
     * @param testClass the test class
     *
     * @return true if the producer can create the application context, false otherwise
     */
    protected abstract boolean supports(TestClass testClass);

    /**
     * <p>Creates instance of {@link TestScopeApplicationContext} for the given test class.</p>
     *
     * @param testClass the test class
     *
     * @return the created instance of {@link TestScopeApplicationContext}
     */
    protected abstract TestScopeApplicationContext createApplicationContext(TestClass testClass);
}
