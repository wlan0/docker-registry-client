package com.dockerregistryclient.api;

import java.io.IOException;

import com.dockerregistryclient.data.DockerRegistrySearchResponse;

public interface DockerRegistryClientIF {

    public DockerRegistrySearchResponse searchRegistry (String searchTerm) throws IOException;
}
