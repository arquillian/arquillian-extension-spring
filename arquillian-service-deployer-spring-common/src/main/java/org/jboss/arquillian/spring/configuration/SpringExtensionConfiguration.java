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
 * <p>The Spring extension configuration</p>
 *
 * <p>Stores the settings for this component.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class SpringExtensionConfiguration {

    /**
     * <p>Represents the Spring Framework artifact version.</p>
     */
    private String springVersion;

    /**
     * <p>Represents the CGLIB version.</p>
     */
    private String cglibVersion;

    /**
     * <p>Represents the Snwodrop version.</p>
     */
    private String snowdropVersion;

    /**
     * <p>Represents the name of the custom context class.</p>
     */
    private String customContextClass;

    /**
     * <p>Represents the name of the custom annotation context class.</p>
     */
    private String customAnnotationContextClass;

    /**
     * <p>Represents whether to include the Snowdrop.</p>
     */
    private boolean includeSnowdrop;

    /**
     * <p>Represents whether to auto package the dependencies.</p>
     *
     * <p>Default is {@code true}.</p>
     */
    private boolean autoPackaging = true;

    /**
     * <p>Creates new instance of {@link SpringExtensionConfiguration} class.</p>
     */
    public SpringExtensionConfiguration() {
        // empty constructor
    }

    /**
     * <p>Retrieves the spring artifact version.</p>
     *
     * @return the spring artifact version
     */
    public String getSpringVersion() {
        return springVersion;
    }

    /**
     * <p>Sets the spring artifact version.</p>
     *
     * @param springVersion the spring artifact version
     */
    public void setSpringVersion(String springVersion) {
        this.springVersion = springVersion;
    }

    /**
     * <p>Retrieves the cglib artifact version.</p>
     *
     * @return the cglib artifact version
     */
    public String getCglibVersion() {
        return cglibVersion;
    }

    /**
     * <p>Sets the cglib artifact version.</p>
     *
     * @param cglibVersion the cglib artifact version
     */
    public void setCglibVersion(String cglibVersion) {
        this.cglibVersion = cglibVersion;
    }

    /**
     * <p>Retrieves the snowdrop version.</p>
     *
     * @return the snowdrop version
     */
    public String getSnowdropVersion() {
        return snowdropVersion;
    }

    /**
     * <p>Sets the snowdrop version.</p>
     *
     * @param snowdropVersion the snowdrop version
     */
    public void setSnowdropVersion(String snowdropVersion) {
        this.snowdropVersion = snowdropVersion;
    }

    /**
     * <p>Retrieves whether to include snowdrop.</p>
     *
     * @return whether to include snowdrop
     */
    public boolean isIncludeSnowdrop() {
        return includeSnowdrop;
    }

    /**
     * <p>Sets whether to include snowdrop.</p>
     *
     * @param includeSnowdrop whether to include snowdrop
     */
    public void setIncludeSnowdrop(boolean includeSnowdrop) {
        this.includeSnowdrop = includeSnowdrop;
    }

    /**
     * <p>Retrieves the name of the custom context class.</p>
     *
     * @return the name of the custom context class
     */
    public String getCustomContextClass() {
        return customContextClass;
    }

    /**
     * <p>Sets the name of the custom context class.</p>
     *
     * @param customContextClass the name of the custom context cla
     */
    public void setCustomContextClass(String customContextClass) {
        this.customContextClass = customContextClass;
    }

    /**
     * <p>Retrieves the custom annotation context class.</p>
     *
     * @return custom annotation context class
     */
    public String getCustomAnnotationContextClass() {
        return customAnnotationContextClass;
    }

    /**
     * <p>Sets custom annotation context class.</p>
     *
     * @param customAnnotationContextClass custom annotation context class
     */
    public void setCustomAnnotationContextClass(String customAnnotationContextClass) {
        this.customAnnotationContextClass = customAnnotationContextClass;
    }

    /**
     * <p>Retrieves whether to auto package the dependencies.</p>
     *
     * <p>Default is {@code true}.</p>
     *
     * @return whether to auto package the dependencies
     */
    public boolean isAutoPackaging() {
        return autoPackaging;
    }

    /**
     * <p>Sets whether to auto package the dependencies.</p>
     *
     * <p>Default is {@code true}.</p>
     *
     * @param autoPackaging whether to auto package the dependencies
     */
    public void setAutoPackaging(boolean autoPackaging) {
        this.autoPackaging = autoPackaging;
    }
}
