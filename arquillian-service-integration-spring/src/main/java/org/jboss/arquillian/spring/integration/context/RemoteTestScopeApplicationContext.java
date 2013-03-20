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

package org.jboss.arquillian.spring.integration.context;

import org.jboss.arquillian.test.spi.TestClass;
import org.springframework.context.ApplicationContext;

/**
 * <p>A test scope application context.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class RemoteTestScopeApplicationContext extends TestScopeApplicationContext {

    /**
     * <p>Creates new instance of {@link RemoteTestScopeApplicationContext} class with given application context.</p>
     *
     * @param applicationContext the {@link ApplicationContext}
     * @param testClass          the test class wrapper
     * @param closable           whether the application context should be closed after the tests
     *
     * @throws IllegalArgumentException is {@code applicationContext} is null or {@code testClass}
     */
    public RemoteTestScopeApplicationContext(ApplicationContext applicationContext, TestClass testClass, boolean closable) {

        super(applicationContext, testClass, closable);
    }
}
