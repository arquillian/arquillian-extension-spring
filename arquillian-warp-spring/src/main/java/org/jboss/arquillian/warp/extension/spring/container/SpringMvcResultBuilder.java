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

import org.jboss.arquillian.warp.extension.spring.SpringMvcResult;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;

/**
 * <p>A {@link SpringMvcResult} builder.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public final class SpringMvcResultBuilder {

    /**
     * <p>Represents the request associated with this builder.</p>
     */
    private final ServletRequest servletRequest;

    /**
     * <p>The {@link SpringMvcResultImpl} instance being build.</p>
     */
    private final SpringMvcResultImpl springMvcResult;

    /**
     * Creates new instance of {@link SpringMvcResult} class.
     *
     * @param servletRequest the servlet request
     */
    private SpringMvcResultBuilder(ServletRequest servletRequest) {

        this.servletRequest = servletRequest;

        this.springMvcResult = getRequestMvcResult();
    }

    /**
     * <p>Creates the {@link SpringMvcResultBuilder} instance associated with the request.</p>
     *
     * @param servletRequest servlet request
     *
     * @return the created instance of {@link SpringMvcResultBuilder}
     */
    public static SpringMvcResultBuilder buildMvc(ServletRequest servletRequest) {

        return new SpringMvcResultBuilder(servletRequest);
    }

    /**
     * <p>Sets the {@link ModelAndView}.</p>
     *
     * @param modelAndView the {@link ModelAndView}
     *
     * @return the builder
     */
    public SpringMvcResultBuilder setModelAndView(ModelAndView modelAndView) {

        this.springMvcResult.setModelAndView(modelAndView);
        return this;
    }

    /**
     * <p>Sets the request handler.</p>
     *
     * @param handler the request handler
     *
     * @return the builder
     */
    public SpringMvcResultBuilder setHandler(Object handler) {

        this.springMvcResult.setHandler(handler);
        return this;
    }

    /**
     * <p>Sets the handler interceptors.</p>
     *
     * @param interceptors the handler interceptors
     *
     * @return the builder
     */
    public SpringMvcResultBuilder setInterceptors(HandlerInterceptor[] interceptors) {

        this.springMvcResult.setInterceptors(interceptors);
        return this;
    }

    /**
     * <p>Sets the exception.</p>
     *
     * @param exception the exception
     *
     * @return the builder
     */
    public SpringMvcResultBuilder setException(Exception exception) {

        this.springMvcResult.setException(exception);
        return this;
    }

    /**
     * <p>Retrieves the {@link SpringMvcResult} class.</p>
     *
     * @return the retrieved {@link SpringMvcResult}
     */
    private SpringMvcResultImpl getRequestMvcResult() {

        SpringMvcResultImpl requestResult =
                (SpringMvcResultImpl) servletRequest.getAttribute(Commons.SPRING_MVC_RESULT_ATTRIBUTE_NAME);

        if (requestResult == null) {

            requestResult = new SpringMvcResultImpl();
            servletRequest.setAttribute(Commons.SPRING_MVC_RESULT_ATTRIBUTE_NAME, requestResult);
        }

        return requestResult;
    }
}
