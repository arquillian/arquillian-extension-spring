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

import org.jboss.arquillian.spring.configuration.SpringDeployerConfiguration;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * <p>Tests {@link Spring25DependencyResolver} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class Spring25DependencyResolverTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private Spring25DependencyResolver instance;

    /**
     * <p>Tests {@link Spring25DependencyResolver#resolveDependencies()} method.</p>
     */
    @Test
    public void testResolveDependencies() {

        SpringDeployerConfiguration springDeployerConfiguration = createConfiguration();
        springDeployerConfiguration.setIncludeSnowdrop(false);

        instance = new Spring25DependencyResolver(springDeployerConfiguration);

        File[] files = instance.resolveDependencies();

        assertDependencies(files, true, true, false);
    }

    /**
     * <p>Tests {@link Spring25DependencyResolver#resolveDependencies()} method.</p>
     */
    @Test
    public void testResolveDependenciesIncludeSnowdrop() {

        SpringDeployerConfiguration springDeployerConfiguration = createConfiguration();

        instance = new Spring25DependencyResolver(springDeployerConfiguration);

        File[] files = instance.resolveDependencies();

        assertDependencies(files, true, true, true);
    }

    /**
     * <p>Asserts that all required dependencies are present.</p>
     *
     * @param files                    the resoled dependencies
     * @param springPresentExpected    whether the dependencies should contain spring-context
     * @param springWebPresentExpected whether the dependencies should contain spring-web
     * @param snowdropPresentExpected  whether the dependencies should contain snowdrop
     */
    private void assertDependencies(File[] files, boolean springPresentExpected, boolean springWebPresentExpected,
                                    boolean snowdropPresentExpected) {

        boolean isSpringPresent = false;
        boolean isSpringWebPresent = false;
        boolean isSnowdropPresent = false;

        for (File file : files) {
            String path = file.getAbsolutePath();

            if (path.contains("spring-context")) {

                isSpringPresent = true;
            } else if (path.contains("spring-web")) {

                isSpringWebPresent = true;
            } else if (path.contains("snowdrop-vfs")) {

                isSnowdropPresent = true;
            }
        }

        assertEquals("Required dependencies is missing: spring-context.", springPresentExpected, isSpringPresent);
        assertEquals("Required dependencies is missing: spring-web.", springWebPresentExpected, isSpringWebPresent);
        assertEquals("Required dependencies is missing: snowdrop.", snowdropPresentExpected, isSnowdropPresent);
    }

    /**
     * <p>Creates new instance of {@link SpringDeployerConfiguration}.</p>
     *
     * @return the create instance of {@link SpringDeployerConfiguration}
     */
    private SpringDeployerConfiguration createConfiguration() {
        SpringDeployerConfiguration springDeployerConfiguration = new SpringDeployerConfiguration();
        springDeployerConfiguration.setAutoPackaging(true);
        springDeployerConfiguration.setSpringVersion("3.1.1.RELEASE");
        springDeployerConfiguration.setCglibVersion("2.2.2");
        springDeployerConfiguration.setIncludeSnowdrop(true);
        return springDeployerConfiguration;
    }
}
