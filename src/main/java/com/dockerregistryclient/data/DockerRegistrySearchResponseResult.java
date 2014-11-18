package com.dockerregistryclient.data;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DockerRegistrySearchResponseResult {

    private final String name;
    private final String description;

    @JsonCreator
    public DockerRegistrySearchResponseResult (@JsonProperty("name") String name,
        @JsonProperty("description") String description) {
        this.name = name;
        this.description = description;
    }

    public String getName () {
        return name;
    }

    public String getDescription () {
        return description;
    }

}
