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
package org.jboss.arquillian.spring.client;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.spring.configuration.SpringExtensionConfiguration;
import org.jboss.arquillian.spring.utils.TestReflectionHelper;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>Tests {@link AbstractSpringEnricherArchiveAppender} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class AbstractSpringEnricherArchiveAppenderTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private AbstractSpringEnricherArchiveAppender instance;

    /**
     * <p>Tests the {@link AbstractSpringEnricherArchiveAppender#buildArchive()} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testInitApplicationContextNotSupported() throws Exception {

        instance = mock(AbstractSpringEnricherArchiveAppender.class);

        SpringExtensionConfiguration extensionConfiguration = new SpringExtensionConfiguration();

        Instance<SpringExtensionConfiguration> mockExtensionConfigurationInstance = mock(Instance.class);
        when(mockExtensionConfigurationInstance.get()).thenReturn(extensionConfiguration);
        TestReflectionHelper.setFieldValue(instance, "configuration", mockExtensionConfigurationInstance);

        doCallRealMethod().when(instance).buildArchive();
        doCallRealMethod().when(instance).createArchive();
        doCallRealMethod().when(instance).appendProperties(any(JavaArchive.class));

        Archive result = instance.buildArchive();

        verify(instance).appendResources(any(JavaArchive.class));
        assertNotNull("The result was null.", result);
    }
}
