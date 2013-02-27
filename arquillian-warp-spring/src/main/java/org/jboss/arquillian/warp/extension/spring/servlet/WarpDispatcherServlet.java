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
package org.jboss.arquillian.warp.extension.spring.servlet;

import org.jboss.arquillian.warp.extension.spring.SpringMvcResult;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.jboss.arquillian.warp.extension.spring.container.SpringMvcResultBuilder.buildMvc;

/**
 * <p>A implementation of {@link DispatcherServlet} that captures it internal state during request processing.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @see SpringMvcResult
 * @see org.jboss.arquillian.warp.extension.spring.interceptor.WarpInterceptor
 */
public class WarpDispatcherServlet extends DispatcherServlet {

    /**
     * <p>Serial version uid</p.>
     */
    private static final long serialVersionUID = 1L;

    /**
     * <p>Creates new instance of {@link WarpDispatcherServlet} class.</p>
     */
    public WarpDispatcherServlet() {
        // empty constructor
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {

        buildMvc(request);

        super.doService(request, response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected HandlerExecutionChain getHandler(HttpServletRequest request, boolean cache) throws Exception {
        HandlerExecutionChain handlerExecutionChain = super.getHandler(request, cache);

        if (handlerExecutionChain != null) {

            buildMvc(request)
                    .setHandler(handlerExecutionChain.getHandler())
                    .setInterceptors(handlerExecutionChain.getInterceptors());
        }

        return handlerExecutionChain;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {

        buildMvc(request).setModelAndView(mv);

        super.render(mv, request, response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ModelAndView processHandlerException(HttpServletRequest request, HttpServletResponse response,
                                                   Object handler, Exception ex) throws Exception {

        ModelAndView mv = super.processHandlerException(request, response, handler, ex);

        buildMvc(request)
                .setModelAndView(mv)
                .setException(ex);

        return mv;
    }
}
