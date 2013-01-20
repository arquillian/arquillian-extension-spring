/**
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
package org.jboss.arquillian.warp.extension.spring.extension;

import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.warp.extension.spring.SpringMvcResource;
import org.jboss.arquillian.warp.extension.spring.SpringMvcResult;
import org.jboss.arquillian.warp.extension.spring.container.Commons;
import org.jboss.arquillian.warp.extension.spring.container.SpringMvcResultImpl;
import org.jboss.arquillian.warp.extension.spring.container.SpringWarpRemoteExtension;
import org.jboss.arquillian.warp.extension.spring.container.SpringWarpTestEnricher;
import org.jboss.arquillian.warp.extension.spring.utils.TestResourceHelper;
import org.jboss.arquillian.warp.spi.WarpDeploymentEnrichmentExtension;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * <p>Tests {@link SpringWarpExtension} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringWarpExtensionTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private SpringWarpExtension instance;


    /**
     * <p>Represents the list of required classes.</p>
     */
    private final static List<Class<?>> REQUIRED_CLASSES = Arrays.asList(Commons.class,
            SpringMvcResultImpl.class, SpringWarpRemoteExtension.class, SpringWarpTestEnricher.class,
            SpringMvcResource.class, SpringMvcResult.class);

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        instance = new SpringWarpExtension();
    }

    /**
     * <p>Tests the {@link SpringWarpExtension#register(LoadableExtension.ExtensionBuilder)} method.</p>
     */
    @Test
    public void testRegister() {

        LoadableExtension.ExtensionBuilder mockExtensionBuilder = mock(LoadableExtension.ExtensionBuilder.class);

        instance.register(mockExtensionBuilder);

        verify(mockExtensionBuilder).service(WarpDeploymentEnrichmentExtension.class, SpringWarpExtension.class);
        verifyNoMoreInteractions(mockExtensionBuilder);
    }

    /**
     * <p>Tests the {@link SpringWarpExtension#getEnrichmentLibrary()} method.</p>
     */
    @Test
    public void testGetEnrichmentLibrary() {

        Archive archive = instance.getEnrichmentLibrary();

        assertNotNull("Method returned null.", archive);

        for (Class c : REQUIRED_CLASSES) {

            assertTrue("The required type is missing: " + c.getName(),
                    archive.contains(TestResourceHelper.getClassResourcePath(c)));
        }
    }
}
