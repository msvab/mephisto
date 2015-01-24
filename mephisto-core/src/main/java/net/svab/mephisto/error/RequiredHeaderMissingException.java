package net.svab.mephisto.error;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import net.svab.mephisto.model.Header;
import net.svab.mephisto.model.ResourceKey;

import java.util.Set;

import static java.lang.String.format;

public class RequiredHeaderMissingException extends ContractBrokenException {

    public RequiredHeaderMissingException(Set<Header> requiredHeaders, ResourceKey resource) {
        super(format("Required headers %s are missing for resource %s", headerNames(requiredHeaders), resource));
    }

    private static Iterable<String> headerNames(Set<Header> headers) {
        return Iterables.transform(headers, new Function<Header, String>() {
            @Override public String apply(Header header) {
                return header.getName();
            }
        });
    }
}
