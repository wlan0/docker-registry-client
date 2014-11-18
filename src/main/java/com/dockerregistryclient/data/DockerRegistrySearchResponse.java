package com.dockerregistryclient.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DockerRegistrySearchResponse {
    private final String query;
    private final Integer numResults;
    private final List<DockerRegistrySearchResponseResult> results;

    @JsonCreator
    public DockerRegistrySearchResponse (@JsonProperty("query") String query,
        @JsonProperty("num_results") Integer numResults,
        @JsonProperty("results") List<DockerRegistrySearchResponseResult> results) {
        this.query = query;
        this.numResults = numResults;
        this.results = results;
    }

    public String getQuery () {
        return query;
    }

    public Integer getNumResults () {
        return numResults;
    }

    public List<DockerRegistrySearchResponseResult> getResults () {
        return results;
    }

}
