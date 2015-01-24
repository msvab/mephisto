package net.svab.mephisto.model;

import net.svab.mephisto.model.matcher.Matcher;

public class QueryParameter extends AbstractParameter {

    public QueryParameter(Matcher parameterName, ParameterType type, Iterable<String> allowedValues, boolean required) {
        super(parameterName, type, allowedValues, required);
    }
}
