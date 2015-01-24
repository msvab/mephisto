package net.svab.mephisto.util;

import com.google.common.base.Joiner;
import net.svab.mephisto.model.ParameterType;
import net.svab.mephisto.model.Resource;
import net.svab.mephisto.model.UriParameter;

import java.util.Set;
import java.util.regex.Pattern;

import static java.lang.String.format;

public class PathPatternGenerator {

    public static Pattern pathPattern(Resource resource) {
        String pattern = format("^%s$", resource.getKey().getPath());
        for (UriParameter param : resource.getUriParameters()) {
            pattern = pattern.replaceFirst(format("\\{%s\\}", param.getName()), regexForType(param.getType(), param.getAllowedValues()));
        }
        return Pattern.compile(pattern);
    }

    private static String regexForType(ParameterType type, Set<String> allowedValues) {
        if (!allowedValues.isEmpty()) {
            return "(" + Joiner.on("|").join(allowedValues) + ")";
        }

        if (type == ParameterType.STRING) {
            return "[^/]+";
        } else if (type == ParameterType.INTEGER) {
            return "\\\\d+(?![^/])";
        } else if (type == ParameterType.NUMBER) {
            return "\\\\d+(\\\\.\\\\d+)?(?![^/])";
        } else if (type == ParameterType.BOOLEAN) {
            return "(true|false)";
        }

        return "";
    }
}
