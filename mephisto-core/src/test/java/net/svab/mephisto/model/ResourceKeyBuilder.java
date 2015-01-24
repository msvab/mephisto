package net.svab.mephisto.model;

public class ResourceKeyBuilder {

    private String path = "/some/path";
    private String method = "GET";

    private ResourceKeyBuilder() {}

    public static ResourceKeyBuilder resourceKeyBuilder() { return new ResourceKeyBuilder();}

    public ResourceKeyBuilder path(String path) {
        this.path = path;
        return this;
    }

    public ResourceKeyBuilder method(String method) {
        this.method = method;
        return this;
    }

    public ResourceKey build() {
        return new ResourceKey(path, method);
    }
}
