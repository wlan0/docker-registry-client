package com.dockerregistryclient.data;

import java.util.List;

public class DockerRepositoryContext {

	private final List<DockerRegistryImagesResponseUnit> imagesList;
	private final String registry;
	private final String token;

	public DockerRepositoryContext(
			List<DockerRegistryImagesResponseUnit> imagesList, String registry,
			String token) {
		this.imagesList = imagesList;
		this.registry = registry;
		this.token = token;
	}

	public List<DockerRegistryImagesResponseUnit> getImagesList() {
		return imagesList;
	}

	public String getRegistry() {
		return registry;
	}

	public String getToken() {
		return token;
	}
}
