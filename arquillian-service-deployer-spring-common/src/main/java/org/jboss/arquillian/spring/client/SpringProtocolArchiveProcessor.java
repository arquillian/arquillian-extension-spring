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
package org.jboss.arquillian.spring.client;

import org.jboss.arquillian.container.test.spi.TestDeployment;
import org.jboss.arquillian.container.test.spi.client.deployment.ProtocolArchiveProcessor;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.spring.configuration.SpringExtensionConfiguration;
import org.jboss.arquillian.spring.dependency.AbstractDependencyResolver;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import java.io.File;

/**
 * <p>Archive processor adds the Spring framework dependencies into the test or archive processor. The extension by
 * default allows to test JAR archives in the container by adding all the required dependencies into protocol WAR.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class SpringProtocolArchiveProcessor implements ProtocolArchiveProcessor {

    /**
     * <p>Represents the instance of {@link SpringExtensionConfiguration}.</p>
     */
    @Inject
    private Instance<SpringExtensionConfiguration> configuration;

    /**
     * <p>Represents the instance of {@link AbstractDependencyResolver}.</p>
     */
    @Inject
    private Instance<AbstractDependencyResolver> dependencyResolver;

    /**
     * {@inheritDoc}
     */
    public void process(TestDeployment testDeployment, Archive<?> protocolArchive) {

        // auto packages the spring dependencies
        if (getConfiguration().isAutoPackaging()) {
            // if the application archive is an war or ear then add the spring dependencies into it
            if (isEnterpriseArchive(testDeployment.getApplicationArchive()) ||
                    isWebArchive(testDeployment.getApplicationArchive())) {

                addDependencies(testDeployment.getApplicationArchive());
            } else if (isEnterpriseArchive(protocolArchive) || isWebArchive(protocolArchive)) {
                // otherwise try to add the required dependencies into the protocol archive

                addDependencies(protocolArchive);
            }
        }
    }

    /**
     * <p>Returns whether the passed archive is an enterprise archive (EAR)</p>
     *
     * @param archive the archive
     *
     * @return true if passed archive is an enterprise archive, false otherwise
     */
    private boolean isEnterpriseArchive(Archive<?> archive) {
        return archive instanceof EnterpriseArchive;
    }

    /**
     * <p>Returns whether the passed archive is an web archive (WAR).</p>
     *
     * @param archive the archive
     *
     * @return true if passed archive is an web archive, false otherwise
     */
    private boolean isWebArchive(Archive<?> archive) {
        return archive instanceof WebArchive;
    }

    /**
     * <p>Adds the dependencies into the given archive.</p>
     *
     * @param archive the archive to which the dependencies will be added
     */
    private void addDependencies(Archive<?> archive) {

        File[] dependencies = getDependencies();

        if (isEnterpriseArchive(archive)) {
            ((EnterpriseArchive) archive).addAsModules(dependencies);
        } else if (isWebArchive(archive)) {
            ((WebArchive) archive).addAsLibraries(dependencies);
        } else {
            throw new RuntimeException("Unsupported archive format[" + archive.getClass().getSimpleName()
                    + ", " + archive.getName() + "] for Spring testing. Please use WAR or EAR.");
        }
    }

    /**
     * <p>Retrieves the extension configuration</p>
     *
     * @return the extension configuration
     */
    public SpringExtensionConfiguration getConfiguration() {

        return configuration.get();
    }

    /**
     * <p>Retrieves the dependencies.</p>
     *
     * @return the dependencies
     */
    public File[] getDependencies() {

        return dependencyResolver.get() != null ? dependencyResolver.get().resolveDependencies() : null;
    }
}
