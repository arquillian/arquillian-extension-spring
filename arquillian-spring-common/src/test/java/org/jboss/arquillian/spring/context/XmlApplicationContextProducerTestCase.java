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

import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.spring.model.PlainClass;
import org.jboss.arquillian.spring.model.XmlAnnotatedClass;
import org.jboss.arquillian.spring.model.XmlAnnotatedCustomContextClass;
import org.jboss.arquillian.spring.model.XmlAnnotatedMissingResourceClass;
import org.jboss.arquillian.spring.utils.TestReflectionHelper;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * <p>Tests {@link XmlApplicationContextProducer} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class XmlApplicationContextProducerTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private XmlApplicationContextProducer instance;

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        instance = new XmlApplicationContextProducer();
    }

    /**
     * <p>Tests the {@link XmlApplicationContextProducer#supports(TestClass)} method.</p>
     */
    @Test
    public void testSupportsFalse() {
        TestClass testClass = new TestClass(PlainClass.class);

        assertFalse("Class without annotations shouldn't be supported.", instance.supports(testClass));
    }

    /**
     * <p>Tests the {@link XmlApplicationContextProducer#supports(TestClass)} method.</p>
     */
    @Test
    public void testSupportsTrue() {
        TestClass testClass = new TestClass(XmlAnnotatedClass.class);

        assertTrue("Class should be supported.", instance.supports(testClass));
    }

    /**
     * <p>Tests the {@link XmlApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
     */
    @Test
    public void testCreateApplicationContextDefault() {
        TestClass testClass = new TestClass(XmlAnnotatedClass.class);

        TestScopeApplicationContext result = instance.createApplicationContext(testClass);

        assertNotNull("The result was null.", result);
        assertTrue("The application context should be marked as closable.", result.isClosable());
        assertNotNull("The application context hasn't been created.", result.getApplicationContext());
    }

    /**
     * <p>Tests the {@link XmlApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
     */
    @Test
    public void testCreateApplicationContextCustomContext() {
        TestClass testClass = new TestClass(XmlAnnotatedCustomContextClass.class);

        TestScopeApplicationContext result = instance.createApplicationContext(testClass);

        assertNotNull("The result was null.", result);
        assertTrue("The application context should be marked as closable.", result.isClosable());
        assertNotNull("The application context hasn't been created.", result.getApplicationContext());
    }

    /**
     * <p>Tests the {@link XmlApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
     *
     * <p>{@link RuntimeException} is expected.</p>
     */
    @Test(expected = RuntimeException.class)
    public void testCreateApplicationContextMissingResource() {
        TestClass testClass = new TestClass(XmlAnnotatedMissingResourceClass.class);

        TestScopeApplicationContext result = instance.createApplicationContext(testClass);
    }

    /**
     * <p>Tests the {@link XmlApplicationContextProducer#initApplicationContext(BeforeClass)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testInitApplicationContextNotSupported() throws Exception {
        BeforeClass event = new BeforeClass(PlainClass.class);

        InstanceProducer<TestScopeApplicationContext> mockProducer = mock(InstanceProducer.class);
        TestReflectionHelper.setFieldValue(instance, "testApplicationContext", mockProducer);

        instance.initApplicationContext(event);

        verifyNoMoreInteractions(mockProducer);
    }

    /**
     * <p>Tests the {@link XmlApplicationContextProducer#initApplicationContext(BeforeClass)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testInitApplicationContextDefault() throws Exception {
        BeforeClass event = new BeforeClass(XmlAnnotatedClass.class);

        InstanceProducer<TestScopeApplicationContext> mockProducer = mock(InstanceProducer.class);
        TestReflectionHelper.setFieldValue(instance, "testApplicationContext", mockProducer);

        instance.initApplicationContext(event);

        ArgumentCaptor<TestScopeApplicationContext> argument =
                ArgumentCaptor.forClass(TestScopeApplicationContext.class);
        verify(mockProducer).set(argument.capture());

        assertNotNull("The result was null.", argument.getValue());
        assertTrue("The application context should be marked as closable.", argument.getValue().isClosable());
        assertNotNull("The application context hasn't been created.", argument.getValue().getApplicationContext());
    }

    /**
     * <p>Tests the {@link XmlApplicationContextProducer#initApplicationContext(BeforeClass)} method.</p>
     *
     * <p>{@link RuntimeException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test(expected = RuntimeException.class)
    public void testInitApplicationContextMissingResource() throws Exception {
        BeforeClass event = new BeforeClass(XmlAnnotatedMissingResourceClass.class);

        InstanceProducer<TestScopeApplicationContext> mockProducer = mock(InstanceProducer.class);
        TestReflectionHelper.setFieldValue(instance, "testApplicationContext", mockProducer);

        instance.initApplicationContext(event);
    }
}


