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
import org.jboss.arquillian.spring.integration.test.annotation.SpringConfiguration;
import org.jboss.arquillian.spring.testsuite.beans.model.Employee;
import org.jboss.arquillian.spring.testsuite.beans.repository.EmployeeRepository;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * <p>Tests the {@link org.jboss.arquillian.spring.testsuite.beans.repository.impl.JpaEmployeeRepository} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@RunWith(Arquillian.class)
@Transactional(manager = "txManager")
@SpringConfiguration("applicationContext-jpa.xml")
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
     * <p>{@link javax.persistence.EntityManager} instance used by tests.</p>
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * <p>Tears down the test environment.</p>
     */
    @After
    public void tearDown() {

        entityManager.createNativeQuery("delete from Employee").executeUpdate();
    }

    /**
     * <p>Tests the {@link org.jboss.arquillian.spring.testsuite.beans.repository.EmployeeRepository#save(org.jboss.arquillian.spring.testsuite.beans.model.Employee)}
     * method.</p>
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testSave() {

        Employee employee = new Employee();
        employee.setName("Test employee");

        employeeRepository.save(employee);

        employee = new Employee();
        employee.setName("New employee");

        employeeRepository.save(employee);

        List<Employee> result = entityManager.createQuery("from Employee").getResultList();

        assertNotNull("Method returned null list as result.", result);
        assertEquals("Two employees were expected.", 2, result.size());
    }

    /**
     * <p>Tests the {@link org.jboss.arquillian.spring.testsuite.beans.repository.EmployeeRepository#getEmployees()}
     * method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetEmployees() throws Exception {

        runScript(entityManager, "insert.sql");

        List<Employee> result = employeeRepository.getEmployees();

        assertNotNull("Method returned null list as result.", result);
        assertEquals("Two employees were expected.", 2, result.size());
    }

    /**
     * <p>Executes a sql script.</p>
     *
     * @param entityManager the entity manager
     * @param fileName      the file name
     *
     * @throws java.io.IOException if any error occurs
     */
    public static void runScript(EntityManager entityManager, String fileName) throws IOException {

        // retrieves the resource from class path
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);

        BufferedReader inputReader = new BufferedReader(new InputStreamReader(input));

        // loads the entire file
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = inputReader.readLine()) != null) {

            if (!line.startsWith("--")) {
                stringBuilder.append(line);
            }
        }

        // splits the commands by semicolon
        String[] commands = stringBuilder.toString().split(";");
        for (final String command : commands) {

            entityManager.createNativeQuery(command).executeUpdate();
        }
    }
}
