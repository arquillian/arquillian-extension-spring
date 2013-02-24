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

package org.jboss.arquillian.spring.integration.inject.client;

import org.jboss.arquillian.spring.integration.configuration.SpringIntegrationConfiguration;
import org.jboss.arquillian.spring.integration.context.ClientTestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.context.TestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.inject.model.ClientXmlAnnotatedClass;
import org.jboss.arquillian.spring.integration.inject.model.ClientXmlAnnotatedClassWithBothCustomAndDefaultLocations;
import org.jboss.arquillian.spring.integration.inject.model.PlainClass;
import org.jboss.arquillian.spring.integration.inject.model.XmlAnnotatedMissingResourceClass;
import org.jboss.arquillian.test.spi.TestClass;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;


/**
 * <p>Tests {@link  XmlClientApplicationContextProducer} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class XmlClientApplicationContextProducerTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private XmlClientApplicationContextProducer instance;

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

        instance = new XmlClientApplicationContextProducer();
    }

    /**
     * <p>Tests the {@link XmlClientApplicationContextProducer#supports(TestClass)} method.</p>
     */
    @Test
    public void testSupportsFalse() {
        TestClass testClass = new TestClass(PlainClass.class);

        assertFalse("Class without annotations shouldn't be supported.", instance.supports(testClass));
    }

    /**
     * <p>Tests the {@link XmlClientApplicationContextProducer#supports(TestClass)} method.</p>
     */
    @Test
    public void testSupportsTrue() {
        TestClass testClass = new TestClass(ClientXmlAnnotatedClass.class);

        assertTrue("Class should be supported.", instance.supports(testClass));
    }

    /**
     * <p>Tests the {@link XmlClientApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
     */
    @Test
    public void testCreateApplicationContextDefault() {
        TestClass testClass = new TestClass(ClientXmlAnnotatedClass.class);

        TestScopeApplicationContext result = instance.createApplicationContext(testClass);

        assertNotNull("The result was null.", result);
        assertTrue("The application context should be marked as closable.", result.isClosable());
        assertNotNull("The application context hasn't been created.", result.getApplicationContext());
    }

    /**
     * <p>Tests the {@link XmlClientApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
     *
     * <p>{@link RuntimeException} is expected.</p>
     */
    @Test(expected = RuntimeException.class)
    public void testCreateApplicationContextMissingResource() {
        TestClass testClass = new TestClass(XmlAnnotatedMissingResourceClass.class);

        instance.createApplicationContext(testClass);
    }

    @Test
    public void customContextShouldBeChosenWhenBothDefaultAndCustomResourcesExist() {
        // given
        TestClass testClass = new TestClass(ClientXmlAnnotatedClassWithBothCustomAndDefaultLocations.class);

        // when

        ClientTestScopeApplicationContext applicationContext = instance.createApplicationContext(testClass);

        // then
        assertThat(applicationContext.getApplicationContext().getBeanDefinitionNames()).isEmpty();
        assertThat(applicationContext.getApplicationContext().getBeanDefinitionCount()).isEqualTo(0);
    }

    @Test
    public void createdApplicationContextCreatedFromDefaultResourceShouldContainSingleBean() {
        // given
        TestClass testClass = new TestClass(ClientXmlAnnotatedClass.class);

        // when
        ClientTestScopeApplicationContext applicationContext = instance.createApplicationContext(testClass);

        // then
        assertThat(applicationContext.getApplicationContext().getBeanDefinitionNames()).isNotEmpty();
        assertThat(applicationContext.getApplicationContext().getBeanDefinitionCount()).isEqualTo(1);
        assertThat(applicationContext.getApplicationContext().getBeanDefinitionNames()[0]).contains(PlainClass.class.getName());
    }
}
