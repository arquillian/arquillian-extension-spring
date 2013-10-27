/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.arquillian.spring.persistence.client;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.persistence.spi.datasource.DataSourceProvider;
import org.jboss.arquillian.spring.persistence.datasource.ApplicationContextDataSourceProvider;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>Tests {@link SpringPersistenceExtension} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringPersistenceExtensionTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private SpringPersistenceExtension instance;

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        instance = new SpringPersistenceExtension();
    }

    /**
     * <p>Tests the {@link SpringPersistenceExtension#register(LoadableExtension.ExtensionBuilder)} method.</p>
     */
    @Test
    public void testRegister() {

        LoadableExtension.ExtensionBuilder mockExtensionBuilder = mock(LoadableExtension.ExtensionBuilder.class);

        instance.register(mockExtensionBuilder);

        verify(mockExtensionBuilder).service(AuxiliaryArchiveAppender.class, SpringPersistenceArchiveAppender.class);
        verify(mockExtensionBuilder).service(DataSourceProvider.class, ApplicationContextDataSourceProvider.class);
        verifyNoMoreInteractions(mockExtensionBuilder);
    }
}
