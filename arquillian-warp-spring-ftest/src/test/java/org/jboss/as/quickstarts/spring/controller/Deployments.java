/**
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
package org.jboss.as.quickstarts.spring.controller;

import org.jboss.as.quickstarts.spring.model.UserCredentials;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import java.io.File;

/**
 * <p>An utility class responsible for creating the deployments..</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public final class Deployments {

    /**
     * <p>Creates new instance of {@link Deployments}.</p>
     *
     * <p>Private constructor prevents from instantiation outside of this class.</p>
     */
    private Deployments() {
        // empty constructor
    }

    /**
     * <p>Creates new test deployment.</p>
     *
     * @return test deployment
     */
    public static WebArchive createDeployment() {

        File[] libs = Maven.resolver().loadPomFromFile("pom.xml").resolve(
                "org.springframework:spring-webmvc:3.1.1.RELEASE",
                "javax.validation:validation-api:1.0.0.GA",
                "org.hibernate:hibernate-validator:4.1.0.Final").withTransitivity().asFile();

        return ShrinkWrap.create(WebArchive.class, "spring-test.war")
                .addPackage(LoginController.class.getPackage())
                .addPackage(UserCredentials.class.getPackage())
                .addAsWebInfResource("WEB-INF/web.xml", "web.xml")
                .addAsWebInfResource("WEB-INF/welcome-servlet.xml", "welcome-servlet.xml")
                .addAsWebInfResource("WEB-INF/jsp/welcome.jsp", "jsp/welcome.jsp")
                .addAsWebInfResource("WEB-INF/jsp/login.jsp", "jsp/login.jsp")
                .addAsWebInfResource("WEB-INF/jsp/error.jsp", "jsp/error.jsp")
                .addAsLibraries(libs);
    }
}
