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

import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.arquillian.container.test.spi.client.deployment.ProtocolArchiveProcessor;
import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.spring.SpringExtensionConstants;
import org.jboss.arquillian.spring.container.SpringExtensionRemoteConfigurationProducer;
import org.jboss.arquillian.spring.container.SpringInjectionEnricher;
import org.jboss.arquillian.spring.context.ApplicationContextDestroyer;
import org.jboss.arquillian.spring.context.XmlApplicationContextProducer;
import org.jboss.arquillian.spring.dependency.Spring25DependencyResolverProducer;
import org.jboss.arquillian.test.spi.TestEnricher;

/**
 * <p>The Spring Arquillian extension.</p>
 *
 * <p>It's responsible for bootstrapping the extension by registering service classes in Arquillian runtime.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class Spring25EnricherExtension implements LoadableExtension {

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(ExtensionBuilder builder) {

        // loads the extension only if Spring Application Context is in Classpath
        if (Validate.classExists(SpringExtensionConstants.APPLICATION_CONTEXT)) {
            builder.service(AuxiliaryArchiveAppender.class, Spring25EnricherArchiveAppender.class)
                    .service(ProtocolArchiveProcessor.class, SpringProtocolArchiveProcessor.class)
                    .observer(SpringExtensionConfigurationProducer.class)
                    .observer(Spring25DependencyResolverProducer.class);

            // registers the extension on client side
            builder.service(TestEnricher.class, SpringInjectionEnricher.class)
                    .observer(SpringExtensionRemoteConfigurationProducer.class)
                    .observer(XmlApplicationContextProducer.class)
                    .observer(ApplicationContextDestroyer.class);
        }
    }
}
