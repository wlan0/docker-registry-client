package com.dockerregistryclient.api;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.dockerregistryclient.config.DockerRegistryConfig;
import com.dockerregistryclient.data.DockerRepositoryContext;
import com.dockerregistryclient.data.DockerRegistryImagesResponseUnit;
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
	private static final String repositoriesPrefixPath = "repository/";
	private static final String repositoriesSuffixPath = "/images";

	private static final String registryHeaderPath = "X-Docker-Endpoints";
	private static final String registryTokenPath = "X-Docker-Token";
	private static final ObjectMapper mapper = new ObjectMapper();

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
	public DockerRepositoryContext getDockerRepositoryContext(String owner,
			String repository, String username, String password)
			throws IOException {
		if (StringUtils.isEmpty(owner)) {
			throw new IOException("owner is null/empty");
		}
		if (StringUtils.isEmpty(repository)) {
			throw new IOException(repository);
		}
		String path = repositoriesPrefixPath + owner + "/" + repository
				+ repositoriesSuffixPath;
		Map<String, Object> headers = null;
		if (StringUtils.isNotEmpty(password)) {
			headers = new HashMap<String, Object>();
			headers.put("Authorization", createBasicAuth(username, password));
		}
		ClientResponse response = getClientResponse(path, null, headers);
		String registry = response.getHeaders().getFirst(registryHeaderPath);
		String token = response.getHeaders().getFirst(registryTokenPath);
		List<DockerRegistryImagesResponseUnit> imagesJson = mapper.readValue(
				response.getEntityInputStream(),
				new TypeReference<List<DockerRegistryImagesResponseUnit>>() {
				});
		return new DockerRepositoryContext(imagesJson, registry, token);
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
