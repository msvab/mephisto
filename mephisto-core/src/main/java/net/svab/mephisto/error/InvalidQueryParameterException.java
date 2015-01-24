package net.svab.mephisto.error;

import net.svab.mephisto.model.ParameterType;
import net.svab.mephisto.model.ResourceKey;

import java.util.Set;

import static java.lang.String.format;

public class InvalidQueryParameterException extends ContractBrokenException {

    public InvalidQueryParameterException(String paramName, ParameterType expectedType, String actualValue, ResourceKey resource) {
        super(format("Invalid value of query parameter %s, it should be %s, but the value was %s for resource %s", paramName, expectedType, actualValue, resource));
    }

    public InvalidQueryParameterException(String paramName, Set<String> allowedValues, String actualValue, ResourceKey resource) {
        super(format("Invalid value of query parameter %s, it should be one of %s, but the value was %s for resource %s", paramName, allowedValues, actualValue, resource));
    }
}
