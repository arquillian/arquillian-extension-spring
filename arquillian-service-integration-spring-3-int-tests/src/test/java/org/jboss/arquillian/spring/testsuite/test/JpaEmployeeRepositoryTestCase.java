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
package org.jboss.arquillian.spring.testsuite.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.*;
import org.jboss.arquillian.spring.integration.test.annotation.SpringConfiguration;
import org.jboss.arquillian.spring.testsuite.beans.model.Employee;
import org.jboss.arquillian.spring.testsuite.beans.repository.EmployeeRepository;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * <p>Tests the {@link org.jboss.arquillian.spring.testsuite.beans.repository.impl.JpaEmployeeRepository} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@Ignore
@RunWith(Arquillian.class)
@Transactional(manager = "txManager")
@SpringConfiguration("applicationContext-jpa.xml")
@DataSource("springDataSource")
public class JpaEmployeeRepositoryTestCase {

    /**
     * <p>Creates the test deployment.</p>
     *
     * @return the test deployment
     */
    @Deployment
    public static Archive createTestArchive() {

        return Deployments.createJpaDeployment();
    }

    /**
     * <p>The injected {@link org.jboss.arquillian.spring.testsuite.beans.repository.EmployeeRepository}.</p>
     */
    @Autowired
    @Qualifier("jpaEmployeeRepository")
    private EmployeeRepository employeeRepository;

    /**
     * <p>Tests the {@link org.jboss.arquillian.spring.testsuite.beans.repository.EmployeeRepository#save(org.jboss.arquillian.spring.testsuite.beans.model.Employee)}
     * method.</p>
     */
    @Test
    @ShouldMatchDataSet(value = "employee.yml", excludeColumns = "id")
    public void testSave() {

        Employee employee = new Employee();
        employee.setName("Test employee");

        employeeRepository.save(employee);

        employee = new Employee();
        employee.setName("New employee");

        employeeRepository.save(employee);
    }

    /**
     * <p>Tests the {@link org.jboss.arquillian.spring.testsuite.beans.repository.EmployeeRepository#getEmployees()}
     * method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    @UsingDataSet("employee.yml")
    @Cleanup(strategy = CleanupStrategy.USED_TABLES_ONLY)
    public void testGetEmployees() throws Exception {

        List<Employee> result = employeeRepository.getEmployees();

        assertNotNull("Method returned null list as result.", result);
        assertEquals("Two employees were expected.", 2, result.size());
    }
}
