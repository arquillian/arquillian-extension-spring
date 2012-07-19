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

package org.jboss.arquillian.container.spring.embedded;

import org.jboss.arquillian.container.spi.client.protocol.ProtocolDescription;
import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * <p>Tests {@link SpringEmbeddedContainer} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringEmbeddedContainerTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private SpringEmbeddedContainer instance;

    /**
     * <p>Represents the thread class loader.</p>
     */
    private ClassLoader threadClassLoader;

    /**
     * <p>Sets up the test environment.</p>
     *
     * @throws Exception if any error occurs
     */
    @Before
    public void setUp() throws Exception {

        threadClassLoader = Thread.currentThread().getContextClassLoader();

        instance = new SpringEmbeddedContainer();

        InstanceProducer<ContextClassLoaderManager> mockClassLoaderManagerProducer = mock(InstanceProducer.class);
        TestReflectionHelper.setFieldValue(instance, "contextClassLoaderManager", mockClassLoaderManagerProducer);

        InstanceProducer<SpringEmbeddedConfiguration> mockConfigurationProducer = mock(InstanceProducer.class);
        TestReflectionHelper.setFieldValue(instance, "configuration", mockConfigurationProducer);
    }

    /**
     * <p>Tears down the test environment.</p>
     */
    @After
    public void tearDown() {

        Thread.currentThread().setContextClassLoader(threadClassLoader);
    }

    /**
     * <p>Tests {@link SpringEmbeddedContainer#SpringEmbeddedContainer()} constructor.</p>
     */
    @Test
    public void testCtor() {

        instance = new SpringEmbeddedContainer();
    }

    /**
     * <p>Tests {@link SpringEmbeddedContainer#getConfigurationClass()} method.</p>
     */
    @Test
    public void testGetConfigurationClass() {

        assertEquals("Method returned invalid type.", SpringEmbeddedConfiguration.class,
                instance.getConfigurationClass());
    }

    /**
     * <p>Tests {@link SpringEmbeddedContainer#setup(SpringEmbeddedConfiguration)} method.</p>
     */
    @Test
    public void testSetup() {

        instance.setup(null);
    }

    /**
     * <p>Tests {@link SpringEmbeddedContainer#start()} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testStart() throws Exception {

        instance.start();
    }

    /**
     * <p>Tests {@link SpringEmbeddedContainer#stop()} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testStop() throws Exception {

        instance.stop();
    }

    /**
     * <p>Tests {@link SpringEmbeddedContainer#getDefaultProtocol()} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetDefaultProtocol() throws Exception {

        ProtocolDescription protocolDescription = instance.getDefaultProtocol();

        assertNotNull("Method returned null result.", protocolDescription);
        assertEquals("The returned protocol has invalid name.", "Local", protocolDescription.getName());
    }

    /**
     * <p>Tests {@link SpringEmbeddedContainer#deploy(Archive)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testDeployArchive() throws Exception {
        Archive archive = ShrinkWrap.create(JavaArchive.class);

        ProtocolMetaData protocolMetaData = instance.deploy(archive);

        assertNotNull("Method returned null result.", protocolMetaData);
    }

    /**
     * <p>Tests {@link SpringEmbeddedContainer#undeploy(Archive)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testUndeployArchive() throws Exception {
        Archive archive = ShrinkWrap.create(JavaArchive.class);

        instance.undeploy(archive);
    }

    /**
     * <p>Tests {@link SpringEmbeddedContainer#deploy(Descriptor)} method.</p>
     *
     * <p>{@link UnsupportedOperationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testDeployDescriptor() throws Exception {

        Descriptor descriptor = mock(Descriptor.class);

        instance.deploy(descriptor);
    }

    /**
     * <p>Tests {@link SpringEmbeddedContainer#undeploy(Descriptor)} method.</p>
     *
     * <p>{@link UnsupportedOperationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testUndeployDescriptor() throws Exception {

        Descriptor descriptor = mock(Descriptor.class);

        instance.undeploy(descriptor);
    }
}
