package net.svab.mephisto.model;

import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import net.svab.mephisto.error.ContractBrokenException;

import java.util.Set;

import static java.lang.String.format;

public abstract class AbstractServiceParameter {

    private final String name;
    private final Set<String> values;

    public AbstractServiceParameter(String name, Iterable<String> values) {
        this.name = name;
        this.values = ImmutableSet.copyOf(values);
    }

    public String getName() {
        return name;
    }

    public Set<String> getValues() {
        return values;
    }

    protected void validateParamEnum(String paramName, String paramValue, Set<String> allowedValues, ResourceKey resourceKey) {
        if (!allowedValues.isEmpty() && !allowedValues.contains(paramValue)) {
            throw invalidParamEnumError(paramName, allowedValues, paramValue, resourceKey);
        }
    }

    protected void validateParamType(String paramName, String paramValue, ParameterType type, ResourceKey resourceKey) {
        switch (type) {
            case INTEGER:
                if (Ints.tryParse(paramValue) == null) {
                    throw invalidParamTypeError(paramName, type, paramValue, resourceKey);
                }
                break;
            case NUMBER:
                if (Doubles.tryParse(paramValue) == null) {
                    throw invalidParamTypeError(paramName, type, paramValue, resourceKey);
                }
                break;
            case BOOLEAN:
                if (!"true".equalsIgnoreCase(paramValue) && !"false".equalsIgnoreCase(paramValue)) {
                    throw invalidParamTypeError(paramName, type, paramValue, resourceKey);
                }
        }
    }

    protected abstract ContractBrokenException invalidParamTypeError(String paramName, ParameterType expectedType,
                                                                     String actualValue, ResourceKey resourceKey);

    protected abstract ContractBrokenException invalidParamEnumError(String paramName, Set<String> allowedValues,
                                                                     String actualValue, ResourceKey resourceKey);

    @Override
    public String toString() {
        return format("%s: %s", name, values);
    }
}
