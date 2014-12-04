package com.dockerregistryclient.data;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DockerRegistryImageInfo {

	private final String id;
	private final String parent;
	private final String created;
	private final String container;
	private final String dockerVersion;
	private final DockerRegistryContainerConfig config;

	@JsonCreator
	public DockerRegistryImageInfo(
			@JsonProperty("id") String id,
			@JsonProperty("parent") String parent,
			@JsonProperty("created") String created,
			@JsonProperty("container") String container,
			@JsonProperty("docker_version") String dockerVersion,
			@JsonProperty("container_config") DockerRegistryContainerConfig config) {
		this.id = id;
		this.parent = parent;
		this.created = created;
		this.container = container;
		this.dockerVersion = dockerVersion;
		this.config = config;
	}

	public String getId() {
		return id;
	}

	public String getParent() {
		return parent;
	}

	public String getCreated() {
		return created;
	}

	public String getContainer() {
		return container;
	}

	public String getDockerversion() {
		return dockerVersion;
	}

	public DockerRegistryContainerConfig getConfig() {
		return config;
	}

}
