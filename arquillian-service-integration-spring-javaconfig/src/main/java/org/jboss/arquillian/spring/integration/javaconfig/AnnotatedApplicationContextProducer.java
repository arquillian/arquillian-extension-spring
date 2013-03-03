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
package org.jboss.arquillian.spring.integration.javaconfig;

import org.jboss.arquillian.test.spi.TestClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * This class is responsible for creation of AnnotationConfigApplicationContext context.
 *
 * @author <a href="mailto:kurlenda@gmail.com">Jakub Kurlenda</a>
 *
 */
public class AnnotatedApplicationContextProducer {

    /**
     * <p>Creates the instance of {@link org.springframework.context.annotation.AnnotationConfigApplicationContext}.</p>
     *
     * @param testClass the test class
     * @param classes   the annotated classes to register
     * @param packages  the packages containing the annotated classes
     *
     * @return the created instance of {@link org.springframework.context.annotation.AnnotationConfigApplicationContext}
     */
    public ApplicationContext createAnnotatedApplicationContext(TestClass testClass, String[] packages, Class<?>[] classes) {

        if (packages.length > 0 || classes.length > 0) {

            return createAnnotatedApplicationContext(classes, packages);
        }

        throw new RuntimeException("The test: " + testClass.getName()
                + " annotated with SpringAnnotationConfiguration must specify the configuration classes or packages.");
    }

    /**
     * <p>Creates instance of {@link org.springframework.context.annotation.AnnotationConfigApplicationContext} class.</p>
     *
     * @param classes  the annotated classes to register
     * @param packages the packages containing the annotated classes
     *
     * @return the created instance of {@link org.springframework.context.annotation.AnnotationConfigApplicationContext}
     */
    private ApplicationContext createAnnotatedApplicationContext(Class<?>[] classes, String[] packages) {

        if (classes.length > 0) {
            AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(classes);

            if (packages.length > 0) {
                applicationContext.scan(packages);
                applicationContext.refresh();
            }

            return applicationContext;

        } else {

            return new AnnotationConfigApplicationContext(packages);
        }
    }
}
