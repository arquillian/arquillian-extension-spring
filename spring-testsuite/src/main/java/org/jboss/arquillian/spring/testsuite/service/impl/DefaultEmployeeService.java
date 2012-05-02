package org.jboss.arquillian.spring.testsuite.service.impl;

import org.jboss.arquillian.spring.testsuite.Employee;
import org.jboss.arquillian.spring.testsuite.repository.EmployeeRepository;
import org.jboss.arquillian.spring.testsuite.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Default employee service.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@Service
public class DefaultEmployeeService implements EmployeeService {

    /**
     * Instance of {@link EmployeeRepository}.
     */
    @Autowired
    @Qualifier("defaultEmployeeRepository")
    EmployeeRepository employeeRepository;

    /**
     * {@inheritDoc}
     */
    public List<Employee> getEmployees() {

        return employeeRepository.getEmployees();
    }
}
