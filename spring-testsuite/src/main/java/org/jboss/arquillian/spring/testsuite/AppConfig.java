package org.jboss.arquillian.spring.testsuite;

import org.jboss.arquillian.spring.testsuite.repository.EmployeeRepository;
import org.jboss.arquillian.spring.testsuite.repository.impl.DefaultEmployeeRepository;
import org.jboss.arquillian.spring.testsuite.service.EmployeeService;
import org.jboss.arquillian.spring.testsuite.service.impl.DefaultEmployeeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
public class AppConfig {

    @Bean
    public EmployeeRepository defaultEmployeeRepository() {

        return new DefaultEmployeeRepository();
    }

    @Bean
    public EmployeeService defaultEmployeeService() {

        return new DefaultEmployeeService();
    }
}
