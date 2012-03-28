package org.jboss.arquillian.spring.testsuite.repository.impl;

import org.jboss.arquillian.spring.testsuite.Employee;
import org.jboss.arquillian.spring.testsuite.repository.EmployeeRepository;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Tests the {@link NullEmployeeRepository} class.
 */
@RunWith(Arquillian.class)
public class NullEmployeeRepositoryTestCase {

    @Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class, "spring-test.jar")
                .addClasses(Employee.class,
                        EmployeeRepository.class, DefaultEmployeeRepository.class, NullEmployeeRepository.class)
                .addAsResource(NullEmployeeRepositoryTestCase.class.getResource("/applicationContext.xml"),
                        "applicationContext.xml");
    }

    @Autowired
    @Qualifier("nullEmployeeRepository")
    private EmployeeRepository employeeRepository;

    @Test
    public void testGetEmployees() throws Exception {

        List<Employee> result = employeeRepository.getEmployees();

        assertNull("Null was expected.", result);
    }
}
