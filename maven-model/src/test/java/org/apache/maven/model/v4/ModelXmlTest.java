/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.maven.model.v4;

import javax.xml.stream.XMLStreamException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.maven.api.model.Model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelXmlTest {

    @Test
    void testXmlRoundtripWithProperties() throws Exception {
        Map<String, String> props = new LinkedHashMap<>();
        props.put("javax.version", "3.1.0");
        props.put("mockito.version", "1.10.19");
        props.put("hamcret.version", "2.1");
        props.put("lombok.version", "1.18.6");
        props.put("junit.version", "4.12");
        Model model = Model.newBuilder(true).properties(props).build();
        String xml = toXml(model);

        for (int i = 0; i < 10; i++) {
            String newStr = toXml(fromXml(xml));
            assertEquals(newStr, xml);
        }
    }

    String toXml(Model model) throws IOException, XMLStreamException {
        StringWriter sw = new StringWriter();
        MavenStaxWriter writer = new MavenStaxWriter();
        writer.setAddLocationInformation(false);
        writer.write(sw, model);
        return sw.toString();
    }

    Model fromXml(String xml) throws XMLStreamException {
        return new MavenStaxReader().read(new StringReader(xml));
    }
}
