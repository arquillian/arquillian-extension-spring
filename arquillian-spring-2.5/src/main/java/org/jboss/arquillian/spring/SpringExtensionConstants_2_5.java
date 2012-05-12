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
public final class SpringExtensionConstants_2_5 {

    /**
     * <p>Creates new instance of {@link SpringExtensionConstants_2_5}.</p>
     *
     * <p>Private constructor prevents from instantiation outside of this class.</p>
     */
    private SpringExtensionConstants_2_5() {
        // empty constructor
    }

    /**
     * <p>Represents the Spring Framework maven artifact name.</p>
     */
    public static final String SPRING_ARTIFACT_NAME = "org.springframework:spring-context";

    /**
     * <p>Represents the Spring Framework Web maven artifact name.</p>
     */
    public static final String SPRING_ARTIFACT_WEB_NAME = "org.springframework:spring-web";

    /**
     * <p>Represents the default version of the Spring Framework.</p>
     */
    public static final String SPRING_ARTIFACT_VERSION = "2.5.6";

    /**
     * <p>Represents the Snowdrop maven artifact name.</p>
     */
    public static final String SNOWDROP_ARTIFACT_NAME = "org.jboss.snowdrop:snowdrop-vfs";

    /**
     * <p>Represents the default version of the Snowdrop.</p>
     */
    public static final String SNOWDROP_ARTIFACT_VERSION = "2.0.3.Final";

    /**
     * <p>Represents the name of the artifacts that will be excluded when resolving Snowdrop.</p>
     */
    public static final String SNOWDROP_EXCLUDED_ARTIFACT = "org.springframework:*";
}
