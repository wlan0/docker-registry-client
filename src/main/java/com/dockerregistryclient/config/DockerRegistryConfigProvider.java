package com.dockerregistryclient.config;

import java.io.InputStream;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.inject.Provider;

public class DockerRegistryConfigProvider implements Provider<DockerRegistryConfig> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public DockerRegistryConfig get () {
        DockerRegistryConfig config = null;
        try (InputStream stream = this.getClass().getResourceAsStream("/docker_registry_config.json")) {
            @SuppressWarnings("unchecked")
            Map<String, String> configMap = mapper.readValue(stream, Map.class);
            config = new DockerRegistryConfig(configMap);
        } catch (Exception e) {
            throw new RuntimeException("could not load config for docker_registry");
        }
        return config;
    }
}
