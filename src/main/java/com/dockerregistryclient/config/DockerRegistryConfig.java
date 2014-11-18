package com.dockerregistryclient.config;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class DockerRegistryConfig {

    private final Map<String, String> configMap;

    public DockerRegistryConfig (Map<String, String> configMap) {
        this.configMap = configMap;
    }

    public String get (String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        return configMap.get(key);
    }
}
