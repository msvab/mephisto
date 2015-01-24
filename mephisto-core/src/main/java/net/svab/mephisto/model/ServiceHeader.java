package net.svab.mephisto.model;

import com.google.common.collect.ImmutableSet;
import com.google.common.net.HttpHeaders;
import net.svab.mephisto.error.ContractBrokenException;
import net.svab.mephisto.error.HeaderNotDefinedException;
import net.svab.mephisto.error.InvalidHeaderException;

import java.util.Set;

public class ServiceHeader extends AbstractServiceParameter {

    public static Set<String> IGNORED_REQUEST_HEADERS = ImmutableSet.<String>builder().add(
            HttpHeaders.ACCEPT, HttpHeaders.ACCEPT_CHARSET, HttpHeaders.ACCEPT_ENCODING, HttpHeaders.ACCEPT_LANGUAGE, HttpHeaders.AUTHORIZATION,
            HttpHeaders.CACHE_CONTROL, HttpHeaders.CONNECTION, HttpHeaders.COOKIE, HttpHeaders.CONTENT_LENGTH, HttpHeaders.CONTENT_MD5, HttpHeaders.CONTENT_TYPE,
            HttpHeaders.DATE, HttpHeaders.EXPECT, HttpHeaders.FROM, HttpHeaders.HOST, HttpHeaders.IF_MATCH, HttpHeaders.IF_MODIFIED_SINCE, HttpHeaders.IF_NONE_MATCH,
            HttpHeaders.IF_RANGE, HttpHeaders.IF_UNMODIFIED_SINCE, HttpHeaders.MAX_FORWARDS, HttpHeaders.ORIGIN, HttpHeaders.PRAGMA, HttpHeaders.PROXY_AUTHORIZATION,
            HttpHeaders.RANGE, HttpHeaders.REFERER, HttpHeaders.TE, HttpHeaders.USER_AGENT, HttpHeaders.UPGRADE, HttpHeaders.VIA, HttpHeaders.WARNING,
            HttpHeaders.X_REQUESTED_WITH, HttpHeaders.X_FORWARDED_FOR, HttpHeaders.DNT).build();

    public ServiceHeader(String name, Iterable<String> values) {
        super(name, values);
    }

    @Override protected ContractBrokenException invalidParamTypeError(String paramName, ParameterType expectedType,
                                                                      String actualValue, ResourceKey resourceKey) {
        return new InvalidHeaderException(paramName, expectedType, actualValue, resourceKey);
    }

    @Override protected ContractBrokenException invalidParamEnumError(String paramName, Set<String> allowedValues,
                                                                      String actualValue, ResourceKey resourceKey) {
        return new InvalidHeaderException(paramName, allowedValues, actualValue, resourceKey);
    }

    public void validate(Header headerDefinition, ResourceKey resourceKey) {
        if (headerDefinition == null) {
            if (IGNORED_REQUEST_HEADERS.contains(getName())) {
                return;
            }
            throw new HeaderNotDefinedException(getName(), resourceKey);
        }

        for (String paramValue : getValues()) {
            validateParamType(getName(), paramValue, headerDefinition.getType(), resourceKey);
            validateParamEnum(getName(), paramValue, headerDefinition.getAllowedValues(), resourceKey);
        }
    }
}
