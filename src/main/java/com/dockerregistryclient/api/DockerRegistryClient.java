package com.dockerregistryclient.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;

import com.dockerregistryclient.config.DockerRegistryConfig;
import com.dockerregistryclient.data.DockerRegistryImageInfo;
import com.dockerregistryclient.data.DockerRepositoryContext;
import com.dockerregistryclient.data.DockerRegistrySearchResponse;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.util.Base64;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class DockerRegistryClient extends AbstractDockerRegistryClient
		implements DockerRegistryClientIF {

	private static final String searchPath = "search";
	private static final String imagesPrefixPath = "images/";
	private static final String ancestrySuffixPath = "/ancestry";
	private static final String repositoriesPrefixPath = "repositories/";
	private static final String repositoriesSuffixPath = "/images";
	private static final String tagsSuffixPath = "/tags";
	private static final String layerSuffixPath = "/layerSuffixPath";
	private static final String jsonSuffixPath = "/json";

	private static final String registryHeaderPath = "X-Docker-Endpoints";
	private static final String registryTokenPath = "X-Docker-Token";

	@Inject
	public DockerRegistryClient(Client client, DockerRegistryConfig config) {
		super(client, config);
	}

	@Override
	public DockerRegistrySearchResponse searchRegistry(String query)
			throws IOException {
		if (StringUtils.isEmpty(query)) {
			throw new IOException("empty query");
		}
		MultivaluedMap<String, String> searchTermMap = new MultivaluedMapImpl();
		searchTermMap.add("q", URLEncoder.encode(query, "UTF-8"));
		return getResponse(searchPath, searchTermMap, null,
				DockerRegistrySearchResponse.class);
	}

	@Override
	public List<String> getImageAncestry(DockerRepositoryContext context,
			String imageId) throws IOException {
		String path = imagesPrefixPath + imageId + ancestrySuffixPath;
		return makeDockerRegistryTwoStepGetRequest(context, path,
				new TypeReference<List<String>>() {
				});
	}

	@Override
	public Map<String, String> getRepositoryTags(DockerRepositoryContext context)
			throws IOException {
		String path = repositoriesPrefixPath + context.getNamespace() + "/"
				+ context.getRepository() + tagsSuffixPath;
		return makeDockerRegistryTwoStepGetRequest(context, path,
				new TypeReference<Map<String, String>>() {
				});
	}

	@Override
	public String getImageIdByTag(DockerRepositoryContext context, String tag)
			throws IOException {
		if (StringUtils.isEmpty(tag)) {
			throw new IOException("empty or null tag");
		}
		String path = repositoriesPrefixPath + context.getNamespace() + "/"
				+ context.getRepository() + tagsSuffixPath + "/" + tag;
		return makeDockerRegistryTwoStepGetRequest(context, path,
				new TypeReference<String>() {
				});
	}

	@Override
	public InputStream getImageById(DockerRepositoryContext context, String id)
			throws IOException {
		if (StringUtils.isEmpty(id)) {
			throw new IOException("empty or null image id");
		}
		String path = imagesPrefixPath + id + layerSuffixPath;
		return makeDockerRegistryTwoStepGetRequestForStream(context, path);
	}

	@Override
	public InputStream getImageByTag(DockerRepositoryContext context, String tag)
			throws IOException {
		String path = imagesPrefixPath + getImageIdByTag(context, tag)
				+ layerSuffixPath;
		return makeDockerRegistryTwoStepGetRequestForStream(context, path);
	}

	@Override
	public DockerRegistryImageInfo getImageJsonById(
			DockerRepositoryContext context, String id) throws IOException {
		String path = imagesPrefixPath + id + jsonSuffixPath;
		return makeDockerRegistryTwoStepGetRequest(context, path,
				new TypeReference<DockerRegistryImageInfo>() {
				});
	}

	@Override
	public DockerRegistryImageInfo getImageJsonByTag(
			DockerRepositoryContext context, String tag) throws IOException {
		String path = imagesPrefixPath + getImageIdByTag(context, tag)
				+ jsonSuffixPath;
		return makeDockerRegistryTwoStepGetRequest(context, path,
				new TypeReference<DockerRegistryImageInfo>() {
				});
	}

	@Override
	public void putImageLayer(DockerRepositoryContext context, String imageId,
			InputStream imageStream) throws IOException {
		if (StringUtils.isEmpty(imageId)) {
			throw new IOException("empty or null image Id");
		}
		String path = imagesPrefixPath + imageId + layerSuffixPath;
		makeDockerRegistryTwoStepPutRequest(context, path, imageStream);
	}

	@Override
	public void putImageLayer(DockerRepositoryContext context, String imageId,
			DockerRegistryImageInfo imageInfo) throws IOException {
		if (StringUtils.isEmpty(imageId)) {
			throw new IOException("empty or null image Id");
		}
		String path = imagesPrefixPath + imageId + jsonSuffixPath;
		makeDockerRegistryTwoStepPutRequest(context, path, imageInfo);
	}

	@Override
	public void setTagByImageId(DockerRepositoryContext context,
			String imageId, String tag) throws IOException {
		if (StringUtils.isEmpty(imageId)) {
			throw new IOException("empty or null image Id");
		}
		if (StringUtils.isEmpty(tag)) {
			throw new IOException("empty or null tag");
		}
		String path = repositoriesPrefixPath + context.getNamespace() + "/"
				+ context.getRepository() + "/" + tag;
		makeDockerRegistryTwoStepPutRequest(context, path, imageId);
	}

	@Override
	public void deleteRepositoryTag(DockerRepositoryContext context, String tag)
			throws IOException {
		if (StringUtils.isEmpty(tag)) {
			throw new IOException("empty or null tag");
		}
		String path = repositoriesPrefixPath + context.getNamespace() + "/"
				+ context.getRepository() + tagsSuffixPath + "/" + tag;
		makeDockerRegistryTwoStepDeleteRequest(context, path);
	}

	@Override
	public void deleteRepository(DockerRepositoryContext context)
			throws IOException {
		String path = repositoriesPrefixPath + context.getNamespace() + "/"
				+ context.getRepository() + "/";
		makeDockerRegistryTwoStepDeleteRequest(context, path);
	}

	private String createBasicAuth(String username, String password) {
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return null;
		}
		return Base64.encode(username + ":" + password).toString();
	}

	private ClientResponse makeDockerRegistryTwoStepDeleteRequest(
			DockerRepositoryContext context, String path) throws IOException {
		Map<String, Object> headers = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(context.getPassword())
				&& StringUtils.isNotEmpty(context.getUserName())) {
			headers.put(
					"Authorization",
					"Basic "
							+ createBasicAuth(context.getUserName(),
									context.getPassword()));
		}
		headers.put(registryTokenPath, true);
		String tokenPath = repositoriesPrefixPath + context.getNamespace()
				+ "/" + context.getRepository() + repositoriesSuffixPath;
		ClientResponse response = getClientResponse(tokenPath, null, headers);
		String registry = response.getHeaders().getFirst(registryHeaderPath);
		String token = response.getHeaders().getFirst(registryTokenPath);
		Map<String, Object> secondHeaders = null;
		if (token != null) {
			secondHeaders = ImmutableMap.of("Authorization", "Token " + token);
		}
		return deleteClientRequest(registry, path, null, secondHeaders);
	}

	private ClientResponse makeDockerRegistryTwoStepPutRequest(
			DockerRepositoryContext context, String path, Object objRequest)
			throws IOException {
		Map<String, Object> headers = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(context.getPassword())
				&& StringUtils.isNotEmpty(context.getUserName())) {
			headers.put(
					"Authorization",
					"Basic "
							+ createBasicAuth(context.getUserName(),
									context.getPassword()));
		}
		headers.put(registryTokenPath, true);
		String tokenPath = repositoriesPrefixPath + context.getNamespace()
				+ "/" + context.getRepository() + repositoriesSuffixPath;
		ClientResponse response = getClientResponse(tokenPath, null, headers);
		String registry = response.getHeaders().getFirst(registryHeaderPath);
		String token = response.getHeaders().getFirst(registryTokenPath);
		Map<String, Object> secondHeaders = null;
		if (token != null) {
			secondHeaders = ImmutableMap.of("Authorization", "Token " + token);
		}
		return putClientRequest(registry, path, null, secondHeaders, objRequest);
	}

	private InputStream makeDockerRegistryTwoStepGetRequestForStream(
			DockerRepositoryContext context, String path) throws IOException {
		Map<String, Object> headers = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(context.getPassword())
				&& StringUtils.isNotEmpty(context.getUserName())) {
			headers.put(
					"Authorization",
					"Basic "
							+ createBasicAuth(context.getUserName(),
									context.getPassword()));
		}
		headers.put(registryTokenPath, true);
		String tokenPath = repositoriesPrefixPath + context.getNamespace()
				+ "/" + context.getRepository() + repositoriesSuffixPath;
		ClientResponse response = getClientResponse(tokenPath, null, headers);
		String registry = response.getHeaders().getFirst(registryHeaderPath);
		String token = response.getHeaders().getFirst(registryTokenPath);
		Map<String, Object> secondHeaders = null;
		if (token != null) {
			secondHeaders = ImmutableMap.of("Authorization", "Token " + token);
		}
		ClientResponse secondResponse = getClientResponse(registry, path, null,
				secondHeaders);
		return secondResponse.getEntityInputStream();
	}

	private <T> T makeDockerRegistryTwoStepGetRequest(
			DockerRepositoryContext context, String path,
			TypeReference<T> typeReference) throws IOException {
		Map<String, Object> headers = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(context.getPassword())
				&& StringUtils.isNotEmpty(context.getUserName())) {
			headers.put(
					"Authorization",
					"Basic "
							+ createBasicAuth(context.getUserName(),
									context.getPassword()));
		}
		headers.put(registryTokenPath, true);
		String tokenPath = repositoriesPrefixPath + context.getNamespace()
				+ "/" + context.getRepository() + repositoriesSuffixPath;
		ClientResponse response = getClientResponse(tokenPath, null, headers);
		String registry = response.getHeaders().getFirst(registryHeaderPath);
		String token = response.getHeaders().getFirst(registryTokenPath);
		Map<String, Object> secondHeaders = null;
		if (token != null) {
			secondHeaders = ImmutableMap.of("Authorization", "Token " + token);
		}
		return getResponse(registry, path, null, secondHeaders, typeReference);
	}

}
