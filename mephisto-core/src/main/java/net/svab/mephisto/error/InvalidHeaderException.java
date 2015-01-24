package net.svab.mephisto.error;

import net.svab.mephisto.model.ParameterType;
import net.svab.mephisto.model.ResourceKey;

import java.util.Set;

import static java.lang.String.format;

public class InvalidHeaderException extends ContractBrokenException {

    public InvalidHeaderException(String name, ParameterType expectedType, String actualValue, ResourceKey resource) {
        super(format("Invalid value of header %s, it should be %s, but the value was %s for resource %s", name, expectedType, actualValue, resource));
    }

    public InvalidHeaderException(String name, Set<String> allowedValues, String actualValue, ResourceKey resource) {
        super(format("Invalid value of header %s, it should be one of %s, but the value was %s for resource %s", name, allowedValues, actualValue, resource));
    }
}
