package org.jboss.arquillian.spring.model;

import org.jboss.arquillian.spring.annotations.SpringConfiguration;

/**
 *
 */
@SpringConfiguration("NotExistingApplicationContext.xml")
public class XmlAnnotatedMissingResourceClass {
}
