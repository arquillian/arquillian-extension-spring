package org.jboss.arquillian.spring.testsuite.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.spring.integration.test.annotation.SpringAnnotationConfiguration;
import org.jboss.arquillian.spring.testsuite.beans.model.Employee;
import org.jboss.arquillian.spring.testsuite.beans.repository.EmployeeRepository;
import org.jboss.arquillian.spring.testsuite.beans.repository.impl.DefaultEmployeeRepository;
import org.jboss.arquillian.spring.testsuite.beans.service.EmployeeService;
import org.jboss.arquillian.spring.testsuite.beans.service.impl.DefaultEmployeeService;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static org.jboss.arquillian.spring.testsuite.test.Deployments.createAppDeployment;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
@SpringAnnotationConfiguration
@RunAsClient
public class AnnotatedRemoteConfigurationDefaultConfigTestCase {
    @Autowired
    EmployeeService employeeService;

    /**
     * <p>Creates the test deployment.</p>
     *
     * @return the test deployment
     */
    @Deployment
    public static Archive createTestArchive() {
        return createAppDeployment().addClass(DefaultConfiguration.class);
    }

    @Test
    public void testGetEmployees() {
        List<Employee> result = employeeService.getEmployees();

        assertNotNull("Method returned null list as result.", result);
        assertEquals("Two employees were expected.", 2, result.size());
    }

    @Configuration
    public static class DefaultConfiguration {

        @Bean
        public EmployeeRepository defaultEmployeeRepository() {
            System.out.println("defaultEmployeeRepositorydefaultEmployeeRepository");
            return new DefaultEmployeeRepository();
        }

        @Bean
        public EmployeeService defaultEmployeeService() {
            System.out.println("defaultEmployeeServicedefaultEmployeeServicedefaultEmployeeService");
            return new DefaultEmployeeService();
        }
    }

}
