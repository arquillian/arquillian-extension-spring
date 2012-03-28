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
package org.jboss.arquillian.testenricher.spring.container;

import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
     * @param suite the event
     */
    public void buildApplicationContext(@Observes BeforeSuite suite) {

        // creates the application context
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        // set the application context to be shared among all tests
        if (applicationContext != null) {
            applicationContextProducer.set(applicationContext);

            log.fine("Successfully created application context");
        } else {

            log.warning("The application context could not be created");
        }
    }
}
