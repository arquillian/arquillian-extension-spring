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
import org.jboss.arquillian.test.spi.event.suite.AfterClass;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.logging.Logger;

/**
 * <p>Closes {@link org.springframework.context.ApplicationContext} where it is not needed any more.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class ApplicationContextDestroyer {

    /**
     * <p>The logger used by this class.</p>
     */
    private static final Logger log = Logger.getLogger(ApplicationContextDestroyer.class.getName());

    /**
     * <p>Producer proxy for {@link org.springframework.context.ApplicationContext}.</p>
     */
    @Inject
    @ApplicationScoped
    private InstanceProducer<TestScopeApplicationContext> testApplicationContext;

    /**
     * <p>Builds the application context before the test suite is being executed.</p>
     *
     * @param afterClass the event fired after execution of test case
     */
    public void destroyApplicationContext(@Observes AfterClass afterClass) {

        if (testApplicationContext.get() != null) {
            TestScopeApplicationContext testContext = testApplicationContext.get();

            if (testContext.isClosable()
                    && testContext.getApplicationContext() instanceof ConfigurableApplicationContext) {

                // closes the application context freeing all resources
                ((ConfigurableApplicationContext) testContext.getApplicationContext()).close();
                log.fine("Closing the application context.");
            }
        }
    }
}
