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
    private static final String ancestryPrefixPath = "images/";
    private static final String ancestrySuffixPath = "/ancestry";
    private static final String repositoriesPrefixPath = "repositories/";
    private static final String repositoriesSuffixPath = "/images";

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
	public InputStream makeDockerRegistryTwoStepRequestForStream(
			DockerRepositoryContext context, String path) throws IOException {
		Map<String, Object> headers = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(context.getPassword())
				&& StringUtils.isNotEmpty(context.getUserName())) {
			headers.put(
					"Authorization",
					createBasicAuth(context.getUserName(),
							context.getPassword()));
		}
		headers.put(registryTokenPath, "true");
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

	@Override
	public <T> T makeDockerRegistryTwoStepRequest(
			DockerRepositoryContext context, String path,
			TypeReference<T> typeReference) throws IOException {
		Map<String, Object> headers = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(context.getPassword())
				&& StringUtils.isNotEmpty(context.getUserName())) {
			headers.put(
					"Authorization",
					createBasicAuth(context.getUserName(),
							context.getPassword()));
		}
		headers.put(registryTokenPath, "true");
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

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getImageAncestry(String endpoint, String imageId,
			String token) throws IOException {
		String path = ancestryPrefixPath + imageId + ancestrySuffixPath;
		return getResponse(endpoint, path, null,
				ImmutableMap.of("Authorization", "Token " + token), List.class);
	}

	private String createBasicAuth(String username, String password) {
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return null;
		}
		return Base64.encode(username + ":" + password).toString();
	}

}
