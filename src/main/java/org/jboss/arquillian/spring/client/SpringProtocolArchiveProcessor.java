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
import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>Archive processor adds the Spring framework dependencies into the test or archive processor. The extension by
 * default allows to test JAR archives in the container by adding all the required dependencies into protocol WAR.</p>
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

            addDependencies(testDeployment.getApplicationArchive());
        } else if (isEnterpriseArchive(protocolArchive) || isWebArchive(protocolArchive)) {
            // otherwise try to add the required dependencies into the protocol archive

            addDependencies(protocolArchive);
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

        File[] dependencies = resolveExtensionDependencies();

        if (archive instanceof EnterpriseArchive) {
            ((EnterpriseArchive) archive).addAsModules(dependencies);
        } else if (archive instanceof WebArchive) {
            ((WebArchive) archive).addAsLibraries(dependencies);
        } else {
            throw new RuntimeException("Unsupported archive format[" + archive.getClass().getSimpleName()
                    + ", " + archive.getName() + "] for Spring testing. Please use WAR or EAR.");
        }
    }

    /**
     * <p>Resolves all dependencies required for the extension.</p>
     *
     * @return resolved dependencies
     */
    private File[] resolveExtensionDependencies() {

        // TODO retrieve the version from configuration
        ArrayList<File> dependencies = new ArrayList<File>();

        // adds the spring dependencies
        dependencies.addAll(Arrays.asList(resolveArtifact(SpringExtensionConsts.SPRING_ARTIFACT_NAME,
                SpringExtensionConsts.SPRING_ARTIFACT_VERSION, SpringExtensionConsts.SPRING_ARTIFACT_VERSION)));

        // adds spring web
        dependencies.addAll(Arrays.asList(resolveArtifact(SpringExtensionConsts.SPRING_ARTIFACT_WEB_NAME,
                SpringExtensionConsts.SPRING_ARTIFACT_VERSION, SpringExtensionConsts.SPRING_ARTIFACT_VERSION)));

        // adds the cglib
        dependencies.addAll(Arrays.asList(resolveArtifact(SpringExtensionConsts.CGLIB_ARTIFACT_NAME,
                SpringExtensionConsts.CGLIB_ARTIFACT_VERSION, SpringExtensionConsts.CGLIB_ARTIFACT_VERSION)));

        return dependencies.toArray(new File[dependencies.size()]);
    }

    /**
     * Resolves the artifact using the given version, if the version hasn't been specified than the default artifact
     * version will be used.
     *
     * @param artifact       the artifact name
     * @param version        the artifact version
     * @param defaultVersion the default artifact version to be used
     *
     * @return the resolved artifacts
     */
    private File[] resolveArtifact(String artifact, String version, String defaultVersion) {
        String artifactVersion;

        if (version == null || version.length() == 0) {
            artifactVersion = defaultVersion;
        } else {
            artifactVersion = version;
        }

        return resolveArtifact(artifact, artifactVersion);
    }

    /**
     * <p>Resolves the given artifact in specified version with help of maven build system.</p>
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
     * <p>Resolves the given artifact by it's name with help of maven build system.</p>
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
     * <p>Returns whether maven is being used in project.</p>
     *
     * @return true if maven is being used in project, false otherwise
     */
    private boolean isMavenUsed() {
        return new File(SpringExtensionConsts.POM_XML).exists();
    }
}
