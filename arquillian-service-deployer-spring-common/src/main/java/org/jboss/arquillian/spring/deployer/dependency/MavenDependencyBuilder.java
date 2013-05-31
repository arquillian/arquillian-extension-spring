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

import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenDependencies;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenDependency;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenDependencyExclusion;

import java.io.File;

/**
 * <p>A helper class for resolving dependency using maven.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class MavenDependencyBuilder {

    /**
     * <p>Represents the maven dependency resolver.</p>
     */
    private MavenResolverSystem mavenDependencyResolver;

    /**
     * <p>Represents whether maven should be run in offline mode.</p>
     */
    private boolean useMavenOffline = false;

    /**
     * <p>Creates new instance of {@link MavenDependencyBuilder} class.</p>
     */
    public MavenDependencyBuilder() {

        mavenDependencyResolver = Maven.resolver();
    }

    /**
     * <p>Sets whether to use maven in offline mode.</p>
     *
     * @param useMavenOffline whether to use maven in offline mode
     */
    public void setUseMavenOffline(boolean useMavenOffline) {
        this.useMavenOffline = useMavenOffline;
    }

    /**
     * <p>Adds the dependency to the builder.</p>
     *
     * @param artifactName    the name of maven artifact
     * @param artifactVersion the version of maven artifact
     * @param exclusions      the list of excluded maven artifacts
     */
    public void addDependency(final String artifactName, final String artifactVersion, final String... exclusions) {

        final MavenDependencyExclusion[] depExclusions = getMavenDependencyExclusions(exclusions);
        final String canonicalForm = String.format("%s:%s", artifactName, artifactVersion);
        final MavenDependency dep = MavenDependencies
                .createDependency(canonicalForm, ScopeType.COMPILE, false, depExclusions);
        mavenDependencyResolver.addDependency(dep);
    }

    /**
     * <p>Loads the project dependencies from given pom file.</p>
     */
    public void importPomDependencies(String pomFile) {

        mavenDependencyResolver.loadPomFromFile(pomFile).importRuntimeAndTestDependencies();
    }

    /**
     * <p>Retrieves the list of resolved artifacts.</p>
     *
     * @return the list of resolved artifacts
     */
    public File[] getDependencies() {

        if (useMavenOffline) {
            mavenDependencyResolver.offline(true);
        }

        return mavenDependencyResolver.resolve().withTransitivity().asFile();
    }

    /**
     * Retrieves the dependency exclusions.
     *
     * @param exclusions the names of the excluded maven artifacts.
     *
     * @return the excluded maven artifacts
     */
    private MavenDependencyExclusion[] getMavenDependencyExclusions(String[] exclusions) {
        final int numExclusions = exclusions.length;
        final MavenDependencyExclusion[] depExclusions = new MavenDependencyExclusion[numExclusions];
        for (int i = 0; i < numExclusions; i++) {
            final MavenDependencyExclusion depExclusion = MavenDependencies.createExclusion(exclusions[i]);
            depExclusions[i] = depExclusion;
        }
        return depExclusions;
    }
}

