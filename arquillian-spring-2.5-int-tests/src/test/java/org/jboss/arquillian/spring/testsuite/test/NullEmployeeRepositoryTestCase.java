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
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.spring.annotations.SpringConfiguration;
import org.jboss.arquillian.spring.testsuite.beans.model.Employee;
import org.jboss.arquillian.spring.testsuite.beans.repository.EmployeeRepository;
import org.jboss.arquillian.spring.testsuite.beans.repository.impl.NullEmployeeRepository;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.spring.vfs.context.VFSClassPathXmlApplicationContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

import static org.junit.Assert.assertNull;

/**
 * <p>Tests the {@link NullEmployeeRepository} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@RunWith(Arquillian.class)
@SpringConfiguration(value = {"applicationContext.xml"}, contextClass = VFSClassPathXmlApplicationContext.class)
public class NullEmployeeRepositoryTestCase {

    /**
     * <p>Creates the test deployment.</p>
     *
     * @return the test deployment
     */
    @Deployment
    public static JavaArchive createTestArchive() {

        return Deployments.createServicesDeployment();
    }

    /**
     * <p>The injected {@link EmployeeRepository}.</p>
     */
    @Autowired
    @Qualifier("nullEmployeeRepository")
    private EmployeeRepository employeeRepository;

    /**
     * <p>Tests the {@link org.jboss.arquillian.spring.testsuite.beans.service.EmployeeService#getEmployees()}</p>
     */
    @Test
    public void testGetEmployees() throws Exception {

        List<Employee> result = employeeRepository.getEmployees();

        assertNull("Null was expected.", result);
    }
}
