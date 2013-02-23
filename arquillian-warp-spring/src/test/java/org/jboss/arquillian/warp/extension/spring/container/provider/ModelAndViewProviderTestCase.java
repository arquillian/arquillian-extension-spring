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
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * <p>Tests {@link ModelAndViewProvider} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ModelAndViewProviderTestCase extends ProviderBaseTestCase {

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

        initProvider(new ModelAndViewProvider());
    }

    /**
     * <p>Tests the {@link ModelAndViewProvider#canProvide(Class)} method.</p>
     */
    @Test
    public void testCanProvide() {

        assertTrue("The provider class does not support required type.", instance.canProvide(ModelAndView.class));
    }

    /**
     * <p>Tests the {@link ModelAndViewProvider#canProvide(Class)} method when the supported class is not
     * supported.</p>
     */
    @Test
    public void testCanProvideFalse() {

        assertFalse("The provider class supports incorrect type.", instance.canProvide(SpringMvcResult.class));
    }

    /**
     * <p>Tests the {@link ModelAndViewProvider#canProvide(Class)} method when the provided resource is null.</p>
     */
    @Test
    public void testCanProvideResourceNull() {

        springMvcResult.setModelAndView(null);

        assertFalse("The provider incorrectly handles null values.", instance.canProvide(ModelAndView.class));
    }

    /**
     * <p>Tests {@link ModelAndViewProvider #lookup(ArquillianResource, java.lang.annotation.Annotation...)}
     * method.</p>
     */
    @Test
    public void testLookup() {

        assertNotNull("The returned resource instance was null.", instance.lookup(arquillianResource));
    }
}
