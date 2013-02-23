/**
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
package org.jboss.arquillian.warp.extension.spring.container.provider;

import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.extension.spring.SpringMvcResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * <p>Tests {@link ExceptionProvider} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExceptionProviderTestCase extends ProviderBaseTestCase {

    /**
     * <p>Represents the {@link ArquillianResource} used for testing.</p>
     */
    @Mock
    private ArquillianResource arquillianResource;

    /**
     * <p>Sets up the test environment.</p>
     *
     * @throws Exception if any error occurs
     */
    @Before
    public void setUp() throws Exception {

        initProvider(new ExceptionProvider());
    }

    /**
     * <p>Tests the {@link ExceptionProvider#canProvide(Class)} method.</p>
     */
    @Test
    public void testCanProvide() {

        assertTrue("The provider class does not support required type.", instance.canProvide(Exception.class));
    }

    /**
     * <p>Tests the {@link ExceptionProvider#canProvide(Class)} method when the resource class is an {@link Exception},
     * but can not be cast to required type.</p>
     */
    @Test
    public void testCanProvideFalseForNonAssignableResource() {

        springMvcResult.setException(new RuntimeException());

        assertFalse("The provider class incorrectly handles inheritance.", instance.canProvide(IOException.class));
    }

    /**
     * <p>Tests the {@link ExceptionProvider#canProvide(Class)} method when the supported class is not supported.</p>
     */
    @Test
    public void testCanProvideFalse() {

        assertFalse("The provider class supports incorrect type.", instance.canProvide(SpringMvcResult.class));
    }

    /**
     * <p>Tests the {@link ExceptionProvider#canProvide(Class)} method when the provided resource is null.</p>
     */
    @Test
    public void testCanProvideResourceNull() {

        springMvcResult.setException(null);

        assertFalse("The provider incorrectly handles null values.", instance.canProvide(Exception.class));
    }

    /**
     * <p>Tests {@link ExceptionProvider#lookup(ArquillianResource, java.lang.annotation.Annotation...)} method.</p>
     */
    @Test
    public void testLookup() {

        assertNotNull("The returned resource instance was null.", instance.lookup(arquillianResource));
    }
}
