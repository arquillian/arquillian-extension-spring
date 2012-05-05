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

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.spring.model.PlainClass;
import org.jboss.arquillian.spring.model.XmlAnnotatedClass;
import org.jboss.arquillian.spring.utils.TestReflectionHelper;
import org.jboss.arquillian.test.spi.event.suite.AfterClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * <p>Tests {@link org.jboss.arquillian.spring.context.ApplicationContextDestroyer} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class ApplicationContextDestroyerTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private ApplicationContextDestroyer instance;

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        instance = new ApplicationContextDestroyer();
    }

    /**
     * <p>Tests {@link ApplicationContextDestroyer#destroyApplicationContext(AfterClass)} method.</p>
     * 
     * @throws Exception if any error occurs
     */
    @Test
    public void testDestroyApplicationContextFalse() throws Exception {

        AfterClass event = new AfterClass(PlainClass.class);

        Instance<TestScopeApplicationContext> mockInstance = mock(Instance.class);
        TestReflectionHelper.setFieldValue(instance, "testApplicationContext", mockInstance);

        ConfigurableApplicationContext mockApplicationContext = mock(ConfigurableApplicationContext.class);
        when(mockInstance.get()).thenReturn(new TestScopeApplicationContext(mockApplicationContext, false));

        instance.destroyApplicationContext(event);

        verifyNoMoreInteractions(mockApplicationContext);
    }

    /**
     * <p>Tests {@link ApplicationContextDestroyer#destroyApplicationContext(AfterClass)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testDestroyApplicationContextTrue() throws Exception {

        AfterClass event = new AfterClass(PlainClass.class);

        Instance<TestScopeApplicationContext> mockInstance = mock(Instance.class);
        TestReflectionHelper.setFieldValue(instance, "testApplicationContext", mockInstance);

        ConfigurableApplicationContext mockApplicationContext = mock(ConfigurableApplicationContext.class);
        when(mockInstance.get()).thenReturn(new TestScopeApplicationContext(mockApplicationContext, true));

        instance.destroyApplicationContext(event);

        verify(mockApplicationContext).close();
    }
}
