package com.dockerregistryclient.api;

import java.io.IOException;
import java.util.List;

import com.dockerregistryclient.data.DockerRepositoryContext;
import com.dockerregistryclient.data.DockerRegistrySearchResponse;

public interface DockerRegistryClientIF {

	public DockerRegistrySearchResponse searchRegistry(String searchTerm)
			throws IOException;

	public DockerRepositoryContext getDockerRepositoryContext(String owner,
			String repository, String user, String password) throws IOException;

	public List<String> getImageAncestry(String endpoint, String imageId,
			String token) throws IOException;
}
