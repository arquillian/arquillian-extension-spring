package org.jboss.arquillian.spring.testsuite.service;

import org.jboss.arquillian.spring.testsuite.Employee;

import java.util.List;

/**
 * Represents an employee service.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public interface EmployeeService {

    /**
     * Retrieves all employees.
     *
     * @return list of employees
     */
    List<Employee> getEmployees();
}
