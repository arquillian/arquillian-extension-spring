/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.spring.client;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.arquillian.spring.SpringExtensionConsts;
import org.jboss.arquillian.spring.annotations.SpringConfiguration;
import org.jboss.arquillian.spring.annotations.SpringWebConfiguration;
import org.jboss.arquillian.spring.configuration.SpringExtensionRemoteConfiguration;
import org.jboss.arquillian.spring.configuration.SpringExtensionRemoteConfigurationUtils;
import org.jboss.arquillian.spring.container.SecurityActions;
import org.jboss.arquillian.spring.container.Spring25EnricherRemoteExtension;
import org.jboss.arquillian.spring.container.SpringExtensionRemoteConfigurationProducer;
import org.jboss.arquillian.spring.container.SpringInjectionEnricher;
import org.jboss.arquillian.spring.context.AbstractApplicationContextProducer;
import org.jboss.arquillian.spring.context.ApplicationContextDestroyer;
import org.jboss.arquillian.spring.context.TestScopeApplicationContext;
import org.jboss.arquillian.spring.context.WebApplicationContextProducer;
import org.jboss.arquillian.spring.context.XmlApplicationContextProducer;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 * <p>Spring Enricher Archive Appender. Adds all the required classes by this extension into a test archive.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class Spring25EnricherArchiveAppender extends AbstractSpringEnricherArchiveAppender {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void appendResources(JavaArchive archive) {

        archive.addClasses(SpringConfiguration.class, SpringWebConfiguration.class)
                .addClasses(AbstractApplicationContextProducer.class, ApplicationContextDestroyer.class,
                        TestScopeApplicationContext.class, XmlApplicationContextProducer.class,
                        WebApplicationContextProducer.class)
                .addClasses(SpringInjectionEnricher.class, SecurityActions.class,
                        SpringExtensionRemoteConfigurationProducer.class)
                .addClasses(SpringExtensionRemoteConfiguration.class, SpringExtensionRemoteConfigurationUtils.class)
                .addClasses(Spring25EnricherRemoteExtension.class)
                .addClasses(SpringExtensionConsts.class)
                .addAsServiceProvider(RemoteLoadableExtension.class, Spring25EnricherRemoteExtension.class);
    }
}
