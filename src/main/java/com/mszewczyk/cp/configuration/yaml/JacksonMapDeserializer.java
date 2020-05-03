package com.mszewczyk.cp.configuration.yaml;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Slf4j
public class JacksonMapDeserializer implements BiFunction<String, Path, Map<String, String>> {
    private final ObjectMapper mapper;
    private final TypeReference<HashMap<String, String>> typeRef;

    public JacksonMapDeserializer() {
        mapper = new ObjectMapper();
        typeRef = new TypeReference<>() {
        };
    }

    @Override
    public Map<String, String> apply(String key, Path path) {
        log.info("Loading map from json file. [path={}]", path);
        try {
            JsonNode node = mapper.readTree(Files.readString(path));
            JsonNode pathsNode = node.at(key);
            return mapper.convertValue(pathsNode, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read map from yaml.", e);
        }
    }
}
