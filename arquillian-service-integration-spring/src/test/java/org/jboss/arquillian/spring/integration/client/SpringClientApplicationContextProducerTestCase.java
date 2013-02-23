package org.jboss.arquillian.spring.integration.client;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.spring.integration.container.SpringContainerApplicationContextProducer;
import org.jboss.arquillian.spring.integration.context.ClientApplicationContextProducer;
import org.jboss.arquillian.spring.integration.context.ClientTestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.context.TestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.utils.TestReflectionHelper;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.GenericApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * <p>Tests {@link SpringClientApplicationContextProducer} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringClientApplicationContextProducerTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private SpringClientApplicationContextProducer instance;

    /**
     * <p>Represents an instance of {@link ClientApplicationContextProducer} that will always support the test
     * class.</p>
     */
    private ClientApplicationContextProducer supportedApplicationContextProducer;

    /**
     * <p>Represents an instance of {@link ClientApplicationContextProducer} that will never support the test
     * class.</p>
     */
    private ClientApplicationContextProducer notSupportedApplicationContextProducer;

    /**
     * <p>Sets up the test environment.</p>
     */
    @Before
    public void setUp() {

        instance = new SpringClientApplicationContextProducer();

        supportedApplicationContextProducer = mock(ClientApplicationContextProducer.class);
        when(supportedApplicationContextProducer.supports(any(TestClass.class))).thenReturn(true);
        when(supportedApplicationContextProducer.createApplicationContext(any(TestClass.class)))
                .thenReturn(new ClientTestScopeApplicationContext(new GenericApplicationContext(), true));

        notSupportedApplicationContextProducer = mock(ClientApplicationContextProducer.class);
        when(notSupportedApplicationContextProducer.supports(any(TestClass.class))).thenReturn(false);
    }

    /**
     * <p>Tests {@link SpringContainerApplicationContextProducer#initApplicationContext(org.jboss.arquillian.test.spi.event.suite.BeforeClass)}
     * method, when the test class is supported.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testInitApplicationContextSupported() throws Exception {

        List<ClientApplicationContextProducer> producers = new ArrayList<ClientApplicationContextProducer>();
        producers.add(notSupportedApplicationContextProducer);
        producers.add(supportedApplicationContextProducer);

        ServiceLoader serviceLoader = mock(ServiceLoader.class);
        when(serviceLoader.all(ClientApplicationContextProducer.class)).thenReturn(producers);

        Instance<ServiceLoader> mockServiceLoader = mock(Instance.class);
        when(mockServiceLoader.get()).thenReturn(serviceLoader);
        TestReflectionHelper.setFieldValue(instance, "serviceLoader", mockServiceLoader);

        InstanceProducer<TestScopeApplicationContext> mockApplicationContext = mock(InstanceProducer.class);
        TestReflectionHelper.setFieldValue(instance, "testApplicationContext", mockApplicationContext);

        instance.initApplicationContext(new BeforeClass(Object.class));

        verify(mockApplicationContext).set((TestScopeApplicationContext) notNull());
    }

    /**
     * <p>Tests {@link SpringContainerApplicationContextProducer#initApplicationContext(BeforeClass)} method, when the
     * test class is not supported.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testInitApplicationContextNotSupported() throws Exception {

        List<ClientApplicationContextProducer> producers = new ArrayList<ClientApplicationContextProducer>();
        producers.add(notSupportedApplicationContextProducer);

        ServiceLoader serviceLoader = mock(ServiceLoader.class);
        when(serviceLoader.all(ClientApplicationContextProducer.class)).thenReturn(producers);

        Instance<ServiceLoader> mockServiceLoader = mock(Instance.class);
        when(mockServiceLoader.get()).thenReturn(serviceLoader);
        TestReflectionHelper.setFieldValue(instance, "serviceLoader", mockServiceLoader);

        InstanceProducer<TestScopeApplicationContext> mockApplicationContext = mock(InstanceProducer.class);
        TestReflectionHelper.setFieldValue(instance, "testApplicationContext", mockApplicationContext);

        instance.initApplicationContext(new BeforeClass(Object.class));

        verifyZeroInteractions(mockApplicationContext);
    }
}
