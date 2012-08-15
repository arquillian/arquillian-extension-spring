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

package org.jboss.arquillian.spring.integration.javaconfig.client;

import org.jboss.arquillian.spring.integration.context.TestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.javaconfig.model.ClientClassesAnnotatedClass;
import org.jboss.arquillian.spring.integration.javaconfig.model.ClientPackagesAnnotatedClass;
import org.jboss.arquillian.spring.integration.javaconfig.model.NoConfigAnnotatedClass;
import org.jboss.arquillian.spring.integration.javaconfig.model.PlainClass;
import org.jboss.arquillian.test.spi.TestClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * <p>Tests {@link  AnnotationClientApplicationContextProducer} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class AnnotationClientApplicationContextProducerTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private AnnotationClientApplicationContextProducer instance;

    /**
     * <p>Sets up the test environment.</p>
     *
     * @throws Exception if any error occurs
     */
    @Before
    public void setUp() throws Exception {

        instance = new AnnotationClientApplicationContextProducer();
    }

    /**
     * <p>Tests the {@link AnnotationClientApplicationContextProducer#supports(TestClass)} method.</p>
     */
    @Test
    public void testSupportsFalse() {
        TestClass testClass = new TestClass(PlainClass.class);

        assertFalse("Class without annotations shouldn't be supported.", instance.supports(testClass));
    }

    /**
     * <p>Tests the {@link AnnotationClientApplicationContextProducer#supports(TestClass)} method.</p>
     */
    @Test
    public void testSupportsTrue1() {
        TestClass testClass = new TestClass(ClientClassesAnnotatedClass.class);

        assertTrue("Class should be supported.", instance.supports(testClass));
    }

    /**
     * <p>Tests the {@link AnnotationClientApplicationContextProducer#supports(TestClass)} method.</p>
     */
    @Test
    public void testSupportsTrue2() {
        TestClass testClass = new TestClass(ClientPackagesAnnotatedClass.class);

        assertTrue("Class should be supported.", instance.supports(testClass));
    }

    /**
     * <p>Tests the {@link AnnotationClientApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testCreateApplicationContextClasses() throws Exception {
        TestClass testClass = new TestClass(ClientClassesAnnotatedClass.class);

        TestScopeApplicationContext result = instance.createApplicationContext(testClass);

        assertNotNull("The result was null.", result);
        assertTrue("The application context should be marked as closable.", result.isClosable());
        assertNotNull("The application context hasn't been created.", result.getApplicationContext());
    }

    /**
     * <p>Tests the {@link AnnotationClientApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testCreateApplicationContextPackages() throws Exception {
        TestClass testClass = new TestClass(ClientClassesAnnotatedClass.class);

        TestScopeApplicationContext result = instance.createApplicationContext(testClass);

        assertNotNull("The result was null.", result);
        assertTrue("The application context should be marked as closable.", result.isClosable());
        assertNotNull("The application context hasn't been created.", result.getApplicationContext());
    }

    /**
     * <p>Tests the {@link AnnotationClientApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
     *
     * <p>{@link RuntimeException} is expected.</p>
     */
    @Test(expected = RuntimeException.class)
    public void testCreateApplicationContextError() {
        TestClass testClass = new TestClass(NoConfigAnnotatedClass.class);

        instance.createApplicationContext(testClass);
    }
}
