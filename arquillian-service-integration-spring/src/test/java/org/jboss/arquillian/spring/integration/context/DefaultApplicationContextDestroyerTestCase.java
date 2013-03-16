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

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.spring.integration.model.PlainClass;
import org.jboss.arquillian.spring.integration.model.StrategyTest;
import org.jboss.arquillian.spring.integration.utils.TestReflectionHelper;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.spi.event.suite.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * <p>Tests {@link ApplicationContextDestroyer} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class DefaultApplicationContextDestroyerTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private ApplicationContextDestroyer instance;

    /**
     * <p>Represents the instance of {@link TestClass}.</p>
     */
    private TestClass testClass;

    // TODO implement tests
    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        testClass = new TestClass(Object.class);

        instance = new DefaultApplicationContextDestroyer();
    }
    /**
     * <p>Tests {@link ApplicationContextDestroyer#destroyApplicationContext(TestScopeApplicationContext)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testDestroyApplicationContextFalse() throws Exception {

        ConfigurableApplicationContext mockApplicationContext = mock(ConfigurableApplicationContext.class);

        instance.destroyApplicationContext(new TestScopeApplicationContext(mockApplicationContext, testClass, false));

        verifyNoMoreInteractions(mockApplicationContext);
    }

    /**
     * <p>Tests {@link ApplicationContextDestroyer#destroyApplicationContext(TestScopeApplicationContext)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testDestroyApplicationContextTrue() throws Exception {

        ConfigurableApplicationContext mockApplicationContext = mock(ConfigurableApplicationContext.class);

        instance.destroyApplicationContext(new TestScopeApplicationContext(mockApplicationContext, testClass, true));

        verify(mockApplicationContext).close();
    }
}