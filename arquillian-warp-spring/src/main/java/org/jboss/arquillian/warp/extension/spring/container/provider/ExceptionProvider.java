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

import org.jboss.arquillian.test.api.ArquillianResource;

import java.lang.annotation.Annotation;

/**
 * <p>A resource provider that supplies the exception caught during runtime, if any occurred.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class ExceptionProvider extends AbstractWarpGenericResourceProvider<Throwable> {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canProvide(Class<?> type) {

        // only applies to exception class hierarchy, excludes Object as a valid injection point
        return Throwable.class.isAssignableFrom(type)
                && getSpringMvcResult().getException() != null
                && type.isAssignableFrom(getSpringMvcResult().getException().getClass());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Throwable internalLookup(ArquillianResource resource, Annotation... qualifiers) {

        return getSpringMvcResult().getException();
    }
}
