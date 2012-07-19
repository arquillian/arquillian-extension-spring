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
package org.jboss.arquillian.container.spring.embedded;

import org.jboss.shrinkwrap.api.classloader.ShrinkWrapClassLoader;

import java.io.IOException;

/**
 * <p>Context class loader manager.</p>
 *
 * <p>It 'overwrites' the current {@link ClassLoader} to limit the classpath only to classes specified by the deployed
 * archive..</p>
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class ContextClassLoaderManager {

    /**
     * <p>Instance of {@link ShrinkWrapClassLoader}.</p>
     */
    private ShrinkWrapClassLoader shrinkWrapClassLoader;

    /**
     * <p>Instance of the class loader used in JVM.</p>
     */
    private ClassLoader contextClassLoader;

    /**
     * <p>Creates new instance of {@link ContextClassLoaderManager} with given class loader.</p>
     *
     * @param shrinkWrapClassLoader the shrink wrap class loader
     */
    public ContextClassLoaderManager(ShrinkWrapClassLoader shrinkWrapClassLoader) {

        if (shrinkWrapClassLoader == null) {
            throw new IllegalArgumentException("ShrinkWrapClassLoader must be specified");
        }

        this.shrinkWrapClassLoader = shrinkWrapClassLoader;
    }

    /**
     * <p>Enables the class loader.</p>
     */
    public void enable() {
        this.contextClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(shrinkWrapClassLoader);
    }

    /**
     * <p>Disables the class loader.</p>
     */
    public void disable() {
        Thread.currentThread().setContextClassLoader(contextClassLoader);
        try {
            this.shrinkWrapClassLoader.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not close ShrinkWrapClassLoader", e);
        }
    }
}
