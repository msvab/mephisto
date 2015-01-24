package net.svab.mephisto.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class Response {
    private final Set<String> contentTypes;

    public Response(Set<String> contentTypes) {
        this.contentTypes = ImmutableSet.copyOf(contentTypes);
    }

    public Set<String> getContentTypes() {
        return contentTypes;
    }

    @Override public int hashCode() {return Objects.hashCode(contentTypes);}

    @Override public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null || getClass() != obj.getClass()) {return false;}
        final Response other = (Response) obj;
        return Objects.equal(this.contentTypes, other.contentTypes);
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this).add("contentTypes", contentTypes).toString();
    }
}
