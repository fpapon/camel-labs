/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.openobject.camel.labs;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

@Component(immediate = true)
public class CamelServiceImpl implements CamelService {

    private CamelContext camelContext = new DefaultCamelContext();

    @Activate
    public void activate(ComponentContext context) throws Exception {
        DefaultCamelContext.class.cast(camelContext).setName("camel-labs");
        camelContext.start();
        context.getBundleContext().registerService(CamelContext.class, camelContext, null);
    }

    @Deactivate
    public void deactivate() throws Exception {
        camelContext.stop();
    }

    public void publish() throws Exception {

        if (camelContext.getRoute("") != null) {
            throw new IllegalArgumentException("already published");
        }

        final RouteDefinition definition = new RouteDefinition();
        definition.routeId("");

        RouteBuilder builder =
                new RouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        configureRoute(definition);
                    }
                };
        camelContext.addRoutes(builder);
    }

}
