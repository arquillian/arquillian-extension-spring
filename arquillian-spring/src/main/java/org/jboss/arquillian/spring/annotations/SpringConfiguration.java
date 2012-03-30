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
package org.jboss.arquillian.spring.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation used for configuring spring initialization within test.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
@Documented
@Retention(RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface SpringConfiguration {

    /**
     * The locations from where the xml config should be loaded.
     *
     * @return locations of xml configs
     */
    String[] value() default {};

    /**
     * The locations from where the xml config should be loaded.
     *
     * @return locations of xml configs
     */
    String[] locations() default {};

    /**
     * The classes annotated with {@link @Configuration} to be loaded.
     *
     * @return classes annotated with {@link @Configuration}
     */
    Class<?>[] classes() default {};

    /**
     * The packages that will scanned for {@link @Configuration} annotated classes.
     *
     * @return names of packages
     */
    String[] packages() default {};
}
