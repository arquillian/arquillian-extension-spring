/**
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
package org.jboss.arquillian.warp.extension.spring.container;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

/**
 * <p>Tests {@link SpringMvcResultImpl} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringMvcResultImplTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private SpringMvcResultImpl instance;

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        instance = new SpringMvcResultImpl();
    }

    /**
     * <p>Tests {@link SpringMvcResultImpl#SpringMvcResultImpl()} constructor.</p>
     */
    @Test
    public void testCtor() {

        instance = new SpringMvcResultImpl();
    }

    /**
     * <p>Tests both {@link SpringMvcResultImpl#setHandler(Object)} and {@link SpringMvcResultImpl#getHandler()}
     * method.</p>
     */
    @Test
    public void testHandlerProperty() {

        Object handler = new Object();

        assertNull("The property has invalid value.", instance.getHandler());

        instance.setHandler(handler);

        assertEquals("The property value is invalid.", handler, instance.getHandler());
    }

    /**
     * <p>Tests both {@link SpringMvcResultImpl#setInterceptors(HandlerInterceptor[])} and {@link
     * SpringMvcResultImpl#getInterceptors()} ()} method.</p>
     */
    @Test
    public void testInterceptorProperty() {

        HandlerInterceptor[] interceptors = new HandlerInterceptor[0];

        assertNull("The property has invalid value.", instance.getInterceptors());

        instance.setInterceptors(interceptors);

        assertEquals("The property value is invalid.", interceptors, instance.getInterceptors());
    }

    /**
     * <p>Tests both {@link SpringMvcResultImpl#setModelAndView(ModelAndView)} and {@link
     * SpringMvcResultImpl#getModelAndView()} method.</p>
     */
    @Test
    public void testModelAndViewProperty() {

        ModelAndView modelAndView = mock(ModelAndView.class);

        assertNull("The property has invalid value.", instance.getModelAndView());

        instance.setModelAndView(modelAndView);

        assertEquals("The property value is invalid.", modelAndView, instance.getModelAndView());
    }

    /**
     * <p>Tests both {@link SpringMvcResultImpl#setException(Exception)} and {@link SpringMvcResultImpl#getException()}
     * method.</p>
     */
    @Test
    public void testExceptionProperty() {

        Exception exception = new Exception();

        assertNull("The property has invalid value.", instance.getException());

        instance.setException(exception);

        assertEquals("The property value is invalid.", exception, instance.getException());
    }
}
