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
package org.jboss.arquillian.spring.container;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.arquillian.spring.SpringExtensionConstants;
import org.jboss.arquillian.spring.context.AnnotatedApplicationContextProducer;
import org.jboss.arquillian.spring.context.ApplicationContextDestroyer;
import org.jboss.arquillian.spring.context.WebApplicationContextProducer;
import org.jboss.arquillian.spring.context.XmlApplicationContextProducer;
import org.jboss.arquillian.test.spi.TestEnricher;

/**
 * <p>A remote extension that configures required services for running Spring tests.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class Spring3EnricherRemoteExtension implements RemoteLoadableExtension {

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(ExtensionBuilder builder) {

        // loads the extension only if Spring Application Context is in ClassPath
        if (Validate.classExists(SpringExtensionConstants.APPLICATION_CONTEXT)) {
            builder.service(TestEnricher.class, SpringInjectionEnricher.class)
                    .observer(SpringExtensionRemoteConfigurationProducer.class)
                    .observer(XmlApplicationContextProducer.class)
                    .observer(AnnotatedApplicationContextProducer.class)
                    .observer(WebApplicationContextProducer.class)
                    .observer(ApplicationContextDestroyer.class);
        }
    }
}
