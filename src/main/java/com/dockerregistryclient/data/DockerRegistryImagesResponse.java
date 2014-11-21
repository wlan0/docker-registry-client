package com.dockerregistryclient.data;

import java.util.List;

public class DockerRegistryImagesResponse {

    private final List<DockerRegistryImagesResponseUnit> imagesList;
    private final String registry;

    public DockerRegistryImagesResponse (List<DockerRegistryImagesResponseUnit> imagesList, String registry) {
        this.imagesList = imagesList;
        this.registry = registry;
    }

    public List<DockerRegistryImagesResponseUnit> getImagesList () {
        return imagesList;
    }

    public String getRegistry () {
        return registry;
    }
}
