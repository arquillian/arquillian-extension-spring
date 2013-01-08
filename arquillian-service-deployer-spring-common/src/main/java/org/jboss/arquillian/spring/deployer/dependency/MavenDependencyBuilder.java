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

import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

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
    private MavenDependencyResolver mavenDependencyResolver;

    /**
     * <p>Represents whether maven should be run in offline mode.</p>
     */
    private boolean useMavenOffline = false;

    /**
     * <p>Creates new instance of {@link MavenDependencyBuilder} class.</p>
     */
    public MavenDependencyBuilder() {

        mavenDependencyResolver = DependencyResolvers.use(MavenDependencyResolver.class);
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
    public void addDependency(String artifactName, String artifactVersion, String... exclusions) {

        mavenDependencyResolver.artifact(String.format("%s:%s", artifactName, artifactVersion)).exclusions(exclusions);
    }

    /**
     * <p>Loads the project dependencies from given pom file.</p>
     */
    public void importPomDependencies(String pomFile, String[] exclusions) {

        mavenDependencyResolver.includeDependenciesFromPom(pomFile);

        if(exclusions != null) {

            mavenDependencyResolver.exclusions(exclusions);
        }
    }

    /**
     * <p>Retrieves the list of resolved artifacts.</p>
     *
     * @return the list of resolved artifacts
     */
    public File[] getDependencies() {

        if (useMavenOffline) {

            mavenDependencyResolver.goOffline();
        }

        return mavenDependencyResolver.resolveAsFiles();
    }
}

