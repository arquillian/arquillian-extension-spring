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
package org.jboss.arquillian.spring.deployer.configuration;

/**
 * <p>The Spring extension configuration.</p>
 *
 * <p>Stores the settings for this component.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class SpringDeployerConfiguration {

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
     * <p>Represents whether cache should be enabled.</p>
     *
     * <p>Default is {@code true}.</p>
     */
    private boolean enableCache = true;

    /**
     * <p>Represents whether the maven should be run in offline mode.</p>
     *
     * <p>Default is {@code false}.</p>
     */
    private boolean useMavenOffline = false;

    /**
     * <p>Represents whether dependencies from maven pom.xml should be imported.</p>
     *
     * <p>Default is {@code true}.</p>
     */
    private boolean importPomDependencies = true;

    /**
     * <p>Represents the path to the pom file.</p>
     *
     * <p>Default is "pom.xml".</p>
     */
    private String pomFile = "pom.xml";

    /**
     * <p>Represents the semicolon separated list of artifacts.</p>
     */
    private String excludedArtifacts;

    /**
     * <p>Creates new instance of {@link SpringDeployerConfiguration} class.</p>
     */
    public SpringDeployerConfiguration() {
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

    /**
     * <p>Retrieves whether the cache should be enabled.</p>
     *
     * @return whether the cache should be enabled
     */
    public boolean isEnableCache() {
        return enableCache;
    }

    /**
     * <p>Sets whether the cache should be enabled.</p>
     *
     * @param enableCache whether the cache should be enabled
     */
    public void setEnableCache(boolean enableCache) {
        this.enableCache = enableCache;
    }

    /**
     * <p>Retrieves whether maven should be run in offline mode.</p>
     *
     * <p>Default is {@code false}.</p>
     *
     * @return whether to run maven in offline mode
     */
    public boolean isUseMavenOffline() {
        return useMavenOffline;
    }

    /**
     * <p>Sets whether maven should be run in offline mode.</p>
     *
     * @param useMavenOffline whether to run maven in offline mode
     */
    public void setUseMavenOffline(boolean useMavenOffline) {
        this.useMavenOffline = useMavenOffline;
    }

    /**
     * <p>Retrieves whether the dependencies should be loaded from maven pom file.</p>
     *
     * @return whether the dependencies should be loaded from maven pom file.
     */
    public boolean isImportPomDependencies() {
        return importPomDependencies;
    }

    /**
     * <p>Sets whether the dependencies should be loaded from maven pom file.</p>
     *
     * @param importPomDependencies whether the dependencies should be loaded from maven pom file.
     */
    public void setImportPomDependencies(boolean importPomDependencies) {
        this.importPomDependencies = importPomDependencies;
    }

    /**
     * <p>Retrieves the path to the pom.xml file.</p>
     *
     * @return the path to the pom.xml file
     */
    public String getPomFile() {
        return pomFile;
    }

    /**
     * <p>Sets the path to the pom.xml file.</p>
     *
     * @param pomFilePath the path to the pom.xml file.
     */
    public void setPomFile(String pomFilePath) {
        this.pomFile = pomFilePath;
    }

    /**
     * <p>Represents the list of semicolon separated artifacts.</p>
     *
     * @return the list of semicolon separated artifacts
     */
    public String getExcludedArtifacts() {
        return excludedArtifacts;
    }

    /**
     * <p>Represents the list of semicolon separated artifacts.</p>
     *
     * @param excludedArtifacts the list of semicolon separated artifacts
     */
    public void setExcludedArtifacts(String excludedArtifacts) {
        this.excludedArtifacts = excludedArtifacts;
    }
}
