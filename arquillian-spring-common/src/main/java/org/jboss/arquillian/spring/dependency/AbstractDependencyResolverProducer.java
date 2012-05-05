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

/**
 * <p>An abstract producer that is responsible for creating instances of {@link AbstractDependencyResolver}.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public abstract class AbstractDependencyResolverProducer {

    /**
     * <p>Represents the instance of {@link SpringExtensionConfiguration}.</p>
     */
    @Inject
    private Instance<SpringExtensionConfiguration> configuration;

    /**
     * <p>Represents the dependency resolver.</p>
     */
    @Inject
    @ApplicationScoped
    private InstanceProducer<AbstractDependencyResolver> dependencyResolver;

    /**
     * <p>Retrieves the configuration.</p>
     *
     * @return the configuration
     */
    public SpringExtensionConfiguration getConfiguration() {
        return configuration.get() != null ? configuration.get() : null;
    }

    /**
     * <p>Creates the dependency resolver before the test suite is being run.</p>
     *
     * @param beforeSuiteEvent the event fired before execution of the test suite
     */
    public void initDependencyResolver(@Observes(precedence = SpringExtensionConsts.DEFAULT_PRECEDENCE) BeforeSuite beforeSuiteEvent) {

        AbstractDependencyResolver abstractDependencyResolver = createDependencyResolver();

        if (abstractDependencyResolver != null) {

            dependencyResolver.set(abstractDependencyResolver);
        }
    }

    /**
     * <p>Abstract method responsible for creating the concrete instance of {@link AbstractDependencyResolver}.</p>
     *
     * @return instance of {@link AbstractDependencyResolver}
     */
    protected abstract AbstractDependencyResolver createDependencyResolver();
}
