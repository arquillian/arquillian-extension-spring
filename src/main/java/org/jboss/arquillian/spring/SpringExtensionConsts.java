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
package org.jboss.arquillian.spring;

/**
 * Aggreagates all consts used by this extension.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public final class SpringExtensionConsts {

    /**
     * <p>Creates new instance of {@link SpringExtensionConsts}.</p>
     *
     * <p>Private constructor prevents from instantiation outside this class.</p>
     */
    private SpringExtensionConsts() {
        // empty constructor
    }

    /**
     * Represents the maven POM file name.
     */
    public static final String POM_XML = "pom.xml";

    /**
     * Represents the fully qualified name to {@link org.springframework.context.ApplicationContext} class.
     */
    public static final String APPLICATION_CONTEXT = "org.springframework.context.ApplicationContext";

    /**
     * Represents the default location where to look for Spring application context.
     */
    public static final String DEFAULT_LOCATION = "classpath:applicationContext.xml";

    /**
     * Represents the Spring Framework maven artifact name.
     */
    public static final String SPRING_ARTIFACT_NAME = "org.springframework:spring-context";

    /**
     * Representst the Spring Framework Web maven artifact name.
     */
    public static final String SPRING_ARTIFACT_WEB = "org.springframework:spring-web";

    /**
     * Represents the default version of the Spring Framework.
     */
    public static final String SPRING_ARTIFACT_VERSION = "3.1.1.RELEASE";

    /**
     * Represents the CGLIB maven artifact name.
     */
    public static final String CGLIB_ARTIFACT_NAME = "cglib:cglib";

    /**
     * Represents the default CGLIB artifact name.
     */
    public static final String CGLIB_ARTIFACT_VERSION = "2.2.2";
}
