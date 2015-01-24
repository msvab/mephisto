package net.svab.mephisto.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

public class Resource {

    private final ResourceKey key;
    private final Set<UriParameter> uriParameters;
    private final Set<String> requestContentTypes;
    private final Map<Integer, Response> responses;
    private final Map<String, QueryParameter> queryParameters;
    private final Set<Header> headers;

    public Resource(ResourceKey key,
                    Iterable<UriParameter> uriParameters,
                    Map<Integer, Response> responses,
                    Set<String> requestContentTypes,
                    Map<String, QueryParameter> queryParameters,
                    Set<Header> headers) {
        this.key = key;
        this.headers = ImmutableSet.copyOf(headers);
        this.queryParameters = ImmutableMap.copyOf(queryParameters);
        this.uriParameters = ImmutableSet.copyOf(uriParameters);
        this.responses = ImmutableMap.copyOf(responses);
        this.requestContentTypes = ImmutableSet.copyOf(requestContentTypes);
    }

    public ResourceKey getKey() {
        return key;
    }

    public Set<UriParameter> getUriParameters() {
        return uriParameters;
    }

    public Map<Integer, Response> getResponses() {
        return responses;
    }

    public Set<String> getRequestContentTypes() {
        return requestContentTypes;
    }

    public Map<String, QueryParameter> getQueryParameters() {
        return queryParameters;
    }

    public Set<Header> getHeaders() {
        return headers;
    }

    public Header getHeader(String name) {
        for (Header header : headers) {
            if (header.hasMatchingName(name)) {
                return header;
            }
        }
        return null;
    }

    public Set<String> getRequiredQueryParameters() {
        return Maps.filterEntries(queryParameters, new Predicate<Map.Entry<String, QueryParameter>>() {
            @Override public boolean apply(Map.Entry<String, QueryParameter> entry) {
                return entry.getValue().isRequired();
            }
        }).keySet();
    }

    public Set<Header> getRequiredHeaders() {
        return Sets.filter(headers, new Predicate<Header>() {
            @Override public boolean apply(Header header) {
                return header.isRequired();
            }
        });
    }

    @Override public int hashCode() {
        return Objects.hashCode(key, uriParameters, responses, requestContentTypes, queryParameters, headers);
    }

    @Override public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null || getClass() != obj.getClass()) {return false;}
        final Resource other = (Resource) obj;
        return Objects.equal(this.key, other.key)
                && Objects.equal(this.uriParameters, other.uriParameters)
                && Objects.equal(this.responses, other.responses)
                && Objects.equal(this.requestContentTypes, other.requestContentTypes)
                && Objects.equal(this.headers, other.headers)
                && Objects.equal(this.queryParameters, other.queryParameters);
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("key", key)
                .add("uriParameters", uriParameters)
                .add("responses", responses)
                .add("requestContentTypes", requestContentTypes)
                .add("headers", headers)
                .add("queryParameters", queryParameters).toString();
    }
}
