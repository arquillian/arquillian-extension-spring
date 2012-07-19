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

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.classloader.ShrinkWrapClassLoader;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * <p>Tests {@link ContextClassLoaderManager} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class ContextClassLoaderManagerTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private ContextClassLoaderManager instance;

    /**
     * <p>Instance of {@link ShrinkWrapClassLoader} used for testing.</p>
     */
    private ShrinkWrapClassLoader shrinkWrapClassLoader;

    /**
     * <p>Represents the thread class loader.</p>
     */
    private ClassLoader threadClassLoader;

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        threadClassLoader = Thread.currentThread().getContextClassLoader();

        shrinkWrapClassLoader = new ShrinkWrapClassLoader(ShrinkWrap.create(JavaArchive.class));
        instance = new ContextClassLoaderManager(shrinkWrapClassLoader);
    }

    /**
     * <p>Tears down the test environment.</p>
     */
    @After
    public void tearDown() {

        Thread.currentThread().setContextClassLoader(threadClassLoader);
    }

    /**
     * <p>Tests {@link ContextClassLoaderManager#ContextClassLoaderManager(ShrinkWrapClassLoader)} constructor.</p>
     */
    @Test
    public void testCtor() {

        instance = new ContextClassLoaderManager(shrinkWrapClassLoader);
    }

    /**
     * <p>Tests {@link ContextClassLoaderManager#ContextClassLoaderManager(ShrinkWrapClassLoader)} constructor when
     * shrinkWrapClassLoader is null.</p>
     *
     * <p>{@link IllegalArgumentException} is expected.</p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtorNull() {

        new ContextClassLoaderManager(null);
    }

    /**
     * <p>Tests the {@link ContextClassLoaderManager#enable()} method.</p>
     */
    @Test
    public void testEnable() {

        instance.enable();

        assertEquals("The class loader hasn't been set.", shrinkWrapClassLoader,
                Thread.currentThread().getContextClassLoader());
    }

    /**
     * <p>Tests the {@link ContextClassLoaderManager#enable()} method.</p>
     */
    @Test
    public void testDisable() {

        instance.enable();

        instance.disable();

        assertEquals("The class loader hasn't been restored.", threadClassLoader,
                Thread.currentThread().getContextClassLoader());
    }
}
