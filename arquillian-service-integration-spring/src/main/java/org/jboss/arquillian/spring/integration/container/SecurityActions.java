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

package org.jboss.arquillian.spring.integration.container;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Defines a set of operations that are meant to be executed within security context.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public final class SecurityActions {

    /**
     * <p>Creates new instance of {@link SecurityActions}.</p>
     *
     * <p>Private constructor prevents from instantiation outside this class.</p>
     */
    private SecurityActions() {
        // empty constructor
    }

    /**
     * <p>Returns whether given class is prevent within the class path.</p>
     *
     * @param name the class name
     *
     * @return true if the given class is present in the class path, false otheriwse
     */
    public static boolean isClassPresent(String name) {
        try {
            ClassLoader classLoader = getThreadContextClassLoader();
            classLoader.loadClass(name);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * <p>Loads the resources using the executing thread class loader.</p>
     *
     * @param resourceName the resource name
     *
     * @return the loaded resource as stream
     */
    public static InputStream getResource(String resourceName) {

        return getThreadContextClassLoader().getResourceAsStream(resourceName);
    }

    /**
     * <p>Loads the class by it's name.</p>
     *
     * @param name the class name
     *
     * @return the loaded class
     */
    public static Class<?> classForName(String name) {

        try {
            ClassLoader classLoader = getThreadContextClassLoader();
            return classLoader.loadClass(name);
        } catch (ClassNotFoundException e) {

            throw new RuntimeException("Could not load class by name " + name, e);
        }
    }

    /**
     * Retrieves the list of methods that were annotated with given annotation.
     *
     * @param source          the class to scan for classes
     * @param annotationClass the annotation
     *
     * @return list of the methods of the given class that were annotated with specified annotation
     */
    public static Method[] getStaticMethodsWithAnnotation(final Class<?> source, final Class<? extends Annotation> annotationClass) {

        return AccessController.doPrivileged(new PrivilegedAction<Method[]>() {
            public Method[] run() {

                List<Method> foundMethods = new ArrayList<Method>();
                Class<?> nextSource = source;
                while (nextSource != Object.class) {
                    for (Method method : nextSource.getDeclaredMethods()) {
                        if (Modifier.isStatic(method.getModifiers()) && method.isAnnotationPresent(annotationClass)) {
                            if (!method.isAccessible()) {
                                method.setAccessible(true);
                            }
                            foundMethods.add(method);
                        }
                    }
                    nextSource = nextSource.getSuperclass();
                }

                return foundMethods.toArray(new Method[foundMethods.size()]);
            }
        });
    }

    /**
     * <p>Retrieves current thread class loader.</p>
     *
     * @return the class loader
     */
    private static ClassLoader getThreadContextClassLoader() {
        return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {

            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
    }
}
