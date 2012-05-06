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
package org.jboss.arquillian.spring.client;

import org.jboss.arquillian.spring.SpringExtensionConsts;
import org.jboss.arquillian.spring.annotations.SpringConfiguration;
import org.jboss.arquillian.spring.annotations.SpringWebConfiguration;
import org.jboss.arquillian.spring.container.Spring25EnricherRemoteExtension;
import org.jboss.arquillian.spring.context.TestScopeApplicationContext;
import org.jboss.arquillian.spring.context.WebApplicationContextProducer;
import org.jboss.arquillian.spring.context.XmlApplicationContextProducer;
import org.jboss.arquillian.spring.testenricher.SpringInjectionEnricher;
import org.jboss.arquillian.spring.utils.TestResourceHelper;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * <p>Tests {@link Spring25EnricherArchiveAppender} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class Spring25EnricherArchiveAppenderTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private Spring25EnricherArchiveAppender instance;

    /**
     * <p>Represents the list of required classes.</p>
     */
    private List<Class<?>> REQUIRED_CLASSES = Arrays.asList(SpringConfiguration.class, SpringWebConfiguration.class,
            XmlApplicationContextProducer.class, WebApplicationContextProducer.class,
            Spring25EnricherRemoteExtension.class, SpringInjectionEnricher.class,
            TestScopeApplicationContext.class, SpringExtensionConsts.class);

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        instance = new Spring25EnricherArchiveAppender();
    }

    /**
     * <p>Tests the {@link Spring25EnricherArchiveAppender#buildArchive()} method.</p>
     */
    @Test
    public void testBuildArchive() {

        Archive archive = instance.buildArchive();

        assertNotNull("Method returned null.", archive);
        assertTrue("The returned archive has incorrect type.", archive instanceof JavaArchive);

        for (Class c : REQUIRED_CLASSES) {

            assertTrue("The required type is missing: " + c.getName(), archive.contains(
                    TestResourceHelper.getClassResourcePath(c)));
        }
    }
}
