package com.dockerregistryclient.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.dockerregistryclient.data.DockerRegistryImageInfo;
import com.dockerregistryclient.data.DockerRepositoryContext;
import com.dockerregistryclient.data.DockerRegistrySearchResponse;

public interface DockerRegistryClientIF {

	public DockerRegistrySearchResponse searchRegistry(String searchTerm)
			throws IOException;

	public List<String> getImageAncestry(DockerRepositoryContext context,
			String imageId) throws IOException;

	public Map<String, String> getRepositoryTags(DockerRepositoryContext context)
			throws IOException;

	public String getImageIdByTag(DockerRepositoryContext context, String tag)
			throws IOException;

	public InputStream getImageById(DockerRepositoryContext context, String id)
			throws IOException;

	public InputStream getImageByTag(DockerRepositoryContext context, String tag)
			throws IOException;

	public DockerRegistryImageInfo getImageJsonById(
			DockerRepositoryContext context, String id) throws IOException;

	public DockerRegistryImageInfo getImageJsonByTag(
			DockerRepositoryContext context, String tag) throws IOException;

	public void putImageLayer(DockerRepositoryContext context, String imageId,
			InputStream imageStream) throws IOException;

	public void putImageLayer(DockerRepositoryContext context, String imageId,
			DockerRegistryImageInfo imageInfo) throws IOException;

	public void setTagByImageId(DockerRepositoryContext context,
			String imageId, String tag) throws IOException;

	public void deleteRepositoryTag(DockerRepositoryContext context, String tag)
			throws IOException;

	public void deleteRepository(DockerRepositoryContext context)
			throws IOException;
}
