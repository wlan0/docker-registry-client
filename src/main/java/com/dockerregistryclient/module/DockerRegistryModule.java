package com.dockerregistryclient.module;

import com.dockerregistryclient.api.DockerRegistryClient;
import com.dockerregistryclient.api.DockerRegistryClientIF;
import com.dockerregistryclient.config.DockerRegistryConfig;
import com.dockerregistryclient.config.DockerRegistryConfigProvider;
import com.google.inject.AbstractModule;

public class DockerRegistryModule extends AbstractModule {

    @Override
    protected void configure () {
        bind(DockerRegistryClientIF.class).to(DockerRegistryClient.class);
        bind(DockerRegistryClient.class).toProvider(DockerRegistryClientProvider.class).asEagerSingleton();
        bind(DockerRegistryConfig.class).toProvider(DockerRegistryConfigProvider.class).asEagerSingleton();
    }

}
