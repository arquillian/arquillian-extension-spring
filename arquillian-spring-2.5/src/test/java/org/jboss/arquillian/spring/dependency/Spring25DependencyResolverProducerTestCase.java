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
import org.jboss.arquillian.spring.configuration.SpringExtensionConfiguration;
import org.jboss.arquillian.spring.context.TestScopeApplicationContext;
import org.jboss.arquillian.spring.utils.TestReflectionHelper;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * <p>Tests {@link Spring25DependencyResolverProducer} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class Spring25DependencyResolverProducerTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private Spring25DependencyResolverProducer instance;

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        instance = new Spring25DependencyResolverProducer();
    }

    /**
     * <p>Tests the {@link Spring25DependencyResolverProducer#initDependencyResolver(BeforeSuite)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testInitDependencyResolver() throws Exception {
        BeforeSuite event = new BeforeSuite();

        Instance<SpringExtensionConfiguration> mockConfigurationInstance = mock(Instance.class);
        TestReflectionHelper.setFieldValue(instance, "configuration", mockConfigurationInstance);

        InstanceProducer<AbstractDependencyResolver> mockProducer = mock(InstanceProducer.class);
        TestReflectionHelper.setFieldValue(instance, "dependencyResolver", mockProducer);

        instance.initDependencyResolver(event);

        ArgumentCaptor<AbstractDependencyResolver> dependencyResolver =
                ArgumentCaptor.forClass(AbstractDependencyResolver.class);
        verify(mockProducer).set(dependencyResolver.capture());
        
        assertNotNull("The crated dependency resolver was null.", dependencyResolver.getValue());
        assertTrue("The producer created incorrect type.",
                dependencyResolver.getValue() instanceof Spring25DependencyResolver);
    }

}
