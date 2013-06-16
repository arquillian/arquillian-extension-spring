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

package org.jboss.arquillian.spring.integration.inject.client;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.arquillian.spring.integration.SpringInjectConstants;
import org.jboss.arquillian.spring.integration.client.AbstractSpringEnricherArchiveAppender;
import org.jboss.arquillian.spring.integration.configuration.SpringIntegrationConfiguration;
import org.jboss.arquillian.spring.integration.context.AbstractApplicationContextProducer;
import org.jboss.arquillian.spring.integration.context.ApplicationContextProducer;
import org.jboss.arquillian.spring.integration.inject.container.CustomRemoteApplicationContextProducer;
import org.jboss.arquillian.spring.integration.inject.container.SpringInjectRemoteExtension;
import org.jboss.arquillian.spring.integration.inject.container.WebApplicationContextProducer;
import org.jboss.arquillian.spring.integration.inject.container.XmlRemoteApplicationContextProducer;
import org.jboss.arquillian.spring.integration.inject.utils.ClassPathResourceLocationsProcessor;
import org.jboss.arquillian.spring.integration.test.annotation.SpringConfiguration;
import org.jboss.arquillian.spring.integration.test.annotation.SpringContextConfiguration;
import org.jboss.arquillian.spring.integration.test.annotation.SpringWebConfiguration;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 * <p>Spring Inject Archive Appender. Adds all the required classes by this extension into a test archive.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class SpringInjectArchiveAppender extends AbstractSpringEnricherArchiveAppender {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void appendResources(JavaArchive archive) {

        archive.addClasses(SpringConfiguration.class, SpringContextConfiguration.class, SpringWebConfiguration.class)
                .addClasses(SpringInjectConstants.class,
                        XmlRemoteApplicationContextProducer.class, WebApplicationContextProducer.class,
                        CustomRemoteApplicationContextProducer.class, SpringInjectRemoteExtension.class,
                        ClassPathResourceLocationsProcessor.class)
                .addClasses(ApplicationContextProducer.class, AbstractApplicationContextProducer.class,
                        SpringIntegrationConfiguration.class)
                .addAsServiceProvider(RemoteLoadableExtension.class, SpringInjectRemoteExtension.class);
    }
}
