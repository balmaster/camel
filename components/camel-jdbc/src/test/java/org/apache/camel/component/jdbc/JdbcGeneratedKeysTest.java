/**
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
package org.apache.camel.component.jdbc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.junit.Test;


public class JdbcGeneratedKeysTest extends JdbcRouteTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testRetrieveGeneratedKeys() throws Exception {
        // first we create our exchange using the endpoint
        Endpoint endpoint = context.getEndpoint("direct:hello");

        Exchange exchange = endpoint.createExchange();
        // then we set the SQL on the in body
        exchange.getIn().setBody("insert into tableWithAutoIncr (content) values ('value2')");
        exchange.getIn().setHeader(JdbcConstants.JDBC_RETRIEVE_GENERATED_KEYS, true);

        // now we send the exchange to the endpoint, and receives the response from Camel
        Exchange out = template.send(endpoint, exchange);

        // assertions of the response
        assertNotNull(out);
        assertNotNull(out.getOut());
        assertNotNull(out.getOut().getHeader(JdbcConstants.JDBC_GENERATED_KEYS_DATA));
        assertNotNull(out.getOut().getHeader(JdbcConstants.JDBC_GENERATED_KEYS_ROW_COUNT));

        List<Map<String, Object>> generatedKeys = out.getOut().getHeader(JdbcConstants.JDBC_GENERATED_KEYS_DATA, List.class);
        assertNotNull("out body could not be converted to an ArrayList - was: "
                + out.getOut().getBody(), generatedKeys);
        assertEquals(1, generatedKeys.size());

        Map<String, Object> row = generatedKeys.get(0);
        assertEquals("auto increment value should be 2", BigDecimal.valueOf(2), row.get("1"));

        assertEquals("generated keys row count should be one", 1, out.getOut().getHeader(JdbcConstants.JDBC_GENERATED_KEYS_ROW_COUNT));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRetrieveGeneratedKeysWithStringGeneratedColumns() throws Exception {
        // first we create our exchange using the endpoint
        Endpoint endpoint = context.getEndpoint("direct:hello");

        Exchange exchange = endpoint.createExchange();
        // then we set the SQL on the in body
        exchange.getIn().setBody("insert into tableWithAutoIncr (content) values ('value2')");
        exchange.getIn().setHeader(JdbcConstants.JDBC_RETRIEVE_GENERATED_KEYS, true);
        exchange.getIn().setHeader(JdbcConstants.JDBC_GENERATED_COLUMNS, new String[]{"ID"});

        // now we send the exchange to the endpoint, and receives the response from Camel
        Exchange out = template.send(endpoint, exchange);

        // assertions of the response
        assertNotNull(out);
        assertNotNull(out.getOut());
        assertNotNull(out.getOut().getHeader(JdbcConstants.JDBC_GENERATED_KEYS_DATA));
        assertNotNull(out.getOut().getHeader(JdbcConstants.JDBC_GENERATED_KEYS_ROW_COUNT));

        List<Map<String, Object>> generatedKeys = out.getOut().getHeader(JdbcConstants.JDBC_GENERATED_KEYS_DATA, List.class);
        assertNotNull("out body could not be converted to an ArrayList - was: "
                + out.getOut().getBody(), generatedKeys);
        assertEquals(1, generatedKeys.size());

        Map<String, Object> row = generatedKeys.get(0);
        assertEquals("auto increment value should be 2", BigDecimal.valueOf(2), row.get("1"));

        assertEquals("generated keys row count should be one", 1, out.getOut().getHeader(JdbcConstants.JDBC_GENERATED_KEYS_ROW_COUNT));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRetrieveGeneratedKeysWithIntGeneratedColumns() throws Exception {
        // first we create our exchange using the endpoint
        Endpoint endpoint = context.getEndpoint("direct:hello");

        Exchange exchange = endpoint.createExchange();
        // then we set the SQL on the in body
        exchange.getIn().setBody("insert into tableWithAutoIncr (content) values ('value2')");
        exchange.getIn().setHeader(JdbcConstants.JDBC_RETRIEVE_GENERATED_KEYS, true);
        exchange.getIn().setHeader(JdbcConstants.JDBC_GENERATED_COLUMNS, new int[]{1});

        // now we send the exchange to the endpoint, and receives the response from Camel
        Exchange out = template.send(endpoint, exchange);

        // assertions of the response
        assertNotNull(out);
        assertNotNull(out.getOut());
        assertNotNull(out.getOut().getHeader(JdbcConstants.JDBC_GENERATED_KEYS_DATA));
        assertNotNull(out.getOut().getHeader(JdbcConstants.JDBC_GENERATED_KEYS_ROW_COUNT));

        List<Map<String, Object>> generatedKeys = out.getOut().getHeader(JdbcConstants.JDBC_GENERATED_KEYS_DATA, List.class);
        assertNotNull("out body could not be converted to an ArrayList - was: "
                + out.getOut().getBody(), generatedKeys);
        assertEquals(1, generatedKeys.size());

        Map<String, Object> row = generatedKeys.get(0);
        assertEquals("auto increment value should be 2", BigDecimal.valueOf(2), row.get("1"));

        assertEquals("generated keys row count should be one", 1, out.getOut().getHeader(JdbcConstants.JDBC_GENERATED_KEYS_ROW_COUNT));
    }

    @Test
    public void testGivenAnInvalidGeneratedColumnsHeaderThenAnExceptionIsThrown() throws Exception {
        // first we create our exchange using the endpoint
        Endpoint endpoint = context.getEndpoint("direct:hello");

        Exchange exchange = endpoint.createExchange();
        // then we set the SQL on the in body
        exchange.getIn().setBody("insert into tableWithAutoIncr (content) values ('value2')");
        exchange.getIn().setHeader(JdbcConstants.JDBC_RETRIEVE_GENERATED_KEYS, true);

        // set wrong data type for generated columns
        exchange.getIn().setHeader(JdbcConstants.JDBC_GENERATED_COLUMNS, new Object[]{});

        // now we send the exchange to the endpoint, and receives the response from Camel
        template.send(endpoint, exchange);

        assertTrue(exchange.isFailed());
    }
}
