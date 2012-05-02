package org.jboss.arquillian.spring.testsuite.repository.impl;

import org.jboss.arquillian.spring.testsuite.Employee;
import org.jboss.arquillian.spring.testsuite.repository.EmployeeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * A {@code null} repository.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@Repository(value = "nullEmployeeRepository")
public class NullEmployeeRepository implements EmployeeRepository {

    /**
     * {@inheritDoc}
     */
    public List<Employee> getEmployees() {
        return null;
    }
}
