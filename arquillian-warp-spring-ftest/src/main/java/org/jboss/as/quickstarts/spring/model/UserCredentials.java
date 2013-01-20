/**
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
package org.jboss.as.quickstarts.spring.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>Represents the user credentials.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class UserCredentials {

    /**
     * <p>Represents the user login.</p>
     */
    @NotNull
    @Size(min = 1)
    private String login;

    /**
     * <p>Represents the user password.</p>
     */
    @NotNull
    @Size(min = 1)
    private String password;

    /**
     * <p>Creates new instance of {@link UserCredentials}.</p>
     */
    public UserCredentials() {
        // empty constructor
    }

    /**
     * <p>Retrieves the user login.</p>
     *
     * @return the user login
     */
    public String getLogin() {
        return login;
    }

    /**
     * <p>Sets the user login.</p>
     *
     * @param login the user login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * <p>Retrieves the user password.</p>
     *
     * @return the user password
     */
    public String getPassword() {
        return password;
    }

    /**
     * <p>Sets the user login.</p>
     *
     * @param password the user password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
