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
 * <p>Defines constants used by this extension.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public final class SpringDeployerConstants {

    /**
     * <p>Creates new instance of {@link SpringDeployerConstants}.</p>
     *
     * <p>Private constructor prevents from instantiation outside this class.</p>
     */
    private SpringDeployerConstants() {
        // empty constructor
    }

    /**
     * <p>Represents the maven POM file name.</p>
     */
    public static final String POM_XML = "pom.xml";

    /**
     * <p>Represents the fully qualified name to {@link org.springframework.context.ApplicationContext} class.</p>
     */
    public static final String APPLICATION_CONTEXT = "org.springframework.context.ApplicationContext";

    /**
     * <p>Represents the extension configuration prefix.</p>
     */
    public static final String EXTENSION_CONFIGURATION_PREFIX = "spring-deployer";

    /**
     * <p>Represents the extension configuration property name.</p>
     */
    public static final String CONFIGURATION_AUTO_PACKAGE = "autoPackage";

    /**
     * <p>Represents the extension configuration property name.</p>
     */
    public static final String CONFIGURATION_SPRING_VERSION = "springVersion";

    /**
     * <p>Represents the extension configuration property name.</p>
     */
    public static final String CONFIGURATION_CGLIB_VERSION = "cglibVersion";

    /**
     * <p>Represents the extension configuration property name.</p>
     */
    public static final String CONFIGURATION_INCLUDE_SNOWDROP = "includeSnowdrop";

    /**
     * <p>Represents the extension configuration property name.</p>
     */
    public static final String CONFIGURATION_SNOWDROP_VERSION = "snowdropVersion";

    /**
     * <p>Represents the precedence of observer initialization class.</p>
     */
    public static final int INIT_PRECEDENCE = 1;

    /**
     * <p>Represents the default precedence of observer class.</p>
     */
    public static final int DEFAULT_PRECEDENCE = 2;
}
