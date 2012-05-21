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
import org.jboss.arquillian.spring.configuration.SpringExtensionRemoteConfiguration;
import org.jboss.arquillian.spring.model.PlainClass;
import org.jboss.arquillian.spring.utils.TestReflectionHelper;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * <p>Tests {@link AbstractApplicationContextProducer} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class AbstractApplicationContextProducerTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private AbstractApplicationContextProducer instance;

    /**
     * <p>Tests the {@link AbstractApplicationContextProducer#initApplicationContext(BeforeClass)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testInitApplicationContextNotSupported() throws Exception {
        BeforeClass event = new BeforeClass(PlainClass.class);

        instance = mock(AbstractApplicationContextProducer.class);

        SpringExtensionRemoteConfiguration extensionRemoteConfiguration = new SpringExtensionRemoteConfiguration();

        Instance<SpringExtensionRemoteConfiguration> mockExtensionRemoteConfigurationInstance = mock(Instance.class);
        when(mockExtensionRemoteConfigurationInstance.get()).thenReturn(extensionRemoteConfiguration);
        TestReflectionHelper.setFieldValue(instance, "remoteConfiguration", mockExtensionRemoteConfigurationInstance);

        InstanceProducer<TestScopeApplicationContext> mockProducer = mock(InstanceProducer.class);
        TestReflectionHelper.setFieldValue(instance, "testApplicationContext", mockProducer);

        doCallRealMethod().when(instance).initApplicationContext(any(BeforeClass.class));
        when(instance.supports(any(TestClass.class))).thenReturn(false);

        instance.initApplicationContext(event);

        verifyNoMoreInteractions(mockProducer);
    }

    /**
     * <p>Tests the {@link AbstractApplicationContextProducer#initApplicationContext(BeforeClass)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testInitApplicationContextSupported() throws Exception {
        BeforeClass event = new BeforeClass(PlainClass.class);

        ApplicationContext mockApplicationContext = mock(ApplicationContext.class);
        TestScopeApplicationContext testScopeApplicationContext =
                new TestScopeApplicationContext(mockApplicationContext, false);

        instance = mock(AbstractApplicationContextProducer.class);

        SpringExtensionRemoteConfiguration extensionRemoteConfiguration = new SpringExtensionRemoteConfiguration();

        Instance<SpringExtensionRemoteConfiguration> mockExtensionRemoteConfigurationInstance = mock(Instance.class);
        when(mockExtensionRemoteConfigurationInstance.get()).thenReturn(extensionRemoteConfiguration);
        TestReflectionHelper.setFieldValue(instance, "remoteConfiguration", mockExtensionRemoteConfigurationInstance);

        doCallRealMethod().when(instance).initApplicationContext(any(BeforeClass.class));
        when(instance.supports(any(TestClass.class))).thenReturn(true);
        when(instance.createApplicationContext(any(TestClass.class))).thenReturn(testScopeApplicationContext);

        InstanceProducer<TestScopeApplicationContext> mockProducer = mock(InstanceProducer.class);
        TestReflectionHelper.setFieldValue(instance, "testApplicationContext", mockProducer);

        instance.initApplicationContext(event);

        verify(mockProducer).set(testScopeApplicationContext);
    }
}
