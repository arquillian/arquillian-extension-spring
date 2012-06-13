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

import java.util.HashMap;
import java.util.Map;

/**
 * <p>The Spring integration configuration</p>
 *
 * <p>Stores the settings for this component.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class SpringIntegrationConfiguration {

    /**
     * <p>Represents properties map.</p>
     */
    private final Map<String, String> properties;

    /**
     * <p>Creates new instance of {@link SpringIntegrationConfiguration} class.</p>
     *
     * @param properties the properties
     */
    public SpringIntegrationConfiguration(Map<String, String> properties) {

        this.properties = properties;
    }

    /**
     * <p>Retrieves the properties.</p>
     *
     * @return the properties
     */
    public Map<String, String> getProperties() {

        // clones of the collection, disallowing modification
        return new HashMap<String, String>(properties);
    }

    /**
     * <p>Returns the property value. If the property with such name does not {@code null} will be returned.</p>
     *
     * @param name the property name
     *
     * @return the value of the property, may be null.
     */
    public String getProperty(String name) {

        return properties.get(name);
    }
}
