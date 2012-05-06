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

import org.jboss.arquillian.spring.configuration.SpringExtensionConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

/**
 * <p>Tests {@link Spring3DependencyResolver} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class Spring3DependencyResolverTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private Spring3DependencyResolver instance;

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        SpringExtensionConfiguration springExtensionConfiguration = new SpringExtensionConfiguration();
        springExtensionConfiguration.setAutoPackaging(true);
        springExtensionConfiguration.setSpringVersion("3.1.1.RELEASE");
        springExtensionConfiguration.setCglibVersion("2.2.2");
        springExtensionConfiguration.setIncludeSnowdrop(true);

        instance = new Spring3DependencyResolver(springExtensionConfiguration);
    }

    /**
     * <p>Tests {@link Spring3DependencyResolver#resolveDependencies()} method.</p>
     */
    @Test
    public void testResolveDependencies() {

        boolean isSpringPresent = false;
        boolean isSpringWebPresent = false;
        boolean isCglibPresent = false;
        boolean isSnowdropPresent = false;

        File[] files = instance.resolveDependencies();

        for (File file : files) {
            String path = file.getAbsolutePath();

            if (path.contains("spring-context")) {

                isSpringPresent = true;
            } else if (path.contains("spring-web")) {

                isSpringWebPresent = true;
            } else if (path.contains("cglib")) {

                isCglibPresent = true;
            } else if (path.contains("snowdrop-vfs")) {

                isSnowdropPresent = true;
            }
        }

        assertTrue("Required dependencies is missing: spring-context.", isSpringPresent);
        assertTrue("Required dependencies is missing: spring-web.", isSpringWebPresent);
        assertTrue("Required dependencies is missing: cglib.", isCglibPresent);
        assertTrue("Required dependencies is missing: snowdrop.", isSnowdropPresent);
    }
}
