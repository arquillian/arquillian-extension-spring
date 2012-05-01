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
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filter;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * <p>Tests {@link org.jboss.arquillian.spring.client.SpringProtocolArchiveProcessor} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringProtocolArchiveProcessorTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private SpringProtocolArchiveProcessor instance;

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        instance = new SpringProtocolArchiveProcessor();
    }

    /**
     * <p>Tests the {@link org.jboss.arquillian.spring.client.SpringProtocolArchiveProcessor#process(TestDeployment,
     * Archive)} method when the test deployment is a JAR archive.</p>
     */
    @Test
    @Ignore
    public void testProcessJar() {

        JavaArchive deploymentArchive = ShrinkWrap.create(JavaArchive.class, "deployment.jar");
        JavaArchive auxiliaryArchive = ShrinkWrap.create(JavaArchive.class, "auxiliary.jar");
        WebArchive protocolArchive = ShrinkWrap.create(WebArchive.class, "protocol.war");
        List<Archive<?>> auxiliaryArchives = new ArrayList<Archive<?>>();
        auxiliaryArchives.add(auxiliaryArchive);
        TestDeployment testDeployment = new TestDeployment(null, deploymentArchive,
                auxiliaryArchives);

        instance.process(testDeployment, protocolArchive);

        assertDependencies(protocolArchive);
    }

    /**
     * <p>Tests the {@link org.jboss.arquillian.spring.client.SpringProtocolArchiveProcessor#process(TestDeployment,
     * Archive)} method when the test deployment is a WAR archive.</p>
     */
    @Test
    @Ignore
    public void testProcessWar() {

        WebArchive deploymentArchive = ShrinkWrap.create(WebArchive.class, "deployment.war");
        JavaArchive auxiliaryArchive = ShrinkWrap.create(JavaArchive.class, "auxiliary.jar");
        WebArchive protocolArchive = ShrinkWrap.create(WebArchive.class, "protocol.war");
        List<Archive<?>> auxiliaryArchives = new ArrayList<Archive<?>>();
        auxiliaryArchives.add(auxiliaryArchive);
        TestDeployment testDeployment = new TestDeployment(null, deploymentArchive,
                auxiliaryArchives);

        instance.process(testDeployment, protocolArchive);

        assertDependencies(deploymentArchive);
    }

    /**
     * <p>Checks if all requirement dependencies are present.</p>
     *
     * @param archive the archive
     */
    private void assertDependencies(WebArchive archive) {

        boolean isSpringPresent = false;
        boolean isSpringWebPresent = false;
        boolean isCglibPresent = false;

        Map<ArchivePath, Node> contentMap = archive.getContent(new Filter<ArchivePath>() {
            public boolean include(ArchivePath object) {
                return object.get().startsWith("/WEB-INF/lib");
            }
        });

        for (ArchivePath key : contentMap.keySet()) {

            if (key.get().contains("/spring-context")) {

                isSpringPresent = true;
            } else if (key.get().contains("/spring-web")) {

                isSpringWebPresent = true;
            } else if (key.get().contains("/cglib")) {

                isCglibPresent = true;
            }
        }

        assertTrue("Required dependencies is missing: spring-context.", isSpringPresent);
        assertTrue("Required dependencies is missing: spring-web.", isSpringWebPresent);
        assertTrue("Required dependencies is missing: cglib.", isCglibPresent);
    }
}
