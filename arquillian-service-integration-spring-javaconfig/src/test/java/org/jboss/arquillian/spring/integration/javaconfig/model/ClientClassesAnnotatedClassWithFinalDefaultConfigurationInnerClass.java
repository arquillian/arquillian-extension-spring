package org.jboss.arquillian.spring.integration.javaconfig.model;

import org.jboss.arquillian.spring.integration.test.annotation.SpringClientAnnotationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringClientAnnotationConfiguration
public class ClientClassesAnnotatedClassWithFinalDefaultConfigurationInnerClass {
    @Configuration
    public static final class ClientClassesAnnotatedClassWithFinalDefaultConfigurationInnerClassConfig {
        @Bean
        public PlainClass plainClass() {
            return new PlainClass();
        }
    }

}
