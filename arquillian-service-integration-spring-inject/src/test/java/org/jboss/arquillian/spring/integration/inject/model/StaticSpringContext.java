package org.jboss.arquillian.spring.integration.inject.model;

import org.jboss.arquillian.spring.integration.test.annotation.ClassToScan;
import org.jboss.arquillian.spring.integration.test.annotation.SpringConfiguration;
import org.springframework.context.support.StaticApplicationContext;

@SpringConfiguration(contextClass=StaticApplicationContext.class)
@ClassToScan(StaticSpringContext.class)
public class StaticSpringContext {

}
