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
package org.jboss.arquillian.warp.extension.spring;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>Aggregates the execution state of {@link org.jboss.arquillian.warp.extension.spring.servlet.WarpDispatcherServlet}
 * after is execution.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @see org.jboss.arquillian.warp.extension.spring.servlet.WarpDispatcherServlet
 */
public interface SpringMvcResult {

    /**
     * <p>Retrieves the handler responsible for processing the request.</p>
     *
     * @return the handler that was responsible for processing the request
     */
    Object getHandler();

    /**
     * <p>Retrieves the array of {@link HandlerInterceptor} that was used during request processing.</p>
     *
     * @return array of interceptors
     */
    HandlerInterceptor[] getInterceptors();

    /**
     * <p>Retrieves the {@link ModelAndView} returned by controller.</p>
     *
     * @return {@link ModelAndView} returned by controller
     */
    ModelAndView getModelAndView();

    /**
     * <p>The exception that was thrown by the controller during it's execution.</p>
     *
     * @return the exception throw by controller during execution, may be null
     */
    Throwable getException();

    /**
     * <p>Retrieves the validation errors for the given attribute.</p>
     *
     * @param attributeName the attribute name
     *
     * @return the validation errors, may be null
     */
    Errors getErrors(String attributeName);

    /**
     * <p>Retrieves the validation errors.</p>
     *
     * @return the validation errors, may be null
     */
    Errors getErrors();
}
