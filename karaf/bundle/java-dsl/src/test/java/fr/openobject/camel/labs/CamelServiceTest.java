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
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.apache.camel.builder.Builder.constant;

public class CamelServiceTest {

    @Test
    public void writeFileCamelContextTest() throws Exception {
        CamelContext camelContext = new DefaultCamelContext();
        Assertions.assertNotNull(camelContext);

        DefaultCamelContext.class.cast(camelContext).setName("camel-labs");
        camelContext.start();
        Assertions.assertTrue(camelContext.getStatus().isStarted());
        System.out.println("camel context started");

        RouteBuilder routeBuilder = new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                from("timer:fire?period=1000").
                        id("timer-filer-example").
                        setHeader(Exchange.FILE_NAME,constant("camel-example.txt")).
                        setBody(constant("it works well!")).
                        to("file:///tmp");
            }
        };
        camelContext.addRoutes(routeBuilder);
        System.out.println("camel routes file added");
        Thread.sleep(3000L);
    }

    @Test
    public void publishHttpCamelContextTest() throws Exception {
        CamelContext camelContext = new DefaultCamelContext();
        Assertions.assertNotNull(camelContext);

        DefaultCamelContext.class.cast(camelContext).setName("camel-labs");
        camelContext.start();
        Assertions.assertTrue(camelContext.getStatus().isStarted());
        System.out.println("camel context started");

        RouteBuilder routeBuilder = new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                from("jetty:http://localhost:9090/labs").
                        id("jetty-example").
                        setHeader(Exchange.HTTP_RESPONSE_CODE,constant(200)).
                        setBody(constant("it works well!"));
            }
        };
        camelContext.addRoutes(routeBuilder);
        System.out.println("camel routes jetty added -> http://localhost:9090/labs");
        Thread.sleep(10000L);
    }

    @Test
    public void publishHttpCamelContextWithDefinitionTest() throws Exception {
        CamelContext camelContext = new DefaultCamelContext();
        Assertions.assertNotNull(camelContext);

        DefaultCamelContext.class.cast(camelContext).setName("camel-labs");
        camelContext.start();
        Assertions.assertTrue(camelContext.getStatus().isStarted());
        System.out.println("camel context started");

        final RouteDefinition definition = new RouteDefinition();
        definition.from("jetty:http://localhost:9090/labs").id("jetty-example").
                setHeader(Exchange.HTTP_RESPONSE_CODE,constant(200)).
                setBody(constant("it works well!"));

        DefaultCamelContext.class.cast(camelContext).addRouteDefinition(definition);
        System.out.println("camel routes with definition jetty added -> http://localhost:9090/labs");
        Thread.sleep(10000L);
    }
}
