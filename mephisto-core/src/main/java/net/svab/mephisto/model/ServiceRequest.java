package net.svab.mephisto.model;

import net.svab.mephisto.error.InvalidResourceBodyException;
import net.svab.mephisto.error.RequiredHeaderMissingException;
import net.svab.mephisto.error.RequiredQueryParametersMissingException;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static java.lang.String.format;

public class ServiceRequest {

    private final String path;
    private final String method;
    private final String contentType;
    private final Set<ServiceQueryParameter> queryParams;
    private final Set<ServiceHeader> headers;

    public ServiceRequest(String path, String method, String contentType, Set<ServiceQueryParameter> queryParams,
                          Set<ServiceHeader> headers) {
        this.path = path;
        this.method = method;
        this.contentType = contentType;
        this.queryParams = queryParams;
        this.headers = headers;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public void validate(Resource resource) {
        if (contentType != null && !resource.getRequestContentTypes().contains(contentType)) {
            throw new InvalidResourceBodyException(method, path, contentType);
        }
        validateQueryParams(resource);
        validateHeaders(resource);
    }

    private void validateHeaders(Resource resource) {
        Set<Header> requiredHeaders = newHashSet(resource.getRequiredHeaders());
        for (ServiceHeader header : headers) {
            Header validatedBy = validateHeader(header, resource);
            if (validatedBy != null) {
                requiredHeaders.remove(validatedBy);
            }
        }
        if (!requiredHeaders.isEmpty()) {
            throw new RequiredHeaderMissingException(requiredHeaders, resource.getKey());
        }
    }

    private void validateQueryParams(Resource resource) {
        Set<String> requiredQueryParameters = newHashSet(resource.getRequiredQueryParameters());
        for (ServiceQueryParameter queryParam : queryParams) {
            queryParam.validate(resource.getQueryParameters().get(queryParam.getName()), resource.getKey());
            requiredQueryParameters.remove(queryParam.getName());
        }
        if (!requiredQueryParameters.isEmpty()) {
            throw new RequiredQueryParametersMissingException(requiredQueryParameters, resource.getKey());
        }
    }

    private Header validateHeader(ServiceHeader header, Resource resource) {
        Header headerDefinition = resource.getHeader(header.getName());
        header.validate(headerDefinition, resource.getKey());
        return headerDefinition;
    }

    @Override public String toString() {
        return format("%s %s (%s)", method, path, contentType);
    }
}
