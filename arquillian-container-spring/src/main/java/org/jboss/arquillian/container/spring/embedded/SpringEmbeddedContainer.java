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
package org.jboss.arquillian.container.spring.embedded;

import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.container.spi.client.container.DeploymentException;
import org.jboss.arquillian.container.spi.client.container.LifecycleException;
import org.jboss.arquillian.container.spi.client.protocol.ProtocolDescription;
import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.arquillian.container.spi.context.annotation.ContainerScoped;
import org.jboss.arquillian.container.spi.context.annotation.DeploymentScoped;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.classloader.ShrinkWrapClassLoader;
import org.jboss.shrinkwrap.descriptor.api.Descriptor;

/**
 * <p>Spring embedded container.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class SpringEmbeddedContainer implements DeployableContainer<SpringEmbeddedConfiguration> {

    /**
     * <p>Class loader manager.</p>
     */
    @Inject
    @DeploymentScoped
    private InstanceProducer<ContextClassLoaderManager> contextClassLoaderManager;

    /**
     * <p>Container configuration.</p>
     */
    @Inject
    @ContainerScoped
    private InstanceProducer<SpringEmbeddedConfiguration> configuration;

    /**
     * <p>Creates new instance of {@link SpringEmbeddedContainer} class.</p>
     */
    public SpringEmbeddedContainer() {
        // empty constructor
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<SpringEmbeddedConfiguration> getConfigurationClass() {

        return SpringEmbeddedConfiguration.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setup(SpringEmbeddedConfiguration configuration) {

        // assigns the configuration
        this.configuration.set(configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() throws LifecycleException {

        // empty method
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() throws LifecycleException {

        // empty method
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProtocolDescription getDefaultProtocol() {

        return new ProtocolDescription("Local");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProtocolMetaData deploy(Archive archive) throws DeploymentException {

        // creates the class loader based on the passed archive
        ShrinkWrapClassLoader classLoader = getClassLoader(archive);

        // instantiates the class loader manager
        ContextClassLoaderManager classLoaderManager = new ContextClassLoaderManager(classLoader);
        contextClassLoaderManager.set(classLoaderManager);

        // 'starts' the manager
        classLoaderManager.enable();

        return new ProtocolMetaData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undeploy(Archive archive) throws DeploymentException {

        // 'disables' the context class loader
        if (contextClassLoaderManager.get() != null) {
            contextClassLoaderManager.get().disable();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deploy(Descriptor descriptor) throws DeploymentException {

        throw new UnsupportedOperationException("The embedded container does not supports deploying a descriptor.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undeploy(Descriptor descriptor) throws DeploymentException {

        throw new UnsupportedOperationException("The embedded container does not supports undeploying a descriptor.");
    }

    /**
     * <p>Creates the class loader based on the passed archive.</p>
     *
     * @param archive the shrinkwrap archive
     *
     * @return the created class loader
     */
    private ShrinkWrapClassLoader getClassLoader(Archive<?> archive) {
        return new ShrinkWrapClassLoader(archive.getClass().getClassLoader(), archive);
    }
}
