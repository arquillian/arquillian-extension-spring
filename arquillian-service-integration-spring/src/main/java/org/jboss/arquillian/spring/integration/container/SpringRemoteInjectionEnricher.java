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

package org.jboss.arquillian.spring.integration.container;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.spring.integration.context.RemoteTestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.enricher.AbstractSpringInjectionEnricher;

/**
 * <p>Spring remote test enricher, injects spring beans into the test class.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public class SpringRemoteInjectionEnricher extends AbstractSpringInjectionEnricher<RemoteTestScopeApplicationContext> {

    /**
     * <p>Instance of {@link RemoteTestScopeApplicationContext}.</p>
     */
    @Inject
    private Instance<RemoteTestScopeApplicationContext> testApplicationContext;

    /**
     * {@inheritDoc}
     */
    @Override
    public RemoteTestScopeApplicationContext getTestScopeApplicationContext() {

        return testApplicationContext.get();
    }
}
