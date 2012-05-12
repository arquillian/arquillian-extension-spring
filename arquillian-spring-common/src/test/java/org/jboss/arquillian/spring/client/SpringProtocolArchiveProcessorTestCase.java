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
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.spring.configuration.SpringExtensionConfiguration;
import org.jboss.arquillian.spring.dependency.AbstractDependencyResolver;
import org.jboss.arquillian.spring.dependency.MavenDependencyBuilder;
import org.jboss.arquillian.spring.utils.TestReflectionHelper;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filter;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <p>Tests {@link SpringProtocolArchiveProcessor} class.</p>
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
     * <p>Tests the {@link SpringProtocolArchiveProcessor#process(TestDeployment, Archive)} method when the test
     * deployment is a JAR archive.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testProcessJarAutoPackageTrue() throws Exception {

        injectConfiguration(true);

        JavaArchive deploymentArchive = ShrinkWrap.create(JavaArchive.class, "deployment.jar");
        JavaArchive auxiliaryArchive = ShrinkWrap.create(JavaArchive.class, "auxiliary.jar");
        WebArchive protocolArchive = ShrinkWrap.create(WebArchive.class, "protocol.war");
        List<Archive<?>> auxiliaryArchives = new ArrayList<Archive<?>>();
        auxiliaryArchives.add(auxiliaryArchive);
        TestDeployment testDeployment = new TestDeployment(null, deploymentArchive,
                auxiliaryArchives);

        instance.process(testDeployment, protocolArchive);

        assertDependencies(protocolArchive, true);
    }

    /**
     * <p>Tests the {@link SpringProtocolArchiveProcessor#process(TestDeployment, Archive)} method when the test
     * deployment is a JAR archive.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testProcessJarAutoPackageFalse() throws Exception {

        injectConfiguration(false);

        JavaArchive deploymentArchive = ShrinkWrap.create(JavaArchive.class, "deployment.jar");
        JavaArchive auxiliaryArchive = ShrinkWrap.create(JavaArchive.class, "auxiliary.jar");
        WebArchive protocolArchive = ShrinkWrap.create(WebArchive.class, "protocol.war");
        List<Archive<?>> auxiliaryArchives = new ArrayList<Archive<?>>();
        auxiliaryArchives.add(auxiliaryArchive);
        TestDeployment testDeployment = new TestDeployment(null, deploymentArchive,
                auxiliaryArchives);

        instance.process(testDeployment, protocolArchive);

        assertDependencies(protocolArchive, false);
    }

    /**
     * <p>Tests the {@link SpringProtocolArchiveProcessor#process(TestDeployment, Archive)} method when the test
     * deployment is a WAR archive.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testProcessWarAutoPackageTrue() throws Exception {

        injectConfiguration(true);

        WebArchive deploymentArchive = ShrinkWrap.create(WebArchive.class, "deployment.war");
        JavaArchive auxiliaryArchive = ShrinkWrap.create(JavaArchive.class, "auxiliary.jar");
        WebArchive protocolArchive = ShrinkWrap.create(WebArchive.class, "protocol.war");
        List<Archive<?>> auxiliaryArchives = new ArrayList<Archive<?>>();
        auxiliaryArchives.add(auxiliaryArchive);
        TestDeployment testDeployment = new TestDeployment(null, deploymentArchive,
                auxiliaryArchives);

        instance.process(testDeployment, protocolArchive);

        assertDependencies(deploymentArchive, true);
    }

    /**
     * <p>Tests the {@link SpringProtocolArchiveProcessor#process(TestDeployment, Archive)} method when the test
     * deployment is a WAR archive.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testProcessWarAutoPackageFalse() throws Exception {

        injectConfiguration(false);

        WebArchive deploymentArchive = ShrinkWrap.create(WebArchive.class, "deployment.war");
        JavaArchive auxiliaryArchive = ShrinkWrap.create(JavaArchive.class, "auxiliary.jar");
        WebArchive protocolArchive = ShrinkWrap.create(WebArchive.class, "protocol.war");
        List<Archive<?>> auxiliaryArchives = new ArrayList<Archive<?>>();
        auxiliaryArchives.add(auxiliaryArchive);
        TestDeployment testDeployment = new TestDeployment(null, deploymentArchive,
                auxiliaryArchives);

        instance.process(testDeployment, protocolArchive);

        assertDependencies(deploymentArchive, false);
    }

    /**
     * <p>Injects into the tests instance all the required fields.</p>
     *
     * @param autoPackage will be used as the value return by extension configuration injected into the test instance
     *
     * @throws Exception if any error occurs
     */
    private void injectConfiguration(boolean autoPackage) throws Exception {
        SpringExtensionConfiguration extensionConfiguration = new SpringExtensionConfiguration();
        extensionConfiguration.setAutoPackaging(autoPackage);

        Instance<SpringExtensionConfiguration> mockExtensionConfigurationInstance = mock(Instance.class);
        when(mockExtensionConfigurationInstance.get()).thenReturn(extensionConfiguration);
        TestReflectionHelper.setFieldValue(instance, "configuration", mockExtensionConfigurationInstance);

        MavenDependencyBuilder mavenDependencyBuilder = new MavenDependencyBuilder();
        mavenDependencyBuilder.addDependency("org.springframework:spring-context", "3.1.1.RELEASE", "3.1.1.RELEASE");
        mavenDependencyBuilder.addDependency("org.springframework:spring-web", "3.1.1.RELEASE", "3.1.1.RELEASE");
        mavenDependencyBuilder.addDependency("cglib:cglib", "2.2.2", "2.2.2");
        File[] dependencies = mavenDependencyBuilder.getDependencies();

        AbstractDependencyResolver abstractDependencyResolver = mock(AbstractDependencyResolver.class);
        when(abstractDependencyResolver.resolveDependencies()).thenReturn(dependencies);

        Instance<AbstractDependencyResolver> mockDependencyResolverInstance = mock(Instance.class);
        when(mockDependencyResolverInstance.get()).thenReturn(abstractDependencyResolver);
        TestReflectionHelper.setFieldValue(instance, "dependencyResolver", mockDependencyResolverInstance);
    }

    /**
     * <p>Checks if all requirement dependencies are present.</p>
     *
     * @param archive  the archive
     * @param required whether the dependencies are required
     */
    private void assertDependencies(WebArchive archive, boolean required) {

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

        if (required) {
            assertTrue("Required dependencies is missing: spring-context.", isSpringPresent);
            assertTrue("Required dependencies is missing: spring-web.", isSpringWebPresent);
            assertTrue("Required dependencies is missing: cglib.", isCglibPresent);
        } else {
            assertFalse("Dependencies should not be added: spring-context.", isSpringPresent);
            assertFalse("Dependencies should not be added: spring-web.", isSpringWebPresent);
            assertFalse("Dependencies should not be added: cglib.", isCglibPresent);
        }
    }
}
