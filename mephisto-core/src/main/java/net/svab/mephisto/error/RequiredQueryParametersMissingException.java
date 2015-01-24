package net.svab.mephisto.error;

import net.svab.mephisto.model.ResourceKey;

import java.util.Set;

import static java.lang.String.format;

public class RequiredQueryParametersMissingException extends ContractBrokenException {

    public RequiredQueryParametersMissingException(Set<String> requiredParamNames, ResourceKey resource) {
        super(format("Required query parameters %s are missing for resource %s", requiredParamNames, resource));
    }
}
