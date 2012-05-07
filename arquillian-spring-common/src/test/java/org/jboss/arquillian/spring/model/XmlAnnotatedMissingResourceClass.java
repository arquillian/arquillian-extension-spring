package org.jboss.arquillian.spring.model;

import org.jboss.arquillian.spring.annotations.SpringConfiguration;

/**
 * <p>Simple class used for testing the enricher.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@SpringConfiguration("NotExistingApplicationContext.xml")
public class XmlAnnotatedMissingResourceClass {
}