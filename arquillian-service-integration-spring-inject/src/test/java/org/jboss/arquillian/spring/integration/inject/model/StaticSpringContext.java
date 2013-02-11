package org.jboss.arquillian.spring.integration.inject.model;

import org.jboss.arquillian.spring.integration.test.annotation.SpringConfiguration;
import org.springframework.context.support.StaticApplicationContext;

@SpringConfiguration(contextClass=StaticApplicationContext.class)
public class StaticSpringContext {

}
