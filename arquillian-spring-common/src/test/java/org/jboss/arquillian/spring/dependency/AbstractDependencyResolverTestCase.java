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
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * <p>Tests {@link AbstractDependencyResolver} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class AbstractDependencyResolverTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private AbstractDependencyResolver instance;

    /**
     * <p>Tests the {@link AbstractDependencyResolver# method.</p>
     */
    @Test
    public void testCtor() throws Exception {

        SpringExtensionConfiguration extensionConfiguration = new SpringExtensionConfiguration();

        instance = new MockDependencyResolver(extensionConfiguration);
    }

    /**
     * <p>Tests the {@link AbstractDependencyResolver#getConfiguration()}  method.</p>
     */
    @Test
    public void testGetConfiguration() throws Exception {

        SpringExtensionConfiguration extensionConfiguration = new SpringExtensionConfiguration();

        instance = new MockDependencyResolver(extensionConfiguration);

        assertEquals("Invalid result has been returned.", extensionConfiguration, instance.getConfiguration());
    }

    /**
     * <p>Tests the {@link AbstractDependencyResolver#getConfiguration()}  method.</p>
     */
    @Test
    public void testGetConfigurationNull() throws Exception {

        instance = new MockDependencyResolver(null);

        assertNull("Null was suppose to be returned.", instance.getConfiguration());
    }

    /**
     * <p>Mock implementation of {@link AbstractDependencyResolver} used for testing.</p>
     *
     * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
     */
    private static final class MockDependencyResolver extends AbstractDependencyResolver {

        /**
         * <p>Creates new instance of {@link AbstractDependencyResolver} class with given configuration.</p>
         *
         * @param configuration the extension configuration
         */
        protected MockDependencyResolver(SpringExtensionConfiguration configuration) {
            super(configuration);
        }

        /**
         * <p>Mock implementation, does nothing.</p>
         *
         * @return returns null
         */
        @Override
        public File[] resolveDependencies() {
            return null;
        }
    }
}
