package net.svab.mephisto.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import net.svab.mephisto.model.matcher.Matcher;

import java.util.Set;

public abstract class AbstractParameter {

    protected final Matcher parameterName;
    protected final ParameterType type;
    protected final Set<String> allowedValues;
    protected final boolean required;

    public AbstractParameter(Matcher parameterName, ParameterType type, Iterable<String> allowedValues, boolean required) {
        this.parameterName = parameterName;
        this.type = type;
        this.allowedValues = allowedValues == null ? ImmutableSet.<String>of() : ImmutableSet.copyOf(allowedValues);
        this.required = required;
    }

    public ParameterType getType() {
        return type;
    }

    public Set<String> getAllowedValues() {
        return allowedValues;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean hasMatchingName(String name) {
        return parameterName.matches(name);
    }

    public String getName() {
        return parameterName.toString();
    }

    @Override public int hashCode() {
        return Objects.hashCode(parameterName, type, allowedValues, required);
    }

    @Override public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null || getClass() != obj.getClass()) {return false;}
        final AbstractParameter other = (AbstractParameter) obj;
        return Objects.equal(this.parameterName, other.parameterName)
                && Objects.equal(this.type, other.type)
                && Objects.equal(this.allowedValues, other.allowedValues)
                && Objects.equal(this.required, other.required);
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this).add("parameterName", parameterName).add("type", type).add("allowedValues", allowedValues).add("required", required)
                .toString();
    }
}
