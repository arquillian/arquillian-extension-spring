package org.jboss.arquillian.spring.testsuite.service.impl;

import org.jboss.arquillian.spring.testsuite.Employee;
import org.jboss.arquillian.spring.testsuite.repository.EmployeeRepository;
import org.jboss.arquillian.spring.testsuite.repository.impl.DefaultEmployeeRepository;
import org.jboss.arquillian.spring.testsuite.repository.impl.NullEmployeeRepository;
import org.jboss.arquillian.spring.testsuite.service.EmployeeService;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests the {@link DefaultEmployeeService} class.
 */
@RunWith(Arquillian.class)
public class DefaultEmployeeServiceTestCase {

    @Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class, "spring-test.jar")
                .addClasses(Employee.class,
                        EmployeeService.class, DefaultEmployeeService.class,
                        EmployeeRepository.class, DefaultEmployeeRepository.class, NullEmployeeRepository.class)
                .addAsResource(DefaultEmployeeServiceTestCase.class.getResource("/applicationContext.xml"),
                        "applicationContext.xml");
    }

    @Inject
    private EmployeeService employeeService;

    @Test
    public void testGetEmployees() throws Exception {

        List<Employee> result = employeeService.getEmployees();

        assertNotNull("Method returned null list as result.", result);
        assertEquals("Two employees were expected.", 2, result.size());
    }
}
