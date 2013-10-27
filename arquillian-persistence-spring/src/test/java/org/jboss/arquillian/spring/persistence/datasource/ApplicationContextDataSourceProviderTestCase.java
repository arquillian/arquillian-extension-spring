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
package org.jboss.arquillian.spring.persistence.datasource;

import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.spring.integration.context.RemoteTestScopeApplicationContext;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.test.AbstractTestTestBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * <p>Tests {@link ApplicationContextDataSourceProvider} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ApplicationContextDataSourceProviderTestCase extends AbstractTestTestBase {

    /**
     * Represents the data source name.
     */
    private static final String DATA_SOURCE_NAME = "dataSource";

    /**
     * The mocked {@link DataSource} instance.
     */
    @Mock
    private DataSource dataSource;

    /**
     * The mocked {@link ApplicationContext} instance.
     */
    @Mock
    private ApplicationContext applicationContext;

    /**
     * The instance of the tested class.
     */
    private ApplicationContextDataSourceProvider applicationContextDataSourceProvider;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addExtensions(List<Class<?>> extensions) {
        extensions.add(ApplicationContextDataSourceProvider.class);
    }

    /**
     * Sets up the test environment.
     *
     * @throws Exception if any error occurs
     */
    @Before
    public void setUp() throws Exception {

        // given
        applicationContextDataSourceProvider = new ApplicationContextDataSourceProvider();

        bind(ApplicationScoped.class, RemoteTestScopeApplicationContext.class,
                new RemoteTestScopeApplicationContext(applicationContext, new TestClass(this.getClass()), false));

        getManager().inject(applicationContextDataSourceProvider);
    }

    /**
     * Tests the {@link ApplicationContextDataSourceProvider#lookupDataSource(String)} method.
     */
    @Test
    public void shouldLookupDataSource() {

        // given
        when(applicationContext.getBean(eq(DATA_SOURCE_NAME), eq(DataSource.class))).thenReturn(dataSource);

        // when
        DataSource result = applicationContextDataSourceProvider.lookupDataSource(DATA_SOURCE_NAME);

        // then
        assertNotNull("The returned data source was null.", result);
        assertEquals("The returned data source was incorrect.", result, dataSource);
    }

    /**
     * Tests the {@link ApplicationContextDataSourceProvider#lookupDataSource(String)} method.
     */
    @Test
    public void shouldLookupNullDataSource() {

        // given
        when(applicationContext.getBean(any(String.class), any(Class.class))).thenReturn(null);

        // when
        DataSource result = applicationContextDataSourceProvider.lookupDataSource("not existing data source");

        // then
        assertNull("The returned data source was null.", result);
    }
}
