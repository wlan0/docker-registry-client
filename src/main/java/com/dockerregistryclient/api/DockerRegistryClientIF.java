package com.dockerregistryclient.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import com.dockerregistryclient.data.DockerRepositoryContext;
import com.dockerregistryclient.data.DockerRegistrySearchResponse;

public interface DockerRegistryClientIF {

	public DockerRegistrySearchResponse searchRegistry(String searchTerm)
			throws IOException;

	public <T> T makeDockerRegistryTwoStepRequest(
			DockerRepositoryContext context, String path,
			TypeReference<T> typeReference) throws IOException;

	public List<String> getImageAncestry(DockerRepositoryContext context,
			String imageId) throws IOException;

	public InputStream makeDockerRegistryTwoStepRequestForStream(
			DockerRepositoryContext context, String path) throws IOException;
}
