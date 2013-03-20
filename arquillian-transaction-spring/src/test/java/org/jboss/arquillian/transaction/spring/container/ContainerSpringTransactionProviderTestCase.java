package org.jboss.arquillian.transaction.spring.container;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.spring.integration.context.RemoteTestScopeApplicationContext;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.arquillian.transaction.impl.test.TransactionalTestImpl;
import org.jboss.arquillian.transaction.spring.provider.AbstractSpringTransactionProvider;
import org.jboss.arquillian.transaction.spring.utils.TestReflectionHelper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * <p>Tests {@link ContainerSpringTransactionProvider} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class ContainerSpringTransactionProviderTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private AbstractSpringTransactionProvider instance;

    /**
     * <p>Mock instance of {@link RemoteTestScopeApplicationContext}.</p>
     */
    private Instance<RemoteTestScopeApplicationContext> mockTestScopeApplicationContextInstance;

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
     * <p>Represents the instance of {@link org.jboss.arquillian.test.spi.TestClass}.</p>
     */
    private org.jboss.arquillian.test.spi.TestClass testClass;

    /**
     * <p>Sets up the test environment.</p>
     *
     * @throws Exception if any error occurs
     */
    @Before
    public void setUp() throws Exception {

        testClass = new org.jboss.arquillian.test.spi.TestClass(Object.class);

        instance = new ContainerSpringTransactionProvider();

        mockApplicationContext = mock(ApplicationContext.class);
        mockPlatformTransactionManager = mock(PlatformTransactionManager.class);
        mockTransactionStatus = mock(TransactionStatus.class);

        when(mockApplicationContext.getBean(any(String.class), any(Class.class)))
                .thenReturn(mockPlatformTransactionManager);
        when(mockPlatformTransactionManager.getTransaction(any(TransactionDefinition.class)))
                .thenReturn(mockTransactionStatus);

        mockTestScopeApplicationContextInstance = mock(Instance.class);
        when(mockTestScopeApplicationContextInstance.get())
                .thenReturn(new RemoteTestScopeApplicationContext(mockApplicationContext, testClass, false));

        mockPlatformTransactionManagerInstance = mock(InstanceProducer.class);
        mockTransactionStatusInstance = mock(InstanceProducer.class);

        TestReflectionHelper.setFieldValue(instance, "applicationContextInstance",
                mockTestScopeApplicationContextInstance);
        TestReflectionHelper.setFieldValue(instance, "transactionManagerInstance",
                mockPlatformTransactionManagerInstance);
        TestReflectionHelper.setFieldValue(instance, "transactionStatusInstance", mockTransactionStatusInstance);
    }

    /**
     * <p>Tests the {@link AbstractSpringTransactionProvider#
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
     * <p>Tests the {@link AbstractSpringTransactionProvider#rollbackTransaction(
     * org.jboss.arquillian.transaction.spi.test.TransactionalTest)} method.</p>
     */
    @Test
    public void testRollbackTransaction() {

        when(mockPlatformTransactionManagerInstance.get()).thenReturn(mockPlatformTransactionManager);
        when(mockTransactionStatusInstance.get()).thenReturn(mockTransactionStatus);

        instance.rollbackTransaction(new TransactionalTestImpl(null));

        verify(mockPlatformTransactionManager).rollback(mockTransactionStatus);
    }

    /**
     * <p>Tests the {@link AbstractSpringTransactionProvider#
     * commitTransaction(org.jboss.arquillian.transaction.spi.test.TransactionalTest)}method.</p>
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
