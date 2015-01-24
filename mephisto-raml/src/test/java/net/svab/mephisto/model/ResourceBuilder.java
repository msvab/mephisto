package net.svab.mephisto.model;

import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

public class ResourceBuilder {

    private String path = "/some/path";
    private String method = "GET";
    private Set<UriParameter> uriParameters = newHashSet();
    private Map<Integer, Response> responses = newHashMap();
    private Set<String> requestContentTypes = newHashSet();
    private final Map<String, QueryParameter> queryParameters = newHashMap();
    private final Set<Header> headers = newHashSet();

    private ResourceBuilder() {}

    public static ResourceBuilder resourceBuilder() { return new ResourceBuilder();}

    public ResourceBuilder path(String path) {
        this.path = path;
        return this;
    }

    public ResourceBuilder method(String method) {
        this.method = method;
        return this;
    }

    public ResourceBuilder uriParameter(UriParameter parameter) {
        uriParameters.add(parameter);
        return this;
    }

    public ResourceBuilder response(int responseCode, Response response) {
        responses.put(responseCode, response);
        return this;
    }

    public ResourceBuilder queryParameter(String name, QueryParameter queryParameter) {
        queryParameters.put(name, queryParameter);
        return this;
    }

    public ResourceBuilder header(Header header) {
        headers.add(header);
        return this;
    }

    public ResourceBuilder requestContentType(String requestContentType) {
        requestContentTypes.add(requestContentType);
        return this;
    }

    public Resource build() {
        return new Resource(new ResourceKey(path, method), uriParameters, responses, requestContentTypes, queryParameters, headers);
    }
}
