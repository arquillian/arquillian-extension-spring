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
package org.jboss.arquillian.spring.dependency;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.spring.SpringExtensionConsts;
import org.jboss.arquillian.spring.configuration.SpringExtensionConfiguration;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;

import java.io.File;

/**
 * <p>Dependency resolver.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public abstract class AbstractDependencyResolver {

    /**
     * <p>Represents the instance of {@link SpringExtensionConfiguration}.</p>
     */
    private SpringExtensionConfiguration configuration;

    /**
     * <p>Creates new instance of {@link AbstractDependencyResolver} class with given configuration.</p>
     *
     * @param configuration the extension configuration
     */
    protected AbstractDependencyResolver(SpringExtensionConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * <p>Retrieves the extension configuration</p>
     *
     * @return the extension configuration
     */
    public SpringExtensionConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * <p>Resolves the dependencies for the extension.</p>
     *
     * @return the resolved dependencies
     */
    public abstract File[] resolveDependencies();
}
