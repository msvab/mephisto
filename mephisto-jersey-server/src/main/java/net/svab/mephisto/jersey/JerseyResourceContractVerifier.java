package net.svab.mephisto.jersey;

import net.svab.mephisto.model.Contract;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceMethod;

public class JerseyResourceContractVerifier {

    private final Contract contract;

    public JerseyResourceContractVerifier(Contract contract) {
        this.contract = contract;
    }

    public void validateResources(ResourceConfig resourceConfig) {
        for (Class<?> resourceClass : resourceConfig.getClasses()) {
            Resource jerseyResource = Resource.builder(resourceClass).build();
            String uriPrefix = jerseyResource.getPath();
            if (!uriPrefix.startsWith("/")) {
                uriPrefix = "/" + uriPrefix;
            }

            for (Resource childResource : jerseyResource.getChildResources()) {
                contract.validateResource(childResource.getResourceMethods().get(0).getHttpMethod(), uriPrefix + "/" + childResource.getPath());
            }

            for (ResourceMethod resourceMethod : jerseyResource.getResourceMethods()) {
                contract.validateResource(resourceMethod.getHttpMethod(), uriPrefix);
            }
        }
    }
}
