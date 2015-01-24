package net.svab.mephisto.model;

import net.svab.mephisto.model.matcher.Matcher;

public class Header extends AbstractParameter {

    public Header(Matcher parameterName, ParameterType type, Iterable<String> allowedValues, boolean required) {
        super(parameterName, type, allowedValues, required);
    }
}
