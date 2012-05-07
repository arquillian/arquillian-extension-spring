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
package org.jboss.arquillian.spring.testenricher;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.spring.context.TestScopeApplicationContext;
import org.jboss.arquillian.spring.model.StrategyTest;
import org.jboss.arquillian.spring.utils.TestReflectionHelper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Method;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <p>Tests {@link org.jboss.arquillian.spring.testenricher.SpringInjectionEnricher} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringInjectionEnricherTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private SpringInjectionEnricher instance;

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        instance = new SpringInjectionEnricher();
    }

    /**
     * <p>Tests {@link SpringInjectionEnricher#enrich(Object)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testEnrich() throws Exception {

        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("testApplicationContext.xml");

        TestScopeApplicationContext testScopeApplicationContext =
                new TestScopeApplicationContext(applicationContext, true);

        Instance<TestScopeApplicationContext> mockApplicationContextInstance = mock(Instance.class);
        TestReflectionHelper.setFieldValue(instance, "testApplicationContext", mockApplicationContextInstance);
        when(mockApplicationContextInstance.get()).thenReturn(testScopeApplicationContext);

        TestReflectionHelper.setFieldValue(instance, "testApplicationContext", mockApplicationContextInstance);

        StrategyTest strategyTest = new StrategyTest();

        instance.enrich(strategyTest);

        assertNotNull("The bean hasn't been autowired.", strategyTest.getStrategy());
    }

    /**
     * <p>Tests {@link SpringInjectionEnricher#resolve(java.lang.reflect.Method)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testResolve() throws Exception {

        Class clazz = SpringInjectionEnricherTestCase.class;
        Method method = clazz.getMethod("testResolve", (Class[]) null);

        Object[] result = instance.resolve(method);

        assertNotNull("Method returned null result.", result);
    }
}
