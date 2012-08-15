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
package org.jboss.arquillian.spring.testsuite.beans.controller;

import org.jboss.arquillian.spring.testsuite.beans.model.Employee;
import org.jboss.arquillian.spring.testsuite.beans.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>An employee controller.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@Controller
public class EmployeeRestController {

    /**
     * <p>The autowired {@link EmployeeService}.</p>
     */
    @Autowired
    private EmployeeService employeeService;

    /**
     * <p>Retrieves list of all employees.</p>
     *
     * @return list of all employees
     */
    @RequestMapping(value = "/Employees/{employeeId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Employee getEmployee(@PathVariable long employeeId) {

        // returns list of all employees
        return employeeService.getEmployees().get(0);
    }
}
