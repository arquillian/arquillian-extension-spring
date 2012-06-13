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
package org.jboss.arquillian.warp.extension.spring.container;

/**
 * <p>Defines constants used by this extension.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public final class Commons {

    /**
     * <p>Creates new instance of {@link Commons} class.</p>
     *
     * <p>Private constructor prevents from instantiation outside this class.</p>
     */
    private Commons() {
        // empty constructor
    }

    /**
     * <p>Represents the name of the attribute in which the {@link org.jboss.arquillian.warp.extension.spring.SpringMvcResult}
     * is being stored.</p>
     */
    public static final String SPRING_MVC_RESULT_ATTRIBUTE_NAME =
            "org.jboss.arquillian.warp.extension.spring.SPRING_MVC_RESULT";
}
