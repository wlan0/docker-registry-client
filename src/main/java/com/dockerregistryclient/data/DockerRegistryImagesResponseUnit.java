package com.dockerregistryclient.data;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DockerRegistryImagesResponseUnit {

    private final String id;
    private final String checksum;

    @JsonCreator
    public DockerRegistryImagesResponseUnit (@JsonProperty("id") String id, @JsonProperty("checksum") String checksum) {
        this.id = id;
        this.checksum = checksum;
    }

    public String getId () {
        return id;
    }

    public String getChecksum () {
        return checksum;
    }

}
