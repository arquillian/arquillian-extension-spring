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

package org.jboss.arquillian.spring.integration.inject.container;

import org.jboss.arquillian.spring.integration.SpringInjectConstants;
import org.jboss.arquillian.spring.integration.context.AbstractApplicationContextProducer;
import org.jboss.arquillian.spring.integration.context.RemoteTestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.test.annotation.SpringWebConfiguration;
import org.jboss.arquillian.test.spi.TestClass;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.text.MessageFormat;

/**
 * <p>The instance of {@link AbstractApplicationContextProducer} that access the {@link WebApplicationContext} from
 * servlet context for specific Spring's FrameworkServlet or when the servlet name was not specified retrieves the root
 * web application context.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class WebApplicationContextProducer extends AbstractApplicationContextProducer {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(TestClass testClass) {
        return testClass.isAnnotationPresent(SpringWebConfiguration.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RemoteTestScopeApplicationContext createApplicationContext(TestClass testClass) {

        return new RemoteTestScopeApplicationContext(getWebApplicationContext(testClass), testClass, false);
    }

    /**
     * <p>Retrieves the web application context for the given test.</p>
     *
     * @param testClass the test class
     *
     * @return {@link ApplicationContext} retrieves from web application
     */
    private ApplicationContext getWebApplicationContext(TestClass testClass) {

        SpringWebConfiguration springWebConfiguration;
        WebApplicationContext rootContext;
        ApplicationContext applicationContext;

        rootContext = ContextLoader.getCurrentWebApplicationContext();

        if (rootContext == null) {

            throw new RuntimeException("The Spring Root Web Application Context could not be found.");
        }

        springWebConfiguration = testClass.getAnnotation(SpringWebConfiguration.class);

        if (!isEmpty(springWebConfiguration.servletName())) {

            applicationContext = getServletApplicationContext(rootContext, springWebConfiguration.servletName());

            if (applicationContext == null) {

                throw new RuntimeException("Could not find the application context for servlet: " +
                        springWebConfiguration.servletName());
            }
        } else {
            // uses the root context as the main application context
            applicationContext = rootContext;
        }

        return applicationContext;
    }

    /**
     * <p>Retrieves the application context for the given servlet.</p>
     *
     * @param rootContext the root web application context
     * @param servletName the  servlet name
     *
     * @return the retrieved application context
     */
    private ApplicationContext getServletApplicationContext(WebApplicationContext rootContext, String servletName) {

        // retrieves the servlet context from web application context
        ServletContext servletContext = rootContext.getServletContext();

        // retrieves the application context for the given servlet using it's name
        return (ApplicationContext) servletContext.getAttribute(MessageFormat.format(
                SpringInjectConstants.SERVLET_APPLICATION_CONTEXT, servletName));
    }


    /**
     * <p>Whether the passed string is null or empty string (after trimming).</p>
     *
     * @param value the string to validate
     *
     * @return true if the passed is null or empty, false otherwise
     */
    private static boolean isEmpty(String value) {

        return value == null || value.trim().length() == 0;
    }
}
