/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat Middleware LLC, and individual contributors
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

import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.spring.integration.context.RemoteApplicationContextProducer;
import org.jboss.arquillian.spring.integration.context.RemoteTestScopeApplicationContext;
import org.jboss.arquillian.spring.integration.lifecycle.AbstractApplicationContextLifecycleHandler;

/**
 * <p>A container application context life cycle handler.</p>
 *
 * <p>This class is responsible for instantiating and destroying the application context on the server side.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class ContainerApplicationContextLifecycleHandler extends
        AbstractApplicationContextLifecycleHandler<RemoteApplicationContextProducer, RemoteTestScopeApplicationContext> {

    /**
     * <p>{@link RemoteTestScopeApplicationContext} producer.</p>
     */
    @Inject
    @ApplicationScoped
    private InstanceProducer<RemoteTestScopeApplicationContext> applicationContextInstance;

    /**
     * {@inheritDoc}
     */
    @Override
    protected RemoteTestScopeApplicationContext getApplicationContext() {

        return applicationContextInstance.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setApplicationContext(RemoteTestScopeApplicationContext applicationContext) {


        applicationContextInstance.set(applicationContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<RemoteApplicationContextProducer> getProducerClass() {

        return RemoteApplicationContextProducer.class;
    }
}
