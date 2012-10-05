/*
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
package org.jboss.arquillian.spring.deployer.dependency;

import java.io.File;

/**
 * <p>A cached dependency resolver. It delegates to </p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class CachedDependencyResolver extends AbstractDependencyResolver {

    /**
     * <p>The instance {@link AbstractDependencyResolver} to which execution is delegated.</p>
     */
    private final AbstractDependencyResolver dependencyResolver;

    /**
     * <p>Represents the resolved files.</p>
     */
    private static File[] resolvedDependencies;

    /**
     * <p>Creates new instance of {@link CachedDependencyResolver} class with given dependency resolver.</p>
     *
     * @param dependencyResolver the dependency resolver
     */
    public CachedDependencyResolver(AbstractDependencyResolver dependencyResolver) {
        super(dependencyResolver.getConfiguration());

        this.dependencyResolver = dependencyResolver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File[] resolveDependencies() {

        if (resolvedDependencies == null) {

            resolvedDependencies = dependencyResolver.resolveDependencies();
        }

        return resolvedDependencies;
    }
}
