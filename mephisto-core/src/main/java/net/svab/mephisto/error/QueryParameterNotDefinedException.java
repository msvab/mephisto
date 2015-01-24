package net.svab.mephisto.error;

import net.svab.mephisto.model.ResourceKey;

import static java.lang.String.format;

public class QueryParameterNotDefinedException extends ContractBrokenException {

    public QueryParameterNotDefinedException(String paramName, ResourceKey resourceKey) {
        super(format("Query parameter %s isn't defiend for resource %s", paramName, resourceKey));
    }
}
