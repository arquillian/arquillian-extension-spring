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
package org.jboss.arquillian.spring.configuration;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * <p>Tests {@link SpringExtensionRemoteConfiguration} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringExtensionRemoteConfigurationTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private SpringExtensionRemoteConfiguration instance;

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        instance = new SpringExtensionRemoteConfiguration();
    }

    /**
     * <p>Tests {@link SpringExtensionRemoteConfiguration#SpringExtensionRemoteConfiguration()} constructor.</p>
     */
    @Test
    public void testCtor() {

        instance = new SpringExtensionRemoteConfiguration();
    }

    /**
     * <p>Tests both {@link SpringExtensionRemoteConfiguration#getCustomContextClass()} and {@link
     * SpringExtensionRemoteConfiguration#setCustomContextClass(String)} method.</p>
     */
    @Test
    public void testGetCustomContextClass() {

        String className = "org.springframework.context.ApplicationContext";

        assertNull("The default value is incorrect.", instance.getCustomContextClass());

        instance.setCustomContextClass(className);

        assertEquals("Invalid value set.", className, instance.getCustomContextClass());
    }

    /**
     * <p>Tests both {@link SpringExtensionRemoteConfiguration#getCustomAnnotationContextClass()} and {@link
     * SpringExtensionRemoteConfiguration#setCustomAnnotationContextClass(String)} method.</p>
     */
    @Test
    public void testGetCustomAnnotationContextClass() {

        String className = "org.springframework.context.annotation.AnnotationConfigApplicationContext";

        assertNull("The default value is incorrect.", instance.getCustomAnnotationContextClass());

        instance.setCustomAnnotationContextClass(className);

        assertEquals("Invalid value set.", className, instance.getCustomAnnotationContextClass());
    }
}
