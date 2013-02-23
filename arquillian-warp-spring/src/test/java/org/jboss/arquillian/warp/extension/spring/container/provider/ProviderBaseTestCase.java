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
package org.jboss.arquillian.warp.extension.spring.container.provider;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;
import org.jboss.arquillian.warp.extension.spring.SpringMvcResult;
import org.jboss.arquillian.warp.extension.spring.container.Commons;
import org.jboss.arquillian.warp.extension.spring.container.SpringMvcResultImpl;
import org.jboss.arquillian.warp.extension.spring.utils.TestReflectionHelper;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <p>The base test class for all providers.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class ProviderBaseTestCase {

    /**
     * <p>Represents the instance of the tested class.</p>
     */
    protected ResourceProvider instance;

    /**
     * <p>Represents the instance of {@link SpringMvcResult} used for testing.</p>
     */
    protected SpringMvcResultImpl springMvcResult;

    /**
     * <p>Represents the mocked instance of {@link ModelAndView}.</p>
     */
    protected ModelAndView modelAndView;

    /**
     * <p>Initialize the provider instance used by this test.</p>
     *
     * @param provider the provider instance
     *
     * @throws Exception if any error occurs
     */
    protected void initProvider(ResourceProvider provider) throws Exception {

        instance = provider;

        modelAndView = mock(ModelAndView.class);

        springMvcResult = new SpringMvcResultImpl();
        springMvcResult.setModelAndView(modelAndView);
        springMvcResult.setException(new Exception());
        springMvcResult.setInterceptors(new HandlerInterceptor[0]);
        springMvcResult.setHandler(new Object());

        setSpringMvcResult();
    }

    /**
     * <p>Sets the {@link SpringMvcResult} on instance of the test object.</p>
     *
     * @throws Exception if any error occurs
     */
    @SuppressWarnings("unchecked")
    protected void setSpringMvcResult() throws Exception {

        ServletRequest mockServletRequest = mock(ServletRequest.class);
        when(mockServletRequest.getAttribute(Commons.SPRING_MVC_RESULT_ATTRIBUTE_NAME)).thenReturn(springMvcResult);

        Instance<ServletRequest> mockServletRequestInstance = mock(Instance.class);
        TestReflectionHelper.setFieldValue(instance, "servletRequestInstance", mockServletRequestInstance);
        when(mockServletRequestInstance.get()).thenReturn(mockServletRequest);
    }
}
