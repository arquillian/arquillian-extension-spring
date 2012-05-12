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

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * <p>Tests {@link SpringExtensionRemoteConfigurationUtils} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringExtensionRemoteConfigurationUtilsTestCase {

    /**
     * <p>Tests {@link SpringExtensionRemoteConfigurationUtils#toString(SpringExtensionRemoteConfiguration)}
     * method.</p>
     */
    @Test
    public void testToStringEmpty() {

        SpringExtensionRemoteConfiguration config = new SpringExtensionRemoteConfiguration();

        String result = SpringExtensionRemoteConfigurationUtils.toString(config);

        assertNotNull("The result was null.", result);
    }

    /**
     * <p>Tests {@link SpringExtensionRemoteConfigurationUtils#toString(SpringExtensionRemoteConfiguration)}
     * method.</p>
     */
    @Test
    public void testToString() {

        SpringExtensionRemoteConfiguration config = new SpringExtensionRemoteConfiguration();
        config.setCustomContextClass("customContextClass");
        config.setCustomContextClass("customAnnotatedContextClass");

        String result = SpringExtensionRemoteConfigurationUtils.toString(config);

        assertNotNull("The result was null.", result);
    }

    /**
     * <p>Tests {@link SpringExtensionRemoteConfigurationUtils#loadResource(java.io.InputStream)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testLoadResource() throws Exception {

        SpringExtensionRemoteConfiguration result = SpringExtensionRemoteConfigurationUtils.loadResource(
                new FileInputStream(new File("src/test/resources",
                        "arquillian-spring-remote-configuration.properties")));

        assertNotNull("The result was null.", result);
        assertEquals("The custom context class name is incorrect.", "testCustomContextClass",
                result.getCustomContextClass());
        assertEquals("The custom annotated context class name is incorrect.", "testCustomAnnotatedContextClass",
                result.getCustomAnnotatedContextClass());
    }
}
