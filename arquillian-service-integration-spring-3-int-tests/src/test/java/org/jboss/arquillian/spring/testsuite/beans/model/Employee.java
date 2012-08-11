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
package org.jboss.arquillian.spring.testsuite.beans.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>POJO representing an employee.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */

/**
 * <p>POJO representing an employee.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@Entity
public class Employee {

    /**
     * <p>Represents the employee id.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * <p>Represents the employee name.</p>
     */
    private String name;

    /**
     * <p>Creates new instance of {@link Employee} class.</p>
     */
    public Employee() {
        // empty constructor
    }

    /**
     * <p>Retrieves the employee id.</p>
     *
     * @return the employee id
     */
    public long getId() {
        return id;
    }

    /**
     * <p>Sets the employee id.</p>
     *
     * @param id the employee id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * <p>Retrieves the employee name.</p>
     *
     * @return the employee name
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Sets the employee name.</p>
     *
     * @param name the employee name
     */
    public void setName(String name) {
        this.name = name;
    }
}
