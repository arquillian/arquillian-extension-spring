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

package org.jboss.arquillian.spring.integration.inject.model;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>Simple class used for testing the enricher.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class StrategyTest {

    /**
     * <p>Instance of {@link Strategy} injected by IOC container.</p>
     */
    private Strategy strategy;

    /**
     * <p>Retrieves the strategy.</p>
     *
     * @return the strategy.
     */
    public Strategy getStrategy() {
        return strategy;
    }

    /**
     * <p>Sets the strategy.</p>
     *
     * @param strategy the strategy.
     */
    @Autowired
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
}
