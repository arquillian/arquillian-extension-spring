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

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.servlet.AfterServlet;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.web.servlet.ModelAndView;

import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * <p>Tests the {@link WelcomeController}.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@WarpTest
@RunWith(Arquillian.class)
public class WelcomeControllerTestCase {

    /**
     * <p>The selenium driver.</p>
     */
    @Drone
    WebDriver browser;

    /**
     * <p>The deployment url.</p>
     */
    @ArquillianResource
    URL contextPath;

    /**
     * <p>Creates the test deployment.</p>
     *
     * @return the test deployment
     */
    @Deployment
    public static WebArchive createDeployment() {

        return Deployments.createDeployment();
    }

    /**
     * Tests the welcome page.
     */
    @Test
    @RunAsClient
    public void test() {

        Warp.initiate(new Activity() {

            @Override
            public void perform() {
                browser.navigate().to(contextPath + "welcome.do");
            }
        }).inspect(new WelcomeControllerVerification());
    }

    public static class WelcomeControllerVerification extends Inspection {

        private static final long serialVersionUID = 1L;

        @ArquillianResource
        private ModelAndView modelAndView;

        @AfterServlet
        public void testWelcome() {

            assertEquals("welcome", modelAndView.getViewName());
            assertEquals("Warp welcomes!", modelAndView.getModel().get("message"));
        }
    }
}
