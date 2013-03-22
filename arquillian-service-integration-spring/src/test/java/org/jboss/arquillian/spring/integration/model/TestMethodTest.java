package org.jboss.arquillian.spring.integration.model;

import org.jboss.arquillian.spring.integration.test.annotation.ContextLifeCycle;
import org.jboss.arquillian.spring.integration.test.annotation.ContextLifeCycleMode;
import org.junit.Test;

/**
 * <p>A dummy test class used for testing instantiation of Spring's application within the extension.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@ContextLifeCycle(ContextLifeCycleMode.TEST)
public class TestMethodTest {

    /**
     * A empty test method, used only for indicating as a test method for execution.
     */
    @Test
    public void testMethod() {
        // empty method
    }
}
