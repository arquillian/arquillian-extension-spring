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
package org.jboss.arquillian.spring.dependency;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.spring.configuration.SpringDeployerConfiguration;
import org.jboss.arquillian.spring.utils.TestReflectionHelper;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * <p>Tests {@link AbstractDependencyResolverProducer} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class AbstractDependencyResolverProducerTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private AbstractDependencyResolverProducer instance;

    /**
     * <p>Tests the {@link AbstractDependencyResolverProducer#initDependencyResolver(BeforeSuite)}  method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testInitDependencyResolverNull() throws Exception {
        BeforeSuite event = new BeforeSuite();

        instance = mock(AbstractDependencyResolverProducer.class);

        Instance<SpringDeployerConfiguration> mockConfigurationInstance = mock(Instance.class);
        TestReflectionHelper.setFieldValue(instance, "configuration", mockConfigurationInstance);

        InstanceProducer<AbstractDependencyResolver> mockProducer = mock(InstanceProducer.class);
        TestReflectionHelper.setFieldValue(instance, "dependencyResolver", mockProducer);

        doCallRealMethod().when(instance).initDependencyResolver(any(BeforeSuite.class));
        when(instance.createDependencyResolver()).thenReturn(null);

        instance.initDependencyResolver(event);

        verifyNoMoreInteractions(mockProducer);
    }

    /**
     * <p>Tests the {@link AbstractDependencyResolverProducer#initDependencyResolver(BeforeSuite)}  method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testInitDependencyResolver() throws Exception {
        BeforeSuite event = new BeforeSuite();

        AbstractDependencyResolver mockDependencyResolver = mock(AbstractDependencyResolver.class);

        instance = mock(AbstractDependencyResolverProducer.class);

        Instance<SpringDeployerConfiguration> mockConfigurationInstance = mock(Instance.class);
        TestReflectionHelper.setFieldValue(instance, "configuration", mockConfigurationInstance);

        InstanceProducer<AbstractDependencyResolver> mockProducer = mock(InstanceProducer.class);
        TestReflectionHelper.setFieldValue(instance, "dependencyResolver", mockProducer);

        doCallRealMethod().when(instance).initDependencyResolver(any(BeforeSuite.class));
        when(instance.createDependencyResolver()).thenReturn(mockDependencyResolver);

        instance.initDependencyResolver(event);

        verify(mockProducer).set(mockDependencyResolver);
    }
}
