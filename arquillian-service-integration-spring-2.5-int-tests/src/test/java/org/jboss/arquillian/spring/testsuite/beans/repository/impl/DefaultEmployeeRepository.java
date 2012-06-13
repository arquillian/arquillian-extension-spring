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
package org.jboss.arquillian.spring.testsuite.beans.repository.impl;

import org.jboss.arquillian.spring.testsuite.beans.model.Employee;
import org.jboss.arquillian.spring.testsuite.beans.repository.EmployeeRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>The default implementation of {@link EmployeeRepository}.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@Repository(value = "defaultEmployeeRepository")
public class DefaultEmployeeRepository implements EmployeeRepository {

    /**
     * <p>Represents the list of employees.</p>
     */
    private List<Employee> employees = new ArrayList<Employee>();

    /**
     * <p>Initializes the bean.</p>
     */
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
