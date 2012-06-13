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

package org.jboss.arquillian.spring.integration.configuration;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * <p>Tests {@link SpringIntegrationConfigurationExporter} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringIntegrationConfigurationExporterTestCase {

    /**
     * <p>Tests {@link SpringIntegrationConfigurationExporter#toString(SpringIntegrationConfiguration)} method.</p>
     */
    @Test
    public void testToStringEmpty() {

        SpringIntegrationConfiguration config = new SpringIntegrationConfiguration(
                Collections.<String, String>emptyMap());

        String result = SpringIntegrationConfigurationExporter.toString(config);

        assertNotNull("The result was null.", result);
    }

    /**
     * <p>Tests {@link SpringIntegrationConfigurationExporter#toString(SpringIntegrationConfiguration)} method.</p>
     */
    @Test
    public void testToString() {

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("customContextClass", "customContextClass");
        properties.put("customAnnotationContextClass", "customAnnotationContextClass");

        String result = SpringIntegrationConfigurationExporter.toString(new SpringIntegrationConfiguration(properties));

        assertNotNull("The result was null.", result);
    }

    /**
     * <p>Tests {@link SpringIntegrationConfigurationExporter#loadResource(java.io.InputStream)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testLoadResource() throws Exception {

        SpringIntegrationConfiguration result = SpringIntegrationConfigurationExporter.loadResource(
                new FileInputStream(new File("src/test/resources",
                        "arquillian-spring-remote-configuration.properties")));

        assertNotNull("The result was null.", result);
        assertEquals("The custom context class name is incorrect.", "testCustomContextClass",
                result.getProperty("customContextClass"));
        assertEquals("The custom annotation context class name is incorrect.", "testCustomAnnotationContextClass",
                result.getProperty("customAnnotationContextClass"));
    }
}
