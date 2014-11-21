package com.dockerregistryclient.api;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.dockerregistryclient.config.DockerRegistryConfig;
import com.dockerregistryclient.data.DockerRegistryImagesResponse;
import com.dockerregistryclient.data.DockerRegistryImagesResponseUnit;
import com.dockerregistryclient.data.DockerRegistrySearchResponse;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.util.Base64;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class DockerRegistryClient extends AbstractDockerRegistryClient implements DockerRegistryClientIF {

    private static final String searchPath = "search";
    private static final String ancestryPrefixPath = "images/";
    private static final String ancestrySuffixPath = "/ancestry";
    private static final String repositoriesPrefixPath = "repository/";
    private static final String repositoriesSuffixPath = "/images";

    private static final String registryHeaderPath = "X-Docker-Endpoints";
    private static final ObjectMapper mapper = new ObjectMapper();

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
        searchTermMap.add("q", query);
        return getResponse(searchPath, searchTermMap, null, DockerRegistrySearchResponse.class);
    }

    public DockerRegistryImagesResponse getImageInfo (String user, String repository) throws IOException {
        if (StringUtils.isEmpty(user)) {
            throw new IOException("username is null/empty");
        }
        if (StringUtils.isEmpty(repository)) {
            throw new IOException(repository);
        }
        String path = repositoriesPrefixPath + user + "/" + repository + repositoriesSuffixPath;
        ClientResponse response = getClientResponse(path, null, null);
        String registry = response.getHeaders().getFirst(registryHeaderPath);
        List<DockerRegistryImagesResponseUnit> imagesJson = mapper.readValue(response.getEntityInputStream(),
            new TypeReference<List<DockerRegistryImagesResponseUnit>>() {
            });
        return new DockerRegistryImagesResponse(imagesJson, registry);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getImageAncestry (String endpoint, String imageId, String token) throws IOException {
        String path = ancestryPrefixPath + imageId + ancestrySuffixPath;
        return getResponse(endpoint, path, null, ImmutableMap.of("Authorization", "Token " + token), List.class);
    }

    private String createbasicAuth (String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return null;
        }
        return Base64.encode(username + ":" + password).toString();
    }

}
