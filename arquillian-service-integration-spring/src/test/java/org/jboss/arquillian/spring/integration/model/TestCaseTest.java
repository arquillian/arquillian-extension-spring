/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.arquillian.spring.integration.model;

import org.jboss.arquillian.spring.integration.test.annotation.ContextLifeCycle;
import org.jboss.arquillian.spring.integration.test.annotation.ContextLifeCycleMode;
import org.junit.Test;

/**
 * <p>A dummy test class used for testing instantiation of Spring's application within the extension.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@ContextLifeCycle(ContextLifeCycleMode.TEST_CASE)
public class TestCaseTest {

    /**
     * A empty test method, used only for indicating as a test method for execution.
     */
    @Test
    public void testMethod() {
        // empty method
    }
}
