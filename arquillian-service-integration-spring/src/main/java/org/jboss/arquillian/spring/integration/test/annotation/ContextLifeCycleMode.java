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
package org.jboss.arquillian.spring.integration.test.annotation;

/**
 * <p>Defines the possible application context instantiations modes: once per test class or separately for each test
 * method.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public enum ContextLifeCycleMode {

    /**
     * Implies that the {@code ApplicationContext} instance will be created only once for the whole test case and will
     * be shared by all tests.
     */
    TEST_CASE,

    /**
     * Implies that the {@code ApplicationContext} instance will be created separately for each test.
     */
    TEST
}
