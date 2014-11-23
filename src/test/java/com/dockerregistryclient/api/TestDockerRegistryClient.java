package com.dockerregistryclient.api;

import java.io.IOException;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.dockerregistryclient.config.DockerRegistryConfig;
import com.dockerregistryclient.data.DockerRegistrySearchResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class TestDockerRegistryClient {

    private final DockerRegistryClient docker;
    private final Client client;
    private final DockerRegistryConfig config;

    public TestDockerRegistryClient () {
        MockitoAnnotations.initMocks(this);
        config = Mockito.mock(DockerRegistryConfig.class);
        client = Mockito.mock(Client.class);
        docker = new DockerRegistryClient(client, config);
    }

    @Test
    public void testDockerRegistrySearch () throws IOException {
        WebResource resource = Mockito.mock(WebResource.class);
        Mockito.when(resource.path("search")).thenReturn(resource);
        Mockito.when(resource.queryParams(Mockito.any(MultivaluedMapImpl.class))).thenReturn(resource);
        WebResource.Builder resourceBuilder = Mockito.mock(WebResource.Builder.class);
        Mockito.when(resourceBuilder.accept(Mockito.anyString())).thenReturn(resourceBuilder);
        ClientResponse response = Mockito.mock(ClientResponse.class);
        Mockito.when(response.getStatusInfo()).thenReturn(Status.OK);
        Mockito.when(response.getEntityInputStream()).thenReturn(
            this.getClass().getResourceAsStream("/test_cattle_registry_search_response.json"));
        Mockito.when(resourceBuilder.get(ClientResponse.class)).thenReturn(response);
        Mockito.when(resource.getRequestBuilder()).thenReturn(resourceBuilder);
        Mockito.when(client.resource(Mockito.anyString())).thenReturn(resource);
        DockerRegistrySearchResponse searchResponse = docker.searchRegistry("cattle");
        Assert.assertEquals(searchResponse.getQuery(), "cattle");
        Assert.assertEquals(searchResponse.getNumResults(), new Integer(18));
    }
    
    @Test
    public void testDockerRegistryImageAncestry () throws IOException {
        
    }
}
