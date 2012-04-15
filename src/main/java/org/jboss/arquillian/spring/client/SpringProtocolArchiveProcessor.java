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
import org.jboss.arquillian.spring.SpringExtensionConsts;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

import java.io.File;

/**
 * SpringProtocolArchiveProcessor
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class SpringProtocolArchiveProcessor implements ProtocolArchiveProcessor {

    /**
     * {@inheritDoc}
     */
    public void process(TestDeployment testDeployment, Archive<?> protocolArchive) {

        // if the application archive is an war or ear then add the spring dependencies into it
        if (isEnterpriseArchive(testDeployment.getApplicationArchive()) ||
                isWebArchive(testDeployment.getApplicationArchive())) {
            addSpringLibraries(testDeployment.getApplicationArchive());
            addSpringWebLibraries(testDeployment.getApplicationArchive());
            addCglib(testDeployment.getApplicationArchive());
        } else if (isEnterpriseArchive(protocolArchive) || isWebArchive(protocolArchive)) {
            // otherwise try to add the required dependencies into the protocol archive
            addSpringLibraries(protocolArchive);
            addSpringWebLibraries(protocolArchive);
            addCglib(protocolArchive);
        }
    }

    /**
     * Returns whether the passed archive is an enterprise archive (EAR)
     *
     * @param archive the archive
     *
     * @return true if passed archive is an enterprise archive, false otherwise
     */
    private boolean isEnterpriseArchive(Archive<?> archive) {
        return archive instanceof EnterpriseArchive;
    }

    /**
     * Returns whether the passed archive is an web archive (WAR)
     *
     * @param archive the archive
     *
     * @return true if passed archive is an web archive, false otherwise
     */
    private boolean isWebArchive(Archive<?> archive) {
        return archive instanceof WebArchive;
    }

    /**
     * Adds the spring dependencies into the passed archive.
     *
     * @param archive the archive
     */
    private void addSpringLibraries(Archive<?> archive) {

        File[] springLibraries = resolveSpringDependencies();

        if (archive instanceof EnterpriseArchive) {
            ((EnterpriseArchive) archive).addAsModules(springLibraries);
        } else if (archive instanceof WebArchive) {
            ((WebArchive) archive).addAsLibraries(springLibraries);
        } else {
            throw new RuntimeException("Unsupported archive format[" + archive.getClass().getSimpleName()
                    + ", " + archive.getName() + "] for Spring application. Please use WAR or EAR.");
        }
    }

    private void addSpringWebLibraries(Archive<?> archive) {

        File[] springLibraries = resolveSpringWebDependencies();

        if (archive instanceof EnterpriseArchive) {
            ((EnterpriseArchive) archive).addAsModules(springLibraries);
        } else if (archive instanceof WebArchive) {
            ((WebArchive) archive).addAsLibraries(springLibraries);
        } else {
            throw new RuntimeException("Unsupported archive format[" + archive.getClass().getSimpleName()
                    + ", " + archive.getName() + "] for Spring application. Please use WAR or EAR.");
        }
    }

    /**
     * Adds cglib dependencies into the passed archive.
     *
     * @param archive the archive
     */
    private void addCglib(Archive<?> archive) {

        File[] springLibraries = resolveCglibLibraries();

        if (archive instanceof EnterpriseArchive) {
            ((EnterpriseArchive) archive).addAsModules(springLibraries);
        } else if (archive instanceof WebArchive) {
            ((WebArchive) archive).addAsLibraries(springLibraries);
        } else {
            throw new RuntimeException("Unsupported archive format[" + archive.getClass().getSimpleName()
                    + ", " + archive.getName() + "] for Spring application. Please use WAR or EAR.");
        }
    }

    /**
     * Resolves the spring dependencies using maven.
     *
     * @return the spring dependencies
     */
    private File[] resolveSpringDependencies() {

        return resolveArtifact(SpringExtensionConsts.SPRING_ARTIFACT_NAME, SpringExtensionConsts.SPRING_ARTIFACT_VERSION);
    }

    /**
     * Resolves the spring dependencies using maven.
     *
     * @return the spring dependencies
     */
    private File[] resolveSpringWebDependencies() {

        return resolveArtifact(SpringExtensionConsts.SPRING_ARTIFACT_WEB, SpringExtensionConsts.SPRING_ARTIFACT_VERSION);
    }

    /**
     * Resolves cglib dependencies using maven.
     *
     * @return cglib dependencies
     */
    private File[] resolveCglibLibraries() {

        return resolveArtifact(SpringExtensionConsts.CGLIB_ARTIFACT_NAME, SpringExtensionConsts.CGLIB_ARTIFACT_VERSION);
    }

    /**
     * Resolves the given artifact in specified version with help of maven build system.
     *
     * @param artifact the artifact name
     * @param version  the artifact version
     *
     * @return the resolved files
     */
    private File[] resolveArtifact(String artifact, String version) {
        File[] artifacts = null;
        try {
            artifacts = resolveArtifact(artifact);
        } catch (Exception e) {
            artifacts = resolveArtifact(artifact + ":" + version);
        }
        return artifacts;
    }

    /**
     * Resolves the given artifact by it's name with help of maven build system.
     *
     * @param artifact the fully qualified artifact name
     *
     * @return the resolved files
     */
    private File[] resolveArtifact(String artifact) {
        MavenDependencyResolver mvnResolver = DependencyResolvers.use(MavenDependencyResolver.class);

        if (isMavenUsed()) {
            mvnResolver.loadMetadataFromPom(SpringExtensionConsts.POM_XML);
        }

        return mvnResolver.artifacts(artifact)
                .resolveAsFiles();
    }

    /**
     * Returns whether maven is being used in project.
     *
     * @return true if maven is being used in project, false otherwise
     */
    private boolean isMavenUsed() {
        return new File(SpringExtensionConsts.POM_XML).exists();
    }
}
