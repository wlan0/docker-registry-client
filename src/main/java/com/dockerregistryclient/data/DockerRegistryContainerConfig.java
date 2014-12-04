package com.dockerregistryclient.data;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DockerRegistryContainerConfig {

	private final String hostName;
	private final String user;
	private final Integer memory;
	private final Integer memorySwap;
	private final Boolean attachStdin;
	private final Boolean attachStdout;
	private final Boolean attachStderr;
	private final String portSpecs;
	private final Boolean tty;
	private final Boolean openStdin;
	private final Boolean stdinOnce;
	private final String env;
	private final List<String> cmd;
	private final String dns;
	private final String image;
	private final Map<String, Object> volumes;
	private final String volumesFrom;

	@JsonCreator
	public DockerRegistryContainerConfig(
			@JsonProperty("HostName") String hostName,
			@JsonProperty("User") String user,
			@JsonProperty("Memory") Integer memory,
			@JsonProperty("MemorySwap") Integer memorySwap,
			@JsonProperty("AttachStdin") Boolean attachStdin,
			@JsonProperty("AttachStdout") Boolean attachStdout,
			@JsonProperty("AttachStderr") Boolean attachStderr,
			@JsonProperty("PortSpecs") String portSpecs,
			@JsonProperty("Tty") Boolean tty,
			@JsonProperty("OpenStdin") Boolean openStdin,
			@JsonProperty("StdinOnce") Boolean stdinOnce,
			@JsonProperty("Env") String env,
			@JsonProperty("Cmd") List<String> cmd,
			@JsonProperty("Dns") String dns,
			@JsonProperty("Image") String image,
			@JsonProperty("Volumes") Map<String, Object> volumes,
			@JsonProperty("VolumesFrom") String volumesFrom) {
		this.hostName = hostName;
		this.user = user;
		this.memory = memory;
		this.memorySwap = memorySwap;
		this.attachStdin = attachStdin;
		this.attachStdout = attachStdout;
		this.attachStderr = attachStderr;
		this.portSpecs = portSpecs;
		this.tty = tty;
		this.openStdin = openStdin;
		this.stdinOnce = stdinOnce;
		this.env = env;
		this.cmd = cmd;
		this.dns = dns;
		this.image = image;
		this.volumes = volumes;
		this.volumesFrom = volumesFrom;
	}

	public String getHostName() {
		return hostName;
	}

	public String getUser() {
		return user;
	}

	public Integer getMemory() {
		return memory;
	}

	public Integer getMemorySwap() {
		return memorySwap;
	}

	public Boolean getAttachStdin() {
		return attachStdin;
	}

	public Boolean getAttachStdout() {
		return attachStdout;
	}

	public Boolean getAttachStderr() {
		return attachStderr;
	}

	public String getPortSpecs() {
		return portSpecs;
	}

	public Boolean getTty() {
		return tty;
	}

	public Boolean getOpenStdin() {
		return openStdin;
	}

	public Boolean getStdinOnce() {
		return stdinOnce;
	}

	public String getEnv() {
		return env;
	}

	public List<String> getCmd() {
		return cmd;
	}

	public String getDns() {
		return dns;
	}

	public String getImage() {
		return image;
	}

	public Map<String, Object> getVolumes() {
		return volumes;
	}

	public String getVolumesFrom() {
		return volumesFrom;
	}

}
