package net.svab.mephisto.error;

import net.svab.mephisto.model.ResourceKey;

import static java.lang.String.format;

public class ResponseNotDefinedException extends ContractBrokenException {

    public ResponseNotDefinedException(int statusCode, ResourceKey resource) {
        super(format("Response with status %d is not defined in the contract of resource %s", statusCode, resource));
    }

    public ResponseNotDefinedException(int statusCode, ResourceKey key, String contentType) {
        super(format("Content type %s is not defined in the contract of resource %s for response %d", contentType, key, statusCode));
    }
}
