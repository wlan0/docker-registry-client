package com.dockerregistryclient.module;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.dockerregistryclient.api.DockerRegistryClient;
import com.dockerregistryclient.config.DockerRegistryConfig;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class DockerRegistryClientProvider implements Provider<DockerRegistryClient> {

    private static final Client client;
    static {
        ClientConfig cc = new DefaultClientConfig();
        cc.getClasses().add(JacksonJsonProvider.class);
        cc.getProperties().put(ClientConfig.PROPERTY_READ_TIMEOUT, 500);
        cc.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 500);
        client = Client.create(cc);
    }

    private final DockerRegistryClient docker;

    @Inject
    public DockerRegistryClientProvider (DockerRegistryConfig config) {
        String userName = config.get("default.user_name");
        String password = config.get("default.password");
        if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(password)) {
            client.addFilter(new HTTPBasicAuthFilter(userName, password));
        }
        docker = new DockerRegistryClient(client, config);
    }

    @Override
    public DockerRegistryClient get () {
        return docker;
    }

}
