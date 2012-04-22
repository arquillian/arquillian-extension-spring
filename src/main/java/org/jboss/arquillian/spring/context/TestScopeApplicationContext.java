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

import org.springframework.context.ApplicationContext;

/**
 * <p>Spring test enricher, injects spring beans.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class TestScopeApplicationContext {

    /**
     * <p>Represents instance of {@link ApplicationContext}.</p>
     */
    private ApplicationContext applicationContext;

    /**
     * <p>Represents whether the application context should be closed after the tests.</p>
     */
    private boolean closable;

    /**
     * <p>Creates new instance of {@link TestScopeApplicationContext} class with given application context.</p>
     *
     * @param applicationContext the {@link ApplicationContext}
     * @param closable           whether the application context should be closed after the tests
     *
     * @throws IllegalArgumentException is applicationContext is null
     */
    public TestScopeApplicationContext(ApplicationContext applicationContext, boolean closable) {

        if (applicationContext == null) {
            throw new IllegalArgumentException("The 'applicationContext' must be not null.");
        }

        this.applicationContext = applicationContext;
        this.closable = closable;
    }

    /**
     * <p>Retrieves {@link ApplicationContext} instance.</p>
     *
     * @return the {@link ApplicationContext}
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * <p>Retrieves whether the application context should be closed after the tests.</p>
     *
     * @return whether the application context should be closed after the tests
     */
    public boolean isClosable() {
        return closable;
    }
}
