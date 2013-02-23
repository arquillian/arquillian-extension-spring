/**
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
package org.jboss.arquillian.warp.extension.spring.container;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.arquillian.test.spi.TestEnricher;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;
import org.jboss.arquillian.warp.extension.spring.container.provider.ErrorsProvider;
import org.jboss.arquillian.warp.extension.spring.container.provider.ExceptionProvider;
import org.jboss.arquillian.warp.extension.spring.container.provider.HandlerInterceptorsProvider;
import org.jboss.arquillian.warp.extension.spring.container.provider.HandlerProvider;
import org.jboss.arquillian.warp.extension.spring.container.provider.ModelAndViewProvider;
import org.jboss.arquillian.warp.extension.spring.container.provider.SpringMvcResultProvider;

/**
 * <p>A remote extension that registers the extension in the remote container.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringWarpRemoteExtension implements RemoteLoadableExtension {

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(ExtensionBuilder builder) {
        builder.service(TestEnricher.class, SpringWarpTestEnricher.class);

        builder.service(ResourceProvider.class, SpringMvcResultProvider.class);
        builder.service(ResourceProvider.class, ModelAndViewProvider.class);
        builder.service(ResourceProvider.class, ErrorsProvider.class);
        builder.service(ResourceProvider.class, ExceptionProvider.class);
        builder.service(ResourceProvider.class, HandlerInterceptorsProvider.class);
        builder.service(ResourceProvider.class, HandlerProvider.class);
    }
}
