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
package org.jboss.as.quickstarts.spring.controller;

import org.jboss.as.quickstarts.spring.model.UserCredentials;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 *
 */
@Controller
@RequestMapping("login.do")
public class LoginController {

    @RequestMapping(method = RequestMethod.GET)
    public String loginForm(Model model) {
        model.addAttribute(new UserCredentials());
        return "login";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String login(@Valid UserCredentials userCredentials, BindingResult result) {
        if (result.hasErrors()) {
            return "login";
        }

        return "welcome";
    }
}
