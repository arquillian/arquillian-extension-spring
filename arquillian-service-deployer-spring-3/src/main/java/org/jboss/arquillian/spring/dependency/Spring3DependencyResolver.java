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

import org.jboss.arquillian.spring.SpringDeployerConstants_3;
import org.jboss.arquillian.spring.configuration.SpringDeployerConfiguration;

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

        // adds the spring-context dependencies
        mavenDependencyBuilder.addDependency(SpringDeployerConstants_3.SPRING_ARTIFACT_NAME,
                getConfiguration().getSpringVersion(), SpringDeployerConstants_3.SPRING_ARTIFACT_VERSION);

        // adds spring web dependencies
        mavenDependencyBuilder.addDependency(SpringDeployerConstants_3.SPRING_ARTIFACT_WEB_NAME,
                getConfiguration().getSpringVersion(), SpringDeployerConstants_3.SPRING_ARTIFACT_VERSION);

        // adds the cglib
        mavenDependencyBuilder.addDependency(SpringDeployerConstants_3.CGLIB_ARTIFACT_NAME,
                getConfiguration().getCglibVersion(), SpringDeployerConstants_3.CGLIB_ARTIFACT_VERSION);

        if (getConfiguration().isIncludeSnowdrop()) {
            // adds the snowdrop for testing within JBoss AS
            mavenDependencyBuilder.addDependency(SpringDeployerConstants_3.SNOWDROP_ARTIFACT_NAME,
                    getConfiguration().getSnowdropVersion(), SpringDeployerConstants_3.SNOWDROP_ARTIFACT_VERSION,
                    SpringDeployerConstants_3.SNOWDROP_EXCLUDED_ARTIFACT);
        }

        // returns the resolved files
        return mavenDependencyBuilder.getDependencies();
    }
}
