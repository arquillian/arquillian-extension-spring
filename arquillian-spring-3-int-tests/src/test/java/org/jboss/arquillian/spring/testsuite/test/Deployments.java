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
package org.jboss.arquillian.spring.testsuite.test;

import org.jboss.arquillian.spring.testsuite.beans.config.AppConfig;
import org.jboss.arquillian.spring.testsuite.beans.config.WebAppConfig;
import org.jboss.arquillian.spring.testsuite.beans.controller.EmployeeController;
import org.jboss.arquillian.spring.testsuite.beans.model.Employee;
import org.jboss.arquillian.spring.testsuite.beans.repository.EmployeeRepository;
import org.jboss.arquillian.spring.testsuite.beans.repository.impl.DefaultEmployeeRepository;
import org.jboss.arquillian.spring.testsuite.beans.repository.impl.NullEmployeeRepository;
import org.jboss.arquillian.spring.testsuite.beans.service.EmployeeService;
import org.jboss.arquillian.spring.testsuite.beans.service.impl.DefaultEmployeeService;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

import java.io.File;

/**
 * <p>Deployments helper class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class Deployments {

    /**
     * <p>Creates new instance of {@link Deployments} class.</p>
     *
     * <p>Private constructor prevents from instantiation.</p>
     */
    private Deployments() {
        // empty constructor
    }

    /**
     * <p>Creates the test deployment.</p>
     *
     * @return the test deployment
     */
    public static JavaArchive createRepositoriesDeployment() {

        return createAppDeployment().
                addAsResource("applicationContext.xml");
    }

    /**
     * <p>Creates the test deployment.</p>
     *
     * @return the test deployment
     */
    public static JavaArchive createServicesDeployment() {

        return createAppDeployment()
                .addAsResource("applicationContext.xml");
    }

    /**
     * <p>Creates the test deployment.</p>
     *
     * @return the test deployment
     */
    public static JavaArchive createJavaConfigDeployment() {

        return createAppDeployment().addClasses(AppConfig.class);
    }

    /**
     * <p>Creates the test deployment.</p>
     *
     * @return the test deployment
     */
    public static WebArchive createWebApplication() {

        return ShrinkWrap.create(WebArchive.class, "spring-test.war")
                .addClasses(Employee.class,
                        EmployeeService.class, DefaultEmployeeService.class,
                        EmployeeRepository.class, DefaultEmployeeRepository.class, NullEmployeeRepository.class,
                        EmployeeController.class)
                .addAsLibraries(springDependencies())
                .addAsLibraries(mockitoDependencies());
    }

    /**
     * <p>Creates the test deployment.</p>
     *
     * @return the test deployment
     */
    public static WebArchive createWeb3Application() {

        return ShrinkWrap.create(WebArchive.class, "spring-test.war")
                .addClasses(Employee.class,
                        EmployeeService.class, DefaultEmployeeService.class,
                        EmployeeRepository.class, DefaultEmployeeRepository.class, NullEmployeeRepository.class,
                        EmployeeController.class, WebAppConfig.class)
                .addAsLibraries(springDependencies())
                .addAsLibraries(mockitoDependencies());
    }

    /**
     * <p>Creates the test deployment.</p>
     *
     * @return the test deployment
     */
    public static JavaArchive createAppDeployment() {

        return ShrinkWrap.create(JavaArchive.class, "spring-test.jar")
                .addClasses(Employee.class,
                        EmployeeService.class, DefaultEmployeeService.class,
                        EmployeeRepository.class, DefaultEmployeeRepository.class, NullEmployeeRepository.class);
    }

    /**
     * <p>Retrieves spring mvc dependencies.</p>
     *
     * @return spring mvc dependencies
     */
    public static File[] springDependencies() {
        return resolveArtifact("org.springframework:spring-webmvc");
    }

    /**
     * <p>Retrieves mockito dependencies.</p>
     *
     * @return mockito dependencies
     */
    public static File[] mockitoDependencies() {
        return resolveArtifact("org.mockito:mockito-all");
    }

    /**
     * <p>Resolves the given artifact by it's name with help of maven build system.</p>
     *
     * @param artifact the fully qualified artifact name
     *
     * @return the resolved files
     */
    public static File[] resolveArtifact(String artifact) {
        MavenDependencyResolver mvnResolver = DependencyResolvers.use(MavenDependencyResolver.class);

        mvnResolver.loadMetadataFromPom("pom.xml");

        return mvnResolver.artifacts(artifact).resolveAsFiles();
    }
}
