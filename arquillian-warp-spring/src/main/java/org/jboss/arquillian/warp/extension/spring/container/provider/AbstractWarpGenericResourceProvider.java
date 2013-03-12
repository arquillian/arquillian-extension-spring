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

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;

/**
 * <p>A base resource provider that adds generic capabilities for all concrete providers.</p>
 *
 * <p>This class defines {@link #getProvidedResourceClass()} that is able to retrieve the class of the provided class
 * directly from the class hierarchy. The implementing class must specify the concrete class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public abstract class AbstractWarpGenericResourceProvider<T> extends AbstractWarpResourceProvider<T> {

    /**
     * <p>Retrieves the provided class from the generic base type.</p>
     *
     * @return the provided resource class
     */
    @SuppressWarnings("unchecked")
    protected Class<T> getProvidedResourceClass() {

        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
