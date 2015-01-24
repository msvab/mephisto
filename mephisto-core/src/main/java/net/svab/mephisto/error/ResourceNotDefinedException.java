package net.svab.mephisto.error;

import static java.lang.String.format;

public class ResourceNotDefinedException extends ContractBrokenException {

    public ResourceNotDefinedException(String method, String path) {
        super(format("Resource %s %s is not defined in the contract", method, path));
    }
}
