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

package org.jboss.arquillian.spring.integration.javaconfig.container;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.spring.integration.configuration.SpringIntegrationConfiguration;
import org.jboss.arquillian.spring.integration.context.TestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.javaconfig.model.AnnotatedClassesCustomContextClass;
import org.jboss.arquillian.spring.integration.javaconfig.model.AnnotatedPackagesCustomContextClass;
import org.jboss.arquillian.spring.integration.javaconfig.model.ClassesAnnotatedClass;
import org.jboss.arquillian.spring.integration.javaconfig.model.NoConfigAnnotatedClass;
import org.jboss.arquillian.spring.integration.javaconfig.model.PackagesAnnotatedClass;
import org.jboss.arquillian.spring.integration.javaconfig.model.PlainClass;
import org.jboss.arquillian.spring.integration.javaconfig.utils.TestReflectionHelper;
import org.jboss.arquillian.test.spi.TestClass;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <p>Tests {@link AnnotationRemoteApplicationContextProducer} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class AnnotationRemoteApplicationContextProducerTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private AnnotationRemoteApplicationContextProducer instance;

    /**
     * <p>Represents the producer configuration.</p>
     */
    private SpringIntegrationConfiguration remoteConfiguration;

    /**
     * <p>Sets up the test environment.</p>
     *
     * @throws Exception if any error occurs
     */
    @Before
    public void setUp() throws Exception {

        instance = new AnnotationRemoteApplicationContextProducer();

        remoteConfiguration = new SpringIntegrationConfiguration(Collections.<String, String>emptyMap());

        setUpRemoteConfiguration();
    }

    /**
     * <p>Tests the {@link AnnotationRemoteApplicationContextProducer#supports(TestClass)} method.</p>
     */
    @Test
    public void testSupportsFalse() {
        TestClass testClass = new TestClass(PlainClass.class);

        assertFalse("Class without annotations shouldn't be supported.", instance.supports(testClass));
    }

    /**
     * <p>Tests the {@link AnnotationRemoteApplicationContextProducer#supports(TestClass)} method.</p>
     */
    @Test
    public void testSupportsTrue1() {
        TestClass testClass = new TestClass(ClassesAnnotatedClass.class);

        assertTrue("Class should be supported.", instance.supports(testClass));
    }

    /**
     * <p>Tests the {@link AnnotationRemoteApplicationContextProducer#supports(TestClass)} method.</p>
     */
    @Test
    public void testSupportsTrue2() {
        TestClass testClass = new TestClass(PackagesAnnotatedClass.class);

        assertTrue("Class should be supported.", instance.supports(testClass));
    }

    /**
     * <p>Tests the {@link AnnotationRemoteApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testCreateApplicationContextCustomContextClassesConfiguration() throws Exception {
        TestClass testClass = new TestClass(ClassesAnnotatedClass.class);

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("customAnnotationContextClass",
                "org.springframework.context.annotation.AnnotationConfigApplicationContext");
        remoteConfiguration = new SpringIntegrationConfiguration(properties);

        TestScopeApplicationContext result = instance.createApplicationContext(testClass);

        assertNotNull("The result was null.", result);
        assertTrue("The application context should be marked as closable.", result.isClosable());
        assertNotNull("The application context hasn't been created.", result.getApplicationContext());
    }

    /**
     * <p>Tests the {@link AnnotationRemoteApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
     *
     * <p>{@link RuntimeException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test(expected = RuntimeException.class)
    public void testCreateApplicationContextCustomContextClassesConfigurationError() throws Exception {
        TestClass testClass = new TestClass(ClassesAnnotatedClass.class);

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("customAnnotationContextClass",
                "invalid class name");
        remoteConfiguration = new SpringIntegrationConfiguration(properties);

        setUpRemoteConfiguration();

        instance.createApplicationContext(testClass);
    }

    /**
     * <p>Tests the {@link AnnotationRemoteApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testCreateApplicationContextCustomContextPackagesConfiguration() throws Exception {
        TestClass testClass = new TestClass(PackagesAnnotatedClass.class);

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("customAnnotationContextClass",
                "org.springframework.context.annotation.AnnotationConfigApplicationContext");
        remoteConfiguration = new SpringIntegrationConfiguration(properties);

        setUpRemoteConfiguration();

        TestScopeApplicationContext result = instance.createApplicationContext(testClass);

        assertNotNull("The result was null.", result);
        assertTrue("The application context should be marked as closable.", result.isClosable());
        assertNotNull("The application context hasn't been created.", result.getApplicationContext());
    }

    /**
     * <p>Tests the {@link AnnotationRemoteApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
     *
     * <p>{@link RuntimeException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test(expected = RuntimeException.class)
    public void testCreateApplicationContextCustomContextPackagesConfigurationError() throws Exception {
        TestClass testClass = new TestClass(PackagesAnnotatedClass.class);

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("customAnnotationContextClass",
                "invalid class name");
        remoteConfiguration = new SpringIntegrationConfiguration(properties);

        setUpRemoteConfiguration();

        instance.createApplicationContext(testClass);
    }

    /**
     * <p>Tests the {@link AnnotationRemoteApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
     */
    @Test
    public void testCreateApplicationContextCustomContextClasses() {
        TestClass testClass = new TestClass(AnnotatedClassesCustomContextClass.class);

        TestScopeApplicationContext result = instance.createApplicationContext(testClass);

        assertNotNull("The result was null.", result);
        assertTrue("The application context should be marked as closable.", result.isClosable());
        assertNotNull("The application context hasn't been created.", result.getApplicationContext());
    }

    /**
     * <p>Tests the {@link AnnotationRemoteApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
     *
     * <p>{@link RuntimeException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test(expected = RuntimeException.class)
    public void testCreateApplicationContextCustomContextClassesConfigurationAndAnnotation() throws Exception {
        TestClass testClass = new TestClass(AnnotatedClassesCustomContextClass.class);

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("customAnnotationContextClass",
                "invalid class name");
        remoteConfiguration = new SpringIntegrationConfiguration(properties);

        setUpRemoteConfiguration();

        TestScopeApplicationContext result = instance.createApplicationContext(testClass);

        assertNotNull("The result was null.", result);
        assertTrue("The application context should be marked as closable.", result.isClosable());
        assertNotNull("The application context hasn't been created.", result.getApplicationContext());
    }

    /**
     * <p>Tests the {@link AnnotationRemoteApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
     */
    @Test
    public void testCreateApplicationContextCustomContextPackages() {
        TestClass testClass = new TestClass(AnnotatedPackagesCustomContextClass.class);

        TestScopeApplicationContext result = instance.createApplicationContext(testClass);

        assertNotNull("The result was null.", result);
        assertTrue("The application context should be marked as closable.", result.isClosable());
        assertNotNull("The application context hasn't been created.", result.getApplicationContext());
    }

    /**
     * <p>Tests the {@link AnnotationRemoteApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
     *
     * <p>{@link RuntimeException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test(expected = RuntimeException.class)
    public void testCreateApplicationContextCustomContextPackagesConfigurationAndAnnotation() throws Exception {
        TestClass testClass = new TestClass(AnnotatedPackagesCustomContextClass.class);

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("customAnnotationContextClass",
                "invalid class name");
        remoteConfiguration = new SpringIntegrationConfiguration(properties);

        setUpRemoteConfiguration();

        TestScopeApplicationContext result = instance.createApplicationContext(testClass);

        assertNotNull("The result was null.", result);
        assertTrue("The application context should be marked as closable.", result.isClosable());
        assertNotNull("The application context hasn't been created.", result.getApplicationContext());
    }

    /**
     * <p>Tests the {@link AnnotationRemoteApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
     *
     * <p>{@link RuntimeException} is expected.</p>
     */
    @Test(expected = RuntimeException.class)
    public void testCreateApplicationContextError() {
        TestClass testClass = new TestClass(NoConfigAnnotatedClass.class);

        instance.createApplicationContext(testClass);
    }

    /**
     * <p>Sets up remote configuration for the instance of tested class.</p>
     *
     * @throws Exception if any error occurs
     */
    private void setUpRemoteConfiguration() throws Exception {
        Instance<SpringIntegrationConfiguration> mockRemoteConfigurationInstance = mock(Instance.class);
        when(mockRemoteConfigurationInstance.get()).thenReturn(remoteConfiguration);
        TestReflectionHelper.setFieldValue(instance, "remoteConfiguration", mockRemoteConfigurationInstance);
    }
}
