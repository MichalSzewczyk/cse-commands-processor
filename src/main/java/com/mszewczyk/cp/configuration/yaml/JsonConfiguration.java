package com.mszewczyk.cp.configuration.yaml;

import com.mszewczyk.cp.controller.configuration.ControllerConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.Map;
import java.util.function.BiFunction;

@Slf4j
public class JsonConfiguration implements ControllerConfiguration {
    private static final String PATHS_PREFIX = "/cp/http/paths";
    private static final String ADD_TO_GROUP_PATH_KEY = "addToGroup";
    private static final String REMOVE_FROM_GROUP_PATH_KEY = "removeFromGroup";
    private final String addToGroupPath;
    private final String removeFromGroupPath;

    public JsonConfiguration(Path configFilePath, BiFunction<String, Path, Map<String, String>> mapLoader) {
        Map<String, String> configurationMap = mapLoader.apply(PATHS_PREFIX, configFilePath);
        addToGroupPath = configurationMap.get(ADD_TO_GROUP_PATH_KEY);
        removeFromGroupPath = configurationMap.get(REMOVE_FROM_GROUP_PATH_KEY);
        log.info("Initialized configuration. [configurationMap={}]", configurationMap);
    }

    @Override
    public String addToGroupPath() {
        return addToGroupPath;
    }

    @Override
    public String removeFromGroupPath() {
        return removeFromGroupPath;
    }
}
