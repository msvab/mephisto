package net.svab.mephisto.model;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import net.svab.mephisto.error.ResourceNotDefinedException;

import java.util.List;

import static net.svab.mephisto.util.ResourceFinder.findMatchingResource;

public class Contract {

    private final List<Resource> resources;

    public Contract(Iterable<Resource> resources) {
        this.resources = ImmutableList.copyOf(resources);
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void validateResource(String method, String path) {
        boolean found = false;
        for (Resource resource : resources) {
            if (resource.getKey().getPath().equals(path) && resource.getKey().getMethod().equalsIgnoreCase(method)) {
                found = true;
                break;
            }
        }

        if (!found) {
            throw new ResourceNotDefinedException(method, path);
        }
    }

    public void validateRequest(ServiceRequest request) {
        Resource resource = findMatchingResource(resources, request.getPath(), request.getMethod());
        request.validate(resource);
    }

    public void validateResponse(ServiceResponse response) {
        Resource resource = findMatchingResource(resources, response.getPath(), response.getMethod());
        response.validate(resource);
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this).add("resources", resources).toString();
    }
}
