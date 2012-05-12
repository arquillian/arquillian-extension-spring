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

import org.jboss.arquillian.container.test.spi.client.deployment.CachedAuxilliaryArchiveAppender;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.spring.configuration.SpringExtensionConfiguration;
import org.jboss.arquillian.spring.configuration.SpringExtensionRemoteConfiguration;
import org.jboss.arquillian.spring.configuration.SpringExtensionRemoteConfigurationUtils;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 * <p>The base auxiliary archive that adds the properties file with the settings for the remote extension.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @version $Revision: $
 */
public abstract class SpringEnricherArchiveAppender extends CachedAuxilliaryArchiveAppender {

    /**
     * <p>Represents the instance of {@link SpringExtensionConfiguration}.</p>
     */
    @Inject
    private Instance<SpringExtensionConfiguration> configuration;

    /**
     * {@inheritDoc}
     */
    @Override
    protected Archive<?> buildArchive() {

        // creates the auxiliary archive
        JavaArchive archive = createArchive();

        // appends all required resources
        appendResources(archive);

        // adds the settings for the remote extension
        appendProperties(archive);

        return archive;
    }

    /**
     * <p>Appends the resources into the test deployment.</p>
     *
     * @param archive the auxiliary archive
     */
    protected abstract void appendResources(JavaArchive archive);

    /**
     * <p>Appends the properties to the auxiliary archive.</p>
     *
     * @param archive the auxiliary archive
     */
    protected void appendProperties(JavaArchive archive) {

        // TODO
        SpringExtensionRemoteConfiguration remoteConfig = new SpringExtensionRemoteConfiguration();
        remoteConfig.setCustomContextClass(configuration.get().getCustomContextClass());
        remoteConfig.setCustomAnnotatedContextClass(configuration.get().getCustomAnnotatedContextClass());

        archive.addAsResource(new StringAsset(SpringExtensionRemoteConfigurationUtils.toString(remoteConfig)),
                SpringExtensionRemoteConfigurationUtils.SPRING_REMOTE_PROPERTIES);
    }

    /**
     * <p>Creates the auxiliary archive.</p>
     *
     * @return the created auxiliary archive
     */
    protected JavaArchive createArchive() {

        return ShrinkWrap.create(JavaArchive.class, "arquillian-testenricher-spring.jar");
    }
}
