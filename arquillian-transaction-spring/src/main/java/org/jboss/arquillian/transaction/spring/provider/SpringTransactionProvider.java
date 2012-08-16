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
package org.jboss.arquillian.transaction.spring.provider;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.spring.integration.context.RemoteTestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.context.TestScopeApplicationContext;
import org.jboss.arquillian.transaction.spi.annotation.TransactionScope;
import org.jboss.arquillian.transaction.spi.provider.TransactionProvider;
import org.jboss.arquillian.transaction.spi.test.TransactionalTest;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

/**
 * <p>A Spring transaction provider.</p>
 *
 * <p>It delegates all the transaction specific handling to the configured {@link PlatformTransactionManager}.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class SpringTransactionProvider implements TransactionProvider {

    /**
     * <p>Instance of application context.</p>
     */
    @Inject
    Instance<RemoteTestScopeApplicationContext> applicationContextInstance;

    /**
     * <p>Instance of {@link PlatformTransactionManager} to which all the operations are delegated.</p>
     */
    @Inject
    @TransactionScope
    InstanceProducer<PlatformTransactionManager> transactionManagerInstance;

    /**
     * <p>The {@link TransactionStatus} of the currently executed transaction.</p>
     */
    @Inject
    @TransactionScope
    InstanceProducer<TransactionStatus> transactionStatusInstance;

    /**
     * {@inheritDoc}
     */
    @Override
    public void beginTransaction(TransactionalTest transactionalTest) {

        // retrieves the transaction manager
        PlatformTransactionManager transactionManager = getTransactionManager(
                transactionalTest.getManager());
        transactionManagerInstance.set(transactionManager);

        // create the transaction definition
        TransactionDefinition transactionDefinition = createTransactionDefinition();

        // begins transaction
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        transactionStatusInstance.set(transactionStatus);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commitTransaction(TransactionalTest transactionalTest) {

        PlatformTransactionManager transactionManager = transactionManagerInstance.get();
        TransactionStatus transactionStatus = transactionStatusInstance.get();

        // commits the transaction
        transactionManager.commit(transactionStatus);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollbackTransaction(TransactionalTest transactionalTest) {

        PlatformTransactionManager transactionManager = transactionManagerInstance.get();
        TransactionStatus transactionStatus = transactionStatusInstance.get();

        // rollbacks the transaction
        transactionManager.rollback(transactionStatus);
    }

    /**
     * <p>Creates new transaction definition.</p>
     *
     * @return the transaction definition
     */
    private DefaultTransactionAttribute createTransactionDefinition() {

        return new DefaultTransactionAttribute();
    }

    /**
     * <p>Retrieves from the application context the configured {@link PlatformTransactionManager} for the given
     * test.</p>
     *
     * @param transactionManagerName the transaction manager name
     *
     * @return the {@link PlatformTransactionManager} retrieved from application context
     */
    private PlatformTransactionManager getTransactionManager(String transactionManagerName) {

        ApplicationContext applicationContext = getApplicationContext();

        return (PlatformTransactionManager) applicationContext.getBean(transactionManagerName,
                PlatformTransactionManager.class);
    }

    /**
     * <p>Retrieves the application context.</p>
     *
     * @return the application context
     */
    private ApplicationContext getApplicationContext() {

        TestScopeApplicationContext testScopeApplicationContext = applicationContextInstance.get();
        return testScopeApplicationContext.getApplicationContext();
    }
}
