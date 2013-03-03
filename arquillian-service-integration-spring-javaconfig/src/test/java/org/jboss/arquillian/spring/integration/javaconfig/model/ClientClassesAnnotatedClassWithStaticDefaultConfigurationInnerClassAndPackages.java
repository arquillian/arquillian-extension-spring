package org.jboss.arquillian.spring.integration.javaconfig.model;

import org.jboss.arquillian.spring.integration.test.annotation.SpringClientAnnotationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringClientAnnotationConfiguration
public class ClientClassesAnnotatedClassWithStaticDefaultConfigurationInnerClassAndPackages {
    @Configuration
    @ComponentScan(basePackages = {"org.jboss.arquillian.spring.integration.javaconfig.model.testpackage"})
    public static class ClientClassesAnnotatedClassWithDefaultConfigurationClassSpecifiedConfig {
        @Bean
        public PlainClass plainClass() {
            return new PlainClass();
        }
    }
}
