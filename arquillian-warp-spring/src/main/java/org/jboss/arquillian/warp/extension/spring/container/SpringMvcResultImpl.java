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

import org.jboss.arquillian.warp.extension.spring.SpringMvcResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>Simple POJO that stores the execution state.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringMvcResultImpl implements SpringMvcResult {

    /**
     * <p>Represents the request handler.</p>
     */
    private Object handler;

    /**
     * <p>Represents the handler interceptors.</p>
     */
    private HandlerInterceptor[] interceptors;

    /**
     * <p>Represents the captures model and view.</p>
     */
    private ModelAndView modelAndView;

    /**
     * <p>Represents the exception thrown by the controller.</p>
     */
    private Exception exception;

    /**
     * <p>Creates new instance of {@link SpringMvcResultImpl} class.</p>
     */
    public SpringMvcResultImpl() {
        // empty constructor
    }

    /**
     * <p>Retrieves the request handler.</p>
     *
     * @return the request handler
     */
    @Override
    public Object getHandler() {
        return handler;
    }

    /**
     * <p>Sets the request handler.</p>
     *
     * @param handler the request handler
     */
    public void setHandler(Object handler) {
        this.handler = handler;
    }

    /**
     * <p>Retrieves the handler interceptors.</p>
     *
     * @return the handler interceptors
     */
    @Override
    public HandlerInterceptor[] getInterceptors() {
        return interceptors;
    }

    /**
     * <p>Sets the handler interceptors.</p>
     *
     * @param interceptors the handler interceptors
     */
    public void setInterceptors(HandlerInterceptor[] interceptors) {
        this.interceptors = interceptors;
    }

    /**
     * <p>Retrieves the {@link ModelAndView}.</p>
     *
     * @return the {@link ModelAndView}
     */
    @Override
    public ModelAndView getModelAndView() {
        return modelAndView;
    }

    /**
     * <p>Sets the {@link ModelAndView}.</p>
     *
     * @param modelAndView the {@link ModelAndView}
     */
    public void setModelAndView(ModelAndView modelAndView) {
        this.modelAndView = modelAndView;
    }

    /**
     * <p>Retrieves the exception.</p>
     *
     * @return the exception
     */
    @Override
    public Throwable getException() {
        return exception;
    }

    /**
     * <p>Sets the exception.</p>
     *
     * @param exception the exception
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }

    /**
     * <p>Retrieves the validation errors for the given attribute.</p>
     *
     * @param attributeName the attribute name
     *
     * @return the validation errors
     */
    @Override
    public Errors getErrors(String attributeName) {

        return getBindingResult(attributeName);
    }

    /**
     * <p>Retrieves the validation errors.</p>
     *
     * @return the validation errors
     */
    @Override
    public Errors getErrors() {

        return getBindingResult();
    }

    /**
     * <p>Retrieves the {@link BindingResult} from the model for the given attribute.</p>
     *
     * @return the {@link BindingResult}
     */
    private BindingResult getBindingResult(String attributeName) {

        if (modelAndView != null) {
            return (BindingResult) modelAndView.getModel().get(BindingResult.MODEL_KEY_PREFIX + attributeName);
        }

        return null;
    }

    /**
     * <p>Retrieves the {@link BindingResult} from the model.</p>
     *
     * @return the {@link BindingResult}
     */
    private BindingResult getBindingResult() {

        if (modelAndView != null) {
            for (Object obj : modelAndView.getModel().values()) {

                if (obj instanceof BindingResult) {

                    return (BindingResult) obj;
                }
            }
        }

        return null;
    }
}
