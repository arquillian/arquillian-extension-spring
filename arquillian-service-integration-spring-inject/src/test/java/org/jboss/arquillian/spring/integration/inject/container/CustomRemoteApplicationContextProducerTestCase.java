package org.jboss.arquillian.spring.integration.inject.container;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.spring.integration.configuration.SpringIntegrationConfiguration;
import org.jboss.arquillian.spring.integration.context.TestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.inject.model.CustomApplicationContextClass;
import org.jboss.arquillian.spring.integration.inject.model.CustomExceptionApplicationContextClass;
import org.jboss.arquillian.spring.integration.inject.model.CustomInvalidApplicationContextClass;
import org.jboss.arquillian.spring.integration.inject.model.CustomNullApplicationContextClass;
import org.jboss.arquillian.spring.integration.inject.model.PlainClass;
import org.jboss.arquillian.spring.integration.inject.test.TestReflectionHelper;
import org.jboss.arquillian.test.spi.TestClass;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <p>Tests {@link CustomRemoteApplicationContextProducer} class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class CustomRemoteApplicationContextProducerTestCase {

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private CustomRemoteApplicationContextProducer instance;

    /**
     * <p>The producer configuration.</p>
     */
    private SpringIntegrationConfiguration remoteConfiguration;

    /**
     * <p>Sets up the test environment.</p>
     *
     * @throws Exception if any error occurs
     */
    @Before
    public void setUp() throws Exception {

        instance = new CustomRemoteApplicationContextProducer();

        remoteConfiguration = new SpringIntegrationConfiguration(Collections.<String, String>emptyMap());

        setRemoteConfiguration();
    }

    /**
     * <p>Tests the {@link CustomRemoteApplicationContextProducer#supports(org.jboss.arquillian.test.spi.TestClass)}
     * method.</p>
     */
    @Test
    public void testSupportsFalse() {
        TestClass testClass = new TestClass(PlainClass.class);

        assertFalse("Class without annotations shouldn't be supported.", instance.supports(testClass));
    }

    /**
     * <p>Tests the {@link CustomRemoteApplicationContextProducer#supports(TestClass)} method.</p>
     */
    @Test
    public void testSupportsTrue() {
        TestClass testClass = new TestClass(CustomApplicationContextClass.class);

        assertTrue("Class should be supported.", instance.supports(testClass));
    }

    /**
     * <p>Tests the {@link CustomRemoteApplicationContextProducer#createApplicationContext(TestClass)} method.</p>
     */
    @Test
    public void testCreateApplicationContextDefault() {
        TestClass testClass = new TestClass(CustomApplicationContextClass.class);

        TestScopeApplicationContext result = instance.createApplicationContext(testClass);

        assertNotNull("The result was null.", result);
        assertTrue("The application context should be marked as closable.", result.isClosable());
        assertNotNull("The application context hasn't been created.", result.getApplicationContext());
    }

    /**
     * <p>Tests the {@link CustomRemoteApplicationContextProducer#createApplicationContext(TestClass)} method when the
     * factory method returns null.</p>
     *
     * <p>{@link RuntimeException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test(expected = RuntimeException.class)
    public void testCreateApplicationContextNull() throws Exception {

        TestClass testClass = new TestClass(CustomNullApplicationContextClass.class);

        instance.createApplicationContext(testClass);
    }

    /**
     * <p>Tests the {@link CustomRemoteApplicationContextProducer#createApplicationContext(TestClass)} method the test
     * class defines more then one annotated factory method.</p>
     *
     * <p>{@link RuntimeException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test(expected = RuntimeException.class)
    public void testCreateApplicationContextMultipleMethods() throws Exception {

        TestClass testClass = new TestClass(CustomNullApplicationContextClass.class);

        instance.createApplicationContext(testClass);
    }

    /**
     * <p>Tests the {@link CustomRemoteApplicationContextProducer#createApplicationContext(TestClass)} method when the
     * factory method return type can not be casted into ApplicationContext.</p>
     *
     * <p>{@link ClassCastException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test(expected = ClassCastException.class)
    public void testCreateApplicationContextIncorrectMethodContract() throws Exception {

        TestClass testClass = new TestClass(CustomInvalidApplicationContextClass.class);

        instance.createApplicationContext(testClass);
    }

    /**
     * <p>Tests the {@link CustomRemoteApplicationContextProducer#createApplicationContext(TestClass)} method when the
     * factory method throws an exception.</p>
     *
     * <p>{@link RuntimeException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test(expected = RuntimeException.class)
    public void testCreateApplicationContextErrorPropagation() throws Exception {

        TestClass testClass = new TestClass(CustomExceptionApplicationContextClass.class);

        instance.createApplicationContext(testClass);
    }


    /**
     * <p>Sets up the remote configuration for the instance of the tested class.</p>
     *
     * @throws Exception if any error occurs
     */
    private void setRemoteConfiguration() throws Exception {
        Instance<SpringIntegrationConfiguration> mockRemoteConfigurationInstance = mock(Instance.class);
        when(mockRemoteConfigurationInstance.get()).thenReturn(remoteConfiguration);
        TestReflectionHelper.setFieldValue(instance, "remoteConfiguration", mockRemoteConfigurationInstance);
    }
}
