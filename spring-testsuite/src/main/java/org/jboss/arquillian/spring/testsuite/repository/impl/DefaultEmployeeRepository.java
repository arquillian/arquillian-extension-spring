package org.jboss.arquillian.spring.testsuite.repository.impl;

import org.jboss.arquillian.spring.testsuite.Employee;
import org.jboss.arquillian.spring.testsuite.repository.EmployeeRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * The default implementation of {@link EmployeeRepository}.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@Repository(value = "defaultEmployeeRepository")
public class DefaultEmployeeRepository implements EmployeeRepository {

    /**
     * <p>Represents the list of employees.</p>
     */
    private List<Employee> employees = new ArrayList();

    @PostConstruct
    private void init() {

        Employee employee;

        employee = new Employee();
        employee.setName("John Smith");
        employees.add(employee);

        employee = new Employee();
        employee.setName("Marty Smith");
        employees.add(employee);
    }

    /**
     * {@inheritDoc}
     */
    public List<Employee> getEmployees() {

        return employees;
    }
}
