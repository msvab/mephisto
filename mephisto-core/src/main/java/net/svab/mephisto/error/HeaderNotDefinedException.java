package net.svab.mephisto.error;

import net.svab.mephisto.model.ResourceKey;

import static java.lang.String.format;

public class HeaderNotDefinedException extends ContractBrokenException {

    public HeaderNotDefinedException(String name, ResourceKey resourceKey) {
        super(format("Header %s isn't defined for resource %s", name, resourceKey));
    }
}
