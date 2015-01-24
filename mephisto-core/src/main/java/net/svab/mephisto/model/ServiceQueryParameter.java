package net.svab.mephisto.model;

import net.svab.mephisto.error.ContractBrokenException;
import net.svab.mephisto.error.InvalidQueryParameterException;
import net.svab.mephisto.error.QueryParameterNotDefinedException;

import java.util.Set;

public class ServiceQueryParameter extends AbstractServiceParameter {

    public ServiceQueryParameter(String name, Iterable<String> values) {
        super(name, values);
    }

    @Override
    protected ContractBrokenException invalidParamTypeError(String paramName, ParameterType expectedType,
                                                            String actualValue, ResourceKey resourceKey) {
        return new InvalidQueryParameterException(paramName, expectedType, actualValue, resourceKey);
    }

    @Override
    protected ContractBrokenException invalidParamEnumError(String paramName, Set<String> allowedValues,
                                                            String actualValue, ResourceKey resourceKey) {
        return new InvalidQueryParameterException(paramName, allowedValues, actualValue, resourceKey);
    }

    public void validate(QueryParameter paramDefinition, ResourceKey resourceKey) {
        if (paramDefinition == null) {
            throw new QueryParameterNotDefinedException(getName(), resourceKey);
        }
        for (String paramValue : getValues()) {
            validateParamType(getName(), paramValue, paramDefinition.getType(), resourceKey);
            validateParamEnum(getName(), paramValue, paramDefinition.getAllowedValues(), resourceKey);
        }
    }
}
