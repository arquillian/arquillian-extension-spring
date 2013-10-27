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
package org.jboss.arquillian.spring.persistence.client;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.arquillian.spring.persistence.container.SpringPersistenceRemoteExtension;
import org.jboss.arquillian.spring.persistence.datasource.ApplicationContextDataSourceProvider;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 * <p>Packages the spring persistence extension classes into a archive deployed into the container.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class SpringPersistenceArchiveAppender implements AuxiliaryArchiveAppender {

    /**
     * {@inheritDoc}
     */
    @Override
    public Archive<?> createAuxiliaryArchive() {

        // creates the archive
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "arquillian-persistence-spring.jar");

        // adds the container classes
        archive.addPackage(ApplicationContextDataSourceProvider.class.getPackage());
        archive.addClass(SpringPersistenceRemoteExtension.class);

        // registers the remote extension
        archive.addAsServiceProvider(RemoteLoadableExtension.class, SpringPersistenceRemoteExtension.class);

        return archive;
    }
}
