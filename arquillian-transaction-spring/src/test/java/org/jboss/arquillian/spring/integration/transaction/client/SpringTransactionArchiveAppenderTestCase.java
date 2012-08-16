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
package org.jboss.arquillian.spring.integration.transaction.client;

import org.jboss.arquillian.transaction.spring.container.SpringTransactionRemoteExtension;
import org.jboss.arquillian.transaction.spring.provider.SpringTransactionProvider;
import org.jboss.arquillian.transaction.spring.client.SpringTransactionArchiveAppender;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * <p>Tests {@link org.jboss.arquillian.transaction.spring.client.SpringTransactionArchiveAppender} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringTransactionArchiveAppenderTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private SpringTransactionArchiveAppender instance;

    /**
     * <p>Represents the list of required classes.</p>
     */
    private final static List<Class<?>> REQUIRED_CLASSES = Arrays.asList(SpringTransactionRemoteExtension.class,
            SpringTransactionProvider.class);

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        instance = new SpringTransactionArchiveAppender();
    }

    /**
     * <p>Tests the {@link SpringTransactionArchiveAppender#createAuxiliaryArchive()} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testCreateAuxiliaryArchive() throws Exception {

        Archive archive = instance.createAuxiliaryArchive();

        assertNotNull("Method returned null.", archive);
        assertTrue("The returned archive has incorrect type.", archive instanceof JavaArchive);

        for (Class c : REQUIRED_CLASSES) {

            assertTrue("The required type is missing: " + c.getName(),
                    archive.contains(getClassResourcePath(c)));
        }
    }

    /**
     * <p>Retrieves the resource name of the give class.</p>
     *
     * @param c the class
     *
     * @return the resource name for the class
     */
    public static ArchivePath getClassResourcePath(Class c) {

        StringBuilder sb = new StringBuilder();
        sb.append("/");
        sb.append(c.getName().replace(".", "/"));
        sb.append(".class");

        return ArchivePaths.create(sb.toString());
    }
}
