package org.jboss.arquillian.spring.testsuite.repository;

import org.jboss.arquillian.spring.testsuite.Employee;

import java.util.List;

/**
 * Represents an employee repository.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public interface EmployeeRepository {

    /**
     * Retrieves all employees.
     *
     * @return list of employees
     */
    List<Employee> getEmployees();
}
