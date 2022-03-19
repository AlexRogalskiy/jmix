/*
 * Copyright 2020 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.jmix.search.index.mapping.fieldmapper.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Sets;
import io.jmix.search.index.mapping.ParameterKeys;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Component("search_FileFieldMapper")
public class FileFieldMapper extends AbstractFieldMapper {

    protected static final Set<String> supportedParameters = Collections.unmodifiableSet(
            Sets.newHashSet(ParameterKeys.ANALYZER)
    );

    @Override
    public Set<String> getSupportedMappingParameters() {
        return supportedParameters;
    }

    @Override
    public ObjectNode createJsonConfiguration(Map<String, Object> parameters) {
        Map<String, Object> effectiveParameters = createEffectiveParameters(parameters);
        effectiveParameters.put("type", "text");

        JsonNode config = objectMapper.valueToTree(effectiveParameters);

        ObjectNode root = JsonNodeFactory.instance.objectNode();
        ObjectNode properties = root.putObject("properties");
        properties.set("_file_name", config);
        properties.set("_content", config);
        return root;
    }
}
