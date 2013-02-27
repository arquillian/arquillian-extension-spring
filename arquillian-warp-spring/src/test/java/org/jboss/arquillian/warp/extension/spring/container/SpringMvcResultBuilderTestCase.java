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
package org.jboss.arquillian.warp.extension.spring.container;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>Tests {@link SpringMvcResultBuilder} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class SpringMvcResultBuilderTestCase {

    /**
     * <p>Represents the instance of the tested class.</p>
     */
    private SpringMvcResultBuilder instance;

    /**
     * <p>Represents the instance of {@link SpringMvcResultImpl} used for testing.</p>
     */
    private SpringMvcResultImpl springMvcResult;

    /**
     * <p>Represents the mocked {@link ServletRequest} used for testing.</p>
     */
    @Mock
    private ServletRequest servletRequest;

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        // given
        springMvcResult = new SpringMvcResultImpl();

        when(servletRequest.getAttribute(Commons.SPRING_MVC_RESULT_ATTRIBUTE_NAME)).thenReturn(springMvcResult);
    }

    /**
     * <p>Tests the {@link SpringMvcResultBuilder#buildMvc(javax.servlet.ServletRequest)} method when the {@link
     * SpringMvcResultImpl} does not yet exists.</p>
     */
    @Test
    public void testBuildMvc() {

        // given
        when(servletRequest.getAttribute(Commons.SPRING_MVC_RESULT_ATTRIBUTE_NAME)).thenReturn(null);

        // when
        instance = SpringMvcResultBuilder.buildMvc(servletRequest);

        // then
        verify(servletRequest).setAttribute(eq(Commons.SPRING_MVC_RESULT_ATTRIBUTE_NAME), any(SpringMvcResultImpl.class));
    }

    /**
     * <p>Tests the {@link SpringMvcResultBuilder#setModelAndView(ModelAndView)} method.</p>
     */
    @Test
    public void testSetModelAndView() {

        // given
        ModelAndView modelAndView = new ModelAndView();

        when(servletRequest.getAttribute(Commons.SPRING_MVC_RESULT_ATTRIBUTE_NAME)).thenReturn(springMvcResult);

        // when
        instance = SpringMvcResultBuilder.buildMvc(servletRequest).setModelAndView(modelAndView);

        // then
        assertNotNull("The returned builder instance was null.", instance);
        assertEquals("The property has not been set.", modelAndView, springMvcResult.getModelAndView());
    }

    /**
     * <p>Tests the {@link SpringMvcResultBuilder#setHandler(Object)} method.</p>
     */
    @Test
    public void testSetHandler() {

        // given
        Object handler = new Object();

        // when
        instance = SpringMvcResultBuilder.buildMvc(servletRequest).setHandler(handler);

        // then
        assertNotNull("The returned builder instance was null.", instance);
        assertEquals("The property has not been set.", handler, springMvcResult.getHandler());
    }

    /**
     * <p>Tests the {@link SpringMvcResultBuilder#setInterceptors(HandlerInterceptor[])} method.</p>
     */
    @Test
    public void testSetHandlerInterceptors() {

        // given
        HandlerInterceptor[] interceptors = new HandlerInterceptor[0];

        // when
        instance = SpringMvcResultBuilder.buildMvc(servletRequest).setInterceptors(interceptors);

        // then
        assertNotNull("The returned builder instance was null.", instance);
        assertArrayEquals("The property has not been set.", interceptors, springMvcResult.getInterceptors());
    }

    /**
     * <p>Tests the {@link SpringMvcResultBuilder#setException(Exception)} method.</p>
     */
    @Test
    public void testSetException() {

        // given
        Exception exc = new Exception();

        // when
        instance = SpringMvcResultBuilder.buildMvc(servletRequest).setException(exc);

        // then
        assertNotNull("The returned builder instance was null.", instance);
        assertEquals("The property has not been set.", exc, springMvcResult.getException());
    }
}
