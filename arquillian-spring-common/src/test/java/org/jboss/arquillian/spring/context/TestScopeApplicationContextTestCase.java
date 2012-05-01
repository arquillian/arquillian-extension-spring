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

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * <p>Tests {@link org.jboss.arquillian.spring.context.TestScopeApplicationContext} class.</p>
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
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        applicationContext = mock(ApplicationContext.class);
    }

    /**
     * <p>Tests {@link TestScopeApplicationContext#TestScopeApplicationContext(ApplicationContext, boolean)}
     * constructor.</p>
     */
    @Test
    public void testCtor() {

        instance = new TestScopeApplicationContext(applicationContext, false);
    }

    /**
     * <p>Tests {@link org.jboss.arquillian.spring.context.TestScopeApplicationContext#
     * TestScopeApplicationContext(org.springframework.context.ApplicationContext, boolean)} constructor when
     * applicationContext is null.</p>
     *
     * <p>{@link IllegalArgumentException} is expected.</p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtorNull() {

        instance = new TestScopeApplicationContext(null, false);
    }

    /**
     * <p>Tests the {@link org.jboss.arquillian.spring.context.TestScopeApplicationContext#getApplicationContext()}.</p>
     */
    @Test
    public void testGetApplicationContext() {

        instance = new TestScopeApplicationContext(applicationContext, false);
    }

    /**
     * <p>Tests the {@link org.jboss.arquillian.spring.context.TestScopeApplicationContext#isClosable()}.</p>
     */
    @Test
    public void testGetClosable() {

        instance = new TestScopeApplicationContext(applicationContext, false);
        assertFalse("Closable property has invalid value.", instance.isClosable());

        instance = new TestScopeApplicationContext(applicationContext, true);
        assertTrue("Closable property has invalid value.", instance.isClosable());
    }
}
