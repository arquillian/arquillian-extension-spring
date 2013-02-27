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
package org.jboss.arquillian.warp.extension.spring.interceptor;

import org.jboss.arquillian.warp.extension.spring.container.Commons;
import org.jboss.arquillian.warp.extension.spring.container.SpringMvcResultImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * <p>Tests {@link WarpInterceptor} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class WarpInterceptorTestCase {

    /**
     * <p>Represents the instance of the tested class.</p>
     */
    private WarpInterceptor instance;

    /**
     * <p>Mocked instance of {@link HttpServletRequest}.</p>
     */
    @Mock
    private HttpServletRequest httpServletRequest;

    /**
     * <p>Mocked instance of {@link HttpServletResponse}.</p>
     */
    @Mock
    private HttpServletResponse httpServletResponse;

    /**
     * <p>Dummy object used as handler.</p>
     */
    private Object handler;

    /**
     * <p>Represents the instance of {@link ModelAndView} used for test purpose.</p>
     */
    private ModelAndView modelAndView;

    /**
     * <p>Represents the instance of {@link Exception} used for test purpose.</p>
     */
    private Exception exception;

    /**
     * <p>Represents the instance of {@link SpringMvcResultImpl}.</p>
     */
    private SpringMvcResultImpl springMvcResult;

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        // given
        instance = new WarpInterceptor();
        handler = new Object();
        modelAndView = new ModelAndView();
        exception = new Exception();
        springMvcResult = new SpringMvcResultImpl();

        when(httpServletRequest.getAttribute(Commons.SPRING_MVC_RESULT_ATTRIBUTE_NAME)).thenReturn(springMvcResult);
    }

    /**
     * <p>Tests the {@link WarpInterceptor#preHandle(HttpServletRequest, HttpServletResponse, Object)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testPreHandle() throws Exception {

        // when
        boolean result = instance.preHandle(httpServletRequest, httpServletResponse, handler);

        // then
        assertTrue("The method result was expected to be true.", result);
        assertEquals("The handler hasn't been set.", handler, springMvcResult.getHandler());
    }

    /**
     * <p>Tests the {@link WarpInterceptor#postHandle(HttpServletRequest, HttpServletResponse, Object, ModelAndView)}
     * method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testPostHandle() throws Exception {

        // when
        instance.postHandle(httpServletRequest, httpServletResponse, handler, modelAndView);

        // then
        assertEquals("The handler hasn't been set.", handler, springMvcResult.getHandler());
        assertEquals("The modelAndView hasn't been set.", modelAndView, springMvcResult.getModelAndView());
    }

    /**
     * <p>Tests the {@link WarpInterceptor#afterCompletion(HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * Object, Exception)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testAfterCompletion() throws Exception {

        // when
        instance.afterCompletion(httpServletRequest, httpServletResponse, handler, exception);

        // then
        assertEquals("The handler hasn't been set.", handler, springMvcResult.getHandler());
        assertEquals("The handler hasn't been set.", exception, springMvcResult.getException());
    }
}
