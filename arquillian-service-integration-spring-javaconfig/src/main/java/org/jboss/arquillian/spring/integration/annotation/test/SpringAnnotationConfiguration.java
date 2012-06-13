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

package org.jboss.arquillian.spring.integration.annotation.test;

import org.springframework.context.ApplicationContext;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>Defines classes and packages to be scan for configured {@link ApplicationContext}.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@Inherited
public @interface SpringAnnotationConfiguration {

    /**
     * <p>The annotated class that should be loaded by the application context.</p>
     */
    Class<?>[] classes() default {};

    /**
     * <p>The packages that will scanned for annotated classes.</p>
     */
    String[] packages() default {};

    /**
     * <p>The custom context class to be used when instantiating the application context.</p>
     */
    Class<? extends ApplicationContext> contextClass() default ApplicationContext.class;
}
