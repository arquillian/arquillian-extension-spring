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

package org.jboss.arquillian.spring.integration.context;

import org.springframework.context.ConfigurableApplicationContext;

import java.util.logging.Logger;

/**
 * <p>Closes {@link org.springframework.context.ApplicationContext} after execution of test case.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class DefaultApplicationContextDestroyer implements ApplicationContextDestroyer {

    /**
     * <p>The logger used by this class.</p>
     */
    private static final Logger log = Logger.getLogger(DefaultApplicationContextDestroyer.class.getName());

    /**
     * Destroys the application context.
     *
     * @param applicationContext the application context to be destroyed.
     */
    public void destroyApplicationContext(TestScopeApplicationContext applicationContext) {

        if (applicationContext != null) {

            if (applicationContext.isClosable() && applicationContext.getApplicationContext()
                    instanceof ConfigurableApplicationContext) {

                // closes the application context, freeing all resources
                ((ConfigurableApplicationContext) applicationContext.getApplicationContext()).close();
                log.fine("Closing the application context.");
            }
        }
    }
}
