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
package org.jboss.arquillian.spring.integration.inject.utils;

import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ArchivePaths;

/**
 * <p>A helper class used for testing.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public final class TestResourceHelper {

    /**
     * <p>Creates new instance of {@link TestResourceHelper} class.</p>
     *
     * <p>Private constructor prevents from instantiation outside this class.</p>
     */
    private TestResourceHelper() {
        // empty constructor
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
