/**
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
package org.jboss.arquillian.warp.extension.spring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>Marks that the given annotated field represents a DispatcherServlet state and that needs to be injected into test
 * case.</p>
 *
 * <p>Supported field types are: {@link SpringMvcResult}, {@link org.springframework.web.servlet.ModelAndView}, {@link
 * Exception}, array of {@link org.springframework.web.servlet.HandlerInterceptor} and {@link
 * org.springframework.validation.Errors}.</p>
 *
 * <p>It is also possible to inject handler into the test case, but only if the type of the field declared in the test
 * case is exactly the same as the type of handler that was intercepted during request processing.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @see SpringMvcResult
 */
@Inherited
@Documented
@Retention(RUNTIME)
@Target(ElementType.FIELD)
public @interface SpringMvcResource {

}
