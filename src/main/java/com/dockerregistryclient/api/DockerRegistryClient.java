package com.dockerregistryclient.api;

import java.io.IOException;
import java.net.URLEncoder;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang3.StringUtils;

import com.dockerregistryclient.config.DockerRegistryConfig;
import com.dockerregistryclient.data.DockerRegistrySearchResponse;
import com.google.inject.Inject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class DockerRegistryClient extends AbstractDockerRegistryClient implements DockerRegistryClientIF {

    private static final String searchPath = "search";
    
    @Inject
    public DockerRegistryClient (Client client, DockerRegistryConfig config) {
        super(client, config);
    }

    @Override
    public DockerRegistrySearchResponse searchRegistry (String query) throws IOException {
        if (StringUtils.isEmpty(query)) {
            throw new IOException("empty query");
        }
        MultivaluedMap<String, String> searchTermMap = new MultivaluedMapImpl();
        searchTermMap.add("q", URLEncoder.encode(query, "UTF-8"));
        return getResponse(searchPath, searchTermMap, null, DockerRegistrySearchResponse.class);
    }

}
