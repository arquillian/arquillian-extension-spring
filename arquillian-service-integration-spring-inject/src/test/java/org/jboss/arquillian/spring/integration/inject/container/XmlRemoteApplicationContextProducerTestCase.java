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

package org.jboss.arquillian.spring.integration.inject.container;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.spring.integration.configuration.SpringIntegrationConfiguration;
import org.jboss.arquillian.spring.integration.context.RemoteTestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.context.TestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.inject.model.*;
import org.jboss.arquillian.spring.integration.inject.test.TestReflectionHelper;
import org.jboss.arquillian.test.spi.TestClass;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <p>Tests {@link XmlRemoteApplicationContextProducer} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class XmlRemoteApplicationContextProducerTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private XmlRemoteApplicationContextProducer instance;

    /**
     * <p>The producer configuration.</p>
     */
    private SpringIntegrationConfiguration remoteConfiguration;

    /**
     * <p>Sets up the test environment.</p>
     *
     * @throws Exception if any error occurs
     */
    @Before
    public void setUp() throws Exception {

        instance = new XmlRemoteApplicationContextProducer();

        remoteConfiguration = new SpringIntegrationConfiguration(Collections.<String, String>emptyMap());

        setRemoteConfiguration();
    }

    /**
     * <p>Tests the {@link XmlRemoteApplicationContextProducer#supports(org.jboss.arquillian.test.spi.TestClass)} method.</p>
     */
    @Test
    public void testSupportsFalse() {
        TestClass testClass = new TestClass(PlainClass.class);

        assertFalse("Class without annotations shouldn't be supported.", instance.supports(testClass));
    }

    /**
     * <p>Tests the {@link XmlRemoteApplicationContextProducer#supports(TestClass)} method.</p>
     */
    @Test
    public void testSupportsTrue() {
        TestClass testClass = new TestClass(XmlAnnotatedClass.class);

        assertTrue("Class should be supported.", instance.supports(testClass));
    }

    /**
     * <p>Tests the {@link XmlRemoteApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
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
     * <p>Tests the {@link XmlRemoteApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testCreateApplicationContextCustomContextConfiguration() throws Exception {
        TestClass testClass = new TestClass(XmlAnnotatedClass.class);

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("customContextClass", "org.springframework.context.support.ClassPathXmlApplicationContext");
        remoteConfiguration = new SpringIntegrationConfiguration(properties);

        setRemoteConfiguration();

        TestScopeApplicationContext result = instance.createApplicationContext(testClass);

        assertNotNull("The result was null.", result);
        assertTrue("The application context should be marked as closable.", result.isClosable());
        assertNotNull("The application context hasn't been created.", result.getApplicationContext());
    }

    /**
     * <p>Tests the {@link XmlRemoteApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
     *
     * <p>{@link RuntimeException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test(expected = RuntimeException.class)
    public void testCreateApplicationContextCustomContextConfigurationError() throws Exception {
        TestClass testClass = new TestClass(XmlAnnotatedClass.class);

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("customContextClass", "invalid class name");
        remoteConfiguration = new SpringIntegrationConfiguration(properties);

        setRemoteConfiguration();

        instance.createApplicationContext(testClass);
    }

    /**
     * <p>Tests the {@link XmlRemoteApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
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
     * <p>Tests the {@link XmlRemoteApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
     *
     * <p>{@link RuntimeException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test(expected = RuntimeException.class)
    public void testCreateApplicationContextCustomContextConfigurationAndAnnotation() throws Exception {
        TestClass testClass = new TestClass(XmlAnnotatedCustomContextClass.class);

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("customContextClass", "invalid class name");
        remoteConfiguration = new SpringIntegrationConfiguration(properties);

        setRemoteConfiguration();

        TestScopeApplicationContext result = instance.createApplicationContext(testClass);

        assertNotNull("The result was null.", result);
        assertTrue("The application context should be marked as closable.", result.isClosable());
        assertNotNull("The application context hasn't been created.", result.getApplicationContext());
    }

    /**
     * <p>Tests the {@link XmlRemoteApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
     *
     * <p>{@link RuntimeException} is expected.</p>
     */
    @Test(expected = RuntimeException.class)
    public void testCreateApplicationContextMissingResource() {
        TestClass testClass = new TestClass(XmlAnnotatedMissingResourceClass.class);

        instance.createApplicationContext(testClass);
    }

    @Test
    public void shouldCreateApplicationContextFromDefaultLocation() {
        // given
        TestClass testClass = new TestClass(XmlAnnotatedClassWithDefaultContextLocationDefined.class);

        // when
        RemoteTestScopeApplicationContext applicationContext = instance.createApplicationContext(testClass);

        // then
        assertNotNull("Created context was null", applicationContext);
        assertThat(applicationContext.getApplicationContext().getBeanDefinitionCount()).isEqualTo(1);
    }

    @Test
    public void shouldUseCustomResourceLocationsFileInsteadOfDefaultLocation() {
        // given
        TestClass testClass = new TestClass(XmlAnnotatedClassWithBothCustomAndDefaultContextLocationsDefined.class);

        // when
        RemoteTestScopeApplicationContext applicationContext = instance.createApplicationContext(testClass);

        // then
        assertNotNull("Created context was null", applicationContext);
        assertThat(applicationContext.getApplicationContext().getBeanDefinitionNames()).isEmpty();
    }

    /**
     * <p>Sets up the remote configuration for the instance of the tested class.</p>
     *
     * @throws Exception if any error occurs
     */
    private void setRemoteConfiguration() throws Exception {
        Instance<SpringIntegrationConfiguration> mockRemoteConfigurationInstance = mock(Instance.class);
        when(mockRemoteConfigurationInstance.get()).thenReturn(remoteConfiguration);
        TestReflectionHelper.setFieldValue(instance, "remoteConfiguration", mockRemoteConfigurationInstance);
    }
}