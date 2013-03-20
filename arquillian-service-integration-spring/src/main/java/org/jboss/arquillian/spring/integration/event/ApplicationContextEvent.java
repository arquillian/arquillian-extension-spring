/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.arquillian.spring.integration.event;

import org.jboss.arquillian.core.spi.Validate;
import org.jboss.arquillian.core.spi.event.Event;
import org.jboss.arquillian.spring.integration.context.TestScopeApplicationContext;
import org.springframework.context.ApplicationContext;

/**
 * <p>The base event that is used for indicating {@link ApplicationContext} life cycle events.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class ApplicationContextEvent implements Event {

    /**
     * <p>Represents the instance of {@link ApplicationContext}.</p>
     */
    private final TestScopeApplicationContext applicationContext;

    /**
     * <p>Creates new instance of {@link ApplicationContextEvent} with given {@link TestScopeApplicationContext}
     * instance.</p>
     *
     * @param applicationContext the application context
     *
     * @throws IllegalArgumentException if {@code applicationContext} is null
     */
    public ApplicationContextEvent(TestScopeApplicationContext applicationContext) {

        Validate.notNull(applicationContext, "The 'applicationContext' can not be null.");

        this.applicationContext = applicationContext;
    }

    /**
     * <p>Retrieves the application context.</p>
     *
     * @return the application context
     */
    public TestScopeApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
