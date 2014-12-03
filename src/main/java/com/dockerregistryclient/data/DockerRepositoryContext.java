package com.dockerregistryclient.data;

import java.util.Map;

/* Context maintains the state required for a particular repository
 */
public class DockerRepositoryContext {

	private final String namespace;
	private final String repository;
	private Map<String, String> tagsToImageIdMap;

	private String userName;
	private String password;

	// one context per repository
	public DockerRepositoryContext(String namespace, String repository) {
		this.namespace = namespace;
		this.repository = repository;
	}

	public void setAuth(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	public Map<String, String> getTagsToImageIdMap() {
		return tagsToImageIdMap;
	}

	public void setTagsToImageIdMap(Map<String, String> tagsToImageIdMap) {
		this.tagsToImageIdMap = tagsToImageIdMap;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getRepository() {
		return repository;
	}

}
