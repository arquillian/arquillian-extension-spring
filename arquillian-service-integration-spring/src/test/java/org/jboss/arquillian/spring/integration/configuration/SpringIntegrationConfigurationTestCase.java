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

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * <p>Tests {@link SpringIntegrationConfiguration} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringIntegrationConfigurationTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private SpringIntegrationConfiguration instance;

    /**
     * <p>Tests {@link SpringIntegrationConfiguration#SpringIntegrationConfiguration(Map)} constructor.</p>
     */
    @Test
    public void testCtor() {
        Map<String, String> properties = new HashMap<String, String>();

        instance = new SpringIntegrationConfiguration(properties);
    }

    /**
     * <p>Tests both {@link SpringIntegrationConfiguration#getProperty(String)} method.</p>
     */
    @Test
    public void testGetProperty() {

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("key 1", "value 1");

        instance = new SpringIntegrationConfiguration(properties);

        assertEquals("Invalid property value.", "value 1", instance.getProperty("key 1"));
    }
}
