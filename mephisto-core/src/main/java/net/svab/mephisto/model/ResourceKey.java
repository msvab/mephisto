package net.svab.mephisto.model;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

public class ResourceKey {

    private final String path;
    private final String method;

    public ResourceKey(String path, String method) {
        checkNotNull(path, "Resource Path cannot be null");
        checkNotNull(method, "Resource Method cannot be null");

        this.path = path;
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    @Override public int hashCode() {
        return Objects.hashCode(path, method);
    }

    @Override public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null || getClass() != obj.getClass()) {return false;}
        final ResourceKey other = (ResourceKey) obj;
        return Objects.equal(this.path, other.path) && Objects.equal(this.method, other.method);
    }

    @Override public String toString() {
        return format("%s %s", method, path);
    }
}
