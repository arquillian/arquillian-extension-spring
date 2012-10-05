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

import org.jboss.arquillian.spring.deployer.SpringDeployerConstants;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>A helper class for resolving dependency using maven.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class MavenDependencyBuilder {

    /**
     * <p>The dependencies map.</p>
     */
    private final Map<String, File> dependenciesMap;

    /**
     * <p>Represents whether maven should be run in offline mode.</p>
     */
    private boolean useMavenOffline;

    /**
     * <p>Creates new instance of {@link MavenDependencyBuilder}.</p>
     */
    public MavenDependencyBuilder() {

        dependenciesMap = new HashMap<String, File>();
    }

    /**
     * <p>Retrieves whether maven should run in offline mode.</p>
     *
     * @return whether maven should run in offline mode
     */
    public boolean isUseMavenOffline() {
        return useMavenOffline;
    }

    /**
     * <p>Sets whether maven should run in offline mode.</p>
     *
     * @param useMavenOffline whether maven should run in offline mode
     */
    public void setUseMavenOffline(boolean useMavenOffline) {
        this.useMavenOffline = useMavenOffline;
    }

    /**
     * <p>Adds the dependency.</p>
     *
     * @param artifactName    the artifact name
     * @param artifactVersion the artifact version
     * @param defaultVersion  the artifact default version
     * @param exclusions      the names of the artifact which need to excluded during artifact resolving
     */
    public void addDependency(String artifactName, String artifactVersion, String defaultVersion, String... exclusions) {

        mergeDependencies(resolveArtifact(artifactName, artifactVersion, defaultVersion, exclusions));
    }

    /**
     * <p>Retrieves the dependencies.</p>
     *
     * @return the list of dependencies
     */
    public File[] getDependencies() {
        List<File> result = new ArrayList<File>(dependenciesMap.values());

        return result.toArray(new File[result.size()]);
    }

    /**
     * <p>Merges the dependencies.</p>
     *
     * @param files the dependencies
     */
    private void mergeDependencies(File[] files) {

        if (files != null) {
            for (File file : files) {

                dependenciesMap.put(file.getAbsolutePath(), file);
            }
        }
    }

    /**
     * <p>Resolves the artifact using the given version, if the version hasn't been specified than the default artifact
     * version will be used.</p>
     *
     * @param artifact       the artifact name
     * @param version        the artifact version
     * @param defaultVersion the default artifact version to be used
     * @param exclusions     the names of the artifact which need to excluded during artifact resolving
     *
     * @return the resolved artifacts
     */
    private File[] resolveArtifact(String artifact, String version, String defaultVersion, String... exclusions) {
        String artifactVersion;

        if (version != null && version.length() > 0) {
            artifactVersion = version;
        } else {
            artifactVersion = defaultVersion;
        }

        return resolveArtifact(artifact, artifactVersion, exclusions);
    }

    /**
     * <p>Resolves the given artifact in specified version using maven.</p>
     *
     * @param artifact   the artifact name
     * @param version    the artifact version
     * @param exclusions the names of the artifact which need to excluded during artifact resolving
     *
     * @return the resolved files
     */
    private File[] resolveArtifact(String artifact, String version, String... exclusions) {
        File[] artifacts;
        try {
            artifacts = resolveArtifact(artifact, exclusions);
        } catch (Exception e) {
            artifacts = resolveArtifact(artifact + ":" + version, exclusions);
        }
        return artifacts;
    }

    /**
     * <p>Resolves the given artifact in specified version using maven.</p>
     *
     * @param artifact   the artifact name
     * @param exclusions the names of the artifact which need to excluded during artifact resolving
     *
     * @return the resolved files
     */
    private File[] resolveArtifact(String artifact, String... exclusions) {

        MavenDependencyResolver mvnDependencyResolver = DependencyResolvers.use(MavenDependencyResolver.class);

        if (isMavenUsed()) {
            mvnDependencyResolver.loadMetadataFromPom(SpringDeployerConstants.POM_XML);
        }

        mvnDependencyResolver.artifacts(artifact).exclusions(exclusions);

        if(useMavenOffline) {
            mvnDependencyResolver.goOffline();
        }

        return mvnDependencyResolver.resolveAsFiles();
    }

    /**
     * <p>Returns whether maven is being used in project.</p>
     *
     * @return true if maven is being used in project, false otherwise
     */
    private boolean isMavenUsed() {
        return new File(SpringDeployerConstants.POM_XML).exists();
    }
}
