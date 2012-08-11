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
package org.jboss.arquillian.spring.integration.transaction.provider;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.spring.integration.context.TestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.transaction.utils.TestReflectionHelper;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.arquillian.transaction.impl.test.TransactionalTestImpl;
import org.jboss.arquillian.transaction.spring.provider.SpringTransactionProvider;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import static org.mockito.Mockito.*;

/**
 * <p>Tests {@link org.jboss.arquillian.transaction.spring.provider.SpringTransactionProvider} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringTransactionProviderTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private SpringTransactionProvider instance;

    /**
     * <p>Mock instance of {@link TestScopeApplicationContext}.</p>
     */
    private Instance<TestScopeApplicationContext> mockTestScopeApplicationContextInstance;

    /**
     * <p>Mock instance of {@link PlatformTransactionManager}.</p>
     */
    private InstanceProducer<PlatformTransactionManager> mockPlatformTransactionManagerInstance;

    /**
     * <p>Mock instance of {@link TransactionStatus}.</p>
     */
    private InstanceProducer<TransactionStatus> mockTransactionStatusInstance;

    /**
     * <p>Represents the instance of {@link ApplicationContext}</p>
     */
    private ApplicationContext mockApplicationContext;

    /**
     * <p>Represents the instance of {@link PlatformTransactionManager}</p>
     */
    private PlatformTransactionManager mockPlatformTransactionManager;

    /**
     * <p>Represents the instance of {@link TransactionStatus}</p>
     */
    private TransactionStatus mockTransactionStatus;

    /**
     * <p>Sets up the test environment.</p>
     *
     * @throws Exception if any error occurs
     */
    @Before
    public void setUp() throws Exception {

        instance = new SpringTransactionProvider();

        mockApplicationContext = mock(ApplicationContext.class);
        mockPlatformTransactionManager = mock(PlatformTransactionManager.class);
        mockTransactionStatus = mock(TransactionStatus.class);

        when(mockApplicationContext.getBean(any(String.class), any(Class.class)))
                .thenReturn(mockPlatformTransactionManager);
        when(mockPlatformTransactionManager.getTransaction(any(TransactionDefinition.class)))
                .thenReturn(mockTransactionStatus);

        mockTestScopeApplicationContextInstance = mock(Instance.class);
        when(mockTestScopeApplicationContextInstance.get())
                .thenReturn(new TestScopeApplicationContext(mockApplicationContext, false));

        mockPlatformTransactionManagerInstance = mock(InstanceProducer.class);
        mockTransactionStatusInstance = mock(InstanceProducer.class);

        TestReflectionHelper.setFieldValue(instance, "applicationContextInstance", mockTestScopeApplicationContextInstance);
        TestReflectionHelper.setFieldValue(instance, "transactionManagerInstance", mockPlatformTransactionManagerInstance);
        TestReflectionHelper.setFieldValue(instance, "transactionStatusInstance", mockTransactionStatusInstance);
    }

    /**
     * <p>Tests the {@link SpringTransactionProvider#
     * beginTransaction(org.jboss.arquillian.transaction.spi.test.TransactionalTest)} method.</p>
     */
    @Test
    public void testBeginTransaction() {

        instance.beginTransaction(new TransactionalTestImpl(null));

        verify(mockPlatformTransactionManager).getTransaction(any(TransactionDefinition.class));
        verify(mockPlatformTransactionManagerInstance).set(mockPlatformTransactionManager);
        verify(mockTransactionStatusInstance).set(mockTransactionStatus);
    }

    /**
     * <p>Tests the {@link SpringTransactionProvider#
     * rollbackTransaction(org.jboss.arquillian.transaction.spi.test.TransactionalTest)} method.</p>
     */
    @Test
    public void testRollbackTransaction() {

        when(mockPlatformTransactionManagerInstance.get()).thenReturn(mockPlatformTransactionManager);
        when(mockTransactionStatusInstance.get()).thenReturn(mockTransactionStatus);

        instance.rollbackTransaction(new TransactionalTestImpl(null));

        verify(mockPlatformTransactionManager).rollback(mockTransactionStatus);
    }

    /**
     * <p>Tests the {@link SpringTransactionProvider#
     * commitTransaction(org.jboss.arquillian.transaction.spi.test.TransactionalTest)} method.</p>
     */
    @Test
    public void testCommitTransaction() {

        when(mockPlatformTransactionManagerInstance.get()).thenReturn(mockPlatformTransactionManager);
        when(mockTransactionStatusInstance.get()).thenReturn(mockTransactionStatus);

        instance.commitTransaction(new TransactionalTestImpl(null));

        verify(mockPlatformTransactionManager).commit(mockTransactionStatus);
    }

    /**
     * <p>A test class.</p>
     *
     * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
     */
    @Transactional(manager = "txManager")
    public class TestClass {

        public void test() {
            // empty method
        }
    }
}
