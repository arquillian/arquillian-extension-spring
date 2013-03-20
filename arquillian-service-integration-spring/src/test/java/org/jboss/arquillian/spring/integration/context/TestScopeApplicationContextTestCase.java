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
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * <p>Tests {@link TestScopeApplicationContext} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class TestScopeApplicationContextTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private TestScopeApplicationContext instance;

    /**
     * <p>Represents the instance of {@link ApplicationContext}.</p>
     */
    private ApplicationContext applicationContext;

    /**
     * <p>Represents the instance of {@link org.jboss.arquillian.test.spi.TestClass}.</p>
     */
    private TestClass testClass;

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        testClass = new TestClass(Object.class);

        applicationContext = mock(ApplicationContext.class);
    }

    /**
     * <p>Tests {@link TestScopeApplicationContext#TestScopeApplicationContext(ApplicationContext, TestClass, boolean)}
     * constructor.</p>
     */
    @Test
    public void testCtor() {

        instance = new TestScopeApplicationContext(applicationContext, testClass, false);
    }

    /**
     * <p>Tests {@link TestScopeApplicationContext#TestScopeApplicationContext(ApplicationContext, TestClass, boolean)}
     * constructor when applicationContext is null.</p>
     *
     * <p>{@link IllegalArgumentException} is expected.</p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtorNull() {

        instance = new TestScopeApplicationContext(null, testClass, false);
    }

    /**
     * <p>Tests {@link TestScopeApplicationContext#TestScopeApplicationContext(ApplicationContext, TestClass, boolean)}
     * constructor when testClass is null.</p>
     *
     * <p>{@link IllegalArgumentException} is expected.</p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtorTestClassNull() {

        instance = new TestScopeApplicationContext(applicationContext, null, false);
    }

    /**
     * <p>Tests the {@link TestScopeApplicationContext#getApplicationContext()}.</p>
     */
    @Test
    public void testGetApplicationContext() {

        instance = new TestScopeApplicationContext(applicationContext, testClass, false);

        assertNotNull("The applicationContext was null.", instance.getApplicationContext());
    }

    /**
     * <p>Tests the {@link TestScopeApplicationContext#isClosable()}.</p>
     */
    @Test
    public void testGetClosable() {

        instance = new TestScopeApplicationContext(applicationContext, testClass, false);
        assertFalse("Closable property has invalid value.", instance.isClosable());

        instance = new TestScopeApplicationContext(applicationContext, testClass, true);
        assertTrue("Closable property has invalid value.", instance.isClosable());
    }
}
