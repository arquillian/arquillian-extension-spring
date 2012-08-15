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

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.spring.integration.test.annotation.SpringClientConfiguration;
import org.jboss.arquillian.spring.integration.test.annotation.SpringWebConfiguration;
import org.jboss.arquillian.spring.testsuite.beans.controller.EmployeeController;
import org.jboss.arquillian.spring.testsuite.beans.model.Employee;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * <p>Tests REST service.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@RunWith(Arquillian.class)
@SpringClientConfiguration("applicationContext-rest.xml")
public class ClientRestServiceTestCase {

    /**
     * <p>Creates the test deployment.</p>
     *
     * @return the test deployment
     */
    @Deployment
    @OverProtocol("Servlet 3.0")
    public static Archive createTestArchive() {
        return Deployments.createWebApplication()
                .addAsWebInfResource("mvc/web.xml", "web.xml")
                .addAsWebInfResource("mvc/empty.xml", "employee-servlet.xml")
                .addAsWebInfResource("mvc/mvc-applicationContext.xml", "applicationContext.xml");
    }

    /**
     * <p>The context path of the deployed application.</p>
     */
    @ArquillianResource
    private URL contextPath;

    /**
     * <p>Autowired {@link RestTemplate}.</p>
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * <p>Tests invocation of a REST service.</p>
     */
    @Test
    @RunAsClient
    public void testGetEmployees() {

        Employee result = restTemplate.getForObject(contextPath + "/Employees/1", Employee.class);

        assertNotNull("The returned result from REST service was null.", result);
        assertEquals("The returned employee has invalid name.", "John Smith", result.getName());
    }
}
