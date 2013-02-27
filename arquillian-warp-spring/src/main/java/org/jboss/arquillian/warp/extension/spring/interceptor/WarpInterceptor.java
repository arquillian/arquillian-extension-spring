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

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.jboss.arquillian.warp.extension.spring.container.SpringMvcResultBuilder.buildMvc;

/**
 * <p>A {@link HandlerInterceptor} that captures the Spring MVC execution state.</p>
 *
 * <p>The interceptor is alternative approach for using
 * {@link org.jboss.arquillian.warp.extension.spring.servlet.WarpDispatcherServlet} whit fallowing limitations:
 * <ol>
 *     <li>Any exception that occurs during controller execution is not accessible, due to the fact that the handler is
 *     omitted in error handling execution chain.</li>
 *     <li>The list of handler {@link org.springframework.web.servlet.HandlerInterceptor} is not accessible.</li>
 * </ol>
 * </p>
 *
 * <p>Beside that the interceptor can be used for capturing the {@link ModelAndView} from the executed controller.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @see org.jboss.arquillian.warp.extension.spring.SpringMvcResult
 * @see org.jboss.arquillian.warp.extension.spring.servlet.WarpDispatcherServlet
 */
public class WarpInterceptor implements HandlerInterceptor {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                             Object handler) throws Exception {

        buildMvc(httpServletRequest).setHandler(handler);

        // proceed with execution chain
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler,
                           ModelAndView modelAndView) throws Exception {

        buildMvc(httpServletRequest)
                .setModelAndView(modelAndView)
                .setHandler(handler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object handler, Exception e) throws Exception {

        buildMvc(httpServletRequest)
                .setException(e)
                .setHandler(handler);
    }
}
