package net.svab.mephisto.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class UriParameter {

    private final String name;
    private final ParameterType type;
    private final Set<String> allowedValues;

    public UriParameter(String name, ParameterType type) {
        this(name, type, null);
    }

    public UriParameter(String name, ParameterType type, Iterable<String> allowedValues) {
        this.name = name;
        this.type = type;
        this.allowedValues = allowedValues == null ? ImmutableSet.<String>of() : ImmutableSet.copyOf(allowedValues);
    }

    public String getName() {
        return name;
    }

    public ParameterType getType() {
        return type;
    }

    public Set<String> getAllowedValues() {
        return allowedValues;
    }

    @Override public int hashCode() {
        return Objects.hashCode(name, type, allowedValues);
    }

    @Override public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null || getClass() != obj.getClass()) {return false;}
        final UriParameter other = (UriParameter) obj;
        return Objects.equal(this.name, other.name) && Objects.equal(this.type, other.type) && Objects.equal(this.allowedValues, other.allowedValues);
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this).add("name", name).add("type", type).add("allowedValues", allowedValues).toString();
    }
}
