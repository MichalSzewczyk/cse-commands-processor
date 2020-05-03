package com.mszewczyk.cp.configuration.yaml;

import com.mszewczyk.cp.configuration.ApplicationConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.Map;
import java.util.function.BiFunction;

@Slf4j
public class JsonConfiguration implements ApplicationConfiguration {
    private static final String PATHS_PREFIX = "/cp/http/paths";
    private static final String DATABASE_PREFIX = "/cp/persistence";
    private static final String ADD_TO_GROUP_PATH_KEY = "addToGroup";
    private static final String REMOVE_FROM_GROUP_PATH_KEY = "removeFromGroup";
    private static final String DATA_CENTER_KEY = "datacenter";
    private static final String DB_URL_KEY = "dbUrl";
    private static final String PORT_KEY = "port";
    private static final String KEYSPACE_KEY = "keyspace";
    private final String addToGroupPath;
    private final String removeFromGroupPath;
    private final String dataCenter;
    private final String dbUrl;
    private final int port;
    private final String keyspace;

    public JsonConfiguration(Path configFilePath, BiFunction<String, Path, Map<String, String>> mapLoader) {
        Map<String, String> pathsConfigurationMap = mapLoader.apply(PATHS_PREFIX, configFilePath);
        addToGroupPath = pathsConfigurationMap.get(ADD_TO_GROUP_PATH_KEY);
        removeFromGroupPath = pathsConfigurationMap.get(REMOVE_FROM_GROUP_PATH_KEY);

        Map<String, String> persistenceConfigurationMap = mapLoader.apply(DATABASE_PREFIX, configFilePath);
        dataCenter = persistenceConfigurationMap.get(DATA_CENTER_KEY);
        dbUrl = persistenceConfigurationMap.get(DB_URL_KEY);
        port = Integer.parseInt(persistenceConfigurationMap.get(PORT_KEY));
        keyspace = persistenceConfigurationMap.get(KEYSPACE_KEY);
        log.info("Initialized configuration. [pathsConfigurationMap={}] [persistenceConfigurationMap={}]", pathsConfigurationMap, persistenceConfigurationMap);
    }

    @Override
    public String addToGroupPath() {
        return addToGroupPath;
    }

    @Override
    public String removeFromGroupPath() {
        return removeFromGroupPath;
    }

    @Override
    public String dataCenter() {
        return dataCenter;
    }

    @Override
    public String dbUrl() {
        return dbUrl;
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public String keySpace() {
        return keyspace;
    }
}
