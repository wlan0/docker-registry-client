package com.dockerregistryclient.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.dockerregistryclient.config.DockerRegistryConfig;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public abstract class AbstractDockerRegistryClient implements DockerRegistryClientIF {

    private static final ObjectMapper mapper = new ObjectMapper();
    // setting this through the constructor, rather than a private static make
    // it easier to test (by mocking out client)
    private final Client client;
    private final String baseUrl;

    private static final String VERSION = "v1/";

    public AbstractDockerRegistryClient (Client client, DockerRegistryConfig config) {
        this.client = client;
        this.baseUrl = config.get("default.base_url") + VERSION;
    }

    @SuppressWarnings("unchecked")
    protected Map<String, Object> getResponseMap (String path, MultivaluedMap<String, String> queryParams,
        String cookie, Map<String, Object> headers) throws IOException {
        return getResponse(path, queryParams, headers, Map.class);
    }

    protected <T> T getResponse (String path, MultivaluedMap<String, String> queryParams,
        Map<String, Object> headers, Class<T> type) throws IOException {
        if (StringUtils.isEmpty(baseUrl)) {
            throw new IOException("base url is empty!!");
        }
        if (path == null) {
            path = "";
        }
        if (queryParams == null) {
            queryParams = new MultivaluedMapImpl();
        }
        if (headers == null) {
            headers = new HashMap<>();
        }
        WebResource.Builder resource = client.resource(baseUrl).path(path).queryParams(queryParams).getRequestBuilder();
        for (Map.Entry<String, Object> header : headers.entrySet()) {
            resource = resource.header(header.getKey(), header.getValue());
        }
        ClientResponse response = resource.accept("application/json").get(ClientResponse.class);
        if (response.getStatusInfo() != Status.OK) {
            throw new IOException("got a non-200 response from client, status = "
                + Integer.toString(response.getStatus()));
        }
        T responseObject = mapper.readValue(response.getEntityInputStream(), type);
        return responseObject;

    }
}
