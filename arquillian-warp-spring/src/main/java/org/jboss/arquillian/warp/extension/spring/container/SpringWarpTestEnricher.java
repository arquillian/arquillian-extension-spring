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

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.test.spi.TestEnricher;
import org.jboss.arquillian.warp.extension.spring.SpringMvcResource;
import org.jboss.arquillian.warp.extension.spring.SpringMvcResult;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * <p>A test enricher that inject into test case fields annotated with {@link SpringMvcResult}.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringWarpTestEnricher implements TestEnricher {

    /**
     * <p>Represents the {@link SpringMvcResult} captured during request execution.</p>
     */
    @Inject
    private Instance<SpringMvcResult> springMvcResult;

    /**
     * {@inheritDoc}
     */
    @Override
    public void enrich(Object testCase) {

        List<Field> annotatedFields = SecurityActions.getFieldsWithAnnotation(testCase.getClass(),
                SpringMvcResource.class);

        SpringMvcResult mvcResult = springMvcResult.get();

        try {
            for (Field field : annotatedFields) {
                Object value = null;

                if (mvcResult != null) {
                    if (field.getType() == SpringMvcResult.class) {
                        value = mvcResult;
                    } else if (field.getType() == ModelAndView.class) {
                        value = mvcResult.getModelAndView();
                    } else if (field.getType() == Exception.class) {
                        value = mvcResult.getException();
                    } else if (field.getType() == HandlerInterceptor[].class) {
                        value = mvcResult.getInterceptors();
                    } else if (field.getType() == Errors.class) {
                        value = mvcResult.getErrors();
                    } else if (mvcResult.getHandler() != null &&
                            field.getType() == mvcResult.getHandler().getClass()) {
                        value = mvcResult.getHandler();
                    }
                }

                field.set(testCase, value);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not inject field in the test class.", e);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] resolve(Method method) {

        return new Object[method.getParameterTypes().length];
    }
}
