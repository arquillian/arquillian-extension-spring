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
package org.jboss.arquillian.spring.testsuite.beans.web;

import org.jboss.arquillian.spring.testsuite.beans.config.WebAppConfig;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * <p>An instance of {@link org.springframework.web.WebApplicationInitializer} for initializing the web
 * application.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class EmployeeWebRootInitializer implements WebApplicationInitializer {

    /**
     * {@inheritDoc}
     */
    public void onStartup(ServletContext servletContext) throws ServletException {

        // creates the web root app context
        AnnotationConfigWebApplicationContext webRootContext = new AnnotationConfigWebApplicationContext();
        webRootContext.register(WebAppConfig.class);

        // registers context load listener
        servletContext.addListener(new ContextLoaderListener(webRootContext));

        // adds a dispatch servlet, the servlet will be configured from root web app context
        ServletRegistration.Dynamic servletConfig = servletContext.addServlet("employee",
                new DispatcherServlet(new AnnotationConfigWebApplicationContext()));
        servletConfig.setLoadOnStartup(1);
        servletConfig.addMapping("*.htm");
    }
}
