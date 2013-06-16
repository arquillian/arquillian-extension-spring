/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat Middleware LLC, and individual contributors
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

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>Marks the factory method for instantiating and configuring Spring's {@code ApplicationContext} within the test
 * class.</p>
 *
 * <p>The annotated method need to have fallowing signature:
 * <pre>
 *      public static [type] [method name]() {}
 * </pre>
 *
 * Where type can be any type can be casted into the {@code ApplicationContext}. Since the checking is done during the
 * runtime, the constraint can be lowered and the return type can be defined as Object, as long as the actual object
 * returned from the method can be casted into {@code ApplicationContext}.
 *
 * Since the method name is irrelevant, it can be named in any way. Method can also define list of thrown exceptions as
 * needed. </p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
@Inherited
public @interface SpringContextConfiguration {
}
