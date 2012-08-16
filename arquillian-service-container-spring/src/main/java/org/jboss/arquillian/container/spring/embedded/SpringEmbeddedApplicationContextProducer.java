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

package org.jboss.arquillian.container.spring.embedded;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.spring.integration.context.ApplicationContextProducer;
import org.jboss.arquillian.spring.integration.context.RemoteApplicationContextProducer;
import org.jboss.arquillian.spring.integration.context.RemoteTestScopeApplicationContext;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;

import java.util.List;

/**
 * <p>An application context producer that scans for registered {@link ApplicationContextProducer} and invokes the one
 * that is capable of creating the application context for the test class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class SpringEmbeddedApplicationContextProducer {

    /**
     * <p>Service loader used for retrieving extensions.</p>
     */
    @Inject
    private Instance<ServiceLoader> serviceLoader;

    /**
     * <p>Producer proxy for {@link RemoteTestScopeApplicationContext}.</p>
     */
    @Inject
    private InstanceProducer<RemoteTestScopeApplicationContext> testApplicationContext;

    /**
     * <p>Builds the application context before the test suite is being executed.</p>
     *
     * @param beforeClass the event fired before execution of test case
     */
    public void initApplicationContext(@Observes BeforeClass beforeClass) {

        ServiceLoader loader = serviceLoader.get();

        // retrieves the list of all registered application context producers
        List<RemoteApplicationContextProducer> applicationContextProducers =
                (List<RemoteApplicationContextProducer>) loader.all(RemoteApplicationContextProducer.class);

        for (RemoteApplicationContextProducer applicationContextProducer : applicationContextProducers) {

            if (applicationContextProducer.supports(beforeClass.getTestClass())) {

                RemoteTestScopeApplicationContext applicationContext =
                        applicationContextProducer.createApplicationContext(beforeClass.getTestClass());

                if (applicationContext != null) {

                    testApplicationContext.set(applicationContext);

                    break;
                }
            }
        }
    }
}
