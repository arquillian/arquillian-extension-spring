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

import org.jboss.arquillian.spring.deployer.SpringDeployerConstants_3;
import org.jboss.arquillian.spring.deployer.configuration.SpringDeployerConfiguration;

import java.io.File;

/**
 * <p>The dependency resolver.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class Spring3DependencyResolver extends AbstractDependencyResolver {

    /**
     * <p>Creates new instance of {@link Spring3DependencyResolver}.</p>
     *
     * @param configuration the configuration
     */
    public Spring3DependencyResolver(SpringDeployerConfiguration configuration) {
        super(configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File[] resolveDependencies() {

        MavenDependencyBuilder mavenDependencyBuilder = new MavenDependencyBuilder();
        mavenDependencyBuilder.setUseMavenOffline(getConfiguration().isUseMavenOffline());

        if (getConfiguration().isImportPomDependencies()) {

            // imports all dependencies loaded from pom file
            mavenDependencyBuilder.importPomDependencies(getConfiguration().getPomFile(),
                   splitExcludedArtifacts(getConfiguration().getExcludedArtifacts()));
        } else {

            for (String artifactId : SpringDeployerConstants_3.SPRING_ARTIFACTS_IDS) {

                mavenDependencyBuilder.addDependency(
                        String.format("%s:%s", SpringDeployerConstants_3.SPRING_GROUP_ID, artifactId),
                        getArtifactVersion(getConfiguration().getSpringVersion(),
                                SpringDeployerConstants_3.SPRING_ARTIFACT_VERSION));
            }

        }

        // adds the cglib
        mavenDependencyBuilder.addDependency(SpringDeployerConstants_3.CGLIB_ARTIFACT_NAME,
                getArtifactVersion(getConfiguration().getCglibVersion(), SpringDeployerConstants_3.CGLIB_ARTIFACT_VERSION));

        if (getConfiguration().isIncludeSnowdrop()) {
            // adds the snowdrop for testing within JBoss AS
            mavenDependencyBuilder.addDependency(SpringDeployerConstants_3.SNOWDROP_ARTIFACT_NAME,
                    getArtifactVersion(getConfiguration().getSnowdropVersion(),
                            SpringDeployerConstants_3.SNOWDROP_ARTIFACT_VERSION),
                    SpringDeployerConstants_3.SNOWDROP_EXCLUDED_ARTIFACT);
        }

        // returns the resolved files
        return mavenDependencyBuilder.getDependencies();
    }

    /**
     * <p>Spits the semicolon separated list of excluded artifacts.</p>
     *
     * @param excludedArtifacts the semicolon separated list of excluded artifacts
     *
     * @return list of excluded artifacts
     */
    private String[] splitExcludedArtifacts(String excludedArtifacts) {

        if (excludedArtifacts == null) {
            return null;
        }

        return excludedArtifacts.split(";");
    }

    /**
     * <p>Returns the artifact version, if the passed artifact version is null then default is being returned.</p>
     *
     * @param artifactVersion the artifact version
     * @param defaultVersion  the default version to use
     *
     * @return the artifact version
     */
    private String getArtifactVersion(String artifactVersion, String defaultVersion) {

        return artifactVersion != null ? artifactVersion : defaultVersion;
    }
}
