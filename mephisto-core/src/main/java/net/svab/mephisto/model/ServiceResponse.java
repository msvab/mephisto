package net.svab.mephisto.model;

import com.google.common.base.MoreObjects;
import net.svab.mephisto.error.ResponseNotDefinedException;

public class ServiceResponse {
    private final String path;
    private final String method;
    private final int status;
    private final String contentType;

    public ServiceResponse(String path, String method, int status, String contentType) {
        this.path = path;
        this.method = method;
        this.status = status;
        this.contentType = contentType;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public void validate(Resource resource) {
        if (!resource.getResponses().containsKey(status)) {
            throw new ResponseNotDefinedException(status, resource.getKey());
        }

        Response definedResponse = resource.getResponses().get(status);
        if (contentType != null && !definedResponse.getContentTypes().contains(contentType)) {
            throw new ResponseNotDefinedException(status, resource.getKey(), contentType);
        }
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this).add("method", method).add("path", path).add("status", status).add("contentType", contentType).toString();
    }
}
