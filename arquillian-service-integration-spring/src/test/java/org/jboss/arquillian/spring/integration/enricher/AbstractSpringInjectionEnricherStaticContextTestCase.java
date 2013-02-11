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

package org.jboss.arquillian.spring.integration.enricher;

import org.jboss.arquillian.spring.integration.context.TestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.model.StrategyTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import java.lang.reflect.Method;

import static org.junit.Assert.assertNotNull;

/**
 * <p>Tests {@link AbstractSpringInjectionEnricher} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class AbstractSpringInjectionEnricherStaticContextTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private AbstractSpringInjectionEnricher instance;

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        StaticApplicationContext ctx = new StaticApplicationContext();

        TestScopeApplicationContext testScopeApplicationContext =
                new TestScopeApplicationContext(ctx, true);

        instance = new MockAbstractSpringInjectionEnricher();
        ((MockAbstractSpringInjectionEnricher)instance).setTestScopeApplicationContext(testScopeApplicationContext);
    }

    /**
     * <p>Tests {@link AbstractSpringInjectionEnricher#enrich(Object)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testEnrich() throws Exception {

        StrategyTest strategyTest = new StrategyTest();

        instance.enrich(strategyTest);

        assertNotNull("The bean hasn't been autowired.", strategyTest.getStrategy());
    }

    /**
     * <p>Tests {@link AbstractSpringInjectionEnricher#resolve(Method)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testResolve() throws Exception {

        Class clazz = AbstractSpringInjectionEnricherStaticContextTestCase.class;
        Method method = clazz.getMethod("testResolve", (Class[]) null);

        Object[] result = instance.resolve(method);

        assertNotNull("Method returned null result.", result);
    }

    /**
     * <p>A mock implementation of {@link AbstractSpringInjectionEnricher} used for testing..</p>
     *
     * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
     */
    private final class MockAbstractSpringInjectionEnricher
            extends AbstractSpringInjectionEnricher<TestScopeApplicationContext> {

        /**
         * <p>Represents the instance of {@link TestScopeApplicationContext} class.</p>
         */
        private TestScopeApplicationContext testScopeApplicationContext;

        /**
         * <p>Sets the instance of {@link TestScopeApplicationContext}.</p>
         *
         * @param testScopeApplicationContext the instance of {@link TestScopeApplicationContext}
         */
        public void setTestScopeApplicationContext(TestScopeApplicationContext testScopeApplicationContext) {
            this.testScopeApplicationContext = testScopeApplicationContext;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public TestScopeApplicationContext getTestScopeApplicationContext() {

            return testScopeApplicationContext;
        }
    }
}
