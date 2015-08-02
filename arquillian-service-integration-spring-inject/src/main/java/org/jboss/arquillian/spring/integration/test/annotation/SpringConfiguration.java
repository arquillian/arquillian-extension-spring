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

package org.jboss.arquillian.spring.integration.test.annotation;

import org.springframework.context.ApplicationContext;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>Defines the locations from where the xml config should be loaded.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@Inherited
public @interface SpringConfiguration {

    /**
     * <p>The locations from where the xml config should be loaded.</p>
     *
     * <p>If no location is specified the test extension will try to load the configuration from
     * {testClassName}-context.xml file</p>
     */
    String[] value() default {};

    /**
     * <p>The custom context class to be used when instantiating the application context.</p>
     */
    Class<? extends ApplicationContext> contextClass() default org.springframework.context.ApplicationContext.class;
}
