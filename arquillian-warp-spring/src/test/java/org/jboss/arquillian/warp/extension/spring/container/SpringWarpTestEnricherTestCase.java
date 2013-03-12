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
import org.jboss.arquillian.warp.extension.spring.SpringMvcResource;
import org.jboss.arquillian.warp.extension.spring.SpringMvcResult;
import org.jboss.arquillian.warp.extension.spring.utils.TestReflectionHelper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <p>Tests {@link SpringWarpTestEnricher} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringWarpTestEnricherTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private SpringWarpTestEnricher instance;

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        instance = new SpringWarpTestEnricher();
    }

    /**
     * <p>Tests {@link SpringWarpTestEnricher#enrich(Object)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testEnrich() throws Exception {

        SpringMvcResultImpl springMvcResult = new SpringMvcResultImpl();
        springMvcResult.setModelAndView(mock(ModelAndView.class));
        springMvcResult.setException(new Exception());
        springMvcResult.setInterceptors(new HandlerInterceptor[0]);
        setSpringMvcResult(springMvcResult);

        TestClass testClass = new TestClass();

        instance.enrich(testClass);

        assertEquals("The bean property hasn't been injected.", springMvcResult, testClass.getSpringMvcResult());
        assertEquals("The bean property hasn't been injected.", springMvcResult.getModelAndView(),
                testClass.getModelAndView());
        assertEquals("The bean property hasn't been injected.", springMvcResult.getException(), testClass.getException());
        assertArrayEquals("The bean property hasn't been injected.", springMvcResult.getInterceptors(),
                testClass.getHandlerInterceptors());
    }

    /**
     * <p>Tests {@link SpringWarpTestEnricher#resolve(Method)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testResolve() throws Exception {

        Class clazz = SpringWarpTestEnricherTestCase.class;
        Method method = clazz.getMethod("testResolve", (Class[]) null);

        Object[] result = instance.resolve(method);

        assertNotNull("Method returned null result.", result);
    }

    /**
     * <p>Sets the {@link SpringMvcResult} on tested object.</p>
     *
     * @param springMvcResult the {@link SpringMvcResult}
     *
     * @throws Exception if any error occurs
     */
    private void setSpringMvcResult(SpringMvcResult springMvcResult) throws Exception {

        HttpServletRequest mockServletRequest = mock(HttpServletRequest.class);
        when(mockServletRequest.getAttribute(Commons.SPRING_MVC_RESULT_ATTRIBUTE_NAME)).thenReturn(springMvcResult);

        Instance<HttpServletRequest> mockServletRequestInstance = mock(Instance.class);
        TestReflectionHelper.setFieldValue(instance, "servletRequest", mockServletRequestInstance);
        when(mockServletRequestInstance.get()).thenReturn(mockServletRequest);
    }

    /**
     * <p>Represents a simple test class used for testing.</p>
     *
     * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
     */
    private static final class TestClass {

        @SpringMvcResource
        private SpringMvcResult springMvcResult;

        @SpringMvcResource
        private ModelAndView modelAndView;

        @SpringMvcResource
        private Exception exception;

        @SpringMvcResource
        private HandlerInterceptor[] handlerInterceptors;

        public SpringMvcResult getSpringMvcResult() {
            return springMvcResult;
        }

        public ModelAndView getModelAndView() {
            return modelAndView;
        }

        public Exception getException() {
            return exception;
        }

        public HandlerInterceptor[] getHandlerInterceptors() {
            return handlerInterceptors;
        }
    }
}
