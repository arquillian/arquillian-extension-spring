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

import org.jboss.arquillian.container.spi.client.container.ContainerConfiguration;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * <p>Tests {@link SpringEmbeddedConfiguration} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringEmbeddedConfigurationTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private SpringEmbeddedConfiguration instance;

    /**
     * <p>Tests if the {@link SpringEmbeddedConfiguration} implements the {@link ContainerConfiguration} interface.</p>
     */
    @Test
    public void testInterface() {

        assertTrue("Class does not implement required interface.",
                ContainerConfiguration.class.isAssignableFrom(SpringEmbeddedConfiguration.class));
    }

    /**
     * <p>Tests {@link SpringEmbeddedConfiguration#SpringEmbeddedConfiguration()} constructor.</p>
     */
    @Test
    public void testCtor() {

        instance = new SpringEmbeddedConfiguration();
    }

    /**
     * <p>Tests {@link SpringEmbeddedConfiguration#validate()} method.</p>
     */
    @Test
    public void testValidate() {

        instance = new SpringEmbeddedConfiguration();

        instance.validate();
    }
}
