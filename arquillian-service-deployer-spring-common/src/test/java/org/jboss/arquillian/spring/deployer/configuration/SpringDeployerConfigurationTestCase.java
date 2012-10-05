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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * <p>Tests {@link SpringDeployerConfiguration} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringDeployerConfigurationTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private SpringDeployerConfiguration instance;

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        instance = new SpringDeployerConfiguration();
    }

    /**
     * <p>Tests {@link SpringDeployerConfiguration#SpringDeployerConfiguration()} constructor.</p>
     */
    @Test
    public void testCtor() {

        instance = new SpringDeployerConfiguration();
    }

    /**
     * <p>Tests both {@link SpringDeployerConfiguration#isAutoPackaging()}
     * and {@link SpringDeployerConfiguration#setAutoPackaging(boolean)} method.</p>
     */
    @Test
    public void testIsAutoPackaging() {

        boolean value = false;

        assertTrue("The default value is incorrect.", instance.isAutoPackaging());

        instance.setAutoPackaging(value);

        assertEquals("Invalid value set.", value, instance.isAutoPackaging());
    }

    /**
     * <p>Tests both {@link SpringDeployerConfiguration#isUseMavenOffline()}
     * and {@link SpringDeployerConfiguration#setUseMavenOffline(boolean)} method.</p>
     */
    @Test
    public void testUseMavenOffline() {

        boolean value = true;

        assertFalse("The default value is incorrect.", instance.isUseMavenOffline());

        instance.setUseMavenOffline(value);

        assertEquals("Invalid value set.", value, instance.isUseMavenOffline());
    }

    /**
     * <p>Tests both {@link org.jboss.arquillian.spring.deployer.configuration.SpringDeployerConfiguration#isIncludeSnowdrop()}
     * and {@link org.jboss.arquillian.spring.deployer.configuration.SpringDeployerConfiguration#setIncludeSnowdrop(boolean)}
     * method.</p>
     */
    @Test
    public void testIsIncludeSnowdrop() {

        boolean value = true;

        assertFalse("The default value is incorrect.", instance.isIncludeSnowdrop());

        instance.setIncludeSnowdrop(value);

        assertEquals("Invalid value set.", value, instance.isIncludeSnowdrop());
    }

    /**
     * <p>Tests both {@link org.jboss.arquillian.spring.deployer.configuration.SpringDeployerConfiguration#getSpringVersion()}
     * and {@link org.jboss.arquillian.spring.deployer.configuration.SpringDeployerConfiguration#setSpringVersion(String)}
     * method.</p>
     */
    @Test
    public void testGetSpringVersion() {

        String version = "2.5.5";

        assertNull("The default value is incorrect.", instance.getSpringVersion());

        instance.setSpringVersion(version);

        assertEquals("Invalid value set.", version, instance.getSpringVersion());
    }

    /**
     * <p>Tests both {@link org.jboss.arquillian.spring.deployer.configuration.SpringDeployerConfiguration#getSpringVersion()}
     * and {@link org.jboss.arquillian.spring.deployer.configuration.SpringDeployerConfiguration#setCglibVersion(String)}
     * method.</p>
     */
    @Test
    public void testGetCglibVersion() {

        String version = "2.2";

        assertNull("The default value is incorrect.", instance.getCglibVersion());

        instance.setCglibVersion(version);

        assertEquals("Invalid value set.", version, instance.getCglibVersion());
    }

    /**
     * <p>Tests both {@link org.jboss.arquillian.spring.deployer.configuration.SpringDeployerConfiguration#getSnowdropVersion()}
     * and {@link org.jboss.arquillian.spring.deployer.configuration.SpringDeployerConfiguration#setSnowdropVersion(String)}
     * method.</p>
     */
    @Test
    public void testGetSnowdropVersion() {

        String version = "2.0.3";

        assertNull("The default value is incorrect.", instance.getSnowdropVersion());

        instance.setSnowdropVersion(version);

        assertEquals("Invalid value set.", version, instance.getSnowdropVersion());
    }
}
