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
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;
import org.jboss.arquillian.warp.extension.spring.SpringMvcResult;
import org.jboss.arquillian.warp.extension.spring.container.Commons;

import javax.servlet.ServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;

/**
 * <p>A base resource provider that adds generic capabilities for all concrete providers.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public abstract class AbstractWarpGenericResourceProvider<T> implements ResourceProvider {

    /**
     * <p>The injected {@link SpringMvcResult} class.</p>
     */
    @Inject
    private Instance<ServletRequest> servletRequestInstance;

    /**
     * <p>Retrieves the {@link SpringMvcResult} class.</p>
     *
     * <p>The default implementation retrieves the generic type from the class hierarchy.</p>
     *
     * @return the {@link SpringMvcResult}
     */
    protected SpringMvcResult getSpringMvcResult() {

        return (SpringMvcResult) servletRequestInstance.get().getAttribute(Commons.SPRING_MVC_RESULT_ATTRIBUTE_NAME);
    }

    /**
     * <p>Retrieves the provided class from the generic base type.</p>
     *
     * @return the provided resource class
     */
    @SuppressWarnings("unchecked")
    protected Class<T> getProvidedResourceClass() {

        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object lookup(ArquillianResource resource, Annotation... qualifiers) {

        return internalLookup(resource, qualifiers);
    }

    /**
     * <p>Retrieves the resource instance provided by this class.</p>
     *
     * @param resource   the resource information
     * @param qualifiers the additional qualifiers specified for this type
     *
     * @return the resource instance
     */
    protected abstract T internalLookup(ArquillianResource resource, Annotation... qualifiers);
}
