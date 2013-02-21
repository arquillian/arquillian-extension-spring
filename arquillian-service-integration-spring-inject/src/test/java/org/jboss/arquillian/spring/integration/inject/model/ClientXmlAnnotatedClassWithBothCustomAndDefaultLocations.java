package org.jboss.arquillian.spring.integration.inject.model;

import org.jboss.arquillian.spring.integration.test.annotation.SpringClientConfiguration;

@SpringClientConfiguration(value = "applicationContext.xml")
public class ClientXmlAnnotatedClassWithBothCustomAndDefaultLocations {
}
