package net.svab.mephisto.model.matcher;

import com.google.common.base.Preconditions;

import java.util.regex.Pattern;

public class RegexMatcher implements Matcher {

    private final Pattern pattern;

    public RegexMatcher(String pattern) {
        Preconditions.checkNotNull(pattern);
        this.pattern = Pattern.compile(pattern);
    }

    @Override public boolean matches(Object value) {
        Preconditions.checkNotNull(value);
        Preconditions.checkArgument(value instanceof String);

        return pattern.matcher((String)value).matches();
    }

    public static Matcher regex(String pattern) {
        return new RegexMatcher(pattern);
    }

    @Override public String toString() {
        return pattern.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegexMatcher that = (RegexMatcher) o;
        return pattern.toString().equals(that.pattern.toString());
    }

    @Override
    public int hashCode() {
        return pattern.toString().hashCode();
    }
}
