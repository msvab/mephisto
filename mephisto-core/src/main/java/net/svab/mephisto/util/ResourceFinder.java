package net.svab.mephisto.util;

import net.svab.mephisto.error.ResourceNotDefinedException;
import net.svab.mephisto.model.Resource;

import java.util.List;

import static net.svab.mephisto.util.PathPatternGenerator.pathPattern;

public class ResourceFinder {

    public static Resource findMatchingResource(List<Resource> resources, String path, String method) {
        for (Resource resource : resources) {
            if (pathPattern(resource).matcher(path).matches()) {
                if (method.equalsIgnoreCase(resource.getKey().getMethod())) {
                    return resource;
                }
            }
        }

        throw new ResourceNotDefinedException(method, path);
    }
}
