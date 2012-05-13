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

/**
 * <p>Represents the extension remote configuration, used in the test deployment.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class SpringExtensionRemoteConfiguration {

    /**
     * <p>Represents the custom context class name.</p>
     */
    private String customContextClass;

    /**
     * <p>Represents the annotation custom context class name.</p>
     */
    private String customAnnotationContextClass;

    /**
     * <p>Creates new instance of {@link SpringExtensionRemoteConfiguration} class.</p>
     */
    public SpringExtensionRemoteConfiguration() {
        // empty constructor
    }

    /**
     * <p>Retrieves the custom context class name.</p>
     *
     * @return the custom context class name
     */
    public String getCustomContextClass() {
        return customContextClass;
    }

    /**
     * <p>Sets the custom context class name.</p>
     *
     * @param customContextClass the custom context class name
     */
    public void setCustomContextClass(String customContextClass) {
        this.customContextClass = customContextClass;
    }

    /**
     * <p>Retrieves the custom annotation context class name.</p>
     *
     * @return the custom annotation context class name
     */
    public String getCustomAnnotationContextClass() {
        return customAnnotationContextClass;
    }

    /**
     * <p>Sets the custom annotation context class name.</p>
     *
     * @param customAnnotationContextClass the custom annotation context class name
     */
    public void setCustomAnnotationContextClass(String customAnnotationContextClass) {
        this.customAnnotationContextClass = customAnnotationContextClass;
    }
}
